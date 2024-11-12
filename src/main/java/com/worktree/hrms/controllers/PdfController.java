package com.worktree.hrms.controllers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PdfController {

    public static void main(String[] args) {

//        if (args.length < 5) {
//            System.out.println("Usage: java PDFWithDynamicLogo <imagePath> <logoWidth> <logoHeight> <xPosition> <yPosition>");
//            return;
//        }

        String imagePath = "C:\\Users\\91850\\Downloads\\pdfgens\\image.png";
//        float logoWidth = 227;
//        float logoHeight = 154;
//        float xPosition = 345;
//        float yPosition = 652;

//        float logoWidth = 294;
//        float logoHeight = 103;
//        float xPosition = 285;
//        float yPosition = 710;

        float logoWidth = 151;
        float logoHeight = 47;
        float xPosition = 1;
        float yPosition = 308;

        try (PDDocument document = new PDDocument()) {
            // Create a new page
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Load the logo image
            PDImageXObject logo = PDImageXObject.createFromFile(imagePath, document);

            // Prepare content stream
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Set font for title
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.newLineAtOffset(50, page.getMediaBox().getHeight() - 80);
                contentStream.showText("PAYSLIP NOV 2021");
                contentStream.endText();

                // Set font for address
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, page.getMediaBox().getHeight() - 110);
                contentStream.showText("TECTORO CONSULTING PRIVATE LIMITED");
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("BLOCK-D, 8TH FLOOR, ILABS CENTRE, OPP: INORBIT MALL");
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("MADHAPUR, HYDERABAD, TELANGANA-500081");
                contentStream.endText();

                // Add the logo with dynamic size and position
                contentStream.drawImage(logo, xPosition, yPosition, logoWidth, logoHeight);
            }

            // Save the document
            document.save("C:\\Users\\91850\\Downloads\\pdfgens\\DynamicPayslip.pdf");
            System.out.println("PDF created successfully with dynamic logo positioning.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("generate")
    public ResponseEntity generate() throws IOException {
        try (PDDocument document = new PDDocument()) {
            // Create a new page
            PDPage page = new PDPage();
            document.addPage(page);

            // Load the image
            PDImageXObject image = PDImageXObject.createFromFile("C:\\Users\\91850\\Downloads\\pdfgens\\image.png", document);

            // Prepare content stream
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Draw the image on the PDF (x, y, width, height)
                contentStream.drawImage(image, 100, 500, 300, 200);
            }

            // Save the document
            document.save("C:\\Users\\91850\\Downloads\\pdfgens\\GeneratedPDFWithImage.pdf");
            System.out.println("PDF created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity("SUCCESS", HttpStatus.OK);
    }

}
