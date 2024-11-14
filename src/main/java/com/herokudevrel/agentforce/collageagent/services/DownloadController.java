package com.herokudevrel.agentforce.collageagent.services;

import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class DownloadController {

    private final StorageService storageService;

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) throws IOException {
        S3Resource s3Resource = storageService.retrieve(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(s3Resource);
    }
}
