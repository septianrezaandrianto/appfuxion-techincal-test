package com.techincal.test.appfuxion.controller;

import com.techincal.test.appfuxion.entity.DownloadHistory;
import com.techincal.test.appfuxion.request.PdfGenerateRequest;
import com.techincal.test.appfuxion.response.DownloadHistoryResponse;
import com.techincal.test.appfuxion.response.PdfGenerateResponse;
import com.techincal.test.appfuxion.service.GithubSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class GithubSearchController {

    @Autowired
    private GithubSearchService githubSearchService;

    @PostMapping("/generatePdf")
    public ResponseEntity<PdfGenerateResponse> generatePdf(@Validated @RequestBody
                    PdfGenerateRequest pdfGenerateRequest) throws IOException {
        return ResponseEntity.ok(githubSearchService.generatePdf(pdfGenerateRequest));
    }

    @GetMapping("/getAllData")
    public ResponseEntity<DownloadHistoryResponse> getAllData() {
        return ResponseEntity.ok(githubSearchService.getAllData());
    }

    @GetMapping("/downloadPdf")
    public ResponseEntity<DownloadHistory> downloadPdf(@RequestParam("id") String id) throws IOException {
        return ResponseEntity.ok(githubSearchService.downloadPdf(id));
    }

    @DeleteMapping("/doDeleteData")
    public ResponseEntity<DownloadHistoryResponse> doDeleteData(@RequestParam("id") String id) throws IOException {
        return ResponseEntity.ok(githubSearchService.doDeleteData(id));
    }

}
