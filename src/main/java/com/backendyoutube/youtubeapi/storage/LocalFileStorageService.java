package com.backendyoutube.youtubeapi.storage;
import com.backendyoutube.youtubeapi.config.StorageProperties;
import com.backendyoutube.youtubeapi.exceptions.BadRequestException;
import com.backendyoutube.youtubeapi.exceptions.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class LocalFileStorageService implements FileStorageService{
    private static final Set<String> ALLOWED_DIRECTORIES=Set.of("videos","thumbnails");
    private static final Set<String> VIDEO_EXTENSIONS=Set.of("mp4","mov","m4v","webm");
    private static final Set<String> THUMBNAIL_EXTENSIONS=Set.of("jpg","jpeg","png","web");
    private final Path root;
    public LocalFileStorageService(StorageProperties properties){
        this.root=Path.of(properties.baseDir())
                .toAbsolutePath().normalize();
    }
    @PostConstruct
    void init(){
        try{
            Files.createDirectories(resolve("videos"));
            Files.createDirectories(resolve("thumbnails"));
            log.info("local storage initialized");
        }catch (IOException ex) {
            throw new StorageException("Could not initialize local storage");
        }
    }
    private  Path resolve(String relativePath){
        Path resolved=root.resolve(relativePath).normalize();
        if(resolved.equals(root) || !resolved.startsWith(root)){
            throw new StorageException("Invalid storage path ");
        }
        return resolved;
    }
    @Override
     public  String storeVideo(MultipartFile file, String directory, String extension){
        if(file==null || file.isEmpty()){
            throw new BadRequestException("A  file is required");
        }
        String safeDirectory=validateDirectory(directory);
        String safeExtension=validateExtensions(directory,extension);
        String relativePath=safeDirectory +"/" + UUID.randomUUID() + "." +safeExtension;
        Path target=resolve(relativePath);
        try {
            Files.createDirectories(target.getParent());
            try(InputStream input=file.getInputStream()){
                Files.copy(input,target);
            }
            return relativePath;
        }catch (IOException ex){
            try {
                Files.deleteIfExists(target);
            }catch (IOException cleanupException){
                ex.addSuppressed(cleanupException);
            }
            throw new StorageException("could not store the uploaded file" ,ex);
        }

    }
    String validateDirectory(String directory){
        if(directory==null || !ALLOWED_DIRECTORIES.contains(directory)){
            throw new StorageException("Unsupported storage directory");
        }
        return directory;
    }
    String validateExtensions(String directory, String extensions){
        if(!StringUtils.hasText(extensions)){
            throw new BadRequestException("File extension is required");
        }
        String normalized=extensions.trim().toLowerCase(Locale.ROOT);
        if(normalized.startsWith(".")){
            normalized=normalized.substring(1);
        }
        Set<String> allowedExtensions=directory.equals("videos")? VIDEO_EXTENSIONS: THUMBNAIL_EXTENSIONS;
        if(!allowedExtensions.contains(normalized)){
            throw new BadRequestException("Unsupported file extension");
        }
        return normalized;

    }
    @Override
    public void delete(String relativePath) {
        Path target=resolve(relativePath);
        try{
            Files.deleteIfExists(target);
        }catch (IOException ex){
            throw new StorageException("could not delete the stored file",ex);
        }
    }

    @Override
    public Resource load(String relativePath) {
        Path path=resolve(relativePath);
        if(!Files.isReadable(path)){
            throw new ResourceNotFoundException("File not found on storage");
        }
        return new FileSystemResource(path);
    }
}
