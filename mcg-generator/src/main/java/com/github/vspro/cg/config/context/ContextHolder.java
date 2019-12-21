package com.github.vspro.cg.config.context;

import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.config.profile.*;
import com.github.vspro.cg.internal.ObjectFactory;
import com.github.vspro.cg.internal.db.ConnectionFactory;
import com.github.vspro.cg.internal.db.DatabaseIntrospector;
import com.github.vspro.cg.internal.db.JdbcConnectionFactory;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;
import com.github.vspro.cg.internal.types.JavaTypeResolver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.github.vspro.cg.util.Messages.getString;
import static com.github.vspro.cg.util.StringUtil.combineWithSp;

public class ContextHolder {


    JdbcConnectionConfiguration jdbcConnectionConfiguration;

    DomainGeneratorConfiguration domainGeneratorConfiguration;
    MapperGeneratorConfiguration mapperGeneratorConfiguration;
    SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration;


    //template engine config
    TemplateGeneratorConfiguration templateGeneratorConfiguration;

    TableConfiguration tableConfiguration;

    //数据库反射的表以及字段
    List<IntrospectedTable> introspectedTables;

    String baseTargetProject;

    public ContextHolder(Configuration configuration) {
        this.jdbcConnectionConfiguration = configuration.getJdbcConnectionConfiguration();
        this.domainGeneratorConfiguration = configuration.getDomainGeneratorConfiguration();
        this.mapperGeneratorConfiguration = configuration.getMapperGeneratorConfiguration();
        this.sqlMapGeneratorConfiguration = configuration.getSqlMapGeneratorConfiguration();
        this.templateGeneratorConfiguration = configuration.getTemplateGeneratorConfiguration();
        this.tableConfiguration = configuration.getTableConfiguration();
        this.baseTargetProject = configuration.getBaseTargetProject();
    }

    public TemplateGeneratorConfiguration getTemplateGeneratorConfiguration() {
        return templateGeneratorConfiguration;
    }

    public void setTemplateGeneratorConfiguration(TemplateGeneratorConfiguration templateGeneratorConfiguration) {
        this.templateGeneratorConfiguration = templateGeneratorConfiguration;
    }

    public JdbcConnectionConfiguration getJdbcConnectionConfiguration() {
        return jdbcConnectionConfiguration;
    }

    public void setJdbcConnectionConfiguration(JdbcConnectionConfiguration jdbcConnectionConfiguration) {
        this.jdbcConnectionConfiguration = jdbcConnectionConfiguration;
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

    public TableConfiguration getTableConfiguration() {
        return tableConfiguration;
    }

    public void setTableConfiguration(TableConfiguration tableConfiguration) {
        this.tableConfiguration = tableConfiguration;
    }

    public List<IntrospectedTable> getIntrospectedTables() {
        if (introspectedTables == null) {
            introspectTables();
        }
        return introspectedTables;
    }

    private void introspectTables() {
        Connection connection = null;
        JavaTypeResolver javaTypeResolver = ObjectFactory.createJavaTypeResolver(this);

        try {
            connection = getConnection();
            DatabaseIntrospector databaseIntrospector
                    = new DatabaseIntrospector(connection.getMetaData(), this, javaTypeResolver);

            List<IntrospectedTable> tables = databaseIntrospector.introspectTable(tableConfiguration);

            if (tables != null) {
                introspectedTables = tables;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(getString("SQLException.0"));
        } finally {
            closeConnection(connection);
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Connection getConnection() throws SQLException {
        ConnectionFactory connectionFactory = null;
        if (jdbcConnectionConfiguration != null) {
            connectionFactory = new JdbcConnectionFactory(jdbcConnectionConfiguration);
        }

        Connection connection = connectionFactory.getConnection();
        return connection;
    }


    //com.example.Domain
    public String getDomainFullType() {
        return combineWithSp('.',
                getDomainGeneratorConfiguration().getTargetPackage(),
                getTableConfiguration().getDomainObjectName());
    }

    //com.example.Dao
    public String getMapperFullType() {
        return combineWithSp('.',
                getMapperGeneratorConfiguration().getTargetPackage(),
                getTableConfiguration().getMapperInterfaceName());
    }

    public String getBaseTargetProject() {
        return baseTargetProject;
    }
}
