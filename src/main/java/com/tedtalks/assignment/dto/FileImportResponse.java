package com.tedtalks.assignment.dto;

public record FileImportResponse(long totalRecordCount, long successfulRecordCount, long failedRecordCount) { }
