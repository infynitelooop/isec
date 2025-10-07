package com.infyniteloop.runningroom.util;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest {
    @Schema(type = "string", format = "binary", description = "CSV file to upload")
    private MultipartFile file;

    // getter & setter
    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }
}