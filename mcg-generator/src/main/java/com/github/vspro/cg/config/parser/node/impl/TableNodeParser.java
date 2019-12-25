package com.github.vspro.cg.config.parser.node.impl;

import com.github.vspro.cg.config.constant.CfgNodeConstants;
import com.github.vspro.cg.config.parser.node.BaseNodeParser;
import com.github.vspro.cg.config.profile.TableConfiguration;
import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.exception.InvalidException;

import java.util.Properties;

import static com.github.vspro.cg.util.StringUtil.stringHasValue;

public class TableNodeParser extends BaseNodeParser {

	public TableNodeParser() {
		super(CfgNodeConstants.NODE_TABLE);
	}


	@Override
	protected void doInjectProperties(Configuration configuration, Properties properties) throws InvalidException {
		TableConfiguration table = new TableConfiguration();
		configuration.setTableConfiguration(table);
		String connectionURL = configuration.getJdbcConnectionConfiguration().getConnectionURL();

		String tableName = properties.getProperty("tableName");
		String domainObjectName = properties.getProperty("domainObjectName");
		String mapperInterfaceName = properties.getProperty("mapperInterfaceName");
		String mapperXmlName = properties.getProperty("mapperXmlName");
		String enableLogicalDel = properties.getProperty("enableLogicalDel");
		String logicalDelColName = properties.getProperty("logicalDelColName");
		String logicalDelColVal = properties.getProperty("logicalDelColVal");

		String schema = schema(connectionURL);
		table.setSchema(schema);
		table.setTableName(tableName);
		table.setCatalog(schema);
		table.setDomainObjectName(domainObjectName);
		table.setEnableLogicalDel(Boolean.parseBoolean(enableLogicalDel));
		table.setLogicalDelColName(logicalDelColName);
		table.setLogicalDelColVal(logicalDelColVal);
		table.validate();

		if (!stringHasValue(mapperInterfaceName)) {
			table.setMapperInterfaceName(domainObjectName + "Dao");
		} else {
			table.setMapperInterfaceName(mapperInterfaceName);
		}

		if (!stringHasValue(mapperInterfaceName)) {
			table.setMapperXmlName(domainObjectName + "Mapper");
		} else {
			table.setMapperXmlName(mapperXmlName);
		}
	}

	private String schema(String url){
		int i = url.lastIndexOf("/");
		String schema = url.substring(i+1);
		if (schema.length() < 1){
			return null;
		}
		if (schema.contains("?")){
			int index = schema.indexOf("?");
			schema = schema.substring(0,index);
		}
		return schema;
	}
}
