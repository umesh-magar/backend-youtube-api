package com.backendyoutube.youtubeapi.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String storeVideo(MultipartFile file, String directory ,String extension) ;
    void delete (String filePath);
    Resource load(String filePath);
}
