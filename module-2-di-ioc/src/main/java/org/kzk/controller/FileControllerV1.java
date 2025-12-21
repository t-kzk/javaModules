package org.kzk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kzk.data.entity.UserRole;
import org.kzk.dto.FileInfoDto;
import org.kzk.mapper.FileMapper;
import org.kzk.security.CustomPrincipal;
import org.kzk.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileControllerV1 {
    private final FileService fileService;
    private final FileMapper fileMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<FileInfoDto> upload(
            @RequestPart("file") FilePart file,
            Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return fileService.uploadFile(principal.getUserId(), file).map(fileMapper::map);
    }

    @GetMapping
    public Flux<FileInfoDto> getAll(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        if (UserRole.ADMIN_ROLE.equals(principal.getRole())) {
            return fileService.findAll().map(fileMapper::map);
        } else {
            return fileService.findAllByUserId(principal.getUserId()).map(fileMapper::map);
        }
    }

    @GetMapping("/{fileId}")
    public Mono<Resource> download(
            @PathVariable Integer fileId,
            Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return fileService.downloadFile(fileId);
    }

    @PutMapping(value = "/{fileId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<FileInfoDto> update(
            @PathVariable Integer fileId,
            @RequestPart("file") FilePart file,
            Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return fileService.updateFile(principal.getUserId(), fileId, file).map(fileMapper::map);
    }

    @DeleteMapping(value = "/{fileId}")
    public Mono<Void> delete(
            @PathVariable Integer fileId,
            Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return fileService.deleteFile(principal.getUserId(), fileId);
    }


}
