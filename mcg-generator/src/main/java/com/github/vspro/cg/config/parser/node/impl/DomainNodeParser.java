package com.github.vspro.cg.config.parser.node.impl;

import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.config.constant.CfgNodeConstants;
import com.github.vspro.cg.config.parser.node.BaseNodeParser;
import com.github.vspro.cg.config.profile.DomainGeneratorConfiguration;
import com.github.vspro.cg.exception.InvalidException;

import java.util.Properties;

import static com.github.vspro.cg.util.StringUtil.stringHasValue;

public class DomainNodeParser extends BaseNodeParser {

	public DomainNodeParser() {
		super(CfgNodeConstants.NODE_DOMAIN);
	}


	@Override
	protected void doInjectProperties(Configuration configuration, Properties properties) throws InvalidException {
		DomainGeneratorConfiguration domain = new DomainGeneratorConfiguration();
		configuration.setDomainGeneratorConfiguration(domain);

		String targetPackage = properties.getProperty("targetPackage");
		String targetProject = properties.getProperty("targetProject");
		String useClassPath = properties.getProperty("useClassPath");

		domain.setTargetPackage(targetPackage);
		domain.setTargetProject(targetProject);
		if (stringHasValue(useClassPath)){
			domain.setUserClassPath(Boolean.parseBoolean(useClassPath));
		}else {
			domain.setUserClassPath(true);
		}

		domain.validate();

	}
}
