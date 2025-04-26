package com.tedtalks.assignment.controller;

import com.tedtalks.assignment.dto.FileImportResponse;
import com.tedtalks.assignment.service.FileImportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = FileImportController.class)
class FileImportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FileImportService fileImportService;

    @Test
    void importCsv_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                "title,author,date,views,likes,link\nTest Talk,Test Author, December 2023,1000,100,http://test.com".getBytes()
        );

        FileImportResponse response = new FileImportResponse(1, 1, 1);
        when(fileImportService.importCsv(file)).thenReturn(response);

        mockMvc.perform(multipart("/api/v1/ted-talks/import")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void importCsv_NullFile() throws Exception {
        mockMvc.perform(multipart("/api/v1/ted-talks/import"))
                .andExpect(status().isBadRequest());
    }
}