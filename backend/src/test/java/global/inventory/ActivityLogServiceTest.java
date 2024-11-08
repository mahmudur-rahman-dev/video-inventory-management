package global.inventory;

import global.inventory.enums.ActivityAction;
import global.inventory.enums.Role;
import global.inventory.model.ActivityLog;
import global.inventory.model.User;
import global.inventory.model.Video;
import global.inventory.repository.ActivityLogRepository;
import global.inventory.service.ActivityLogServiceImpl;
import global.inventory.service.UserService;
import global.inventory.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityLogServiceTest {

    @Mock
    private ActivityLogRepository activityLogRepository;

    @Mock
    private UserService userService;

    @Mock
    private VideoService videoService;

    @InjectMocks
    private ActivityLogServiceImpl activityLogService;

    private User testUser;
    private Video testVideo;
    private ActivityLog testLog;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .username("testuser")
                .role(Role.USER)
                .build();

        testVideo = Video.builder()
                .id(1L)
                .title("Test Video")
                .description("Test Description")
                .videoUrl("test-url")
                .build();

        testLog = ActivityLog.builder()
                .id(1L)
                .user(testUser)
                .video(testVideo)
                .action(ActivityAction.VIEWED)
                .build();
    }

    @Test
    @DisplayName("Should log activity successfully")
    void logActivitySuccess() {
        when(userService.findById(1L)).thenReturn(testUser);
        when(videoService.findById(1L)).thenReturn(testVideo);
        when(activityLogRepository.save(any(ActivityLog.class))).thenReturn(testLog);

        ActivityLog result = activityLogService.logActivity(1L, 1L, ActivityAction.VIEWED);

        assertNotNull(result);
        assertEquals(ActivityAction.VIEWED, result.getAction());
        assertEquals(testUser, result.getUser());
        assertEquals(testVideo, result.getVideo());
    }

    @Test
    @DisplayName("Should get user activity logs with pagination")
    void getUserActivityLogsSuccess() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<ActivityLog> logs = List.of(testLog);
        Page<ActivityLog> logPage = new PageImpl<>(logs, pageRequest, 1);

        when(activityLogRepository.findByUser_IdOrderByTimestampDesc(1L, pageRequest))
                .thenReturn(logPage);

        Page<ActivityLog> result = activityLogService.getUserActivityLogs(1L, pageRequest);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAction()).isEqualTo(ActivityAction.VIEWED);
    }
}