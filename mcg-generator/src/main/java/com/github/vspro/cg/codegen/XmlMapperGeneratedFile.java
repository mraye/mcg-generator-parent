package com.github.vspro.cg.codegen;

import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.config.profile.SqlMapGeneratorConfiguration;
import com.github.vspro.cg.config.profile.TableConfiguration;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;
import com.github.vspro.cg.template.context.TplContext;

import static com.github.vspro.cg.util.StringUtil.stringHasValue;

public class XmlMapperGeneratedFile extends GeneratedFile {

    public XmlMapperGeneratedFile(ContextHolder contextHolder) {
        super(contextHolder);
    }

    @Override
    public String getTargetProject() {
        return this.contextHolder.getSqlMapGeneratorConfiguration().getTargetProject();
    }

    @Override
    public String getFileName() {
        return getMapperXmlName() + ".xml";
    }

    @Override
    public String getTplLocation() {
        return this.contextHolder.getTemplateGeneratorConfiguration().getSqlMapTplLocation();
    }

    @Override
    public String getPackage() {
        return getSqlMapGeneratorConfiguration().getTargetPackage();
    }

    @Override
    public boolean useClassPath() {
        return this.contextHolder.getSqlMapGeneratorConfiguration().isUserClassPath();
    }


    @Override
    public void loadData(IntrospectedTable introspectedTable) {

        tplContext = new TplContext();
        tplContext.put("namespace", this.contextHolder.getMapperFullType());
        tplContext.put("domainFullType", this.contextHolder.getDomainFullType());
        tplContext.put("domainShortName", getTableConfiguration().getDomainObjectName());

        tplContext.put("primaryColumns", introspectedTable.getPrimaryKeyColumns());
        tplContext.put("baseColumns", introspectedTable.getBaseColumns());
        tplContext.put("blobColumns", introspectedTable.getBlobColumns());
        tplContext.put("columns", introspectedTable.getColumns());
        tplContext.put("columnsNoPrimaryKey", introspectedTable.getColumnsNoPrimaryKey());

        tplContext.put("enableLogicalDel", enableLogicalDel());
        tplContext.put("logicalDelColName", getLogicalDelColName() == null ? "" : getLogicalDelColName());

//        如果指定了逻辑删除列
        if (enableLogicalDel() && stringHasValue(getLogicalDelColVal())) {
            String logicalDelColVal = getLogicalDelColVal();
            String[] val = logicalDelColVal.split(",");

            tplContext.put("logicalDelYesColVal", !stringHasValue(val[0]) ? "" : val[0]);
            tplContext.put("logicalDelYesColValIsNum", logicalDelColValIsNum(val[0]));
            tplContext.put("logicalDelNoColVal", !stringHasValue(val[1]) ? "" : val[1]);
            tplContext.put("logicalDelNoColValIsNum", logicalDelColValIsNum(val[1]));
        }
        tplContext.put("tableName", getTableName());

    }

    private SqlMapGeneratorConfiguration getSqlMapGeneratorConfiguration() {
        return this.contextHolder.getSqlMapGeneratorConfiguration();
    }


    private TableConfiguration getTableConfiguration() {
        return this.contextHolder.getTableConfiguration();
    }

    private String getMapperXmlName() {
        return getTableConfiguration().getMapperXmlName();
    }

    private String getTableName() {
        return getTableConfiguration().getTableName();
    }

    private boolean enableLogicalDel() {
        return getTableConfiguration().isEnableLogicalDel();
    }

    private String getLogicalDelColName() {
        return getTableConfiguration().getLogicalDelColName();
    }

    private boolean logicalDelColValIsNum(String val) {
        if (!stringHasValue(val)) {
            return false;
        }
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getLogicalDelColVal() {
        return getTableConfiguration().getLogicalDelColVal();
    }
}
