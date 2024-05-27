package com.example.vedioservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.annotation.Generated;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "videos")
public class VideoFile {

    @Id
    @Column("id")
    private Long id;

    @Column("path")
    private String rootPath;

    @Column("filename")
    private String fileName;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("created_time")
    public LocalDateTime createdTime;

    @Column("updated_time")
    public LocalDateTime updatedTime;
}
