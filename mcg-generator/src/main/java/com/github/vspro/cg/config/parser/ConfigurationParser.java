package com.github.vspro.cg.config.parser;

import com.github.vspro.cg.api.McgGenerator;
import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.config.constant.XmlConstants;
import com.github.vspro.cg.config.xml.XmlParserEntityResolver;
import com.github.vspro.cg.exception.XMLParseException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.vspro.cg.util.Messages.getString;


public class ConfigurationParser {

	private List<String> warnings;
	private List<String> parseErrors;

	public ConfigurationParser() {
		this.warnings = new ArrayList<>();
		this.parseErrors = new ArrayList<>();
	}

	public ConfigurationParser(List<String> warnings, List<String> parseErrors) {
		this.warnings = warnings;
		this.parseErrors = parseErrors;
	}

	public Configuration parseConfiguration(File file) throws IOException, XMLParseException {
		FileReader reader = new FileReader(file);
		return parseConfiguration(reader);
	}

	public Configuration parseConfiguration(FileReader reader) throws XMLParseException {

		InputSource inputSource = new InputSource(reader);
		return parseConfiguration(inputSource);
	}

	public Configuration parseConfiguration(InputSource inputSource) throws XMLParseException {

		parseErrors.clear();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);

		try {
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new XmlParserEntityResolver());
			builder.setErrorHandler(new ParserErrorHandler(warnings, parseErrors));

			Document document = null;
			try {
				document = builder.parse(inputSource);
			} catch (SAXException | IOException e) {
				e.printStackTrace();
				throw new XMLParseException(getString("XmlParseError.0"));
			}

			if (document == null || !parseErrors.isEmpty()) {
				throw new XMLParseException(getString("XmlParseError.0"));
			}


			Configuration config;
			Element rootElement = document.getDocumentElement();
			DocumentType doctype = document.getDoctype();

			if (XmlConstants.MCG_PUBLIC_ID.equalsIgnoreCase(doctype.getPublicId())) {
				config = parseMgcConfiguration(rootElement);
			} else {
				throw new XMLParseException(getString("XmlParseError.0"));
			}

			if (!parseErrors.isEmpty()) {
				throw new XMLParseException(getString("XmlParseError.0"));
			}

			return config;
		} catch (ParserConfigurationException e) {
			throw new XMLParseException(getString("XmlParseError.0"));
		}


	}

	private Configuration parseMgcConfiguration(Element rootElement) throws XMLParseException {

		MgcConfigurationParser mgcParser = new MgcConfigurationParser();
		return mgcParser.parse(rootElement);
	}



	public static void main(String[] args) throws IOException, XMLParseException {
		ConfigurationParser parser = new ConfigurationParser();
		File file = new File(ConfigurationParser.class.getClassLoader().getResource("mcg-config.xml").getPath());
		Configuration configuration = parser.parseConfiguration(file);
		McgGenerator generator = new McgGenerator(configuration);
		generator.generate();
	}



}
