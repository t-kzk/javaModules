package org.kzk.controller;

import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.kzk.data.FileRepository;
import org.kzk.data.entity.FileE;
import org.kzk.minio.MinioService;
import org.kzk.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.io.File;

@Slf4j
@RestController
@RequestMapping("/api/v1/file")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<FileE> upload(@RequestPart("file") FilePart file, ServerHttpRequest request) {
        return fileService.createFile(1, file);
    }

    @GetMapping
    public Flux<FileE> getAll() {
        return fileService.findAll();

    }
}
