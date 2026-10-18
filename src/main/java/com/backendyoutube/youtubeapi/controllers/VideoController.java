package com.backendyoutube.youtubeapi.controllers;

import com.backendyoutube.youtubeapi.dto.video.VideoRequestDto;
import com.backendyoutube.youtubeapi.dto.video.VideoResponseDto;
import com.backendyoutube.youtubeapi.services.VideoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1")
public class VideoController {
    private final VideoService videoService;
    public VideoController(VideoService videoService){
        this.videoService=videoService;
    }
    @PostMapping(value = "/channels/{channelId}/videos",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<VideoResponseDto> createVideo(@PathVariable Integer channelId,
                                                 @RequestPart("body") VideoRequestDto requestDto,
                                                 @RequestPart("file") MultipartFile file,
                                                 @RequestPart(value = "thumbnail",required = false) MultipartFile thumbnail
                                               ){
        VideoResponseDto responseDto=videoService.createVideo(file,requestDto,channelId,thumbnail);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @GetMapping("/videos/{videoId}")
    ResponseEntity<VideoResponseDto> getVideoById(@PathVariable Integer videoId){
        VideoResponseDto responseDto=videoService.getVideoById(videoId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @GetMapping("/videos")
    ResponseEntity<Page<VideoResponseDto>> getAllVideo(
            @PageableDefault(size=20,
            sort="createdAt",
            direction= Sort.Direction.ASC)
            Pageable pageable){
        Page<VideoResponseDto>responseDto=videoService.getAllVideos(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PutMapping(value = "videos/{videoId}",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<VideoResponseDto> updateMovie( @PathVariable Integer videoId,
                                                   @Valid @RequestPart(value = "body") VideoRequestDto requestDto,
                                                   @RequestPart(value = "file",required = false) MultipartFile file,
                                                  @RequestPart(value = "thumbnail",required = false) MultipartFile thumbnail
                                                   ){
        VideoResponseDto responseDto=videoService.updateVideo(videoId,requestDto,file,thumbnail);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @DeleteMapping("/videos/{videoId}")
    ResponseEntity<String>  DeleteVideo(@PathVariable Integer videoId){
        videoService.deleteVideo(videoId);
        return  ResponseEntity.ok("video deleted successfully");
    }
}
