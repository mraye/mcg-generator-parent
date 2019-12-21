package com.github.vspro.cg.config;

import com.github.vspro.cg.config.constant.CfgNodeConstants;
import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.config.profile.*;
import com.github.vspro.cg.exception.InvalidException;
import com.github.vspro.cg.util.Messages;

public class Configuration implements Validator {

	//database connect
	JdbcConnectionConfiguration jdbcConnectionConfiguration;

	DomainGeneratorConfiguration domainGeneratorConfiguration;
	MapperGeneratorConfiguration mapperGeneratorConfiguration;
	SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration;

	//template engine config
	TemplateGeneratorConfiguration templateGeneratorConfiguration;

	TableConfiguration tableConfiguration;

	//如果外部指定，生成domain,dao,slqmap就心这个为根路径
	//主要在mcg-generator-plugin插件中用到
	String baseTargetProject;


	public JdbcConnectionConfiguration getJdbcConnectionConfiguration() {
		return jdbcConnectionConfiguration;
	}

	public void setJdbcConnectionConfiguration(JdbcConnectionConfiguration jdbcConnectionConfiguration) {
		this.jdbcConnectionConfiguration = jdbcConnectionConfiguration;
	}

	public TableConfiguration getTableConfiguration() {
		return tableConfiguration;
	}

	public void setTableConfiguration(TableConfiguration tableConfiguration) {
		this.tableConfiguration = tableConfiguration;
	}

	public DomainGeneratorConfiguration getDomainGeneratorConfiguration() {
		return domainGeneratorConfiguration;
	}

	public void setDomainGeneratorConfiguration(DomainGeneratorConfiguration domainGeneratorConfiguration) {
		this.domainGeneratorConfiguration = domainGeneratorConfiguration;
	}

	public MapperGeneratorConfiguration getMapperGeneratorConfiguration() {
		return mapperGeneratorConfiguration;
	}

	public void setMapperGeneratorConfiguration(MapperGeneratorConfiguration mapperGeneratorConfiguration) {
		this.mapperGeneratorConfiguration = mapperGeneratorConfiguration;
	}

	public SqlMapGeneratorConfiguration getSqlMapGeneratorConfiguration() {
		return sqlMapGeneratorConfiguration;
	}

	public void setSqlMapGeneratorConfiguration(SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration) {
		this.sqlMapGeneratorConfiguration = sqlMapGeneratorConfiguration;
	}

	public TemplateGeneratorConfiguration getTemplateGeneratorConfiguration() {
		return templateGeneratorConfiguration;
	}

	public void setTemplateGeneratorConfiguration(TemplateGeneratorConfiguration templateGeneratorConfiguration) {
		this.templateGeneratorConfiguration = templateGeneratorConfiguration;
	}


	public String getBaseTargetProject() {
		return baseTargetProject;
	}

	public void setBaseTargetProject(String baseTargetProject) {
		this.baseTargetProject = baseTargetProject;
	}

	@Override
	public void validate() throws InvalidException {

		if (templateGeneratorConfiguration == null){
			initDefaultTemplate();
		}

		if (jdbcConnectionConfiguration == null
				|| domainGeneratorConfiguration == null
				|| sqlMapGeneratorConfiguration == null
				|| mapperGeneratorConfiguration == null
				|| tableConfiguration == null) {
			throw new InvalidException(Messages.getString("XmlParseError.0"));
		}
	}


	//如果没有指定模板引擎，默认是velocity
	private void initDefaultTemplate() {
		templateGeneratorConfiguration = new TemplateGeneratorConfiguration();
		templateGeneratorConfiguration.setType(CfgNodeConstants.TPL_ENGINE_TYPE_VT);
		templateGeneratorConfiguration.setDomainTplLocation(CfgNodeConstants.TPL_VT_DOMAIN_LOCATION);
		templateGeneratorConfiguration.setMapperTplLocation(CfgNodeConstants.TPL_VT_MAPPER_LOCATION);
		templateGeneratorConfiguration.setSqlMapTplLocation(CfgNodeConstants.TPL_VT_SQLMAP_LOCATION);
	}


	/**
	 * context for holding configuration
	 * @return
	 */
	public ContextHolder getContext(){
		return new ContextHolder(this);
	}
}
