package com.backendyoutube.youtubeapi.services.Impl;

import com.backendyoutube.youtubeapi.dto.channel.ChannelRequest;
import com.backendyoutube.youtubeapi.dto.channel.ChannelResponse;
import com.backendyoutube.youtubeapi.entities.Channel;
import com.backendyoutube.youtubeapi.exceptions.ConflictException;
import com.backendyoutube.youtubeapi.exceptions.ResourceNotFoundException;
import com.backendyoutube.youtubeapi.mapper.ChannelMapper;
import com.backendyoutube.youtubeapi.repository.ChannelRepository;
import com.backendyoutube.youtubeapi.repository.VideoRepository;
import com.backendyoutube.youtubeapi.services.ChannelService;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;
    private final VideoRepository videoRepository;
    public ChannelServiceImpl(ChannelRepository channelRepository, ChannelMapper channelMapper, VideoRepository videoRepository){
        this.channelRepository=channelRepository;
        this.channelMapper=channelMapper;
        this.videoRepository=videoRepository;
    }
    @Override
    @Transactional
    public ChannelResponse createChannel(ChannelRequest request) {

        if(channelRepository.existsByChannelHandle(request.getChannelHandle())){
            throw new ConflictException("handle already exists");
        }
        Channel channel=channelMapper.toEntity(request);
        Channel savedChannel =channelRepository.save(channel);
        return channelMapper.toResponse(savedChannel);
    }

    @Override
    @Transactional(readOnly=true)
    public List<ChannelResponse> getALlChannels() {
       List <Channel> channels=channelRepository.findAll();
       return channels
               .stream().
               map(channelMapper::toResponse)
               .toList();
//       List<ChannelResponse> channelDtos=new ArrayList<>();
//       for(Channel channel:channels){
//           ChannelResponse channelResponse=channelMapper.toResponse(channel);
//           channelDtos.add(channelResponse);
//       }
//        return channelDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelResponse getChannelById(Integer channelId) {
        Channel channel=channelRepository.findById(channelId).
                orElseThrow(()->new ResourceNotFoundException("channel not found with id " + channelId));
        return channelMapper.toResponse(channel);
    }

    @Override
    @Transactional(readOnly=true)
    public ChannelResponse getChannelByHandle(String channelHandle) {
        Channel channel=channelRepository.findByChannelHandle(channelHandle).
                orElseThrow(()->new ResourceNotFoundException("channel not found wih handle " + channelHandle));
        return channelMapper.toResponse(channel);
    }

    @Override
    @Transactional
    public ChannelResponse updateChannel(Integer channelId, ChannelRequest request) {
        Channel channel=channelRepository.findById(channelId).orElseThrow(()->new
                ResourceNotFoundException("channel not found with id " + channelId));
        channelRepository.findByChannelHandle(request.getChannelHandle()).ifPresent(existingChannel->{
            if(!existingChannel.getChannelId().equals(channelId)){
                throw new ConflictException("Channel handle already exists");
            }
        });
       channelMapper.updateEntity(request,channel);
       Channel updatedChannel= channelRepository.save(channel);
        return channelMapper.toResponse(updatedChannel);
    }

    @Override
    @Transactional
    public void deleteChannel(Integer channelId) {
        Channel channel=channelRepository.findById(channelId).
                orElseThrow(()->new ResourceNotFoundException("channel not found with id: " + channelId));
        if(videoRepository.existsByChannel_ChannelId(channelId)){
            throw new ConflictException("cannot delete the channel at contains video");
        }
        channelRepository.deleteById(channelId);

    }
}
