package com.techincal.test.appfuxion.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class GithubSearchService {

    final static SimpleDateFormat DATE_FORMAT_REPORT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    @Autowired
    private GithubSearchRest githubSearchRest;
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Autowired
    private DownloadHistoryRepository downloadHistoryRepository;

    public PdfGenerateResponse generatePdf(PdfGenerateRequest pdfGenerateRequest) throws IOException {
        GithubSearchRequest githubSearchRequest = GithubSearchRequest.builder()
                .q(pdfGenerateRequest.getKeyword())
                .per_page(Integer.valueOf(pdfGenerateRequest.getRowPerPage()))
                .build();
        GithubSearchResponse githubSearchResponse = githubSearchRest.doSearchGithubRest(githubSearchRequest).block();

        if(!githubSearchResponse.getItems().isEmpty()) {
            Map<String, Object> pdfResult = generatePdf(githubSearchResponse.getItems());

            DownloadHistory downloadHistory = DownloadHistory.builder()
                    .fileName((String) pdfResult.get("fileName"))
                    .createdDate(new Date())
                    .dataFile((byte[]) pdfResult.get("dataFile"))
                    .build();

            downloadHistoryRepository.save(downloadHistory);

            return PdfGenerateResponse.builder()
                    .generateTime(DATE_FORMAT_REPORT.format(new Date()))
                    .fileName(pdfResult.get("fileName").toString())
                    .responseCode(HttpStatus.OK.value())
                    .responseMessage("Success save pdf file into database")
                    .build();
        } else {
            throw new NotFoundException();
        }
    }

    private Map<String, Object> generatePdf(List<GithubSearchDetailResponse> githubSearchDetailResponseList)
            throws DocumentException, IOException {

        Document document = new Document(PageSize.A4.rotate());
//        PdfWriter.getInstance(document, httpServletResponse.getOutputStream());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);

        Paragraph paragraph = new Paragraph("List Of Data From Github", fontTiltle);

        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(6);

        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1, 2, 3 ,3 ,3, 1});
        table.setSpacingBefore(5);

        generateHeader(table);
        generateBody(table, githubSearchDetailResponseList);

        document.add(table);
        document.close();

        byte[] pdfAsBytes = baos.toByteArray();

        String fileName = "GithubDatas "  + DATE_FORMAT_REPORT.format(new Date()) + ".pdf";
//        httpServletResponse.setContentType("application/pdf");
//        httpServletResponse.setHeader("Content-Disposition",
//                "attachment; filename=" +fileName);

        Map<String, Object> result = new HashMap<>();
        result.put("fileName", fileName);
        result.put("dataFile", pdfAsBytes);
        return result;
    }

    private void generateHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.LIGHT_GRAY);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        font.setSize(10);

        cell.setPhrase(new Phrase("No", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Avatar URL", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("URL", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Repo URL", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Score", font));
        table.addCell(cell);
    }

    private void generateBody(PdfPTable table, List<GithubSearchDetailResponse> githubSearchDetailResponseList) {
        int no = 1;
        for (GithubSearchDetailResponse data : githubSearchDetailResponseList) {
            table.addCell(String.valueOf(no));
            table.addCell(String.valueOf(data.getId()));
            table.addCell(data.getAvatar_url());
            table.addCell(data.getUrl());
            table.addCell(data.getRepos_url());
            table.addCell(String.valueOf(data.getScore()));
            no++;
        }
    }

    public DownloadHistoryResponse getAllData() {
        List<DownloadHistory> downloadHistoryList = downloadHistoryRepository.getAllData();
        if(!downloadHistoryList.isEmpty()) {
            return DownloadHistoryResponse.builder()
                    .responseCode(HttpStatus.OK.value())
                    .responseMessage(HttpStatus.OK.getReasonPhrase())
                    .dataList(downloadHistoryList)
                    .totalData(downloadHistoryList.size())
                    .build();
        } else {
            throw new NotFoundException();
        }
    }

    public DownloadHistory downloadPdf(String id) throws IOException {

        if (!numberValidation(id)) {
            throw CustomException.builder()
                    .responseCode(HttpStatus.BAD_REQUEST.value())
                    .responseMessage("Invalid ID format, just allow numeric")
                    .build();
        }

        DownloadHistory downloadHistory = downloadHistoryRepository.getDataById(Long.valueOf(id));
        if(downloadHistory != null) {
            OutputStream outStream = httpServletResponse.getOutputStream();

            httpServletResponse.setContentLength(downloadHistory.getDataFile().length);
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setHeader("Content-Disposition",
                "attachment; filename=" +downloadHistory.getFileName());
            outStream.write(downloadHistory.getDataFile());
            outStream.flush();
            outStream.close();

            return downloadHistory;
        } else {
            throw new NotFoundException();
        }
    }

    public DownloadHistoryResponse doDeleteData(String id) {
        if (!numberValidation(id)) {
            throw CustomException.builder()
                    .responseCode(HttpStatus.BAD_REQUEST.value())
                    .responseMessage("Invalid ID format, just allow numeric")
                    .build();
        }
        DownloadHistory downloadHistory = downloadHistoryRepository.getDataById(Long.valueOf(id));
        if(downloadHistory != null) {
            downloadHistoryRepository.deleteById(Long.valueOf(id));
            return DownloadHistoryResponse.builder()
                    .responseCode(HttpStatus.OK.value())
                    .responseMessage("Success delete data, ID : " + id)
                    .build();
        } else {
            throw new NotFoundException();
        }
    }

    private boolean numberValidation(String value) {
        boolean isValid = false;
        String regexOfDigitOnly = "^[0-9]+$";
        if (Pattern.compile(regexOfDigitOnly).matcher(String.valueOf(value)).find()) {
            isValid =  true;
        }
        return isValid;
    }
}
