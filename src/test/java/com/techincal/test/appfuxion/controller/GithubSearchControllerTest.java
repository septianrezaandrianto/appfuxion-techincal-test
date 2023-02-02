package com.techincal.test.appfuxion.controller;

import com.google.gson.Gson;
import com.techincal.test.appfuxion.entity.DownloadHistory;
import com.techincal.test.appfuxion.request.PdfGenerateRequest;
import com.techincal.test.appfuxion.response.DownloadHistoryResponse;
import com.techincal.test.appfuxion.response.PdfGenerateResponse;
import com.techincal.test.appfuxion.service.GithubSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GithubSearchController.class)
public class GithubSearchControllerTest {

    @MockBean
    private GithubSearchService githubSearchService;
    @Autowired
    private MockMvc mockMvc;

    final static SimpleDateFormat DATE_FORMAT_REPORT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String fileName = "GithubDatas "  + DATE_FORMAT_REPORT.format(new Date()) + ".pdf";

    @Test
    @DisplayName("should return success generatePdf")
    public void generatePdf() throws Exception {
        when(githubSearchService.generatePdf(pdfGenerateRequest()))
                .thenReturn(pdfGenerateResponse(HttpStatus.OK.value(),
                        "Success save pdf file into database"));

        MockHttpServletResponse result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/generatePdf")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(new Gson().toJson(pdfGenerateRequest())))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), result.getStatus());
    }


    @Test
    @DisplayName("should return success getAllData")
    public void getAllData() throws Exception {
        when(githubSearchService.getAllData())
                .thenReturn(downloadHistoryResponse(HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase()));

        MockHttpServletResponse result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/getAllData")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), result.getStatus());
    }

    @Test
    @DisplayName("should return success downloadPdf")
    public void downloadPdf() throws Exception {
        when(githubSearchService.downloadPdf(anyString()))
                .thenReturn(downloadHistory());

        MockHttpServletResponse result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/downloadPdf")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("id", "1")
                        .characterEncoding("utf-8"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), result.getStatus());
    }

    @Test
    @DisplayName("should return success doDeleteData")
    public void doDeleteData() throws Exception {
        when(githubSearchService.doDeleteData(anyString()))
                .thenReturn(downloadHistoryResponse(HttpStatus.OK.value(),
                        "Success delete data, ID : " + 1));

        MockHttpServletResponse result = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/doDeleteData")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("id", "1")
                        .characterEncoding("utf-8"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), result.getStatus());
    }

    private DownloadHistoryResponse downloadHistoryResponse(Integer responseCode, String responseMessage) {
        return DownloadHistoryResponse.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .totalData(100)
                .dataList(downloadHistoryList())
                .build();
    }

    private List<DownloadHistory> downloadHistoryList() {
        List<DownloadHistory> result = new ArrayList<>();
        result.add(downloadHistory());
        return result;
    }

    private DownloadHistory downloadHistory() {
        return DownloadHistory.builder()
                .fileName(fileName)
                .createdDate(new Date())
                .dataFile(fileName.getBytes())
                .build();
    }

    private PdfGenerateRequest pdfGenerateRequest() {
        return PdfGenerateRequest.builder()
                .keyword("Q")
                .rowPerPage("100")
                .build();
    }

    private PdfGenerateResponse pdfGenerateResponse(Integer responseCode, String responseMessage) {
        return PdfGenerateResponse.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .fileName(fileName)
                .generateTime(DATE_FORMAT_REPORT.format(new Date()))
                .build();
    }
}
