package com.tedtalks.assignment.mapper;

import com.tedtalks.assignment.dto.InfluentialSpeakerDto;
import com.tedtalks.assignment.dto.InfluentialSpeakerPerYearDto;
import com.tedtalks.assignment.dto.TedTalkDto;
import com.tedtalks.assignment.entity.TedTalk;
import com.tedtalks.assignment.projection.InfluentialSpeaker;
import com.tedtalks.assignment.projection.InfluentialSpeakerPerYear;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TedTalkMapper {

    TedTalkDto toDto(TedTalk tedTalk);

    TedTalk fromDto(TedTalkDto tedTalkDto);

    InfluentialSpeakerDto toInfluentialSpeakerDto(InfluentialSpeaker influentialSpeaker);

    InfluentialSpeakerPerYearDto toInfluentialSpeakerPerYearDto(InfluentialSpeakerPerYear influentialSpeakerPerYear);

}
