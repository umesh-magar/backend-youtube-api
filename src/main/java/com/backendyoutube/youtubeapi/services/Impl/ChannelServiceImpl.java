package com.backendyoutube.youtubeapi.services.Impl;

import com.backendyoutube.youtubeapi.dto.channel.ChannelRequest;
import com.backendyoutube.youtubeapi.dto.channel.ChannelResponse;
import com.backendyoutube.youtubeapi.repository.ChannelRepository;
import com.backendyoutube.youtubeapi.services.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    public ChannelServiceImpl(ChannelRepository channelRepository){
        this.channelRepository=channelRepository;
    }
    @Override
    public ChannelResponse createChannel(ChannelRequest request) {
        if(channelRepository.exitsByHandle(request.getChannelHandle())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Channel handle already exists");
        }

        return null;
    }

    @Override
    public List<ChannelResponse> getALlChannel() {
        return List.of();
    }

    @Override
    public ChannelResponse getChannelById(Integer channelId) {
        return null;
    }

    @Override
    public ChannelResponse getChannelByHandle(String channelHandle) {
        return null;
    }

    @Override
    public ChannelResponse updateChannel(Long Id, ChannelRequest request) {
        return null;
    }

    @Override
    public void deleteChannel(Integer ChannelId) {

    }
}
