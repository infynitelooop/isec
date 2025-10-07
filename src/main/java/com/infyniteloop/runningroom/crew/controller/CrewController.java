package com.infyniteloop.runningroom.crew.controller;


import com.infyniteloop.runningroom.crew.dto.CrewRequest;
import com.infyniteloop.runningroom.crew.dto.CrewResponse;
import com.infyniteloop.runningroom.crew.service.CrewService;
import com.infyniteloop.runningroom.util.FileUploadRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/crew")
@Tag(name = "Crew Import", description = "Upload crew data from CSV")
@RequiredArgsConstructor
public class CrewController {

    private final CrewService crewService;


    @PostMapping
    public CrewResponse createCrew(@org.springframework.web.bind.annotation.RequestBody CrewRequest dto) {
        return crewService.createCrew(dto);
    }

    @GetMapping("/{crewId}")
    public CrewResponse getCrew(@PathVariable String crewId) {
        return crewService.getCrew(crewId);
    }

    @GetMapping
    public List<CrewResponse> getAllCrews() {
        return crewService.getAllCrews();
    }

    @PutMapping("/{crewId}")
    public CrewResponse updateCrew(@PathVariable String crewId, @org.springframework.web.bind.annotation.RequestBody CrewRequest dto) {
        return crewService.updateCrew(crewId, dto);
    }

    @DeleteMapping("/{crewId}")
    public void deleteCrew(@PathVariable String crewId) {
        crewService.deleteCrew(crewId);
    }





    @Operation(
            summary = "Import Crew Data",
            description = "Upload a CSV file containing crew information.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    implementation = FileUploadRequest.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Crew data imported successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid CSV file")
            }
    )
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importCrewData(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid CSV file.");
        }

        crewService.importFromCsv(file);
        return ResponseEntity.ok("Crew data imported successfully!");
    }
}
