package com.tedtalks.assignment.dto;

import java.time.LocalDate;

public record TedTalkDto(String title, String author, LocalDate eventDate, Long views, Long likes, String link) {
}
