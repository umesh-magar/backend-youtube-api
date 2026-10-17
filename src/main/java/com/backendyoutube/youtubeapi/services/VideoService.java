package com.backendyoutube.youtubeapi.services;

import com.backendyoutube.youtubeapi.dto.video.VideoRequestDto;
import com.backendyoutube.youtubeapi.dto.video.VideoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    VideoResponseDto createVideo(MultipartFile file ,VideoRequestDto requestDto,Integer channelId, MultipartFile thumbnail);
    VideoResponseDto getVideoById(Integer id);
//    List<VideoResponseDto> getAllVideo();
    Page<VideoResponseDto> getAllVideos(Pageable pageable);
    VideoResponseDto updateVideo(Integer id, VideoRequestDto requestDto,MultipartFile file, MultipartFile thumbnail);
    void deleteVideo(Integer id);
}
