package com.example.htmlToPdf.enginer;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

public class EnginnerPDFItext {
    public static byte[] createPdf() {
        /* first, get and initialize an engine */
        VelocityEngine ve = new VelocityEngine();

        /* next, get the Template */
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate("templates/helloworld.vm", "UTF-8");
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

        // create a new document
        Document document = new Document();
        try {

            document = new Document();
            document.setPageSize(PageSize.LETTER);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);

            document.open();

            CSSResolver cssResolver = new StyleAttrCSSResolver();
            CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream("style.css".getBytes()));
            cssResolver.addCss(cssFile);

            // HTML
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

            htmlContext.setImageProvider(new AbstractImageProvider() {
                @Override
                public String getImageRootPath() {
                    return "src/main/resources/templates";
                }
            });

            // Pipelines
            PdfWriterPipeline pdf = new PdfWriterPipeline(document, pdfWriter);
            HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, htmlPipeline);

            // XML Worker
            XMLWorker worker = new XMLWorker(css, true);
            XMLParser p = new XMLParser(worker);
            p.parse(new ByteArrayInputStream(html.getBytes()));

            document.close();
            System.out.println("PDF generated successfully");

            return baos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static void preenchendoContext(VelocityContext context) {
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

}
