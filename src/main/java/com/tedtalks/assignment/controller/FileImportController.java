package com.tedtalks.assignment.controller;

import com.tedtalks.assignment.dto.ErrorResponseDto;
import com.tedtalks.assignment.dto.FileImportResponse;
import com.tedtalks.assignment.service.FileImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(
        name = "File Import REST API for TedTalks",
        description = "Importing TedTalks to CREATE speeches by author and year and most influential speakers"
)
@RestController
@RequestMapping(value = "/api/v1/ted-talks", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class FileImportController {

    private final FileImportService fileImportService;

    @Operation(
            summary = "Upload TedTalks csv file REST API",
            description = "REST API to upload a csv file containing TedTalks data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<FileImportResponse> importCsv(@RequestParam("file") @Valid @NotNull MultipartFile file) throws IOException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fileImportService.importCsv(file));
    }

}
