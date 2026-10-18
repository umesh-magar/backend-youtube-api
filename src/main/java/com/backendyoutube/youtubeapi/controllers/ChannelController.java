package com.backendyoutube.youtubeapi.controllers;

import com.backendyoutube.youtubeapi.dto.channel.ChannelRequest;
import com.backendyoutube.youtubeapi.dto.channel.ChannelResponse;
import com.backendyoutube.youtubeapi.services.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ChannelController {
    private ChannelService channelService;
    public ChannelController(ChannelService channelService){
        this.channelService=channelService;
    }
    @PostMapping("/channels")
    ResponseEntity<ChannelResponse> createChannel(@RequestBody ChannelRequest request){
        ChannelResponse response=channelService.createChannel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/channels/{channelId}")
    ResponseEntity<ChannelResponse> getChannelById(@PathVariable Integer channelId){
        ChannelResponse response=channelService.getChannelById(channelId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/Channels")
    ResponseEntity<List<ChannelResponse>> getAllChannel(){
        List<ChannelResponse> response=channelService.getALlChannels();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/channels/{channelHandle)")
    ResponseEntity<ChannelResponse> getChannelByHandle(@PathVariable String channelHandle){
        ChannelResponse response=channelService.getChannelByHandle(channelHandle);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/channels/{channelId}")
    ResponseEntity<ChannelResponse> updateChannel(@PathVariable Integer channelId, @RequestBody ChannelRequest request){
        ChannelResponse response=channelService.updateChannel(channelId,request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/channels/{channelId}")
    ResponseEntity<String> deleteChannel(@PathVariable Integer channelId){
       channelService.deleteChannel(channelId);
       return ResponseEntity.status(HttpStatus.OK).body("channel deleted successfully");
    }
}
