package com.tedtalks.assignment.service;

import com.tedtalks.assignment.dto.FileImportResponse;
import com.tedtalks.assignment.dto.InfluentialSpeakerDto;
import com.tedtalks.assignment.dto.InfluentialSpeakerPerYearDto;
import com.tedtalks.assignment.dto.TedTalkDto;
import com.tedtalks.assignment.entity.TedTalk;
import com.tedtalks.assignment.mapper.TedTalkMapper;
import com.tedtalks.assignment.repository.TedTalkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TedTalkServiceTest {

    @Mock
    private TedTalkRepository tedTalkRepository;

    @Mock
    private TedTalkMapper tedTalkMapper;

    @InjectMocks
    private TedTalkService tedTalkService;

    @Test
    void retrieveSpeechByYear_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        TedTalkDto tedTalkDto = new TedTalkDto("Test Title", "Test Author", LocalDate.now(), 1000L, 100L, "http://test.com");
        when(tedTalkRepository.findTedTalksByEventDateBetween(any(LocalDate.class), any(LocalDate.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(new TedTalk())));
        when(tedTalkMapper.toDto(any())).thenReturn(tedTalkDto);

        Page<TedTalkDto> result = tedTalkService.retrieveSpeechByYear(2023, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Title", result.getContent().get(0).title());
    }

    @Test
    void retrieveSpeechByAuthor_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        TedTalkDto tedTalkDto = new TedTalkDto("Test Title", "Test Author", LocalDate.now(), 1000L, 100L, "http://test.com");
        when(tedTalkRepository.findTedTalksByAuthor(anyString(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(new TedTalk())));
        when(tedTalkMapper.toDto(any())).thenReturn(tedTalkDto);

        Page<TedTalkDto> result = tedTalkService.retrieveSpeechByAuthor("Test Author", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Author", result.getContent().get(0).author());
    }

    @Test
    void searchTedTalks_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        TedTalkDto tedTalkDto = new TedTalkDto("Test Title", "Test Author", LocalDate.now(), 1000L, 100L, "http://test.com");
        when(tedTalkRepository.searchTedTalks(anyString(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(new TedTalk())));
        when(tedTalkMapper.toDto(any())).thenReturn(tedTalkDto);

        Page<TedTalkDto> result = tedTalkService.searchTedTalks("Test", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Title", result.getContent().get(0).title());
    }


}