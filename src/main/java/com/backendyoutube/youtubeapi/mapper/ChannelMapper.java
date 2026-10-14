package com.backendyoutube.youtubeapi.mapper;

import com.backendyoutube.youtubeapi.dto.channel.ChannelRequest;
import com.backendyoutube.youtubeapi.dto.channel.ChannelResponse;
import com.backendyoutube.youtubeapi.entities.Channel;
import com.backendyoutube.youtubeapi.repository.ChannelRepository;
import org.springframework.stereotype.Component;

@Component
public class ChannelMapper {
    public Channel toEntity(ChannelRequest request){
        return Channel.builder().
                channelName(request.getChannelName())
                .channelHandle(request.getChannelHandle())
                .description(request.getDescriptions())
                .build();
    }
    public ChannelResponse toResponse(Channel channel){
        return  ChannelResponse.builder().
                channelId(channel.getChannelId())
                .channelName(channel.getChannelName())
                .channelHandle(channel.getChannelHandle())
                .description(channel.getDescription())
                .build();
    }
    public void updateEntity(ChannelRequest request,Channel channel){
        channel.setChannelName(request.getChannelName());
        channel.setChannelHandle(request.getChannelHandle());
        channel.setDescription(request.getDescriptions());
    }

}
