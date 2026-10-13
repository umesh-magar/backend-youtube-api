package com.backendyoutube.youtubeapi.services.Impl;

import com.backendyoutube.youtubeapi.dto.channel.ChannelRequest;
import com.backendyoutube.youtubeapi.dto.channel.ChannelResponse;
import com.backendyoutube.youtubeapi.entities.Channel;
import com.backendyoutube.youtubeapi.exceptions.ConflictException;
import com.backendyoutube.youtubeapi.exceptions.NotFoundException;
import com.backendyoutube.youtubeapi.mapper.ChannelMapper;
import com.backendyoutube.youtubeapi.repository.ChannelRepository;
import com.backendyoutube.youtubeapi.services.ChannelService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;
    public ChannelServiceImpl(ChannelRepository channelRepository, ChannelMapper channelMapper){
        this.channelRepository=channelRepository;
        this.channelMapper=channelMapper;
    }
    @Override
    public ChannelResponse createChannel(ChannelRequest request) {

        if(channelRepository.existsByChannelHandle(request.getChannelHandle())){
            throw new ConflictException("handle already exists");
        }
        Channel channel=channelMapper.toEntity(request);
        Channel savedChannel =channelRepository.save(channel);
        return channelMapper.toResponse(savedChannel);
    }

    @Override
    public List<ChannelResponse> getALlChannel() {
       List <Channel> channels=channelRepository.findAll();
       List<ChannelResponse> channelDtos=new ArrayList<>();
       for(Channel channel:channels){
           ChannelResponse channelResponse=channelMapper.toResponse(channel);
           channelDtos.add(channelResponse);
       }
        return channelDtos;
    }

    @Override
    public ChannelResponse getChannelById(Integer channelId) {
        Channel channel=channelRepository.findById(channelId).orElseThrow(()->new NotFoundException("channel not found"));
        return channelMapper.toResponse(channel);
    }

    @Override
    public ChannelResponse getChannelByHandle(String channelHandle) {
        Channel channel=channelRepository.findByChannelHandle(channelHandle).orElseThrow(()->new NotFoundException("channel not found"));
        return channelMapper.toResponse(channel);
    }

    @Override
    public ChannelResponse updateChannel(Integer id, ChannelRequest request) {
        Channel channel=channelRepository.findById(id).orElseThrow(()->new NotFoundException("channel not found"));
        Channel channel1=channelMapper.toEntity(request);
        channelRepository.save(channel1);
        return channelMapper.toResponse(channel1);
    }

    @Override
    public void deleteChannel(Integer ChannelId) {

    }
}
