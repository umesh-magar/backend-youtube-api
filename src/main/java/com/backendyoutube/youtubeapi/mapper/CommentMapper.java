package com.backendyoutube.youtubeapi.mapper;

import com.backendyoutube.youtubeapi.dto.comment.CommentRequestDto;
import com.backendyoutube.youtubeapi.dto.comment.CommentResponseDto;
import com.backendyoutube.youtubeapi.entities.Comment;
import com.backendyoutube.youtubeapi.entities.Video;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
   public Comment toEntity(CommentRequestDto requestDto, Video video){
        return Comment.builder()
                .content(requestDto.getContent())
                .video(video).
                build();
    }
    public CommentResponseDto toResponse(Comment comment){
       return CommentResponseDto.builder()
               .commentId(comment.getCommentId())
               .content(comment.getContent()).build();
    }
  public void update(Comment comment,CommentRequestDto requestDto){
       comment.setContent(requestDto.getContent());
  }

}
