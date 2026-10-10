package com.backendyoutube.youtubeapi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "channels")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer channelId;
    @Column(nullable = false,length = 100)
    private String channelName;
    @Column(nullable = false,unique = true,length = 100)
    private String channelHandle;
    @Column(length = 1000)
    private String description;
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @PrePersist
    void onCreated(){
        createdAt=LocalDateTime.now();
    }
}
