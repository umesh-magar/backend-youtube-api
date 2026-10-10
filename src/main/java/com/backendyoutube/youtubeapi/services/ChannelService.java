package com.backendyoutube.youtubeapi.services;

import com.backendyoutube.youtubeapi.dto.channel.ChannelRequest;
import com.backendyoutube.youtubeapi.dto.channel.ChannelResponse;

import java.util.List;

public interface ChannelService {
    ChannelResponse createChannel(ChannelRequest request);
    List<ChannelResponse> getALlChannel();
    ChannelResponse getChannelById(Integer channelId);
    ChannelResponse getChannelByHandle(String channelHandle);
    ChannelResponse updateChannel(Long Id, ChannelRequest request);
    void deleteChannel(Integer ChannelId);

}
