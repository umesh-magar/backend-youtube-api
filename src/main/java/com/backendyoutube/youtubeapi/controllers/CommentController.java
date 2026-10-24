package com.backendyoutube.youtubeapi.controllers;

import com.backendyoutube.youtubeapi.dto.comment.CommentRequestDto;
import com.backendyoutube.youtubeapi.dto.comment.CommentResponseDto;
import com.backendyoutube.youtubeapi.services.CommentService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    private final CommentService commentService;
    public CommentController(CommentService commentService){
        this.commentService=commentService;
    }
    @PostMapping("/videos/{videoId}/comments")
    ResponseEntity<CommentResponseDto> createComment(@PathVariable Integer videoId, @RequestBody CommentRequestDto requestDto){
        CommentResponseDto responseDto=commentService.create(videoId,requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @GetMapping("/videos/{videoId}/comments")
    ResponseEntity<Page<CommentResponseDto>> getCommentsForVideos(@PathVariable Integer videoId, @PageableDefault(
            size = 20,
            sort = {"createdAt","commentId"},
            direction = Sort.Direction.DESC
    ) Pageable pageable){
        Page<CommentResponseDto> responseDtos=commentService.getComment(videoId,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
    @GetMapping("/comments/{commentId}")
    ResponseEntity<CommentResponseDto> getCommentHandler(@PathVariable Integer commentId){
        CommentResponseDto responseDto=commentService.getCommentById(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PutMapping("/comments/{commentId}")
    ResponseEntity<CommentResponseDto> updatedCommentHandler(@PathVariable Integer commentId,CommentRequestDto requestDto){
        CommentResponseDto responseDto=commentService.updateComment(commentId,requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @DeleteMapping("/comments/{commentId}")
    ResponseEntity<Void> deleteCommentHandler(@PathVariable Integer commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
