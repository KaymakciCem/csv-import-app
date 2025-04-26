package com.tedtalks.assignment.entity;

import com.tedtalks.assignment.entity.converter.LocalDateConverter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(schema = "ted", name = "ted_talk")
public class TedTalk extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate eventDate;

    @Column(nullable = false)
    private Long views;

    @Column(nullable = false)
    private Long likes;

    private String link;

    public TedTalk() {}

    public TedTalk(String title, String author, LocalDate eventDate, Long views, Long likes, String link) {
        this.title = title;
        this.author = author;
        this.eventDate = eventDate;
        this.views = views;
        this.likes = likes;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
