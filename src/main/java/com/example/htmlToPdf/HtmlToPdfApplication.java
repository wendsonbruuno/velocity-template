package com.example.htmlToPdf;

import com.example.htmlToPdf.enginer.EnginnerPDFFlyingSaucer;
import com.example.htmlToPdf.enginer.EnginnerPDFItext;
import com.itextpdf.text.DocumentException;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
@RestController
public class HtmlToPdfApplication {

    public static void main(String[] args) {
        SpringApplication.run(HtmlToPdfApplication.class, args);
    }

    @GetMapping("/pdf/{fileName}")
    HttpEntity<Void> createPdf(
            @PathVariable("fileName") String fileName) throws IOException {
        try {
            EnginnerPDFFlyingSaucer.createPdf(fileName);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/extrato/{fileName}")
    HttpEntity<byte[]> createPdfItext(
            @PathVariable("fileName") String fileName) throws IOException {
        byte[] pdf = EnginnerPDFItext.createPdf(fileName);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName.replace(" ", "_"));
        header.setContentLength(pdf.length);
        return new HttpEntity<byte[]>(pdf, header);
    }


    @GetMapping("/msg")
    public String printMesssage() {
        return "this is the message";
    }

}
