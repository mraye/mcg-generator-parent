package com.github.vspro.cg.codegen;

import com.github.vspro.cg.config.profile.DomainGeneratorConfiguration;
import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.internal.db.table.IntrospectedColumn;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;
import com.github.vspro.cg.template.context.TplContext;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.vspro.cg.util.StringUtil.stringHasValue;

public class JavaModelGeneratedFile extends GeneratedFile {

    public JavaModelGeneratedFile(ContextHolder contextHolder) {
        super(contextHolder);
    }

    @Override
    public String getTargetProject() {
        return this.contextHolder.getDomainGeneratorConfiguration().getTargetProject();
    }

    @Override
    public String getFileName() {
        return getClassName() + ".java";
    }

    @Override
    public String getTplLocation() {
        return this.contextHolder.getTemplateGeneratorConfiguration().getDomainTplLocation();
    }

    @Override
    public boolean useClassPath() {
        return this.contextHolder.getDomainGeneratorConfiguration().isUserClassPath();
    }

    @Override
    public void loadData(IntrospectedTable introspectedTable) {

        tplContext = new TplContext();
        List<IntrospectedColumn> columns = getRootClass() == null
                ? introspectedTable.getColumns()
                : introspectedTable.getColumns()
                        .stream().filter(introspectedColumn -> {
                    return !getRootClassProper().contains(introspectedColumn.getJavaProperty());
                }).collect(Collectors.toList());

        Set<String> useJSR310Types = new HashSet<>();
        for (IntrospectedColumn column : columns) {
            String javaPropertyName = column.getJavaPropertyName();
            if (!javaPropertyName.toLowerCase().startsWith("local")) {
                continue;
            }
            if ("LocalDateTime".equalsIgnoreCase(javaPropertyName)
                    || "LocalDate".equalsIgnoreCase(javaPropertyName)
                    || "LocalTime".equalsIgnoreCase(javaPropertyName)) {
                useJSR310Types.add(column.getFullyQualifiedJavaType().getFullyQualifiedName());
            }
        }

        tplContext.put("useJSR310Types", new ArrayList<>(useJSR310Types));
        tplContext.put("javaProperties", columns);
        tplContext.put("package", getPackage());
        tplContext.put("className", getClassName());
        tplContext.put("rootClass", getRootClass());
        tplContext.put("rootClassShortName", getRootClassShortName());

    }

    private String getRootClassShortName() {
        return stringHasValue(getRootClass())
                ?getRootClass().substring(getRootClass().lastIndexOf(".") + 1)
                : null;
    }

    private Set<String> getRootClassProper() {
        return getDomainGeneratorConfiguration().getRootClassProperty();
    }

    private String getRootClass() {
        return this.contextHolder.getDomainGeneratorConfiguration().getRootClass();
    }

    private DomainGeneratorConfiguration getDomainGeneratorConfiguration() {
        return this.contextHolder.getDomainGeneratorConfiguration();
    }

    @Override
    public String getPackage() {
        return getDomainGeneratorConfiguration().getTargetPackage();
    }

    private String getClassName() {
        return this.contextHolder.getTableConfiguration().getDomainObjectName();
    }

}
