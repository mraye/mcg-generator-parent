package com.github.vspro.cg.config.profile;

import com.github.vspro.cg.config.PropertyHolder;
import com.github.vspro.cg.config.Validator;
import com.github.vspro.cg.exception.InvalidException;

import static com.github.vspro.cg.util.StringUtil.stringHasValue;
import static com.github.vspro.cg.util.Messages.getString;

public class JdbcConnectionConfiguration extends PropertyHolder implements Validator {


	private String driverClass;
	private String connectionURL;
	private String userName;
	private String password;

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void validate() throws InvalidException {
		if (!stringHasValue(driverClass)){
			throw new InvalidException(getString("ValidationError.4"));
		}
	}
}
