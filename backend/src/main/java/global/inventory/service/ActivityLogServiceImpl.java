package global.inventory.service;

import global.inventory.enums.ActivityAction;
import global.inventory.model.ActivityLog;
import global.inventory.model.User;
import global.inventory.model.Video;
import global.inventory.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityLogServiceImpl implements ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final UserService userService;
    private final VideoService videoService;

    @Override
    public ActivityLog logActivity(Long userId, Long videoId, ActivityAction action) {
        User user = userService.findById(userId);

        Video video = videoService.findById(videoId);

        ActivityLog log = ActivityLog.builder()
                .user(user)
                .video(video)
                .action(action)
                .timestamp(LocalDateTime.now())
                .details(generateActivityDetails(action, video.getTitle()))
                .build();

        return activityLogRepository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getAllActivityLogs() {
        return activityLogRepository.findAllByOrderByTimestampDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getVideoActivityLogs(Long videoId) {
        return activityLogRepository.findByVideo_IdOrderByTimestampDesc(videoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getUserActivityLogs(Long userId) {
        return activityLogRepository.findByUser_IdOrderByTimestampDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getAllActivityLogs(Pageable pageable) {
        return activityLogRepository.findAllByOrderByTimestampDesc(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getVideoActivityLogs(Long videoId, Pageable pageable) {
        return activityLogRepository.findByVideo_IdOrderByTimestampDesc(videoId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getUserActivityLogs(Long userId, Pageable pageable) {
        return activityLogRepository.findByUser_IdOrderByTimestampDesc(userId, pageable);
    }


    private String generateActivityDetails(ActivityAction action, String videoTitle) {
        return switch (action) {
            case VIEWED -> String.format("User viewed video: %s", videoTitle);
            case COMPLETED -> String.format("User completed video: %s", videoTitle);
        };
    }
}

