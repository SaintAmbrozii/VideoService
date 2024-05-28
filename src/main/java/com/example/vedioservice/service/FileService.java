package com.example.vedioservice.service;

import com.example.vedioservice.exception.BadResourceLocationException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class FileService {



    public Mono<Void> deleteFile(Path filePath) throws BadResourceLocationException{
        return Mono.just(filePath.toFile())
                .filter(File::exists)
                .flatMap(file -> Mono.just(FileUtils.deleteQuietly(file)))
                .filter(result -> result)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadResourceLocationException("Неправильно указан ресурс"))))
                .then();
    }
}
