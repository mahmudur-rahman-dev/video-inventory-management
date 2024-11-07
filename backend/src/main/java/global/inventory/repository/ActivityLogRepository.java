package global.inventory.repository;

import global.inventory.model.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findAllByOrderByTimestampDesc();

    List<ActivityLog> findByVideo_IdOrderByTimestampDesc(Long videoId);

    List<ActivityLog> findByUser_IdOrderByTimestampDesc(Long userId);

    @Query("SELECT a FROM ActivityLog a join fetch a.user join fetch a.video ORDER BY a.timestamp DESC")
    Page<ActivityLog> findAllByOrderByTimestampDesc(Pageable pageable);

    Page<ActivityLog> findByVideo_IdOrderByTimestampDesc(Long videoId, Pageable pageable);

    Page<ActivityLog> findByUser_IdOrderByTimestampDesc(Long userId, Pageable pageable);
}
