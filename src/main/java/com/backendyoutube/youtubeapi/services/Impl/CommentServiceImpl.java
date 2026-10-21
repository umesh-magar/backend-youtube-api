package com.backendyoutube.youtubeapi.services.Impl;

import com.backendyoutube.youtubeapi.dto.comment.CommentRequestDto;
import com.backendyoutube.youtubeapi.dto.comment.CommentResponseDto;
import com.backendyoutube.youtubeapi.dto.video.VideoRequestDto;
import com.backendyoutube.youtubeapi.entities.Comment;
import com.backendyoutube.youtubeapi.entities.Video;
import com.backendyoutube.youtubeapi.exceptions.ResourceNotFoundException;
import com.backendyoutube.youtubeapi.mapper.CommentMapper;
import com.backendyoutube.youtubeapi.repository.CommentRepository;
import com.backendyoutube.youtubeapi.repository.VideoRepository;
import com.backendyoutube.youtubeapi.services.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    public CommentServiceImpl(VideoRepository videoRepository, CommentRepository commentRepository, CommentMapper commentMapper){
        this.videoRepository=videoRepository;
        this.commentRepository=commentRepository;
        this.commentMapper=commentMapper;

    }
    @Override
    public CommentResponseDto create(Integer videoId, CommentRequestDto requestDto) {
        Video video=videoRepository.findById(videoId).orElseThrow(()->new ResourceNotFoundException("video not found"));
        Comment comment=commentMapper.toEntity(requestDto,video);
        return commentMapper.responseDto(comment);
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
