package com.backendyoutube.youtubeapi.services.Impl;

import com.backendyoutube.youtubeapi.dto.video.VideoRequestDto;
import com.backendyoutube.youtubeapi.dto.video.VideoResponseDto;
import com.backendyoutube.youtubeapi.entities.Channel;
import com.backendyoutube.youtubeapi.entities.Video;
import com.backendyoutube.youtubeapi.exceptions.ResourceNotFoundException;
import com.backendyoutube.youtubeapi.helper.FileCleanupHelper;
import com.backendyoutube.youtubeapi.mapper.VideoMapper;
import com.backendyoutube.youtubeapi.repository.ChannelRepository;
import com.backendyoutube.youtubeapi.repository.VideoRepository;
import com.backendyoutube.youtubeapi.services.VideoService;
import com.backendyoutube.youtubeapi.storage.FileStorageService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final FileStorageService fileStorageService;
    private final ChannelRepository channelRepository;
    private FileCleanupHelper fileCleanupHelper;
    public VideoServiceImpl(VideoRepository videoRepository,VideoMapper videoMapper, FileStorageService fileStorageService, ChannelRepository channelRepository,FileCleanupHelper fileCleanupHelper){
        this.videoRepository=videoRepository;
        this.videoMapper=videoMapper;
        this.fileStorageService=fileStorageService;
        this.channelRepository=channelRepository;
        this.fileCleanupHelper=fileCleanupHelper;
    }
    @Override
    @Transactional
    public VideoResponseDto createVideo(MultipartFile file , VideoRequestDto requestDto,Integer channelId, MultipartFile thumbnail) {
        Channel channel=channelRepository.findById(channelId).orElseThrow(()->new ResourceNotFoundException("Channel not found with id :" + channelId));
        String videoKey=null;
        String thumbnailKey=null;
        try{
            String videoExtension=getExtension(file);
            String thumbnailExtension=getExtension(file);
            videoKey=fileStorageService.store(file,"videos",videoExtension);

            if(thumbnail!=null || !thumbnail.isEmpty()){
                thumbnailKey=fileStorageService.store(file,"thumbnails",thumbnailExtension);
            }
            Video video=videoMapper.toEntity(requestDto,videoKey,thumbnailKey);
            video.setChannel(channel);
            Video saveVideo=videoRepository.saveAndFlush(video);
            return videoMapper.toResponse(saveVideo);
        }catch (RuntimeException ex){
          fileCleanupHelper.deleteAfterFailure(thumbnailKey,ex);
          fileCleanupHelper.deleteAfterFailure(videoKey,ex);
          throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public VideoResponseDto getVideoById(Integer id) {
        Video video=videoRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("video not found with ths id :"+ id));

        return videoMapper.toResponse(video);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VideoResponseDto> getAllVideo() {
        List<Video> videos=videoRepository.findAll();
        return videos.stream().map(videoMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public VideoResponseDto updateVideo(Integer id, VideoRequestDto requestDto,MultipartFile file, MultipartFile thumbnail) {
        Video video=videoRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("video not found with this id :" +id));
        String thumbnailKey=null;
        String videoKey=null;
        try{
            String thumbnailExtension=getExtension(thumbnail);
            String videoExtension=getExtension(file);
            if(file!=null){
                videoKey=fileStorageService.store(file,"videos",videoExtension);
            }
            if(thumbnail!=null || !thumbnail.isEmpty()){
                thumbnailKey=fileStorageService.store(thumbnail,"thumbnails",thumbnailExtension);
            }
           videoMapper.updateVideo(video,requestDto,thumbnailKey,videoKey);
            Video savedVideo=videoRepository.saveAndFlush(video);
            return videoMapper.toResponse(savedVideo);
        }catch (RuntimeException ex){
                fileCleanupHelper.deleteAfterFailure(videoKey, ex);
                fileCleanupHelper.deleteAfterFailure(thumbnailKey, ex);
                throw ex;
        }
    }

    @Override
    public void deleteVideo(Integer id) {

    }
    private String getExtension(MultipartFile file){
        if(file==null){
            return null;
        }
        return StringUtils.getFilenameExtension(file.getOriginalFilename());
    }
}
