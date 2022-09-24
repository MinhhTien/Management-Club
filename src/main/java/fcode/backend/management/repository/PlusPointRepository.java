package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.PlusPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlusPointRepository extends JpaRepository<PlusPoint, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM plus_point WHERE status = ?1")
    List<PlusPoint> getAllPlusPoints(String status);

    @Query(nativeQuery = true, value = "SELECT * FROM plus_point WHERE id = ?1 AND status = ?2")
    PlusPoint getByIdAndStatus(Integer id, String status);

    @Query(nativeQuery = true, value = "SELECT * FROM plus_point WHERE member_id = ?1 AND status = ?2")
    List<PlusPoint> getByMemberIdAndStatus(Integer memberId, String status);

    @Query(nativeQuery = true, value = "SELECT * FROM fcode_management.plus_point WHERE plus_point.member_id = ?1 AND plus_point.date BETWEEN ?2 AND ?3 AND plus_point.status = ?4")
    List<PlusPoint> getByMemberIdAndStatusBetweenTime(Integer memberId, Date startDate, Date endDate, String status);

    @Query(nativeQuery = true, value = "SELECT * FROM plus_point WHERE member_id = ?1 AND reason = ?2")
    PlusPoint existsByMemberIdAndReason(Integer memberId, String reason);
}
