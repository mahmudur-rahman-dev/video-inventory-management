package global.inventory.controller;

import global.inventory.mapper.VideoAssignmentMapper;
import global.inventory.mapper.VideoMapper;
import global.inventory.payload.request.VideoUpdateRequest;
import global.inventory.payload.request.VideoUploadRequest;
import global.inventory.payload.response.VideoAssignmentResponse;
import global.inventory.payload.response.VideoResponse;
import global.inventory.payload.response.generic.InventoryResponse;
import global.inventory.payload.response.generic.PageInfo;
import global.inventory.service.ActivityLogService;
import global.inventory.service.VideoService;
import global.inventory.util.UtilService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;
    private final ActivityLogService activityLogService;


    @Operation(summary = "Upload a new video")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse<VideoResponse>> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description
    ) {
        VideoUploadRequest request = VideoUploadRequest.builder()
                .title(title)
                .description(description)
                .file(file)
                .build();

        var video = videoService.uploadVideo(request);
        return ResponseEntity.ok(new InventoryResponse<>(VideoMapper.INSTANCE.videoToDto(video)));
    }

    @Operation(summary = "Assign Video to a User")
    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse<VideoAssignmentResponse>> assignVideo(
            @PathVariable("id") Long videoId,
            @RequestParam Long userId
    ) {
        var data = videoService.assignVideoToUser(videoId, userId);
        return ResponseEntity.ok(new InventoryResponse<>(VideoAssignmentMapper.INSTANCE.videoAssignmentToDto(data)));
    }

    @Operation(summary = "Get all video assignments")
    @GetMapping("/assignments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse<List<VideoAssignmentResponse>>> getVideoAssignments(@PageableDefault Pageable pageable) {
        var data = videoService.getAllAssignedVideos(pageable);
        return ResponseEntity.ok(new InventoryResponse<>(VideoAssignmentMapper.INSTANCE.videoAssignmentListToDtoList(data.getContent()), PageInfo.of(data)));
    }

    @Operation(summary = "Get all videos")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse<List<VideoResponse>>> getAllVideos(@PageableDefault Pageable pageable) {
        var videos = videoService.getAllVideosForAdmin(pageable);

        return ResponseEntity.ok(new InventoryResponse<>(
                VideoMapper.INSTANCE.videoListToDtoList(videos.getContent()),
                PageInfo.of(videos)
        ));
    }

    @Operation(summary = "Get a video by ID")
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse<VideoResponse>> getVideo(
            @PathVariable Long id
    ) {
        var video = videoService.findById(id);
        return ResponseEntity.ok(new InventoryResponse<>(VideoMapper.INSTANCE.videoToDto(video)));
    }

    @Operation(summary = "Delete a video by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse<Boolean>> deleteVideo(
            @PathVariable Long id
    ) {
        videoService.deleteVideo(id);
        return ResponseEntity.ok(new InventoryResponse<>(true));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse<VideoResponse>> updateVideo(
            @PathVariable Long id,
            @RequestBody VideoUpdateRequest request
    ) {
        var updated = videoService.updateVideo(id, request);
        return ResponseEntity.ok(new InventoryResponse<>(VideoMapper.INSTANCE.videoToDto(updated)));
    }

    @GetMapping("/user-videos")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InventoryResponse<List<VideoResponse>>> getUserVideos(@PageableDefault Pageable pageable) {
        var videos = videoService.getUserVideos(UtilService.getRequesterUserIdFromSecurityContext(), pageable);
        return ResponseEntity.ok(new InventoryResponse<>(
                VideoMapper.INSTANCE.videoListToDtoList(videos.getContent()),
                PageInfo.of(videos)
        ));
    }

    @DeleteMapping("/remove-assignment/{assignmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse<Boolean>> removeAssignment(
            @PathVariable Long assignmentId
    ) {
        videoService.removeAssignment(assignmentId);
        return ResponseEntity.ok(new InventoryResponse<>(true));
    }
}
