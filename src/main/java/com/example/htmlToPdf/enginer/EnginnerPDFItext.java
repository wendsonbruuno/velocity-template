package com.example.htmlToPdf.enginer;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

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
    preenchendoContext(context);
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

    private static void preenchendoContext(VelocityContext context) {
        Path path = Paths.get("src/main/resources/templates/logo-pan.png");
        String base64Image = convertToBase64(path);
        context.put("logo", "data:image/png;base64,"+base64Image);
        context.put("cnpj", "059.285.411/0001-13");
        context.put("endereco-banco", "PAULISTA 1374 11 ANDAR 1374 - 11 ANDAR\n" +
                "- BELA VISTA - SAO PAULO - SP");
        context.put("ouvidoria", "0800 7769595");
        context.put("email", "pan@pan.com.br");
        context.put("data", "24/03/2022");
        context.put("nome", "LIGIA APARECIDA FRANCISCO ALBANEZ");
        context.put("cpf", "33801206858");
        context.put("endereco-cliente", "AVENIDA ALBERT BARTHOLOME, 172 AP 138 TOR A");
        context.put("agencia", "0019-MATRIZ");
        context.put("conta", "4400003753");
        context.put("cep", "05541000");
        context.put("cidade", "SAO PAULO ");
        context.put("bairro", "JARDIM DAS VERTENTE");
        context.put("total-aplicado", "83.040,02");
        context.put("saldo-atual-bruto", "85.870,73");
        context.put("iof-atual", "65,26");
        context.put("saldo-atual-liquido", "85.805,47");
        context.put("saldo-bloq-jud", "0,00");
        context.put("saldo-bloq-out", "25,01");
        context.put("saldo-bruto-disponivel", "85.845,44");

    }

    static String convertToBase64(Path path) {
        byte[] imageAsBytes = new byte[0];
        try {
            Resource resource = new UrlResource(path.toUri());
            InputStream inputStream = resource.getInputStream();
            imageAsBytes = IOUtils.toByteArray(inputStream);

        } catch (IOException e) {
            System.out.println("\n File read Exception");
        }

        return Base64.getEncoder().encodeToString(imageAsBytes);
    }
}
