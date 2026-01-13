package com.infyniteloop.runningroom.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class FileUploadRequest {
    @Schema(type = "string", format = "binary", description = "CSV file to upload")
    private MultipartFile file;

}