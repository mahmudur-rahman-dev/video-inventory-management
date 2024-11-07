package global.inventory.mapper;

import global.inventory.model.ActivityLog;
import global.inventory.payload.response.ActivityLogResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityLogMapper {
    ActivityLogMapper INSTANCE = Mappers.getMapper(ActivityLogMapper.class);

    ActivityLogResponse activityLogToDto(ActivityLog activityLog);
    List<ActivityLogResponse> activityLogListToDtoList(List<ActivityLog> activityLogs);
}