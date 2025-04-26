package com.tedtalks.assignment.repository;

import com.tedtalks.assignment.entity.TedTalk;
import com.tedtalks.assignment.projection.InfluentialSpeaker;
import com.tedtalks.assignment.projection.InfluentialSpeakerPerYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TedTalkRepository extends JpaRepository<TedTalk, Long> {

    Page<TedTalk> findTedTalksByAuthor(String author, Pageable pageable);

    Page<TedTalk> findTedTalksByEventDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(value = """
            SELECT * 
            FROM ted.ted_talk
            WHERE document @@ to_tsquery(:searchTerm)
                        """,
            nativeQuery = true)
    Page<TedTalk> searchTedTalks(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query(value = """
            SELECT 
                t.author AS author,
                SUM(t.views) AS totalViews,
                SUM(t.likes) AS totalLikes
            FROM ted.ted_talk AS t 
            GROUP BY t.author 
            ORDER BY (SUM(t.views) * :viewsWeight + SUM(t.likes) * :likesWeight) DESC
            """, nativeQuery = true)
    Page<InfluentialSpeaker> findInfluentialSpeakers(@Param("viewsWeight") float viewsWeight,
                                                     @Param("likesWeight") double likesWeight,
                                                     Pageable pageable);

    @Query(value = """
            WITH scored_talks AS (
                SELECT
                    EXTRACT(YEAR FROM t.event_date) AS year,
                    t.title AS title,
                    t.author AS author,
                    ROW_NUMBER() OVER (
                        PARTITION BY EXTRACT(YEAR FROM t.event_date)
                        ORDER BY (t.views * :viewsWeight + t.likes * :likesWeight) DESC
                    ) AS rn
                FROM ted.ted_talk AS t
            )
            SELECT
                year, title, author
            FROM scored_talks
            WHERE rn = 1 order by year desc
            """, nativeQuery = true)
    List<InfluentialSpeakerPerYear> findMostInfluentialTalksPerYear(@Param("viewsWeight") float viewsWeight,
                                                                    @Param("likesWeight") double likesWeight);
}
