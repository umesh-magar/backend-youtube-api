package com.backendyoutube.youtubeapi.services;

import com.backendyoutube.youtubeapi.dto.comment.CommentRequestDto;
import com.backendyoutube.youtubeapi.dto.comment.CommentResponseDto;
import com.backendyoutube.youtubeapi.dto.video.VideoRequestDto;

import java.util.List;

public interface CommentService {
     CommentResponseDto create(Integer videoId, VideoRequestDto requestDto);
     List <CommentResponseDto> getComment(Integer commentId);
     public CommentResponseDto updateComment(Integer commentId, CommentRequestDto requestDto);
     public void deleteComment(Integer videoId);
}
