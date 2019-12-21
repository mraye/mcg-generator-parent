package com.github.vspro.cg.config.parser.node;

import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.exception.InvalidException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public abstract class BaseNodeParser implements NodeParser {

	private String nodeName;

	public BaseNodeParser(String nodeName) {
		this.nodeName = nodeName;
	}


	@Override
	public void parseNode(Configuration configuration, Node node) throws InvalidException {

		Properties properties = parseProperties(node);
		doInjectProperties(configuration, properties);
	}

	protected abstract void doInjectProperties(Configuration configuration, Properties properties) throws InvalidException;

	@Override
	public boolean support(String nodeName) {
		return getNodeName().equalsIgnoreCase(nodeName);
	}

	public String getNodeName() {
		return nodeName;
	}



	private Properties parseProperties(Node node) {
		Properties properties = new Properties();
		NamedNodeMap attributes = node.getAttributes();

		for (int i = 0; i < attributes.getLength() ; i++) {
			Node attr = attributes.item(i);
			String value = parsePropertyToken(attr.getNodeValue());
			properties.put(attr.getNodeName(), value);
		}
		return properties;
	}

	String parsePropertyToken(String s) {
		final String OPEN = "${";
		final String CLOSE = "}";
		int currentIndex = 0;

		List<String> answer = new ArrayList<>();

		int markerStartIndex = s.indexOf(OPEN);
		if (markerStartIndex < 0) {
			// no parameter markers
			answer.add(s);
			currentIndex = s.length();
		}

		while (markerStartIndex > -1) {
			if (markerStartIndex > currentIndex) {
				// add the characters before the next parameter marker
				answer.add(s.substring(currentIndex, markerStartIndex));
				currentIndex = markerStartIndex;
			}

			int markerEndIndex = s.indexOf(CLOSE, currentIndex);
			int nestedStartIndex = s.indexOf(OPEN, markerStartIndex + OPEN.length());
			while (nestedStartIndex > -1 && markerEndIndex > -1 && nestedStartIndex < markerEndIndex) {
				nestedStartIndex = s.indexOf(OPEN, nestedStartIndex + OPEN.length());
				markerEndIndex = s.indexOf(CLOSE, markerEndIndex + CLOSE.length());
			}

			if (markerEndIndex < 0) {
				// no closing delimiter, just move to the end of the string
				answer.add(s.substring(markerStartIndex));
				currentIndex = s.length();
				break;
			}

			// we have a valid property marker...
			String property = s.substring(markerStartIndex + OPEN.length(), markerEndIndex);
			String propertyValue = resolveProperty(parsePropertyToken(property));
			if (propertyValue == null) {
				// add the property marker back into the stream
				answer.add(s.substring(markerStartIndex, markerEndIndex + 1));
			} else {
				answer.add(propertyValue);
			}

			currentIndex = markerEndIndex + CLOSE.length();
			markerStartIndex = s.indexOf(OPEN, currentIndex);
		}

		if (currentIndex < s.length()) {
			answer.add(s.substring(currentIndex));
		}

		return answer.stream().collect(Collectors.joining());
	}

	private String resolveProperty(String key) {
		String property = System.getProperty(key);
		return property;
	}

}
