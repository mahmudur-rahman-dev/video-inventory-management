package global.inventory.service;

import global.inventory.model.VideoAssignment;
import global.inventory.repository.VideoAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VideoAssignmentServiceImpl implements VideoAssignmentService {
    private final VideoAssignmentRepository videoAssignmentRepository;

    @Override
    public void assignVideoToUser(Long videoId, Long userId, Long adminId) {

    }

    @Override
    public Page<VideoAssignment> getAssignedVideosForUser(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public void deleteAssignment(Long assignmentId, Long adminId) {

    }

    @Override
    public Page<VideoAssignment> getAllAssignments(Pageable pageable) {
        return videoAssignmentRepository.findAllAssignments(pageable);
    }

    public VideoAssignment save(VideoAssignment videoAssignment) {
        return videoAssignmentRepository.save(videoAssignment);
    }

    @Override
    public Boolean existsByVideoIdAndUserId(Long id, Long requesterUserIdFromSecurityContext) {
        return videoAssignmentRepository.existsByVideoIdAndUserId(id, requesterUserIdFromSecurityContext);
    }

    @Override
    public Optional<VideoAssignment> findByVideoIdAndUserId(Long videoId, Long userId) {
        return videoAssignmentRepository.findByVideoIdAndUserId(videoId, userId);
    }

    @Override
    public void delete(Long id) {
        videoAssignmentRepository.deleteById(id);
    }

    @Override
    public VideoAssignment findById(Long videoId) {
        return videoAssignmentRepository.findById(videoId).orElseThrow(
                () -> new RuntimeException("Video assignment not found")
        );
    }
}
