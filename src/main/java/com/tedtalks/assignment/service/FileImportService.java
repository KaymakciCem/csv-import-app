package com.tedtalks.assignment.service;

import com.tedtalks.assignment.dto.FileImportResponse;
import com.tedtalks.assignment.entity.TedTalk;
import com.tedtalks.assignment.repository.TedTalkRepository;
import com.tedtalks.assignment.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileImportService {

    private static final int BATCH_SIZE = 1000;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");

    private final TedTalkRepository tedTalkRepository;

    public FileImportResponse importCsv(final MultipartFile file) throws IOException {
        log.info("Importing csv file");

        long successfulRecordCount = 0L;
        long recordCount = 0L;

        try (
                Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {

            final List<TedTalk> tedTalks = new ArrayList<>();

            for (CSVRecord csvRecord : csvParser) {
                recordCount++;

                if (!FileUtil.verifyCsvRecord(csvRecord, DATE_FORMATTER)) {
                    continue;
                }

                tedTalks.add(new TedTalk(csvRecord.get(0),
                        csvRecord.get(1),
                        YearMonth.parse(csvRecord.get(2), DATE_FORMATTER).atDay(1),
                        Long.parseLong(csvRecord.get(3)),
                        Long.parseLong(csvRecord.get(4)),
                        csvRecord.get(5)));

                if (tedTalks.size() == BATCH_SIZE) {
                    log.info("Saving {} records to the database", BATCH_SIZE);
                    successfulRecordCount += BATCH_SIZE;
                    tedTalkRepository.saveAll(tedTalks);
                    tedTalks.clear();
                }
            }

            // Check if the file has been read completely and save the leftovers
            if (!CollectionUtils.isEmpty(tedTalks)) {
                log.info("Saving {} leftover records to the database", tedTalks.size());
                successfulRecordCount += tedTalks.size();
                tedTalkRepository.saveAll(tedTalks);
                tedTalks.clear();
            }
        }

        log.info("Successfully imported {} records", successfulRecordCount);

        return new FileImportResponse(recordCount, successfulRecordCount, recordCount - successfulRecordCount);
    }
}
