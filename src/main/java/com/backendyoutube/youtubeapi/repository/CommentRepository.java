package com.backendyoutube.youtubeapi.repository;

import com.backendyoutube.youtubeapi.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByVideo_Id(Integer videoId);
    List<Comment> findByVideo_IdOrderByCreatedAtDesc(Integer videoId);
}
