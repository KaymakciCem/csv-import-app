package com.tedtalks.assignment.service;

import com.tedtalks.assignment.dto.FileImportResponse;
import com.tedtalks.assignment.entity.TedTalk;
import com.tedtalks.assignment.repository.TedTalkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileImportServiceTest {

    @Mock
    private TedTalkRepository tedTalkRepository;

    @InjectMocks
    private FileImportService fileImportService;

    @Test
    void importCsv_Success() throws IOException {
        String csvContent = "title,author,date,views,likes,link\n" +
                "Test Talk,Test Author,December 2023,1000,100,http://test.com";

        MockMultipartFile file = getMultiPartFile(csvContent);

        when(tedTalkRepository.saveAll(anyList())).thenReturn(List.of(new TedTalk()));

        FileImportResponse response = fileImportService.importCsv(file);

        assertEquals(1, response.successfulRecordCount());
        assertEquals(1, response.totalRecordCount());
        assertEquals(0, response.failedRecordCount());
    }

    @Test
    void importCsv_WithInvalidRecord() throws IOException {
        String csvContent = "title,author,date,views,likes,link\n" +
                "Test Talk,Test Author,Invalid Date,1000,100,http://test.com";

        MockMultipartFile file = getMultiPartFile(csvContent);

        FileImportResponse result = fileImportService.importCsv(file);

        assertEquals(0, result.successfulRecordCount());
        assertEquals(1, result.totalRecordCount());
        assertEquals(1, result.failedRecordCount());
    }

    @Test
    void importCsv_WithMultipleRecords() throws IOException {
        String csvContent = "title,author,date,views,likes,link\n" +
                "Talk 1,Author 1,December 2023,1000,100,http://test1.com\n" +
                "Talk 2,Author 2,January 2023,2000,200,http://test2.com";

        MockMultipartFile file = getMultiPartFile(csvContent);

        when(tedTalkRepository.saveAll(anyList())).thenReturn(List.of(new TedTalk(), new TedTalk()));

        FileImportResponse response = fileImportService.importCsv(file);

        assertEquals(2, response.successfulRecordCount());
        assertEquals(2, response.totalRecordCount());
        assertEquals(0, response.failedRecordCount());
    }

    @Test
    void importCsv_WithBatchSizeExceeded() throws IOException {
        StringBuilder csvContent = new StringBuilder("title,author,date,views,likes,link\n");
        for (int i = 0; i < 1500; i++) {
            csvContent.append(String.format("Talk %d,Author %d,December 2023,%d,%d,http://test%d.com\n",
                    i, i, 1000 + i, 100 + i, i));
        }

        MockMultipartFile file = getMultiPartFile(csvContent.toString());

        when(tedTalkRepository.saveAll(anyList())).thenReturn(List.of(new TedTalk()));

        FileImportResponse response = fileImportService.importCsv(file);

        assertEquals(1500, response.successfulRecordCount());
        assertEquals(1500, response.totalRecordCount());
        assertEquals(0, response.failedRecordCount());
    }


    private MockMultipartFile getMultiPartFile(String csvContent) {
        return new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

    }
}