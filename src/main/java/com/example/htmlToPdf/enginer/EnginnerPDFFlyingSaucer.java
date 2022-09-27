package com.example.htmlToPdf.enginer;

import com.itextpdf.text.DocumentException;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class EnginnerPDFFlyingSaucer {

	public static byte[] createPdf(String fileName) throws IOException, DocumentException {

		VelocityEngine ve = new VelocityEngine();
		initEngine(ve);
		Template t = ve.getTemplate("helloworld.vm", "UTF-8");
		VelocityContext context = new VelocityContext();
		preenchendoContext(context);
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		System.out.println(writer.toString());
		String pdfHtml = htmlToXhtml(writer.toString());

		return xhtmlToPdf(pdfHtml, fileName).toByteArray();

	}

	private static void preenchendoContext(VelocityContext context) {
		Path path = Paths.get("src/main/resources/templates/logo-pan.png");

		String base64Image = convertToBase64(path);
		context.put("logo", base64Image);
		context.put("cnpj", "059.285.411/0001-13");
		context.put("endereco-banco", "PAULISTA 1374 11 ANDAR 1374 - 11 ANDAR\n" + "- BELA VISTA - SAO PAULO - SP");
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
		context.put("telefone", "(09) 67882227");
		context.put("total-aplicado", "83.040,02");
		context.put("saldo-atual-bruto", "85.870,73");
		context.put("iof-atual", "65,26");
		context.put("saldo-atual-liquido", "85.805,47");
		context.put("saldo-bloq-jud", "0,00");
		context.put("saldo-bloq-out", "25,01");
		context.put("saldo-bruto-disponivel", "85.845,44");
		
		
		Map<String, String> item = new HashMap<>();
		item.put("papel", "GUARDAR DINHEIRO");
		item.put("nota", "1040166");
		item.put("indicador-taxa", "103,00% CDI / 0,00");
		item.put("emissao", "17/05/2021");
		item.put("vencimento", "17/05/2021");
		item.put("valor-aplicado", "35.000,00");
		item.put("saldo-bruto", "37.635,85");
		item.put("ir", "31,95");
		item.put("iof", "31,95");
		item.put("saldo-liquido", "37.635,85");
		item.put("liquidez", "25/01/2024");
		
		List<Map<String, String>> lista = new ArrayList<>(); 
		
		for (int i = 0; i < 15; i++) {
			lista.add(item);
		}
		
		context.put("lista", lista);
	}

	private static void initEngine(VelocityEngine ve) {
		Properties props = new Properties();
		final String path = "src/main/resources/templates/";
		props.put("file.resource.loader.path", path);
		ve.init(props);
	}

	private static String htmlToXhtml(String html) {
		Document document = Jsoup.parse(html);
		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		return document.html();
	}

	private static ByteArrayOutputStream xhtmlToPdf(String xhtml, String outFileName) throws IOException, DocumentException {
		ITextRenderer iTextRenderer = new ITextRenderer(20f * 4f / 3f, 20);
		iTextRenderer.getSharedContext().setReplacedElementFactory(new MediaReplacedElementFactory(iTextRenderer.getSharedContext().getReplacedElementFactory()));
		iTextRenderer.setDocumentFromString(xhtml);
		iTextRenderer.layout();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		iTextRenderer.createPDF(baos);
		return baos;
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
