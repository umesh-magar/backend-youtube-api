package com.backendyoutube.youtubeapi.helper;

import com.backendyoutube.youtubeapi.storage.FileStorageService;
import org.springframework.stereotype.Component;

@Component
public class FileCleanupHelper {
    private  final FileStorageService fileStorageService;
    public  FileCleanupHelper(FileStorageService fileStorageService){
        this.fileStorageService=fileStorageService;
    }
    public void deleteAfterFailure(String fileKey, RuntimeException originalException){
        if(fileKey==null || fileKey.isEmpty()){
            return;
        }
        try{
       fileStorageService.delete(fileKey);
        }catch (RuntimeException cleanUpException){
            originalException.addSuppressed(cleanUpException);
        }

    }
}
