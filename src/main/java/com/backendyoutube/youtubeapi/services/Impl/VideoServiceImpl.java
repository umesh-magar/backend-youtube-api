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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
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

//    @Override
//    @Transactional(readOnly = true)
//    public List<VideoResponseDto> getAllVideo() {
//
////        List<Video> videos=videoRepository.findAll();
////        return videos.stream().map(videoMapper::toResponse).toList();
//
//    }
@Override
@Transactional(readOnly = true)
   public Page<VideoResponseDto>getAllVideos(Pageable pageable){
      return videoRepository.findAll(pageable).map(videoMapper::toResponse);
  }

    @Override
    @Transactional
    public VideoResponseDto updateVideo(Integer id, VideoRequestDto requestDto,MultipartFile file, MultipartFile thumbnail) {
        Video video=videoRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("video not found with this id :" +id));
        String oldVideoKey=video.getVideoUrl();
        String oldThumbnailKey=video.getThumbnailUrl();
        String newThumbnailKey=null;
        String newVideoKey=null;
        try{
            String thumbnailExtension=getExtension(thumbnail);
            String videoExtension=getExtension(file);
            if(file!=null){
                newVideoKey=fileStorageService.store(file,"videos",videoExtension);
            }
            if(thumbnail!=null || !thumbnail.isEmpty()){
                newThumbnailKey=fileStorageService.store(thumbnail,"thumbnails",thumbnailExtension);
            }
            String effectiveVideoKey=newVideoKey!=null?newVideoKey:oldVideoKey;
            String effectiveThumbnailUrl=newThumbnailKey!=null? newThumbnailKey:oldThumbnailKey;
           videoMapper.updateVideo(video,requestDto,newThumbnailKey,newVideoKey);
            Video savedVideo=videoRepository.saveAndFlush(video);
            return videoMapper.toResponse(savedVideo);
        }catch (RuntimeException ex){
                fileCleanupHelper.deleteAfterFailure(newVideoKey, ex);
                fileCleanupHelper.deleteAfterFailure(newThumbnailKey, ex);
                throw ex;
        }
    }

    @Override
    @Transactional
    public void deleteVideo(Integer id) {
        Video video=videoRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("video not found with this id "+ id));
        String videoKey=video.getVideoUrl();
        String thumbnailKey=video.getThumbnailUrl();
        videoRepository.delete(video);
        videoRepository.flush();
        registerFileDeletionAfterCommit(videoKey,thumbnailKey);

    }
    private String getExtension(MultipartFile file){
        if(file==null){
            return null;
        }
        return StringUtils.getFilenameExtension(file.getOriginalFilename());
    }

    private void registerFileCleanupAfterTransaction(String oldVideoKey, String oldThumbnailKey,
                                                     String effectiveVideokey,String effectiveThumbnailKey,
                                                     String newVideoKey, String newThumbnailKey){
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if(status==TransactionSynchronization.STATUS_COMMITTED){
                            deleteReplacedFile(oldVideoKey, effectiveVideokey);
                            deleteReplacedFile(oldThumbnailKey,effectiveThumbnailKey);
                        }else {
                            deleteSafely(newThumbnailKey);
                            deleteSafely(newVideoKey);
                        }
                    }
                }
        );
    }
   private void deleteReplacedFile(String oldKey, String effectiveKey){
        if(oldKey!=null && !oldKey.isBlank() && !oldKey.equals(effectiveKey)){
            deleteSafely(oldKey);
        }
    }
    private void deleteSafely(String fileKey){
      if(fileKey==null){
          return;
      }
      try{
          fileStorageService.delete(fileKey);
      }catch (RuntimeException exception){
          log.warn("could not cleanup stored file :{}",fileKey,exception);
      }
    }
    private void registerFileDeletionAfterCommit(String videoKey, String thumbnailKey){
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                      deleteFileSafely(videoKey);
                      deleteFileSafely(thumbnailKey);
                    }
                }
        );
    }
    private void deleteFileSafely(String fileKey){
        if(fileKey!=null && fileKey.isBlank()){
            return;
        }
        try{
            fileStorageService.delete(fileKey);
        }catch (RuntimeException exception){
            log.error("video database record was deleted, but file cleanup failed for : {}",
                    fileKey,
                    exception);
        }
    }
}
