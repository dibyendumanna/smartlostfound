package com.smartlostfound.backend.file.controller;

import com.smartlostfound.backend.file.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file) {

        try {
            String fileName = fileStorageService.saveFile(file);

            return ResponseEntity.ok(fileName);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}