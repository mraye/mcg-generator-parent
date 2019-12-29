package com.github.vspro.cg.codegen;

import com.github.vspro.cg.config.profile.MapperGeneratorConfiguration;
import com.github.vspro.cg.config.profile.TableConfiguration;
import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;
import com.github.vspro.cg.template.context.TplContext;
import com.github.vspro.cg.util.JavaBeanUtil;

import java.util.Set;

import static com.github.vspro.cg.util.StringUtil.stringHasValue;

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

		//可能会有多个主键
		tplContext.put("primaryColumns", introspectedTable.getPrimaryKeyColumns());



		//启动逻辑删除
		tplContext.put("enableLogicalDel", enableLogicalDel());
		tplContext.put("logicalDelColName", getLogicalDelColName() == null?"":getLogicalDelColName());

		//设置父接口
        //指定了父接口，就必须指定自定义的sqlmap模板！！
		tplContext.put("rootInterface", getRootInterface());
		tplContext.put("rootInterfaceShortName", getRootInterfaceShortName());
		//如果指定了父接口，需要合并默认接口的方法，
		//如果父接口除了默认方法还有其他方法，还需要重写sqlmap模板！！
		Set<String> interfaceMethods = mergeInterfaceMethod();
		if (!enableLogicalDel()){
			interfaceMethods.remove("deleteLogicalByPrimaryKey");
		}

		tplContext.put("interfaceMethods", interfaceMethods);
	}

	private String getRootInterface() {
		return getMapperGeneratorConfiguration().getRootInterface();
	}


	private String getRootInterfaceShortName() {
		return stringHasValue(getRootInterface()) ?getRootInterface().substring(getRootInterface().lastIndexOf(".") + 1)
				:null;
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

	public Set<String> mergeInterfaceMethod() {
		return getMapperGeneratorConfiguration().mergeInterfaceMethod();
	}
}
