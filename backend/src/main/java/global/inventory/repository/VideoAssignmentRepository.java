package global.inventory.repository;


import global.inventory.model.VideoAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VideoAssignmentRepository extends JpaRepository<VideoAssignment, Long> {
    Boolean existsByVideoIdAndUserId(Long id, Long requesterUserIdFromSecurityContext);

    @Query("SELECT va FROM VideoAssignment va WHERE va.video.id = :videoId AND va.user.id = :userId")
    Optional<VideoAssignment> findByVideoIdAndUserId(@Param("videoId") Long videoId, @Param("userId") Long userId);
}