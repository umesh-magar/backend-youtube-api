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
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class LocalFileStorageService implements FileStorageService{
//    private static final Set<String> ALLOWED_DIRECTORIES=Set.of("videos","thumbnails");
//    private static final Set<String> VIDEO_EXTENSIONS=Set.of("mp4","mov","m4v","webm");
//    private static final Set<String> THUMBNAIL_EXTENSIONS=Set.of("jpg","jpeg","png","webp");
private static final Map<String, Set<String>> ALLOWED_EXTENSIONS =
        Map.of(
                "videos", Set.of("mp4", "mov", "m4v", "webm"),
                "thumbnails", Set.of("jpg", "jpeg", "png", "webp")
        );
    private final Path root;
    public LocalFileStorageService(StorageProperties properties){
        this.root=Path.of(properties.baseDir())
                .toAbsolutePath().normalize();
    }
    @PostConstruct
    void init(){
        try{
//            Files.createDirectories(resolve("videos"));
//            Files.createDirectories(resolve("thumbnails"));
//            log.info("local storage initialized");
            for(String directory:ALLOWED_EXTENSIONS.keySet()){
                Files.createDirectories(resolve(directory));
            }
        }catch (IOException ex) {
            throw new StorageException("Could not initialize local storage",ex);
        }
    }

    @Override
     public  String store(MultipartFile file, String directory, String extension){
        if(file==null || file.isEmpty()){
            throw new BadRequestException("A  file is required");
        }
        String safeDirectory=validateDirectory(directory);
        String safeExtension=validateExtension(directory,extension);
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
    @Override
    public Resource load(String relativePath) {
        Path path=resolvedStoredFile(relativePath);
        if(!Files.isRegularFile(path) || !Files.isReadable(path)){
            throw new ResourceNotFoundException("File not found in storage");
        }
        return new FileSystemResource(path);
    }

    @Override
    public void delete(String relativePath) {
//        Path target=resolve(relativePath);
        Path target=resolvedStoredFile(relativePath);
        try{

            Files.deleteIfExists(target);
        }catch (IOException ex){
            throw new StorageException("could not delete the stored file",ex);
        }
    }


    private  Path resolve(String relativePath){
        if(!StringUtils.hasText(relativePath)){
            throw new StorageException("path is required");
        }
        final Path suppliedPath;
        try{
            suppliedPath=Path.of(relativePath);
        }catch (InvalidPathException ex){
            throw new StorageException("Invalid storage path",ex);
        }
        if(suppliedPath.isAbsolute()|| !suppliedPath.normalize().equals(suppliedPath)){
            throw  new StorageException("Invalid storage path");
        }
        Path resolved=root.resolve(suppliedPath).normalize();
        if(resolved.equals(root) || !resolved.startsWith(root)){
            throw new StorageException("Invalid storage path ");
        }
        return resolved;
    }
    private Path resolvedStoredFile(String relativePath){
        if(!StringUtils.hasText(relativePath)){
            throw new BadRequestException("storage file path required");
        }
        final Path suppliedPath;
        try{
            suppliedPath=Path.of(relativePath);
        }catch (InvalidPathException ex){
            throw new StorageException("Invalid storage path", ex);
        }
        if(suppliedPath.isAbsolute() || suppliedPath.getNameCount()!=2 || !suppliedPath.normalize().equals(suppliedPath)) {
            throw new StorageException("Invalid storage path");
        }
        String directory=suppliedPath.getName(0).toString();
        String fileName=suppliedPath.getFileName().toString();
        validateDirectory(directory);
        String extension=StringUtils.getFilenameExtension(fileName);
        validateExtension(directory,extension);
        return resolve(suppliedPath.toString());
    }

    String validateDirectory(String directory){
        if(!StringUtils.hasText(directory) || !ALLOWED_EXTENSIONS.containsKey(directory)){
            throw new BadRequestException("Unsupported storage directory");
        }
        return directory;
    }

    String validateExtension(String directory, String extension){
        if(!StringUtils.hasText(extension)){
            throw new BadRequestException("File extension is required");
        }
        String normalized=extension.trim().toLowerCase(Locale.ROOT);
        if(normalized.startsWith(".")){
            normalized=normalized.substring(1);
        }
        Set<String> allowedExtensions = ALLOWED_EXTENSIONS.get(directory);

        if (allowedExtensions == null) {
            throw new BadRequestException("Unsupported storage directory");
        }

        if (!allowedExtensions.contains(normalized)) {
            throw new BadRequestException("Unsupported file extension");
        }
//        Set<String> allowedExtensions=directory.equals("videos")? VIDEO_EXTENSIONS: THUMBNAIL_EXTENSIONS;
//        if(!allowedExtensions.contains(normalized)){
//            throw new BadRequestException("Unsupported file extension");
//        }
        return normalized;

    }
}

