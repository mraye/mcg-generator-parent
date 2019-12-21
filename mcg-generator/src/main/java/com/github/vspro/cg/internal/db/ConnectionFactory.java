package com.github.vspro.cg.internal.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public interface ConnectionFactory {


	Connection getConnection() throws SQLException;


	void addConfigurationProperties(Properties properties);

}
