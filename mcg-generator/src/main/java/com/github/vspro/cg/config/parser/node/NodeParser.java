package com.github.vspro.cg.config.parser.node;

import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.exception.InvalidException;
import org.w3c.dom.Node;

public interface NodeParser {

	void parseNode(Configuration configuration, Node node) throws InvalidException;

	boolean support(String nodeName);

}
