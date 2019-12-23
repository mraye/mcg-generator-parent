package com.github.vspro.cg.config.profile;

import com.github.vspro.cg.config.PropertyHolder;
import com.github.vspro.cg.config.Validator;
import com.github.vspro.cg.exception.InvalidException;

import static com.github.vspro.cg.util.Messages.getString;
import static com.github.vspro.cg.util.StringUtil.stringHasValue;


public class TemplateGeneratorConfiguration extends PropertyHolder implements Validator {


	//模板引擎类型
	private String type;

	//指定do生成模板文件位置
	private String domainTplLocation;

	//指定sqlmap生成模板文件位置
	private String sqlMapTplLocation;

	//指定mapper生成模板文件位置
	private String mapperTplLocation;

	private boolean userClassPath;

	private String rootDir;

	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	public boolean isUserClassPath() {
		return userClassPath;
	}

	public void setUserClassPath(boolean userClassPath) {
		this.userClassPath = userClassPath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDomainTplLocation() {
		return domainTplLocation;
	}

	public void setDomainTplLocation(String domainTplLocation) {
		this.domainTplLocation = domainTplLocation;
	}

	public String getSqlMapTplLocation() {
		return sqlMapTplLocation;
	}

	public void setSqlMapTplLocation(String sqlMapTplLocation) {
		this.sqlMapTplLocation = sqlMapTplLocation;
	}

	public String getMapperTplLocation() {
		return mapperTplLocation;
	}

	public void setMapperTplLocation(String mapperTplLocation) {
		this.mapperTplLocation = mapperTplLocation;
	}

	@Override
	public void validate() throws InvalidException {

		if (!stringHasValue(type) || !stringHasValue(domainTplLocation)
				|| !stringHasValue(sqlMapTplLocation) || !stringHasValue(mapperTplLocation)) {

			throw new InvalidException(getString("ValidationError.4"));
		}

	}


}
