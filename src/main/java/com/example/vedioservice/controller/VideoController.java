package com.example.vedioservice.controller;

import com.example.vedioservice.exception.BadResourceLocationException;
import com.example.vedioservice.exception.VideoNotFoundException;
import com.example.vedioservice.payload.FileDataRecord;
import com.example.vedioservice.domain.VideoFile;
import com.example.vedioservice.dto.VideoFileDTO;
import com.example.vedioservice.repository.VideoRepository;
import com.example.vedioservice.service.FileService;
import com.example.vedioservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final FileService fileService;
    private final VideoService videoService;
    private final VideoRepository videoRepository;


    @Value("${video.location}")
    private String videoLocation;

    @GetMapping
    public Flux<VideoFileDTO> getAllVideos() {
        return videoService.getAllVideos();
    }

    @PatchMapping("{id}")
    public Mono<VideoFile> updated(@PathVariable(name = "id")Long id, @RequestBody FileDataRecord fileDataRecord) {
        return videoService.updateById(id,fileDataRecord)
                .switchIfEmpty(Mono.error(VideoNotFoundException::new));
    }

    @GetMapping("get/{id}")
    public Mono<VideoFile> findById(@PathVariable(name = "id")Long id) {
        return videoService.findById(id).switchIfEmpty(Mono.error(VideoNotFoundException::new));
    }

    @GetMapping(value = "{name}")
    public Mono<UrlResource> getResourceByName(@PathVariable(name = "name") String name) {

        return videoRepository.findByTitle(name)
                .flatMap(this::createUriResourceFromVideo)
                .switchIfEmpty(Mono.error(VideoNotFoundException::new));
    }

    private Mono<UrlResource> createUriResourceFromVideo(VideoFile videoFile) {
        return Mono.<UrlResource>create(monoSink -> {
            try {
                Path pathResource = Path.of(videoFile.getRootPath()).normalize();

                log.info("pathResource",pathResource.toString());

                System.out.println(pathResource.toString());
                UrlResource video = new UrlResource(pathResource.toUri());
                monoSink.success(video);
            } catch (MalformedURLException e) {
                monoSink.error(e);
            }
        }).doOnError(throwable -> {
            throw Exceptions.propagate(throwable);
        });
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<VideoFile> upload(@RequestPart(value = "video",required = true) Mono<FilePart> filePartMono,
                                  @RequestPart(value = "data",required = false) FileDataRecord dataRecord) {
        return filePartMono.flatMap(
                filePart -> {

                    String randomName = UUID.randomUUID()
                            .toString().substring(0, 13);

                    String newFileName = randomName+"_"+filePart.filename();
                    String filePath = Paths.get(videoLocation,newFileName).normalize().toAbsolutePath()
                            .toString();
                    System.out.println(filePath);
                    VideoFile videoFile = VideoFile.builder()
                            .title(dataRecord.title())
                            .fileName(filePart.filename())
                            .description(dataRecord.description())
                            .rootPath(filePath).createdTime(LocalDateTime.now()).build();

                    return videoRepository.save(videoFile)
                            .doOnNext(savedFile-> filePart.transferTo(Path.of(filePath))
                                    .subscribe())
                            .then(Mono.just(videoFile))
                            .switchIfEmpty(Mono.error(new BadResourceLocationException("ошибка пути хранилища")));

                }
        );

    }
    @DeleteMapping("{id}")
    public Mono<VideoFile> deleteVideo(@PathVariable(name = "id") Long id) {
        return videoService.findById(id)
                .flatMap(videoFile -> {
                return fileService.deleteFile(Path.of(videoFile.getRootPath()))
                        .switchIfEmpty(Mono.error(VideoNotFoundException::new))
                            .doOnNext(file->videoRepository.delete(videoFile))
                          .then(Mono.empty());

                });
    }





}

