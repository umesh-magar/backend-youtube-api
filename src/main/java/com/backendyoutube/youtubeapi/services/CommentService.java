package com.backendyoutube.youtubeapi.services;

import com.backendyoutube.youtubeapi.dto.comment.CommentRequestDto;
import com.backendyoutube.youtubeapi.dto.comment.CommentResponseDto;
import com.backendyoutube.youtubeapi.dto.video.VideoRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
     CommentResponseDto create(Integer videoId, CommentRequestDto requestDto);
     Page<CommentResponseDto> getComment(Integer videoId, Pageable pageable);
     public CommentResponseDto updateComment(Integer commentId, CommentRequestDto requestDto);
     public void deleteComment(Integer videoId);
}
