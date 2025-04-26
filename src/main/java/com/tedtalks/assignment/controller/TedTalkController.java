package com.tedtalks.assignment.controller;

import com.tedtalks.assignment.dto.*;
import com.tedtalks.assignment.service.TedTalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "CRUD REST APIs for TedTalks",
        description = "CRUD REST APIs in TedTalks to CREATE AND FETCH speeches by author and year and most influential speakers"
)
@RestController
@RequestMapping(value = "/api/v1/ted-talks", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class TedTalkController {

    private final TedTalkService tedTalkService;

    @Operation(
            summary = "Fetch TedTalks REST API",
            description = "REST API to fetch Ted Talks based on an author name"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
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
    @GetMapping("/authors/{authorName}")
    public ResponseEntity<Page<TedTalkDto>> retrieveByAuthor(@PathVariable("authorName") @Valid
                                                                 @NotEmpty
                                                                 @Size(min = 2, max = 50)
                                                                 String authorName,
                                                             @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(tedTalkService.retrieveSpeechByAuthor(authorName, pageable));
    }

    @Operation(
            summary = "Fetch TedTalks REST API",
            description = "REST API to fetch Ted Talks based on the year"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
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
    @GetMapping("/years/{eventYear}")
    public ResponseEntity<Page<TedTalkDto>> retrieveByYear(@PathVariable("eventYear")
                                                               @Valid @Pattern(regexp = "(^$|[0-9]{4})", message = "Year must be 4 digits")
                                                               Integer eventYear,
                                                           @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(tedTalkService.retrieveSpeechByYear(eventYear, pageable));
    }

    @Operation(
            summary = "Search TedTalks REST API",
            description = "REST API full text search for Ted Talks based on the title and author name"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
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
    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<Page<TedTalkDto>> searchTedTalks(@PathVariable(value = "searchTerm")
                                                           @NotBlank
                                                           @Size(min = 2, max = 100)
                                                           String searchTerm,
                                                           @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(tedTalkService.searchTedTalks(searchTerm, pageable));
    }

    @Operation(
            summary = "Get Influential Speakers REST API",
            description = "REST API to get the most influential TedTalk speakers based on views and likes"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
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
    @GetMapping("/speakers/influential")
    public ResponseEntity<Page<InfluentialSpeakerDto>> getInfluentialSpeakers(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tedTalkService.findInfluentialSpeakers(pageable));
    }

    @Operation(
            summary = "Get Most Influential Talk Per Year REST API",
            description = "REST API to get the most influential TedTalk for each year based on views and likes"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
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
    @GetMapping("/talks/influential/yearly")
    public ResponseEntity<List<InfluentialSpeakerPerYearDto>> getMostInfluentialTalksPerYear() {
        return ResponseEntity.ok(tedTalkService.findMostInfluentialTalksPerYear());
    }

}
