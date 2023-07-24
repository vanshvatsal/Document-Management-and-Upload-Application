package com.fileupload.Vansh_File_Upload;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws DocumentException {
        String uploadDir = "/Users/vanshvatsal/Desktop/CDOT/CDOT_SAVED_FILES_KAVACH/";
        String filePath = uploadDir + file.getOriginalFilename();

        try {
            file.transferTo(new File(filePath));
            // File uploaded successfully

            // Check if the uploaded file is a docx file
            if (file.getOriginalFilename().toLowerCase().endsWith(".docx")) {
                // Convert doc to pdf and render the pdf
                String pdfFilePath = filePath + ".pdf";
                convertDocxToPdf(filePath, pdfFilePath);
                renderPdf(pdfFilePath);
            }
        } catch (IOException e) {
            // Handle the exception
        }

        return "redirect:/"; // Redirect back to the home page
    }

    private void convertDocxToPdf(String docxFilePath, String pdfFilePath) throws IOException, DocumentException {
        FileInputStream inputStream = new FileInputStream(docxFilePath);
        XWPFDocument doc = new XWPFDocument(inputStream);

        Document pdfDoc = new Document();
        PdfWriter.getInstance(pdfDoc, new FileOutputStream(pdfFilePath));
        pdfDoc.open();

        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            pdfDoc.add(new Paragraph(paragraph.getText()));
        }

        pdfDoc.close();
        doc.close();
        inputStream.close();
    }
    

    @SuppressWarnings("unused")
	private void renderPdf(String pdfFilePath) {
        try {
            PDDocument pdfDocument = PDDocument.load(new File(pdfFilePath));
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);

            // Assuming there are multiple pages, you can loop through and display each page
            for (int page = 0; page < pdfDocument.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 100);
                // You can display the image in your preview section
                // For example, you can create an <img> element and set the image as the source
                // Add your logic here to display the image in the preview section
            }

            pdfDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
