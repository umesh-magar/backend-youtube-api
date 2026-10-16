package com.backendyoutube.youtubeapi.dto.video;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoResponseDto {
    private Integer videoId;
    private String title;
    private String description;
    private Integer durationSecond;
    private String videoUrl;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
}
