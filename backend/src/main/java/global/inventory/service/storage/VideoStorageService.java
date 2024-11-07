package global.inventory.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface VideoStorageService {
    String store(MultipartFile file);

    Resource loadAsResource(String storedPath);

    void delete(String storedPath);

    String generatePublicUrl(String storedPath);
}