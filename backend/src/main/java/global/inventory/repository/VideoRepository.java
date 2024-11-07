package global.inventory.repository;

import global.inventory.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("SELECT v FROM Video v WHERE v.deleted = false order by v.createdAt desc")
    Page<Video> findAllVideosForAdmin(Pageable pageable);

    @Query("SELECT DISTINCT v FROM Video v " +
            "JOIN v.videoAssignments va " +
            "WHERE va.user.id = :userId AND v.deleted = false")
    Page<Video> findByVideoAssignments_User_Id(Long userId, Pageable pageable);
}