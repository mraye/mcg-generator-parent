package com.github.vspro.cg.config.profile;

import com.github.vspro.cg.config.PropertyHolder;
import com.github.vspro.cg.config.Validator;
import com.github.vspro.cg.exception.InvalidException;

import static com.github.vspro.cg.util.Messages.getString;
import static com.github.vspro.cg.util.StringUtil.stringHasValue;

public class TableConfiguration extends PropertyHolder implements Validator {

	//表名
	private String tableName;
	//Do类名称
	private String domainObjectName;
	//Dao接口名称
	private String mapperInterfaceName;
	//sqlxml名称
	private String mapperXmlName;
	//是否逻辑删除，默认是否
	private boolean enableLogicalDel=false;
	//enableLogicalDel=true时有效，逻辑删除列名
	private String logicalDelColName;
	//enableLogicalDel=true时有效，逻辑删除列值
	private String logicalDelColVal;

	private String schema;
	private String catalog;

	public String getLogicalDelColVal() {
		return logicalDelColVal;
	}

	public void setLogicalDelColVal(String logicalDelColVal) {
		this.logicalDelColVal = logicalDelColVal;
	}

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

		if (enableLogicalDel){
			if (!stringHasValue(logicalDelColName)
					|| !stringHasValue(logicalDelColVal)){
				throw new InvalidException(getString("ValidationError.4"));
			}

			if (logicalDelColVal.split(",").length !=2){

			}
		}


	}
}
