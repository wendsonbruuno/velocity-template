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
		context.put("cnpj-cpf", "33801206858");
		context.put("endereco-cliente", "AVENIDA ALBERT BARTHOLOME, 172 AP 138 TOR A");
		context.put("cep", "05541000");
		context.put("cidade", "SAO PAULO ");
		context.put("bairro", "JARDIM DAS VERTENTE");

		context.put("importante",
				"Devem estar formalizados neste documento, compromissos de recompra ou compra caso existente.\n Este documento é intransferível e inegociável. \n Informamos para todos os fins que o controle e acompanhamento das datas de vencimento das aplicações em depósito a prazo realizado pelos clientes são de sua\r\n"
						+ "inteira responsabilidade.");

		context.put("observacoes", "EMISSAO POUPA PAN");

		Map<String, String> itemCaracteristicaTitulo = new HashMap<>();
		itemCaracteristicaTitulo.put("titulo", "GUARDAR DINHEIRO");
		itemCaracteristicaTitulo.put("emissao", "23/08/2022");
		itemCaracteristicaTitulo.put("vencimento", "21/08/2023");
		itemCaracteristicaTitulo.put("local-custodia", "CRT4");
		itemCaracteristicaTitulo.put("indexador", "CDI");
		itemCaracteristicaTitulo.put("percentual", "111,0000");
		itemCaracteristicaTitulo.put("taxa-nominal", "0,0000");
		itemCaracteristicaTitulo.put("codigo-if", "");

		context.put("caracteristica-titulo", itemCaracteristicaTitulo);

		Map<String, String> itemCaracteristicaoOperacao = new HashMap<>();
		itemCaracteristicaoOperacao.put("quantidade", "400");
		itemCaracteristicaoOperacao.put("pu-operacao", "0,01000000");
		itemCaracteristicaoOperacao.put("forma-liquidacao", "CONTA CORRENTE");
		itemCaracteristicaoOperacao.put("valor-bruto", "4,00");
		itemCaracteristicaoOperacao.put("if", "");
		itemCaracteristicaoOperacao.put("iof", "111,0000");
		itemCaracteristicaoOperacao.put("valor-liquido", "4,00");
		itemCaracteristicaoOperacao.put("condicao-resgate", "M - Condição de resgate antecipado");

		context.put("caracteristica-operacao", itemCaracteristicaoOperacao);

		Map<String, String> itemCaracteristicaoCompromisso = new HashMap<>();
		itemCaracteristicaoCompromisso.put("taxa", "0,000000%");
		itemCaracteristicaoCompromisso.put("prazo", "0");
		itemCaracteristicaoCompromisso.put("pu-retorno", "0,00000000");
		itemCaracteristicaoCompromisso.put("indexador", "0,000%");
		itemCaracteristicaoCompromisso.put("vencimento", "");
		itemCaracteristicaoCompromisso.put("valor-retorno-bruto", "0,00");
		itemCaracteristicaoCompromisso.put("ir-retido", "0,00");
		itemCaracteristicaoCompromisso.put("valor-retorno-liquido", "0,00");

		context.put("caracteristica-compromisso", itemCaracteristicaoCompromisso);
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

	private static ByteArrayOutputStream xhtmlToPdf(String xhtml, String outFileName)
			throws IOException, DocumentException {
		ITextRenderer iTextRenderer = new ITextRenderer(20f * 4f / 3f, 20);
		iTextRenderer.getSharedContext().setReplacedElementFactory(
				new MediaReplacedElementFactory(iTextRenderer.getSharedContext().getReplacedElementFactory()));
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
