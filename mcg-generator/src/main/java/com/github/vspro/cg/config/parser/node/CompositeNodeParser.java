package com.github.vspro.cg.config.parser.node;

import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.config.parser.node.impl.*;
import com.github.vspro.cg.exception.InvalidException;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class CompositeNodeParser implements NodeParser {

	private List<NodeParser> parsers;
	private NodeParser delegate;

	public CompositeNodeParser(){
		initParser();
	}

	private void initParser() {
		parsers = new ArrayList<>();
		parsers.add(new DomainNodeParser());
		parsers.add(new JdbcNodeParser());
		parsers.add(new MapperNodeParser());
		parsers.add(new SqlMapNodeParser());
		parsers.add(new TemplateNodeParser());
		parsers.add(new TableNodeParser());
	}


	@Override
	public void parseNode(Configuration configuration, Node node) throws InvalidException {
		delegate.parseNode(configuration, node);
	}

	@Override
	public boolean support(String nodeName) {
		for (NodeParser parser: parsers){
			if (parser.support(nodeName)){
				delegate = parser;
				return true;
			}
		}
		return false;
	}
}
