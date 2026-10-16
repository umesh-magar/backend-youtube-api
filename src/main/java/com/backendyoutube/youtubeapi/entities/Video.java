package com.backendyoutube.youtubeapi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "videos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer videoId;
    @Column(nullable = false,length = 200)
    private String title;
    @Column(length = 500)
    private String description;
    @Column(nullable = false,length = 500)
    private String videoUrl;
    @Column(length = 100)
    private String thumbnailUrl;
    @Column(nullable = false)
    private Integer durationSecond;
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="channel_id",nullable = false)
    private Channel channel;
    @PrePersist
    public void onCreated(){
        createdAt=LocalDateTime.now();
    }

}
