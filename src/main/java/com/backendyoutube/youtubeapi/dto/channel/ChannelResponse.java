package com.backendyoutube.youtubeapi.dto.channel;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelResponse {
    private Integer channelId;
    private String channelName;
    private String channelHandle;
    private String description;
    private LocalDateTime createdAt;
}
