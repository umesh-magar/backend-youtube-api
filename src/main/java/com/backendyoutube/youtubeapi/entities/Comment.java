package com.backendyoutube.youtubeapi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name="comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;
    @Column(nullable = false,length = 500)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "video_id",nullable = false)
    private Video video;
    private LocalDateTime createdAt;
    @PrePersist
    public void  onCreated(){
        createdAt=LocalDateTime.now();
    }
}
