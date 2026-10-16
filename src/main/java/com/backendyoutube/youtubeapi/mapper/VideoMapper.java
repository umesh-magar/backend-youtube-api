package com.backendyoutube.youtubeapi.mapper;

import com.backendyoutube.youtubeapi.dto.video.VideoRequestDto;
import com.backendyoutube.youtubeapi.dto.video.VideoResponseDto;
import com.backendyoutube.youtubeapi.entities.Video;

public class VideoMapper {
    public Video toEntity(VideoRequestDto requestDto, String storageKey, String thumbnailKey) {
        return Video.builder().
                title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .durationSecond(requestDto.getDurationSecond())
                .videoUrl(storageKey)
                .thumbnailUrl(thumbnailKey)
                .build();
    }

    public VideoResponseDto toResponse(Video video){
        return VideoResponseDto.builder()
                .videoId(video.getVideoId())
                .title(video.getTitle())
                .description(video.getDescription())
                .durationSecond(video.getDurationSecond())
                .videoUrl(video.getVideoUrl())
                .thumbnailUrl(video.getThumbnailUrl())
                .createdAt(video.getCreatedAt())
                .build();
    }

    public void updateVideo(Video video,VideoRequestDto requestDto,String thumbnailUrl, String videoUrl){
        video.setTitle(requestDto.getTitle());
        video.setDescription(requestDto.getDescription());
        video.setDurationSecond(requestDto.getDurationSecond());
        video.setVideoUrl(videoUrl);
        video.setThumbnailUrl(thumbnailUrl);
    }
}