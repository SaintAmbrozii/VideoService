package com.example.vedioservice.repository;

import com.example.vedioservice.domain.VideoFile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface VideoRepository extends ReactiveCrudRepository<VideoFile, Long> {

    Mono<VideoFile> findByTitle(String title);

}
