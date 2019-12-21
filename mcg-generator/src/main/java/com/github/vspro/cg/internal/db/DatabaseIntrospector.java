package com.github.vspro.cg.internal.db;

import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.config.profile.TableConfiguration;
import com.github.vspro.cg.exception.CodeAutoGenException;
import com.github.vspro.cg.internal.FullyQualifiedJavaType;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;
import com.github.vspro.cg.util.JavaBeanUtil;
import com.github.vspro.cg.util.Messages;
import com.github.vspro.cg.util.StringUtil;
import com.github.vspro.cg.internal.ObjectFactory;
import com.github.vspro.cg.internal.db.table.ActualTableName;
import com.github.vspro.cg.internal.db.table.IntrospectedColumn;
import com.github.vspro.cg.internal.types.JavaTypeResolver;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static com.github.vspro.cg.util.Messages.getString;

public class DatabaseIntrospector {

	private DatabaseMetaData databaseMetaData;
	private ContextHolder contextHolder;
	private JavaTypeResolver javaTypeResolver;

	public DatabaseIntrospector(DatabaseMetaData databaseMetaData,
	                            ContextHolder contextHolder,
	                            JavaTypeResolver javaTypeResolver) {
		this.databaseMetaData = databaseMetaData;
		this.contextHolder = contextHolder;
		this.javaTypeResolver = javaTypeResolver;
	}


	public List<IntrospectedTable> introspectTable(TableConfiguration tc) throws SQLException {

		//introspect all columns of the table
		Map<ActualTableName, List<IntrospectedColumn>> columns = getColumns(tc);

		if (columns == null || columns.isEmpty()) {
			return null;
		}

		//calculate javaType,jdbcType,columnName
		calculateExtraColumnInformation(tc, columns);
		//can read override column from tc
		//can apply key generator

		List<IntrospectedTable> tables = calculateIntrospectedTables(tc, columns);

		//remove invalid table
		Iterator<IntrospectedTable> iterator = tables.iterator();
		while (iterator.hasNext()) {
			IntrospectedTable next = iterator.next();

			if (!next.hasAnyColumns()) {
				//all columns are empty
				iterator.remove();

			} else if (!next.hasPrimaryKeyColumns() && !next.hasBaseColumns()) {
				//remove only has blob column
				iterator.remove();
			}
		}

		return tables;
	}

	private List<IntrospectedTable> calculateIntrospectedTables(TableConfiguration tc, Map<ActualTableName, List<IntrospectedColumn>> columns) {

		List<IntrospectedTable> result = new ArrayList<>();
		for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {

//			boolean enableLogicalDel = tc.isEnableLogicalDel();
//			String logicalDelColName = tc.getLogicalDelColName();

			ActualTableName atn = entry.getKey();

			IntrospectedTable introspectedTable = ObjectFactory.createIntrospectedTable(atn, contextHolder);

			for (IntrospectedColumn column : entry.getValue()) {
				//如果开启逻辑删除，需要过滤这个逻辑删除的列
//				if (enableLogicalDel && logicalDelColName.equalsIgnoreCase(column.getActualColumnName())) {
//					continue;
//				}

				introspectedTable.addColumn(column);
			}

			//find primary key
			calculatePrimaryKey(atn, introspectedTable);

			//find table remark etc..
			enhanceIntrospectedTable(atn, introspectedTable);
			result.add(introspectedTable);

		}
		return result;
	}

	private void enhanceIntrospectedTable(ActualTableName atn, IntrospectedTable introspectedTable) {
		try {

			ResultSet rs = databaseMetaData.getTables(
					atn.getCatalog(), atn.getSchema(),
					atn.getTableName(), null);
			if (rs.next()) {
				String remarks = rs.getString("REMARKS"); //$NON-NLS-1$
				String tableType = rs.getString("TABLE_TYPE"); //$NON-NLS-1$
				introspectedTable.setRemarks(remarks);
				introspectedTable.setTableType(tableType);
			}
			closeResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void calculatePrimaryKey(ActualTableName atn, IntrospectedTable introspectedTable) {


		ResultSet resultSet = null;
		try {
			resultSet = databaseMetaData.getPrimaryKeys(atn.getCatalog(), atn.getSchema(),
					atn.getTableName());
		} catch (SQLException e) {
			e.printStackTrace();
			closeResultSet(resultSet);
			return;
		}

		try {
			Map<Short, String> keyColumns = new TreeMap<>();

			while (resultSet.next()) {
				String columnName = resultSet.getString("COLUMN_NAME"); //$NON-NLS-1$
				short keySeq = resultSet.getShort("KEY_SEQ"); //$NON-NLS-1$
				keyColumns.put(keySeq, columnName);
			}


			for (String column : keyColumns.values()) {
				introspectedTable.addPrimaryKeyColumn(column);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(resultSet);
		}


	}

	private void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	private void calculateExtraColumnInformation(TableConfiguration tc, Map<ActualTableName, List<IntrospectedColumn>> columns) {

		Set<Map.Entry<ActualTableName, List<IntrospectedColumn>>> entries = columns.entrySet();

		//可以列名称处理增加策略模式
		for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : entries) {

			for (IntrospectedColumn introspectedColumn : entry.getValue()) {

				introspectedColumn.setJavaProperty(
						JavaBeanUtil.getCamelCaseString(introspectedColumn.getActualColumnName(), false));

				FullyQualifiedJavaType fullyQualifiedJavaType
						= javaTypeResolver.calculateJavaType(introspectedColumn);
				if (fullyQualifiedJavaType != null) {
					introspectedColumn.setFullyQualifiedJavaType(fullyQualifiedJavaType);
					introspectedColumn.setJdbcTypeName(javaTypeResolver.calculateJdbcTypeName(introspectedColumn));
					introspectedColumn.setJavaPropertyName(javaTypeResolver.calculateJavaPropertyName(introspectedColumn));
				} else {
					throw new CodeAutoGenException(Messages.getString("TypeCouldNotResolved.0"));
				}

			}
		}
	}


	private Map<ActualTableName, List<IntrospectedColumn>> getColumns(TableConfiguration tc) throws SQLException {
		String localCatalog;
		String localSchema;
		String localTableName;

		boolean delimitIdentifiers = StringUtil.stringContainsSpace(tc.getCatalog())
				|| StringUtil.stringContainsSpace(tc.getSchema())
				|| StringUtil.stringContainsSpace(tc.getTableName());

		if (delimitIdentifiers) {
			localCatalog = tc.getCatalog();
			localSchema = tc.getSchema();
			localTableName = tc.getTableName();
		} else if (databaseMetaData.storesLowerCaseIdentifiers()) {
			localCatalog = tc.getCatalog() == null ? null : tc.getCatalog().toLowerCase();
			localSchema = tc.getSchema() == null ? null : tc.getSchema().toLowerCase();
			localTableName = tc.getTableName() == null ? null : tc.getTableName().toLowerCase();
		} else if (databaseMetaData.storesUpperCaseIdentifiers()) {
			localCatalog = tc.getCatalog() == null ? null : tc.getCatalog().toUpperCase();
			localSchema = tc.getSchema() == null ? null : tc.getSchema().toUpperCase();
			localTableName = tc.getTableName() == null ? null : tc.getTableName().toUpperCase();
		} else {
			localCatalog = tc.getCatalog();
			localSchema = tc.getSchema();
			localTableName = tc.getTableName();
		}

		ResultSet rs = databaseMetaData.getColumns(localCatalog,
				localSchema, localTableName, "%");

		boolean supportsIsAutoIncrement = false;
		boolean supportsIsGeneratedColumn = false;
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			if ("IS_AUTOINCREMENT".equals(rsmd.getColumnName(i))) {
				supportsIsAutoIncrement = true;
			}
			if ("IS_GENERATEDCOLUMN".equals(rsmd.getColumnName(i))) {
				supportsIsGeneratedColumn = true;
			}

		}

		Map<ActualTableName, List<IntrospectedColumn>> result = new HashMap<>();
		while (rs.next()) {
			//construct introspectColumn
			IntrospectedColumn introspectedColumn =
					ObjectFactory.createIntrospectedColumn(contextHolder);

			introspectedColumn.setJdbcType(rs.getInt("DATA_TYPE"));
			introspectedColumn.setActualTypeName(rs.getString("TYPE_NAME"));
			introspectedColumn.setLength(rs.getInt("COLUMN_SIZE"));
			introspectedColumn.setActualColumnName(rs.getString("COLUMN_NAME"));
			introspectedColumn
					.setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable); //$NON-NLS-1$

			introspectedColumn.setScale(rs.getInt("DECIMAL_DIGITS"));
			introspectedColumn.setRemarks(rs.getString("REMARKS"));

			introspectedColumn.setDefaultValue(rs.getString("COLUMN_DEF")); //$NON-NLS-1$

			if (supportsIsAutoIncrement) {
				introspectedColumn.setAutoIncrement(
						"YES".equals(rs.getString("IS_AUTOINCREMENT"))); //$NON-NLS-1$ //$NON-NLS-2$
			}

			if (supportsIsGeneratedColumn) {
				introspectedColumn.setGeneratedColumn(
						"YES".equals(rs.getString("IS_GENERATEDCOLUMN"))); //$NON-NLS-1$ //$NON-NLS-2$
			}

			ActualTableName actualTableName = new ActualTableName(
					rs.getString("TABLE_CAT"),
					rs.getString("TABLE_SCHEM"),
					rs.getString("TABLE_NAME"));
			List<IntrospectedColumn> columnList = result.get(actualTableName);

			if (columnList == null) {
				columnList = new ArrayList<>();
				result.put(actualTableName, columnList);
			}
			columnList.add(introspectedColumn);
		}

		return result;
	}
}
