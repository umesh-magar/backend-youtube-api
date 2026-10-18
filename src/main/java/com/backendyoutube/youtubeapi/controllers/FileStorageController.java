package com.backendyoutube.youtubeapi.controllers;

import com.backendyoutube.youtubeapi.storage.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/storage-test")
public class FileStorageController {
    private  final FileStorageService fileStorageService;
    public FileStorageController(FileStorageService fileStorageService){
        this.fileStorageService=fileStorageService;
    }
    @PostMapping(value = "/videos",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> store(@RequestPart("file") MultipartFile file){
        String extension= StringUtils.getFilenameExtension(file.getOriginalFilename());
        String storageKey=fileStorageService.store(file,"videos",extension);
        return ResponseEntity.status(HttpStatus.CREATED).body(storageKey);
    }

}
