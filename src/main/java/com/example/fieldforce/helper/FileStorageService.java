package com.example.fieldforce.helper;

import com.example.fieldforce.exception.FfaException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Helper class providing the functions for
 * loadingFileAsByteArray, loadingFileAsByteResource, loadingFileAsResource
 * storingMultiPartFile
 */
@Service
public class FileStorageService {

    public Resource loadFileAsResource(String fileLocation) throws Exception {
        try {
            Path filePath = Paths.get(fileLocation).toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FfaException("FileNotFound", "No File Found.., please generate it first");
            }
        } catch (MalformedURLException ex) {
            throw new FfaException("FileNotFound", "No File Found.., please try to generate one");
        }
    }

    public ByteArrayResource loadFileAsByteArrayResource(String fileLocation) throws IOException {

        System.out.println("fileName: " + fileLocation);

        Path path = Paths.get(fileLocation);
        byte[] data = Files.readAllBytes(path);
        ByteArrayResource resource = new ByteArrayResource(data);

        return resource;
    }

    public byte[] loadFileAsBytes(String fileLocation) throws IOException {

        System.out.println("fileName: " + fileLocation);

        Path path = Paths.get(fileLocation);
        byte[] data = Files.readAllBytes(path);

        return data;
    }

    public String storeFile(String filePath, MultipartFile file) throws Exception {
        // Normalize file name

        try {
            // Copy file to the target location (Replacing existing file with the custom name)
            Path targetLocation = Paths.get(filePath).toAbsolutePath().normalize();
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "File Uploaded Successfully";
        } catch (Exception ex) {
            throw new FfaException("File Upload Error", "Error occured while uploading file");
        }
    }
}
