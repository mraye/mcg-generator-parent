package com.github.vspro.cg.config.parser;

import com.github.vspro.cg.config.parser.node.NodeParser;
import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.config.parser.node.CompositeNodeParser;
import com.github.vspro.cg.exception.InvalidException;
import com.github.vspro.cg.exception.XMLParseException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static com.github.vspro.cg.util.Messages.getString;

public class MgcConfigurationParser {

	private NodeParser nodeParser = new CompositeNodeParser();

	public Configuration parse(Element rootNode) throws XMLParseException {
		Configuration configuration = new Configuration();

		NodeList childNodes = rootNode.getChildNodes();

		try {

			for (int i = 0; i < childNodes.getLength(); i++) {

				Node node = childNodes.item(i);
				if (node.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}

				if (nodeParser.support(node.getNodeName())) {
					nodeParser.parseNode(configuration, node);
				}

			}
		} catch (InvalidException e) {
			throw new XMLParseException(getString("XmlParseError.0"));
		}

		return configuration;
	}

}
