package com.techincal.test.appfuxion.service;

import com.techincal.test.appfuxion.entity.DownloadHistory;
import com.techincal.test.appfuxion.exception.CustomException;
import com.techincal.test.appfuxion.exception.NotFoundException;
import com.techincal.test.appfuxion.repository.DownloadHistoryRepository;
import com.techincal.test.appfuxion.request.GithubSearchRequest;
import com.techincal.test.appfuxion.request.PdfGenerateRequest;
import com.techincal.test.appfuxion.response.DownloadHistoryResponse;
import com.techincal.test.appfuxion.response.GithubSearchDetailResponse;
import com.techincal.test.appfuxion.response.GithubSearchResponse;
import com.techincal.test.appfuxion.response.PdfGenerateResponse;
import com.techincal.test.appfuxion.rest.GithubSearchRest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GithubSearchServiceTest {

    @Mock
    private GithubSearchRest githubSearchRest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private DownloadHistoryRepository downloadHistoryRepository;

    @InjectMocks
    private GithubSearchService githubSearchService;


    final static SimpleDateFormat DATE_FORMAT_REPORT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String fileName = "GithubDatas "  + DATE_FORMAT_REPORT.format(new Date()) + ".pdf";

    @Test
    @DisplayName("should return doDeleteData_success success")
    public void doDeleteData_success() {
        when(downloadHistoryRepository.getDataById(1L))
                .thenReturn(downloadHistory());
        DownloadHistoryResponse result = githubSearchService.doDeleteData("1");
        assertEquals(downloadHistoryResponse(HttpStatus.OK.value(),
                "Success delete data, ID : 1", "delete"), result);
        verify(downloadHistoryRepository, times(1))
                .deleteById(1L);
    }

    @Test
    @DisplayName("should return doDeleteData_invalid_parameter success")
    public void doDeleteData_invalid_parameter() {
        when(downloadHistoryRepository.getDataById(1L))
                .thenReturn(downloadHistory());

        CustomException response = assertThrows(CustomException.class, () -> {
             githubSearchService.doDeleteData("%^");
        });
        assertEquals(customException(400, "Invalid ID format, just allow numeric"), response);
    }

    @Test
    @DisplayName("should return oDeleteData_data_not_found success")
    public void doDeleteData_data_not_found() {
        when(downloadHistoryRepository.getDataById(1L))
                .thenReturn(null);

        NotFoundException response = assertThrows(NotFoundException.class, () -> {
            githubSearchService.doDeleteData("1");
        });
        assertEquals(notFoundException(), response);
    }


    @Test
    @DisplayName("should return getAllData_success success")
    public void getAllData_success() {
        when(downloadHistoryRepository.getAllData())
                .thenReturn(downloadHistoryList());
        DownloadHistoryResponse result = githubSearchService.getAllData();
        assertEquals(HttpStatus.OK.value(), result.getResponseCode());
        assertEquals(HttpStatus.OK.getReasonPhrase(), result.getResponseMessage());
    }

    @Test
    @DisplayName("should return getAllData_data_not_found success")
    public void getAllData_data_not_found() {
        when(downloadHistoryRepository.getAllData())
                .thenReturn(new ArrayList<>());

        NotFoundException response = assertThrows(NotFoundException.class, () -> {
            githubSearchService.getAllData();
        });
        assertEquals(notFoundException(), response);
    }

    @Test
    @DisplayName("should return downloadPdf_success success")
    public void downloadPdf_success() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(httpServletResponse.getOutputStream())
                .thenReturn(new ServletOutputStream() {
                    @Override
                    public boolean isReady() {
                        return false;
                    }
                    @Override
                    public void setWriteListener(WriteListener writeListener) {}
                    public void write(int b) throws IOException {
                        outputStream.write(b);
                    }
                });

        when(downloadHistoryRepository.getDataById(1L))
                .thenReturn(downloadHistory());
        DownloadHistory result = githubSearchService.downloadPdf("1");
        assertNotNull(result);
    }

    @Test
    @DisplayName("should return downloadPdf_invalid_parameter success")
    public void downloadPdf_invalid_parameter() {
        when(downloadHistoryRepository.getDataById(1L))
                .thenReturn(downloadHistory());
        CustomException result = assertThrows(CustomException.class, () -> {
            githubSearchService.downloadPdf("!#");
        });

        assertEquals(customException(HttpStatus.BAD_REQUEST.value(), "Invalid ID format, just allow numeric"),
                result);
    }

    @Test
    @DisplayName("should return downloadPdf_data_not_found success")
    public void downloadPdf_data_not_found() {
        when(downloadHistoryRepository.getDataById(1L))
                .thenReturn(null);
        NotFoundException result = assertThrows(NotFoundException.class, () -> {
            githubSearchService.downloadPdf("1");
        });

        assertEquals(notFoundException(), result);
    }

    @Test
    @DisplayName("should return generatePdf_success success")
    public void generatePdf_success() throws IOException {
        when(githubSearchRest.doSearchGithubRest(githubSearchRequest()))
                .thenReturn(Mono.just(githubSearchResponse(false)));
        PdfGenerateResponse result = githubSearchService.generatePdf(pdfGenerateRequest());
        assertNotNull(result);
        verify(downloadHistoryRepository, times(1)).save(any(DownloadHistory.class));
    }

    @Test
    @DisplayName("should return generatePdf_data_not_found success")
    public void generatePdf_data_not_found() throws IOException {
        when(githubSearchRest.doSearchGithubRest(githubSearchRequest()))
                .thenReturn(Mono.just(githubSearchResponse(true)));
        NotFoundException result = assertThrows(NotFoundException.class, () -> {
            githubSearchService.generatePdf(pdfGenerateRequest());
        });

        assertEquals(notFoundException(), result);
    }

    private GithubSearchResponse githubSearchResponse(boolean isEmpty) {
        List<GithubSearchDetailResponse> items = new ArrayList<>();
        if (!isEmpty) {
            items = githubSearchDetailResponseList();
        }
        return GithubSearchResponse.builder()
                .total_count(1)
                .incomplete_results(true)
                .items(items)
                .build();
    }

    private List<GithubSearchDetailResponse> githubSearchDetailResponseList() {
        List<GithubSearchDetailResponse> result = new ArrayList<>();
        result.add(githubSearchDetailResponse());
        return result;
    }

    private GithubSearchDetailResponse githubSearchDetailResponse() {
        return GithubSearchDetailResponse.builder()
                .id(12345)
                .avatar_url("http://dummy.avatar-url.com")
                .url("http://dummy.url.com")
                .repos_url("http://dummy.repos-url.com")
                .score(1.0)
                .build();
    }

    private PdfGenerateRequest pdfGenerateRequest() {
        return PdfGenerateRequest.builder()
                .keyword("Q")
                .rowPerPage("1")
                .build();
    }

    private GithubSearchRequest githubSearchRequest() {
        return GithubSearchRequest.builder()
                .q("Q")
                .per_page(1)
                .build();
    }

    private NotFoundException notFoundException() {
        return NotFoundException.builder()
                .responseCode(null)
                .responseMessage(null)
                .build();
    }
    private CustomException customException(Integer responseCode, String responseMessage) {
        return CustomException.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .build();
    }
    private DownloadHistoryResponse downloadHistoryResponse(Integer responseCode, String responseMessage, String flag) {
        if(flag.equalsIgnoreCase("delete")) {
            return DownloadHistoryResponse.builder()
                    .responseCode(responseCode)
                    .responseMessage(responseMessage)
                    .build();
        } else {
            return DownloadHistoryResponse.builder()
                    .responseCode(responseCode)
                    .responseMessage(responseMessage)
                    .totalData(1)
                    .dataList(downloadHistoryList())
                    .build();
        }
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
}
