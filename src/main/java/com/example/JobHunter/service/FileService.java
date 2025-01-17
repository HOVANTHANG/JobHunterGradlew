package com.example.JobHunter.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    @Value("${thangka.upload-file.base-uri}")
    private String baseURI;

    public void createFolder(String folderName) throws URISyntaxException {
        // Create folder in baseURI
        URI uri = new URI(folderName);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.exists()) {
            try {
                Files.createDirectories(tmpDir.toPath());
                System.out.println("Folder created: " + tmpDir.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Folder already exists: " + tmpDir.toPath());
        }

    }

    public String store(MultipartFile file, String folder)
            throws URISyntaxException, IOException {
        String finalName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        URI uri = new URI(baseURI + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File stored: " + path);
        }
        return finalName;
    }

    public long getFileLength(String filename, String folder) throws URISyntaxException {
        URI uri = new URI(baseURI + folder + "/" + filename);
        Path path = Paths.get(uri);

        File tmpDir = new File(path.toString());
        if (!tmpDir.exists() || tmpDir.isDirectory()) {
            return 0;
        }
        return tmpDir.length();
    }

    public InputStreamResource getResource(String filename, String folder)
            throws URISyntaxException, FileNotFoundException {

        URI uri = new URI(baseURI + folder + "/" + filename);
        Path path = Paths.get(uri);

        File file = new File(path.toString());

        return new InputStreamResource(new FileInputStream(file));

    }

}
