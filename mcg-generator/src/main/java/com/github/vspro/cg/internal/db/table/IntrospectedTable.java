package com.github.vspro.cg.internal.db.table;


import com.github.vspro.cg.config.context.ContextHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据库反射表--对应的实体
 */
public class IntrospectedTable {

	private ActualTableName tableName;
	//all columns
	private List<IntrospectedColumn> columns = null;
	private ContextHolder contextHolder;

	/**
	 * Table remarks retrieved from database metadata.
	 */
	protected String remarks;

	/**
	 * Table type retrieved from database metadata.
	 */
	protected String tableType;

	protected List<IntrospectedColumn> primaryKeyColumns = new ArrayList<>();

	protected List<IntrospectedColumn> baseColumns = new ArrayList<>();

	protected List<IntrospectedColumn> blobColumns = new ArrayList<>();

	public ActualTableName getTableName() {
		return tableName;
	}

	public void setTableName(ActualTableName tableName) {
		this.tableName = tableName;
	}

	public List<IntrospectedColumn> getColumns() {
		if (columns == null){
			columns = Stream.of(primaryKeyColumns.stream(),baseColumns.stream(),
					blobColumns.stream())
					.flatMap(Function.identity())
					.collect(Collectors.toList());
		}
		return columns;
	}

	public List<IntrospectedColumn> getColumnsNoPrimaryKey() {
		return Stream.of(baseColumns.stream(), blobColumns.stream())
				.flatMap(Function.identity())
				.collect(Collectors.toList());
	}

	public void addColumn(IntrospectedColumn introspectedColumn) {
		if (introspectedColumn.isBLOBColumn()) {
			blobColumns.add(introspectedColumn);
		} else {
			baseColumns.add(introspectedColumn);
		}
	}

	public void addPrimaryKeyColumn(String columnName) {
		boolean found = false;
		// first search base columns
		Iterator<IntrospectedColumn> iter = baseColumns.iterator();
		while (iter.hasNext()) {
			IntrospectedColumn introspectedColumn = iter.next();
			if (introspectedColumn.getActualColumnName().equals(columnName)) {
				primaryKeyColumns.add(introspectedColumn);
				iter.remove();
				found = true;
				break;
			}
		}

		// search blob columns in the weird event that a blob is the primary key
		if (!found) {
			iter = blobColumns.iterator();
			while (iter.hasNext()) {
				IntrospectedColumn introspectedColumn = iter.next();
				if (introspectedColumn.getActualColumnName().equals(columnName)) {
					primaryKeyColumns.add(introspectedColumn);
					iter.remove();
					break;
				}
			}
		}
	}

	public List<IntrospectedColumn> getPrimaryKeyColumns() {
		return primaryKeyColumns;
	}


	public List<IntrospectedColumn> getBaseColumns() {
		return baseColumns;
	}


	public List<IntrospectedColumn> getBlobColumns() {
		return blobColumns;
	}

	public ContextHolder getContextHolder() {
		return contextHolder;
	}

	public void setContextHolder(ContextHolder contextHolder) {
		this.contextHolder = contextHolder;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public boolean hasAnyColumns() {
		return hasPrimaryKeyColumns() || hasBaseColumns() || hasBLOBColumns();
	}

	public boolean hasPrimaryKeyColumns() {
		return !primaryKeyColumns.isEmpty();
	}

	public boolean hasBLOBColumns() {
		return !blobColumns.isEmpty();
	}

	public boolean hasBaseColumns() {
		return !baseColumns.isEmpty();
	}
}
