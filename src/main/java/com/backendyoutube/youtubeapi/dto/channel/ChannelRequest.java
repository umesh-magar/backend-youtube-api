package com.backendyoutube.youtubeapi.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChannelRequest {
    @NotBlank(message = "Channel name is required")
    @Size(max=100,message = "Channel name cannot exceeds 100 character")
    private String ChannelName;
    @NotBlank(message = "Channel handle is required")
    @Size(max=100, message = "Channel name cannot exceeds 100 character")
    private String channelHandle;
    @Size(max=1000,message = "descriptions cannot exceeds 1000 character")
    private String descriptions;

}
