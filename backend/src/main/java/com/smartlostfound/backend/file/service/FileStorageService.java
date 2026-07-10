package com.smartlostfound.backend.file.service;

import com.smartlostfound.backend.exception.InvalidFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "uploads";

    public String saveFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        String originalFileName  = file.getOriginalFilename();

        if (originalFileName  == null) {
            throw new RuntimeException("Invalid file");
        }

        String lower = originalFileName.toLowerCase();

        if (!(lower.endsWith(".jpg")
                || lower.endsWith(".jpeg")
                || lower.endsWith(".png")
                || lower.endsWith(".webp"))) {
            throw new InvalidFileException(
                    "Only JPG, JPEG, PNG and WEBP files are allowed");
        }

        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis()
                + "_"
                + originalFileName;

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );

        return fileName;
    }

    public void deleteFile(String fileName) {

        if (fileName == null || fileName.isBlank()) {
            return;
        }

        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
//            e.printStackTrace();
            throw new RuntimeException("Failed to delete image");
        }
    }
}