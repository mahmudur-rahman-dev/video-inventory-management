package global.inventory.controller;

import global.inventory.service.storage.VideoStorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
@Slf4j
public class VideoStreamController {
    private final VideoStorageService videoStorageService;
    private static final int BUFFER_SIZE = 8192;
    private static final long CHUNK_SIZE = 1024 * 1024; // 1MB chunks

    @GetMapping("/**")
    public void streamVideo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getRequestURI().substring("/uploads/".length());
        Resource video = videoStorageService.loadAsResource(path);
        File videoFile = video.getFile();
        long fileSize = videoFile.length();

        String rangeHeader = request.getHeader(HttpHeaders.RANGE);
        long start = 0;
        long end = fileSize - 1;

        if (rangeHeader != null) {
            try {
                if (rangeHeader.startsWith("bytes=")) {
                    String[] ranges = rangeHeader.substring(6).split("-");
                    start = Long.parseLong(ranges[0]);

                    if (ranges.length > 1 && !ranges[1].isEmpty()) {
                        end = Long.parseLong(ranges[1]);
                    }

                    if (end > fileSize - 1) {
                        end = fileSize - 1;
                    }

                    if (start > end) {
                        response.setStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value());
                        return;
                    }

                    if (end - start + 1 > CHUNK_SIZE) {
                        end = start + CHUNK_SIZE - 1;
                    }

                    response.setStatus(HttpStatus.PARTIAL_CONTENT.value());
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            }
        }

        long contentLength = end - start + 1;

        String contentType = request.getServletContext().getMimeType(videoFile.getAbsolutePath());
        if (contentType == null) {
            contentType = "video/mp4";
        }

        response.setContentType(contentType);
        response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        response.setHeader(HttpHeaders.EXPIRES, "0");

        if (rangeHeader != null) {
            response.setHeader(HttpHeaders.CONTENT_RANGE,
                    String.format("bytes %d-%d/%d", start, end, fileSize));
        }

        try (RandomAccessFile raf = new RandomAccessFile(videoFile, "r");
             OutputStream out = new BufferedOutputStream(response.getOutputStream())) {

            raf.seek(start);
            byte[] buffer = new byte[BUFFER_SIZE];
            long remaining = contentLength;
            int read;

            while (remaining > 0) {
                read = raf.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (read == -1) {
                    break;
                }
                try {
                    out.write(buffer, 0, read);
                    remaining -= read;
                } catch (IOException e) {
                    log.debug("Client disconnected while streaming video: {}", path);
                    return;
                }
            }
        } catch (IOException e) {
            if (!e.getMessage().contains("Broken pipe") &&
                    !e.getMessage().contains("Connection reset")) {
                log.error("Error streaming video: {}", path, e);
            }
        }
    }
}