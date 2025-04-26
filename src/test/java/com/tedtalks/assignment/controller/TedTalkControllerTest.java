package com.tedtalks.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tedtalks.assignment.dto.InfluentialSpeakerDto;
import com.tedtalks.assignment.dto.InfluentialSpeakerPerYearDto;
import com.tedtalks.assignment.dto.TedTalkDto;
import com.tedtalks.assignment.service.TedTalkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TedTalkController.class)
class TedTalkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TedTalkService tedTalkService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void retrieveByAuthor_Success() throws Exception {
        String authorName = "Test Author";
        TedTalkDto tedTalk = new TedTalkDto("Test Talk", "Test Author", LocalDate.of(2023,12,1), 1000L, 100L, "http://test.com");

        Page<TedTalkDto> page = new PageImpl<>(List.of(tedTalk));
        when(tedTalkService.retrieveSpeechByAuthor(eq(authorName), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/ted-talks/authors/{authorName}", authorName)
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].author").value(authorName))
                .andExpect(jsonPath("$.content[0].title").value("Test Talk"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void retrieveByAuthor_EmptyResult() throws Exception {
        String authorName = "NonExistent Author";
        Page<TedTalkDto> emptyPage = new PageImpl<>(List.of());
        when(tedTalkService.retrieveSpeechByAuthor(eq(authorName), any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/ted-talks/authors/{authorName}", authorName)
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void getInfluentialSpeakers_Success() throws Exception {
        InfluentialSpeakerDto speaker1 = new InfluentialSpeakerDto("Author 1", 1000L, 100L);
        InfluentialSpeakerDto speaker2 = new InfluentialSpeakerDto("Author 2", 2000L, 200L);

        Page<InfluentialSpeakerDto> page = new PageImpl<>(List.of(speaker1, speaker2));
        when(tedTalkService.findInfluentialSpeakers(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/ted-talks/speakers/influential")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].author").value("Author 1"))
                .andExpect(jsonPath("$.content[0].totalViews").value(1000))
                .andExpect(jsonPath("$.content[0].totalLikes").value(100))
                .andExpect(jsonPath("$.content[1].author").value("Author 2"))
                .andExpect(jsonPath("$.content[1].totalViews").value(2000))
                .andExpect(jsonPath("$.content[1].totalLikes").value(200));
    }

    @Test
    void getInfluentialSpeakers_EmptyResult() throws Exception {
        Page<InfluentialSpeakerDto> emptyPage = new PageImpl<>(List.of());
        when(tedTalkService.findInfluentialSpeakers(any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/ted-talks/speakers/influential")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void getMostInfluentialTalksPerYear_Success() throws Exception {
        InfluentialSpeakerPerYearDto talk1 = new InfluentialSpeakerPerYearDto(2023, "Talk 1", "Author 1");
        InfluentialSpeakerPerYearDto talk2 = new InfluentialSpeakerPerYearDto(2022, "Talk 2", "Author 2");

        List<InfluentialSpeakerPerYearDto> talks = List.of(talk1, talk2);
        when(tedTalkService.findMostInfluentialTalksPerYear())
                .thenReturn(talks);

        mockMvc.perform(get("/api/v1/ted-talks/talks/influential/yearly")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].year").value(2023))
                .andExpect(jsonPath("$[0].title").value("Talk 1"))
                .andExpect(jsonPath("$[0].author").value("Author 1"))
                .andExpect(jsonPath("$[1].year").value(2022))
                .andExpect(jsonPath("$[1].title").value("Talk 2"))
                .andExpect(jsonPath("$[1].author").value("Author 2"));
    }

    @Test
    void getMostInfluentialTalksPerYear_EmptyResult() throws Exception {
        when(tedTalkService.findMostInfluentialTalksPerYear())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/ted-talks/talks/influential/yearly")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }


}