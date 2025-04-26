package com.tedtalks.assignment.service;

import com.tedtalks.assignment.dto.InfluentialSpeakerDto;
import com.tedtalks.assignment.dto.InfluentialSpeakerPerYearDto;
import com.tedtalks.assignment.dto.TedTalkDto;
import com.tedtalks.assignment.mapper.TedTalkMapper;
import com.tedtalks.assignment.repository.TedTalkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TedTalkService {

    private static final float VIEWS_WEIGHT = 0.7f;
    private static final float LIKES_WEIGHT = 0.3f;

    private final TedTalkRepository tedTalkRepository;
    private final TedTalkMapper tedTalkMapper;

    public Page<TedTalkDto> retrieveSpeechByYear(final Integer eventYear, final Pageable pageable) {
        return tedTalkRepository.findTedTalksByEventDateBetween(LocalDate.of(eventYear, Month.JANUARY, 1),
                        LocalDate.of(eventYear, Month.DECEMBER, 31), pageable)
                .map(tedTalkMapper::toDto);
    }

    public Page<TedTalkDto> retrieveSpeechByAuthor(final String authorName, final Pageable pageable) {
        return tedTalkRepository.findTedTalksByAuthor(authorName, pageable)
                .map(tedTalkMapper::toDto);
    }

    public Page<TedTalkDto> searchTedTalks(final String searchTerm, final Pageable pageable) {
        return tedTalkRepository.searchTedTalks(searchTerm, pageable)
                .map(tedTalkMapper::toDto);
    }

    public Page<InfluentialSpeakerDto> findInfluentialSpeakers(final Pageable pageable) {
        return tedTalkRepository.findInfluentialSpeakers(VIEWS_WEIGHT, LIKES_WEIGHT, pageable)
                .map(tedTalkMapper::toInfluentialSpeakerDto);
    }

    public List<InfluentialSpeakerPerYearDto> findMostInfluentialTalksPerYear() {
        return tedTalkRepository.findMostInfluentialTalksPerYear(VIEWS_WEIGHT, LIKES_WEIGHT)
                .stream()
                .map(tedTalkMapper::toInfluentialSpeakerPerYearDto)
                .toList();
    }

}
