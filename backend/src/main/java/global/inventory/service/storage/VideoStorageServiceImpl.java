package global.inventory.service.storage;

import global.inventory.exception.StorageException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class VideoStorageServiceImpl implements VideoStorageService {

    private final Path rootLocation;
    private final long maxFileSize;

    public VideoStorageServiceImpl(
            @Value("${app.video.storage.location}") String storageLocation,
            @Value("${app.video.max-size:524288000}") long maxFileSize) {
        this.rootLocation = Paths.get(storageLocation);
        this.maxFileSize = maxFileSize;
    }

    @PostConstruct
    public void initialize() {
        try {
            Files.createDirectories(rootLocation);
            log.info("Initialized storage location at: {}", rootLocation.toAbsolutePath());
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        validateFile(file);

        try {
            String relativePath = createRelativePath();
            Path fullPath = rootLocation.resolve(relativePath);
            Files.createDirectories(fullPath);

            String filename = createUniqueFilename(file);
            Path destinationFile = fullPath.resolve(filename);

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            // Create and return the URL path that will be stored in the database
            String storedPath = relativePath + "/" + filename;
            log.info("Stored video file at: {}", storedPath);

            return storedPath;

        } catch (IOException e) {
            throw new StorageException("Failed to store video file", e);
        }
    }

    @Override
    public Resource loadAsResource(String storedPath) {
        try {
            Path file = rootLocation.resolve(storedPath);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file: " + storedPath);
            }

        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + storedPath, e);
        }
    }

    @Override
    public void delete(String storedPath) {
        try {
            Path file = rootLocation.resolve(storedPath);
            FileSystemUtils.deleteRecursively(file);

            // Try to clean up empty parent directories
            Path parent = file.getParent();
            while (parent != null && !parent.equals(rootLocation)) {
                if (isDirEmptyOrNotExists(parent)) {
                    Files.deleteIfExists(parent);
                    parent = parent.getParent();
                } else {
                    break;
                }
            }

            log.info("Successfully deleted video file: {}", storedPath);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file: " + storedPath, e);
        }
    }

    @Override
    public String generatePublicUrl(String storedPath) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(storedPath)
                .toUriString();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Cannot store empty file");
        }

        if (file.getSize() > maxFileSize) {
            throw new StorageException(String.format("File size %d exceeds maximum limit of %d bytes",
                    file.getSize(), maxFileSize));
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
        if (filename.contains("..")) {
            throw new StorageException("Cannot store file with relative path outside current directory");
        }
    }

    private String createRelativePath() {
        LocalDateTime now = LocalDateTime.now();
        return String.format("%d/%02d/%02d",
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth());
    }

    private String createUniqueFilename(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String baseFilename = UUID.randomUUID().toString();

        return extension != null ? baseFilename + "." + extension : baseFilename;
    }

    private boolean isDirEmptyOrNotExists(Path path) throws IOException {
        if (Files.exists(path)) {
            try (var entries = Files.list(path)) {
                return !entries.findFirst().isPresent();
            }
        }
        return true;
    }
}