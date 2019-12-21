package com.github.vspro.cg.internal.db;

import com.github.vspro.cg.config.profile.JdbcConnectionConfiguration;
import com.github.vspro.cg.util.Messages;
import com.github.vspro.cg.util.StringUtil;
import com.github.vspro.cg.internal.ObjectFactory;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import static com.github.vspro.cg.util.Messages.getString;

public class JdbcConnectionFactory implements ConnectionFactory {


	private String driverClass;
	private String connectionURL;
	private String userName;
	private String password;
	private Properties extProperties;

	public JdbcConnectionFactory(JdbcConnectionConfiguration config){
		this.driverClass = config.getDriverClass();
		this.connectionURL = config.getConnectionURL();
		this.userName = config.getUserName();
		this.password = config.getPassword();
		this.extProperties = new Properties();
	}


	@Override
	public Connection getConnection() throws SQLException {

		Properties props = new Properties();
		if (StringUtil.stringHasValue(userName)) {
			props.setProperty("user", userName);
		}

		if (StringUtil.stringHasValue(password)) {
			props.setProperty("password", password);
		}

		Driver driver = getDriver();
		Connection connect = driver.connect(connectionURL, props);
		if (connect == null){
			throw new SQLException(Messages.getString("SQLException.0"));
		}

		return connect;
	}

	private Driver getDriver() {

		Driver driver;
		try {
			Class<?> clz = ObjectFactory.extClassForName(driverClass);
			driver = (Driver) clz.getConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(Messages.getString("XmlParseError.0"));
		} 
		return driver;
	}

	@Override
	public void addConfigurationProperties(Properties properties) {

		//do nothing..
	}
}
