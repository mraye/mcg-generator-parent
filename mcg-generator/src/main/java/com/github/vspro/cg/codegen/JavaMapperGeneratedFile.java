package com.github.vspro.cg.codegen;

import com.github.vspro.cg.config.profile.MapperGeneratorConfiguration;
import com.github.vspro.cg.config.profile.TableConfiguration;
import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;
import com.github.vspro.cg.template.context.TplContext;
import com.github.vspro.cg.util.JavaBeanUtil;

public class JavaMapperGeneratedFile extends GeneratedFile {

	public JavaMapperGeneratedFile(ContextHolder contextHolder) {
		super(contextHolder);
	}

	@Override
	public String getTargetProject() {
		return this.contextHolder.getMapperGeneratorConfiguration().getTargetProject();
	}

	@Override
	public String getFileName() {
		return getMapperInterfaceName() + ".java";
	}

	@Override
	public String getTplLocation() {
		return this.contextHolder.getTemplateGeneratorConfiguration().getMapperTplLocation();
	}

	@Override
	public boolean useClassPath() {
		return this.contextHolder.getMapperGeneratorConfiguration().isUserClassPath();
	}

	@Override
	public void loadData(IntrospectedTable introspectedTable) {

		tplContext = new TplContext();

		//com.example
		tplContext.put("package", getPackage());

		//ExampleDao
		tplContext.put("interfaceName", getMapperInterfaceName());

		tplContext.put("domainFullType", this.contextHolder.getDomainFullType());
		//ExampleDo
		tplContext.put("domainType", getTableConfiguration().getDomainObjectName());
		//exampleDo
		tplContext.put("domainShortName", JavaBeanUtil.getCamelCaseString(getTableConfiguration().getDomainObjectName(),false));

		tplContext.put("primaryColumns", introspectedTable.getPrimaryKeyColumns());


		tplContext.put("enableLogicalDel", enableLogicalDel());
		tplContext.put("logicalDelColName", getLogicalDelColName() == null?"":getLogicalDelColName());

	}

	private MapperGeneratorConfiguration getMapperGeneratorConfiguration() {
		return this.contextHolder.getMapperGeneratorConfiguration();
	}

	@Override
	public String getPackage() {
		return getMapperGeneratorConfiguration().getTargetPackage();
	}

	private String getMapperInterfaceName() {
		return this.contextHolder.getTableConfiguration().getMapperInterfaceName();
	}

	private TableConfiguration getTableConfiguration() {
		return this.contextHolder.getTableConfiguration();
	}

	private boolean enableLogicalDel() {
		return getTableConfiguration().isEnableLogicalDel();
	}

	private String getLogicalDelColName() {
		return getTableConfiguration().getLogicalDelColName();
	}


}
