package com.tedtalks.assignment.filters;

import com.tedtalks.assignment.exception.InvalidFileTypeException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class MultipartValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String requestURI = httpRequest.getRequestURI();
            if (httpRequest.getContentType() != null &&
                    httpRequest.getContentType().startsWith(MediaType.MULTIPART_FORM_DATA_VALUE) &&
                    requestURI.startsWith("/api/v1/ted-talks/import")) {

                if (httpRequest.getParts().stream().anyMatch(part -> part.getSize() == 0)) {
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty file is not allowed");
//                    throw new FileNotFoundException("Empty file is not allowed");
                }

                for (Part part : httpRequest.getParts()) {

                    String contentType = part.getContentType();
                    if (!"text/csv".equals(contentType)) {
                        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, "File type is not allowed");

//                        throw new InvalidFileTypeException("Invalid file type: " + contentType);
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }
}