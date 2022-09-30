package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Integer> {
    Fee findFeeByName(String name);
    Fee findFeeById(Integer id);
    @Query(nativeQuery = true, value = "SELECT * FROM fee")
    Set<Fee> getAllFees();
    boolean existsByName(String name);
}
