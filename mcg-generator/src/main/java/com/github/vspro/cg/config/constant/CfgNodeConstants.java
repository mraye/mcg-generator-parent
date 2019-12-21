package com.github.vspro.cg.config.constant;

/**
 * 配置文件节点常量
 */
public abstract class CfgNodeConstants {

	//节点
	public static final String NODE_DOMAIN = "domainGenerator";
	public static final String NODE_JDBC = "jdbcConnection";
	public static final String NODE_MAPPER = "mapperGenerator";
	public static final String NODE_SQLMAP = "sqlmapGenerator";
	public static final String NODE_TABLE = "table";
	public static final String NODE_TEMPLATE = "templateGenerator";


	//模板文件名称
	public static final String TPL_DOMAIN_FILE_NAME = "domain";
	public static final String TPL_SQLMAP_FILE_NAME = "sqlmap";
	public static final String TPL_MAPPER_FILE_NAME = "mapper";

	//velocity
	private static final String TPL_VT_PREFIX = "/com/github/vspro/cg/config/tpl/vt/";
	public static final String TPL_VT_SUFFIX= ".vm";
	public static final String TPL_ENGINE_TYPE_VT = "vt";
	public static final String TPL_VT_DOMAIN_LOCATION = TPL_VT_PREFIX + TPL_DOMAIN_FILE_NAME + TPL_VT_SUFFIX;
	public static final String TPL_VT_SQLMAP_LOCATION = TPL_VT_PREFIX + TPL_SQLMAP_FILE_NAME + TPL_VT_SUFFIX;
	public static final String TPL_VT_MAPPER_LOCATION = TPL_VT_PREFIX + TPL_MAPPER_FILE_NAME + TPL_VT_SUFFIX;


	//freemarker
	public static final String TPL_FM_PREFIX = "/com/github/vspro/cg/config/tpl/fm";
	public static final String TPL_FM_SUFFIX= ".ftl";
	public static final String TPL_ENGINE_TYPE_FM = "fm";
	public static final String TPL_FM_DOMAIN_LOCATION = TPL_DOMAIN_FILE_NAME + TPL_FM_SUFFIX;
	public static final String TPL_FM_SQLMAP_LOCATION = TPL_SQLMAP_FILE_NAME + TPL_FM_SUFFIX;
	public static final String TPL_FM_MAPPER_LOCATION = TPL_MAPPER_FILE_NAME + TPL_FM_SUFFIX;


}
