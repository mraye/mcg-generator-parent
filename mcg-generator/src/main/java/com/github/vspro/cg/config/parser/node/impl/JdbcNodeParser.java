package com.github.vspro.cg.config.parser.node.impl;

import com.github.vspro.cg.config.constant.CfgNodeConstants;
import com.github.vspro.cg.config.parser.node.BaseNodeParser;
import com.github.vspro.cg.config.profile.JdbcConnectionConfiguration;
import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.exception.InvalidException;

import java.util.Properties;

import static com.github.vspro.cg.util.StringUtil.stringHasValue;

public class JdbcNodeParser extends BaseNodeParser {

	public JdbcNodeParser() {
		super(CfgNodeConstants.NODE_JDBC);
	}


	@Override
	protected void doInjectProperties(Configuration configuration, Properties properties) throws InvalidException {
		JdbcConnectionConfiguration jdbc = new JdbcConnectionConfiguration();
		configuration.setJdbcConnectionConfiguration(jdbc);

		String driverClass = properties.getProperty("driverClass");
		String connectionUrl = properties.getProperty("connectionUrl");
		String userName = properties.getProperty("userName");
		String password = properties.getProperty("password");

		if (stringHasValue(driverClass)){
			jdbc.setDriverClass(driverClass);
		}

		if (stringHasValue(connectionUrl)){
			jdbc.setConnectionURL(connectionUrl);
		}

		jdbc.setPassword(password);
		jdbc.setUserName(userName);

		jdbc.validate();
	}
}
