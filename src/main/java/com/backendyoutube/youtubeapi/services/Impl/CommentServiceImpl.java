package com.backendyoutube.youtubeapi.services.Impl;

import com.backendyoutube.youtubeapi.dto.comment.CommentRequestDto;
import com.backendyoutube.youtubeapi.dto.comment.CommentResponseDto;
import com.backendyoutube.youtubeapi.entities.Comment;
import com.backendyoutube.youtubeapi.entities.Video;
import com.backendyoutube.youtubeapi.exceptions.ResourceNotFoundException;
import com.backendyoutube.youtubeapi.mapper.CommentMapper;
import com.backendyoutube.youtubeapi.repository.CommentRepository;
import com.backendyoutube.youtubeapi.repository.VideoRepository;
import com.backendyoutube.youtubeapi.services.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public CommentResponseDto create(Integer videoId, CommentRequestDto requestDto) {
        Video video=videoRepository.findById(videoId).orElseThrow(()->
                new ResourceNotFoundException("video not found with id :" + videoId));
        Comment comment=commentMapper.toEntity(requestDto,video);
        commentRepository.save(comment);
        return commentMapper.toResponse(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getComment(Integer videoId, Pageable pageable) {
        if(!videoRepository.existsById(videoId)){
            throw new ResourceNotFoundException("video not found with id: " + videoId);
        };
        return commentRepository.findByVideo_VideoId(videoId,pageable).map(commentMapper::toResponse);
    }
    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(Integer commentId){
        Comment comment=commentRepository.findById(commentId).orElseThrow(()->
                new ResourceNotFoundException("comment not found with id: " + commentId));
        return commentMapper.toResponse(comment);
    }


    @Override
    @Transactional
    public CommentResponseDto updateComment(Integer commentId, CommentRequestDto requestDto) {
        Comment comment=commentRepository.findById(commentId).orElseThrow(()->
                new ResourceNotFoundException("comment not found with id: " + commentId));
        commentMapper.update(comment,requestDto);
        Comment savedComment=commentRepository.saveAndFlush(comment);
        return commentMapper.toResponse(savedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Integer commentId) {
        Comment comment=commentRepository.findById(commentId).orElseThrow(()->
                new ResourceNotFoundException("comment not found with id: " + commentId));
        commentRepository.delete(comment);

    }
}
