package com.example.JobHunter.controller.admin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.JobHunter.Util.annotation.ApiMessage;
import com.example.JobHunter.Util.error.StorageException;
import com.example.JobHunter.domain.dto.response.file.ResUploadFile;
import com.example.JobHunter.service.FileService;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Value("${thangka.upload-file.base-uri}")
    private String baseURI;
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/files")
    @ApiMessage("Download a file")
    public ResponseEntity<Resource> downLoadFile(
            @RequestParam(name = "fileName", required = false) String filename,
            @RequestParam(name = "folder", required = false) String folder)
            throws StorageException, URISyntaxException, FileNotFoundException {
        if (filename == null || folder == null) {
            throw new StorageException("Missing required params :(filename or folder)");
        }
        long fileLength = this.fileService.getFileLength(filename, folder);
        if (fileLength == 0) {
            throw new StorageException("File with name = " + filename + " not found.");
        }

        InputStreamResource resource = this.fileService.getResource(filename, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/files")
    @ApiMessage("Upload file")
    public ResponseEntity<ResUploadFile> uploadFile(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {

        // Check Valid
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty, please select a file to upload");
        }
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
        String fileName = file.getOriginalFilename();
        Boolean isValid = allowedExtensions.stream().anyMatch(fileName::endsWith);
        if (!isValid) {
            throw new StorageException(
                    "File extension is not allowed, please select a file with extension jpg, jpeg, png");
        }

        // create a new Folder
        this.fileService.createFolder(baseURI + folder);

        // store the file
        String currentFilename = this.fileService.store(file, folder);

        ResUploadFile resUploadFile = new ResUploadFile(currentFilename, Instant.now());

        return ResponseEntity.ok().body(resUploadFile);
    }
}
