package com.worktree.hrms.controllers;

import com.worktree.hrms.annotations.Feature;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.exceptions.BadRequestException;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Base64;
import java.util.Map;

@RestController
public class PdfController {

//    @Feature
//    @PostMapping("generate")
//    public ResponseEntity generate(@RequestBody Map<String, Object> payload) throws IOException {
//
//        File tempFile = File.createTempFile("tempImage_", ".png");
//
//        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
//            fos.write(Base64.getDecoder().decode(payload.get("logo").toString().contains(",") ? payload.get("logo").toString().split(",")[1]
//                    : payload.get("logo").toString()));
//        } catch (IOException e) {
//            throw new BadRequestException(CommonConstants.INTERNAL_SERVER_ERROR);
//        }
//
//        float logoWidth = Float.valueOf(payload.get("logoWidth").toString());
//        float logoHeight = Float.valueOf(payload.get("logoHeight").toString());
//        float xPosition = Float.valueOf(payload.get("xPosition").toString());
//        float yPosition = Float.valueOf(payload.get("yPosition").toString());
//
//        try (PDDocument document = new PDDocument()) {
//            // Create a new page
//            PDPage page = new PDPage(getSizeSheet(payload.get("size").toString()));
//            document.addPage(page);
//
//            // Load the logo image
//            PDImageXObject logo = PDImageXObject.createFromFile(tempFile.getAbsolutePath(), document);
//
//            // Prepare content stream
//            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                // Set font for title
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
//                contentStream.newLineAtOffset(50, page.getMediaBox().getHeight() - 80);
//                contentStream.showText("PAYSLIP NOV 2021");
//                contentStream.endText();
//
//                // Set font for address
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, 12);
//                contentStream.newLineAtOffset(50, page.getMediaBox().getHeight() - 110);
//                contentStream.showText("TECTORO CONSULTING PRIVATE LIMITED");
//                contentStream.newLineAtOffset(0, -15);
//                contentStream.showText("BLOCK-D, 8TH FLOOR, ILABS CENTRE, OPP: INORBIT MALL");
//                contentStream.newLineAtOffset(0, -15);
//                contentStream.showText("MADHAPUR, HYDERABAD, TELANGANA-500081");
//                contentStream.endText();
//
//                // Add the logo with dynamic size and position
//                contentStream.drawImage(logo, xPosition, yPosition, logoWidth, logoHeight);
//            }
//
//            File tempPDF = File.createTempFile("tempImage_", ".pdf");
//            // Save the document
//            document.save(tempPDF);
//
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(new FileInputStream(tempPDF)));
//
//            tempFile.delete();
//            tempPDF.delete();
//            System.out.println("PDF created successfully with dynamic logo positioning.");
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "abc.pdf")
//                    .contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(byteArrayInputStream));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity("SUCCESS", HttpStatus.OK);
//    }
//
//    private PDRectangle getSizeSheet(String sizeSheet) {
//        switch (sizeSheet) {
//            case "A0":
//                return PDRectangle.A0;
//            case "A1":
//                return PDRectangle.A1;
//            case "A2":
//                return PDRectangle.A2;
//            case "A3":
//                return PDRectangle.A3;
//            case "A4":
//                return PDRectangle.A4;
//            default:
//                throw new BadRequestException("Invalid Size");
//
//        }
//    }

    Logger logger = LoggerFactory.getLogger(PdfController.class);

    @Feature(feature = CommonConstants.Features.PAYSLIP_SETTINGS)
    @PostMapping("/generate")
    public ResponseEntity<InputStreamResource> generate(@RequestBody Map<String, Object> payload) {
        validatePayload(payload);

        File tempImageFile = null;
        File tempPDFFile = null;

        try {
            // Decode Base64 logo to a temporary image file
            tempImageFile = File.createTempFile("tempLogo_", ".png");
            try (FileOutputStream fos = new FileOutputStream(tempImageFile)) {
                byte[] logoBytes = Base64.getDecoder().decode(
                        payload.get("logo").toString().contains(",")
                                ? payload.get("logo").toString().split(",")[1]
                                : payload.get("logo").toString()
                );
                fos.write(logoBytes);
            }

            // Extract positioning and size values from payload
            float logoWidth = Float.parseFloat(payload.get("logoWidth").toString());
            float logoHeight = Float.parseFloat(payload.get("logoHeight").toString());
            float xPosition = Float.parseFloat(payload.get("xPosition").toString());
            float yPosition = Float.parseFloat(payload.get("yPosition").toString());

            // Create PDF document
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(getSizeSheet(payload.get("size").toString()));
                document.addPage(page);

                PDImageXObject logo = PDImageXObject.createFromFile(tempImageFile.getAbsolutePath(), document);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    addText(contentStream, page);
                    contentStream.drawImage(logo, xPosition, yPosition, logoWidth, logoHeight);
                }

                // Save the PDF to a temporary file
                tempPDFFile = File.createTempFile("tempPayslip_", ".pdf");
                document.save(tempPDFFile);

                try (FileInputStream fis = new FileInputStream(tempPDFFile);
                     ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(fis))) {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=Payslip.pdf")
                            .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(new InputStreamResource(byteArrayInputStream));
                }
            }

        } catch (IOException e) {
            logger.error("Error creating PDF", e);
            throw new BadRequestException("Error creating PDF: ");
        } finally {
            if (tempImageFile != null && tempImageFile.exists()) {
                tempImageFile.delete();
            }
            if (tempPDFFile != null && tempPDFFile.exists()) {
                tempPDFFile.delete();
            }
        }
    }

    private void validatePayload(Map<String, Object> payload) {
        if (payload.get("logo") == null || payload.get("logoWidth") == null ||
                payload.get("logoHeight") == null || payload.get("xPosition") == null ||
                payload.get("yPosition") == null || payload.get("size") == null) {
            throw new BadRequestException("Missing required fields in payload");
        }
    }

    private void addText(PDPageContentStream contentStream, PDPage page) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
        contentStream.newLineAtOffset(50, page.getMediaBox().getHeight() - 80);
        contentStream.showText("PAYSLIP NOV 2021");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(50, page.getMediaBox().getHeight() - 110);
        contentStream.showText("TECTORO CONSULTING PRIVATE LIMITED");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("BLOCK-D, 8TH FLOOR, ILABS CENTRE, OPP: INORBIT MALL");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("MADHAPUR, HYDERABAD, TELANGANA-500081");
        contentStream.endText();
    }

    private PDRectangle getSizeSheet(String sizeSheet) {
        switch (sizeSheet) {
            case "A0":
                return PDRectangle.A0;
            case "A1":
                return PDRectangle.A1;
            case "A2":
                return PDRectangle.A2;
            case "A3":
                return PDRectangle.A3;
            case "A4":
                return PDRectangle.A4;
            default:
                throw new BadRequestException("Invalid size provided");
        }
    }

}
