package com.github.vspro.cg.config.profile;

import com.github.vspro.cg.config.PropertyHolder;
import com.github.vspro.cg.config.Validator;
import com.github.vspro.cg.exception.InvalidException;

import static com.github.vspro.cg.util.Messages.getString;
import static com.github.vspro.cg.util.StringUtil.stringHasValue;


public class MapperGeneratorConfiguration extends PropertyHolder implements Validator {


	//包名
	private String targetPackage;
	//项目名称
	private String targetProject;
	//使用相对路径
	private boolean userClassPath;


	public String getTargetPackage() {
		return targetPackage;
	}

	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}

	public String getTargetProject() {
		return targetProject;
	}

	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}

	public boolean isUserClassPath() {
		return userClassPath;
	}

	public void setUserClassPath(boolean userClassPath) {
		this.userClassPath = userClassPath;
	}

	@Override
	public void validate() throws InvalidException {

		if (!stringHasValue(targetPackage)){
			throw new InvalidException(getString("ValidationError.4"));
		}

		if (!stringHasValue(targetProject)){
			throw new InvalidException(getString("ValidationError.4"));
		}
	}


}
