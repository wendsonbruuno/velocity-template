package com.example.htmlToPdf.enginer;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class EnginnerPDFItext {
    public static byte[] createPdf(String fileName){
    /* first, get and initialize an engine */
    VelocityEngine ve = new VelocityEngine();

    /* next, get the Template */
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class",
    ClasspathResourceLoader .class.getName());
		ve.init();
    Template t = ve.getTemplate("templates/helloworld.vm");
    /* create a context and add data */
    VelocityContext context = new VelocityContext();
    /* now render the template into a StringWriter */
    StringWriter writer = new StringWriter();
		t.merge(context, writer);
    /* show the World */
		System.out.println(writer.toString());

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    baos = generatePdf(writer.toString());



		return baos.toByteArray();

}

    public static ByteArrayOutputStream generatePdf(String html) {

        String pdfFilePath = "";
        PdfWriter pdfWriter = null;

        // create a new document
        Document document = new Document();
        try {

            document = new Document();
            // document header attributes
            document.addAuthor("Kinns");
            document.addAuthor("Kinns123");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("kinns123.github.io");
            document.addTitle("HTML to PDF using itext");
            document.setPageSize(PageSize.LETTER);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            // open document
            document.open();

            XMLWorkerHelper xmlWorkerHelper = XMLWorkerHelper.getInstance();
            xmlWorkerHelper.getDefaultCssResolver(true);
            xmlWorkerHelper.parseXHtml(pdfWriter, document, new StringReader(
                    html));
            // close the document
            document.close();
            System.out.println("PDF generated successfully");

            return baos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
