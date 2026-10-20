package com.backendyoutube.youtubeapi.dto.comment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Integer videoId;
    private String content;
}
