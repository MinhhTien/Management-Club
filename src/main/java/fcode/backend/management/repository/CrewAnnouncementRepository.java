package fcode.backend.management.repository;

import fcode.backend.management.repository.entity.Crew;
import fcode.backend.management.repository.entity.CrewAnnouncement;
import fcode.backend.management.service.constant.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface CrewAnnouncementRepository extends JpaRepository<CrewAnnouncement, Integer> {
    boolean existsByCrew(Crew crew);

    CrewAnnouncement findCrewAnnouncementByIdAndStatus(Integer id, Status status);

    Set<CrewAnnouncement> findCrewAnnouncementByStatus(Status status);

    CrewAnnouncement findCrewAnnouncementByIdAndStatusIsNot(Integer id, Status status);
    
}
