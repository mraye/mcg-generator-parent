package com.github.vspro.cg.config.parser.node.impl;

import com.github.vspro.cg.config.constant.CfgNodeConstants;
import com.github.vspro.cg.config.parser.node.BaseNodeParser;
import com.github.vspro.cg.config.profile.SqlMapGeneratorConfiguration;
import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.exception.InvalidException;

import java.util.Properties;

import static com.github.vspro.cg.util.StringUtil.stringHasValue;

public class SqlMapNodeParser extends BaseNodeParser {

	public SqlMapNodeParser() {
		super(CfgNodeConstants.NODE_SQLMAP);
	}


	@Override
	protected void doInjectProperties(Configuration configuration, Properties properties) throws InvalidException {
		SqlMapGeneratorConfiguration sqlMap = new SqlMapGeneratorConfiguration();
		configuration.setSqlMapGeneratorConfiguration(sqlMap);

		String targetPackage = properties.getProperty("targetPackage");
		String targetProject = properties.getProperty("targetProject");
		String useClassPath = properties.getProperty("useClassPath");

		sqlMap.setTargetPackage(targetPackage);
		sqlMap.setTargetProject(targetProject);

		if (stringHasValue(useClassPath)) {
			sqlMap.setUserClassPath(Boolean.parseBoolean(useClassPath));
		} else {
			sqlMap.setUserClassPath(true);
		}

		sqlMap.validate();

	}
}
