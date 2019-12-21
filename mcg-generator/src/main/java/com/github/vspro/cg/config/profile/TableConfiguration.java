package com.github.vspro.cg.config.profile;

import com.github.vspro.cg.config.PropertyHolder;
import com.github.vspro.cg.config.Validator;
import com.github.vspro.cg.exception.InvalidException;

import static com.github.vspro.cg.util.Messages.getString;
import static com.github.vspro.cg.util.StringUtil.stringHasValue;

public class TableConfiguration extends PropertyHolder implements Validator {

	private String tableName;
	private String domainObjectName;
	private String mapperInterfaceName;
	private String mapperXmlName;
	private boolean enableLogicalDel=false;
	private String logicalDelColName;

	private String schema;
	private String catalog;

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDomainObjectName() {
		return domainObjectName;
	}

	public void setDomainObjectName(String domainObjectName) {
		this.domainObjectName = domainObjectName;
	}

	public String getMapperInterfaceName() {
		return mapperInterfaceName;
	}

	public void setMapperInterfaceName(String mapperInterfaceName) {
		this.mapperInterfaceName = mapperInterfaceName;
	}

	public String getMapperXmlName() {
		return mapperXmlName;
	}

	public void setMapperXmlName(String mapperXmlName) {
		this.mapperXmlName = mapperXmlName;
	}

	public boolean isEnableLogicalDel() {
		return enableLogicalDel;
	}

	public void setEnableLogicalDel(boolean enableLogicalDel) {
		this.enableLogicalDel = enableLogicalDel;
	}

	public String getLogicalDelColName() {
		return logicalDelColName;
	}

	public void setLogicalDelColName(String logicalDelColName) {
		this.logicalDelColName = logicalDelColName;
	}

	@Override
	public void validate() throws InvalidException {
		if (!stringHasValue(tableName)){
			throw new InvalidException(getString("ValidationError.4"));
		}

		if (enableLogicalDel && !stringHasValue(logicalDelColName)){
			throw new InvalidException(getString("ValidationError.4"));
		}

	}
}
