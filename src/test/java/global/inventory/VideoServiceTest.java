package global.inventory;

import global.inventory.enums.Role;
import global.inventory.model.User;
import global.inventory.model.Video;
import global.inventory.model.VideoAssignment;
import global.inventory.payload.request.VideoUploadRequest;
import global.inventory.repository.VideoRepository;
import global.inventory.service.UserService;
import global.inventory.service.VideoAssignmentService;
import global.inventory.service.VideoServiceImpl;
import global.inventory.service.storage.VideoStorageService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private UserService userService;

    @Mock
    private VideoStorageService videoStorageService;

    @Mock
    private VideoAssignmentService videoAssignmentService;

    @InjectMocks
    private VideoServiceImpl videoService;

    private Video testVideo;
    private User testUser;
    private VideoAssignment testAssignment;

    @BeforeEach
    void setUp() {
        testVideo = Video.builder()
                .id(1L)
                .title("Test Video")
                .description("Test Description")
                .videoUrl("test-url")
                .build();

        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .username("testuser")
                .role(Role.USER)
                .build();

        testAssignment = VideoAssignment.builder()
                .id(1L)
                .user(testUser)
                .video(testVideo)
                .build();
    }

    @Test
    @DisplayName("Should successfully upload a video")
    void uploadVideoSuccess() {
        // Arrange
        MultipartFile file = new MockMultipartFile(
                "video",
                "test.mp4",
                "video/mp4",
                "test video content".getBytes()
        );
        when(videoStorageService.store(any(MultipartFile.class))).thenReturn("stored-path");
        when(videoRepository.save(any(Video.class))).thenReturn(testVideo);

        // Act
        Video result = videoService.uploadVideo(VideoUploadRequest.builder()
                .title("Test Video")
                .description("Test Description")
                .file(file)
                .build());

        // Assert
        assertNotNull(result);
        assertEquals("Test Video", result.getTitle());
        verify(videoStorageService).store(any(MultipartFile.class));
        verify(videoRepository).save(any(Video.class));
    }

    @Test
    @DisplayName("Should assign video to user successfully")
    void assignVideoToUserSuccess() {
        // Arrange
        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));
        when(userService.findById(1L)).thenReturn(testUser);
        when(videoAssignmentService.existsByVideoIdAndUserId(1L, 1L)).thenReturn(false);
        when(videoAssignmentService.save(any(VideoAssignment.class))).thenReturn(testAssignment);

        // Act
        VideoAssignment result = videoService.assignVideoToUser(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals(testVideo, result.getVideo());
    }

    @Test
    @DisplayName("Should get user videos with pagination")
    void getUserVideosSuccess() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Video> videos = List.of(testVideo);
        Page<Video> videoPage = new PageImpl<>(videos, pageRequest, 1);

        when(videoRepository.findByVideoAssignments_User_Id(1L, pageRequest))
                .thenReturn(videoPage);

        // Act
        Page<Video> result = videoService.getUserVideos(1L, pageRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test Video");
    }
}