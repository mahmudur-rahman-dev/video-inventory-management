package global.inventory.service;

import global.inventory.enums.ActivityAction;
import global.inventory.model.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityLogService {
    ActivityLog logActivity(Long userId, Long videoId, ActivityAction action);

    List<ActivityLog> getAllActivityLogs();

    List<ActivityLog> getVideoActivityLogs(Long videoId);

    List<ActivityLog> getUserActivityLogs(Long userId);

    Page<ActivityLog> getAllActivityLogs(Pageable pageable);

    Page<ActivityLog> getVideoActivityLogs(Long videoId, Pageable pageable);

    Page<ActivityLog> getUserActivityLogs(Long userId, Pageable pageable);
}
