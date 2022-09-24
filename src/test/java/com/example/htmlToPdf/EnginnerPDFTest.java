package com.example.htmlToPdf;

import com.example.htmlToPdf.enginer.EnginnerPDFFlyingSaucer;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;

@ExtendWith(MockitoExtension.class)
class EnginnerPDFTest {

	@InjectMocks
	EnginnerPDFFlyingSaucer enginner;

	@Test
	void createPdf() {
		try {
			enginner.createPdf("teste");
			//byteArrayToFile(pdf);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	static void byteArrayToFile(byte[] bArray) {
		try {
			// Create file
			OutputStream out = new FileOutputStream("out.pdf");
			out.write(bArray);
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}