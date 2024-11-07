package global.inventory.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "Request object for uploading a video")
@Builder
public class VideoUploadRequest {
    private String title;
    private String description;
    private MultipartFile file;
}