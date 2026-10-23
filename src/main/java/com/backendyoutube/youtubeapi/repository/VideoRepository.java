package com.backendyoutube.youtubeapi.repository;

import com.backendyoutube.youtubeapi.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VideoRepository extends JpaRepository<Video,Integer> {

    List<Video> findByChannel_ChannelId(Integer channelId);
    List<Video> findByChannel_ChannelIdOrderByCreatedAtDesc(Integer channelId);
    boolean existsByChannel_ChannelId(Integer channelId);

}
