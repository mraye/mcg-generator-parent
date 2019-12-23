package com.github.vspro.cg.config.parser.node.impl;

import com.github.vspro.cg.config.constant.CfgNodeConstants;
import com.github.vspro.cg.config.parser.node.BaseNodeParser;
import com.github.vspro.cg.config.profile.TemplateGeneratorConfiguration;
import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.exception.InvalidException;

import java.util.Properties;

import static com.github.vspro.cg.util.Messages.getString;
import static com.github.vspro.cg.util.StringUtil.stringHasValue;

public class TemplateNodeParser extends BaseNodeParser {

	public TemplateNodeParser() {
		super(CfgNodeConstants.NODE_TEMPLATE);
	}

	@Override
	protected void doInjectProperties(Configuration configuration, Properties properties) throws InvalidException {

		TemplateGeneratorConfiguration template = new TemplateGeneratorConfiguration();
		configuration.setTemplateGeneratorConfiguration(template);

		String type = properties.getProperty("type");
		String domainTplLocation = properties.getProperty("domainTplLocation");
		String sqlMapTplLocation = properties.getProperty("sqlMapTplLocation");
		String mapperTplLocation = properties.getProperty("mapperTplLocation");
		String useClassPath = properties.getProperty("useClassPath");
		String rootDir = properties.getProperty("rootDir");


		if (!CfgNodeConstants.TPL_ENGINE_TYPE_FM.equalsIgnoreCase(type)
				&& !CfgNodeConstants.TPL_ENGINE_TYPE_VT.equalsIgnoreCase(type)) {

			throw new InvalidException(getString("TplNotFoundError.0"));
		}

		if (stringHasValue(useClassPath)){
			template.setUserClassPath(Boolean.parseBoolean(useClassPath));
		}else {
			template.setUserClassPath(true);
		}

		if (!template.isUserClassPath() &&
				(!stringHasValue(rootDir)
						|| !stringHasValue(domainTplLocation)
						|| !stringHasValue(sqlMapTplLocation)
						|| !stringHasValue(mapperTplLocation))){

			throw new InvalidException(getString("ValidationError.4"));
		}

		domainTplLocation = stringHasValue(domainTplLocation) ? domainTplLocation :
				CfgNodeConstants.TPL_ENGINE_TYPE_FM.equalsIgnoreCase(type) ?
						CfgNodeConstants.TPL_FM_DOMAIN_LOCATION :

						CfgNodeConstants.TPL_VT_DOMAIN_LOCATION;

		mapperTplLocation = stringHasValue(mapperTplLocation) ? mapperTplLocation :
				CfgNodeConstants.TPL_ENGINE_TYPE_FM.equalsIgnoreCase(type) ?
						CfgNodeConstants.TPL_FM_MAPPER_LOCATION :
						CfgNodeConstants.TPL_VT_MAPPER_LOCATION;

		sqlMapTplLocation = stringHasValue(sqlMapTplLocation) ? sqlMapTplLocation :
				CfgNodeConstants.TPL_ENGINE_TYPE_FM.equalsIgnoreCase(type) ?
						CfgNodeConstants.TPL_FM_SQLMAP_LOCATION :
						CfgNodeConstants.TPL_FM_SQLMAP_LOCATION;


		template.setRootDir(rootDir);
		template.setType(type);
		template.setDomainTplLocation(domainTplLocation);
		template.setMapperTplLocation(mapperTplLocation);
		template.setSqlMapTplLocation(sqlMapTplLocation);

		template.validate();

	}
}
