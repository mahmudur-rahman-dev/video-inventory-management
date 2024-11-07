package global.inventory.mapper;

import global.inventory.model.Video;
import global.inventory.payload.response.VideoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VideoMapper {
    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);

    VideoResponse videoToDto(Video video);

    List<VideoResponse> videoListToDtoList(List<Video> videos);

    Video dtoToVideo(VideoResponse videoDto);
}
