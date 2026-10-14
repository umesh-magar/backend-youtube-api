package com.backendyoutube.youtubeapi.services;

import com.backendyoutube.youtubeapi.dto.channel.ChannelRequest;
import com.backendyoutube.youtubeapi.dto.channel.ChannelResponse;

import java.util.List;

public interface ChannelService {
    ChannelResponse createChannel(ChannelRequest request);
    List<ChannelResponse> getALlChannels();
    ChannelResponse getChannelById(Integer channelId);
    ChannelResponse getChannelByHandle(String channelHandle);
    ChannelResponse updateChannel(Integer channelId, ChannelRequest request);
    void deleteChannel(Integer channelId);

}
