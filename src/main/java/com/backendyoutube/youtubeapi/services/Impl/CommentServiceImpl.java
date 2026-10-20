package com.backendyoutube.youtubeapi.services.Impl;

import com.backendyoutube.youtubeapi.dto.comment.CommentRequestDto;
import com.backendyoutube.youtubeapi.dto.comment.CommentResponseDto;
import com.backendyoutube.youtubeapi.dto.video.VideoRequestDto;
import com.backendyoutube.youtubeapi.services.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public CommentResponseDto create(Integer videoId, VideoRequestDto requestDto) {
        return null;
    }

    @Override
    public List<CommentResponseDto> getComment(Integer commentId) {
        return List.of();
    }

    @Override
    public CommentResponseDto updateComment(Integer commentId, CommentRequestDto requestDto) {
        return null;
    }

    @Override
    public void deleteComment(Integer videoId) {

    }
}
