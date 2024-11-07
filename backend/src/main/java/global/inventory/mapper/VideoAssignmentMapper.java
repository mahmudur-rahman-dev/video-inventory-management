package global.inventory.mapper;

import global.inventory.model.VideoAssignment;
import global.inventory.payload.response.VideoAssignmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VideoAssignmentMapper {
    VideoAssignmentMapper INSTANCE = Mappers.getMapper(VideoAssignmentMapper.class);

    VideoAssignmentResponse videoAssignmentToDto(VideoAssignment videoAssignment);

    VideoAssignment dtoToVideoAssignment(VideoAssignmentResponse videoAssignmentDto);

    List<VideoAssignmentResponse> videoAssignmentListToDtoList(List<VideoAssignment> videoAssignments);
}
