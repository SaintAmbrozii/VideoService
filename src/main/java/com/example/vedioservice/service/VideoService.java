package com.example.vedioservice.service;

import com.example.vedioservice.payload.FileDataRecord;
import com.example.vedioservice.domain.VideoFile;
import com.example.vedioservice.dto.VideoFileDTO;
import com.example.vedioservice.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final FileService fileService;

    public Flux<VideoFileDTO> getAllVideos() {
        Flux<VideoFile> videoFlux = videoRepository.findAll();
        return videoFlux.
                map(videoFile -> {
                    VideoFileDTO videoFileDTO = new VideoFileDTO();
                    videoFileDTO.setId(videoFile.getId());
                    videoFileDTO.setName(videoFile.getTitle());
                    Path resourcePath = Path.of(videoFile.getRootPath()).normalize();
                    videoFileDTO.setUri(resourcePath.toUri());
                    return videoFileDTO;
                });
    }

    public Mono<VideoFile> updateById(Long id, FileDataRecord dataRecord) {
        return videoRepository.findById(id).
                flatMap(videoFile -> {
            videoFile.setTitle(dataRecord.title());
            videoFile.setDescription(dataRecord.description());
            videoFile.setUpdatedTime(LocalDateTime.now());
            return videoRepository.save(videoFile);
        });
    }

    public Mono<VideoFile> findById(Long id) {
        return videoRepository.findById(id);
    }





}
