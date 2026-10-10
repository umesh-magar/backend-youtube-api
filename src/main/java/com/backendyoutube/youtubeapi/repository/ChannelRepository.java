package com.backendyoutube.youtubeapi.repository;

import com.backendyoutube.youtubeapi.entities.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ChannelRepository extends JpaRepository<Channel,Integer> {
    Optional<Channel> findByHandle(String Handle);
    boolean exitsByHandle(String handle);

}
