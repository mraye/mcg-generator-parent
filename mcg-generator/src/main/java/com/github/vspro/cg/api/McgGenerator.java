package com.github.vspro.cg.api;

import com.github.vspro.cg.codegen.JavaMapperGeneratedFile;
import com.github.vspro.cg.codegen.JavaModelGeneratedFile;
import com.github.vspro.cg.codegen.XmlMapperGeneratedFile;
import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.util.Messages;
import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.exception.CodeAutoGenException;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;
import com.github.vspro.cg.template.DefaultTemplateClient;
import com.github.vspro.cg.template.TemplateClient;

import java.io.IOException;
import java.util.List;

import static com.github.vspro.cg.util.Messages.getString;

public class McgGenerator {

    private Configuration configuration;


    public McgGenerator(Configuration configuration) {
        this.configuration = configuration;
    }


    public final void generate() {

        try {
            //校验配置文件完整性
            configuration.validate();

            //数据库字段反射
            ContextHolder context = configuration.getContext();
            List<IntrospectedTable> introspectedTables = context.getIntrospectedTables();

            if (introspectedTables == null || introspectedTables.isEmpty()) {
                throw new CodeAutoGenException(Messages.getString("TypeCouldNotResolved.0"));
            }

            //数据库反射的实体，目前支持生成单表
            IntrospectedTable introspectedTable = introspectedTables.get(0);

            TemplateClient templateClient = new DefaultTemplateClient(context);
            //生成domain文件
            javaModelGenerate(introspectedTable, templateClient);

            //生成mapper文件
            javaMapperGenerate(introspectedTable, templateClient);

            //生成mapper文件
            xmlMapperGenerate(introspectedTable, templateClient);

            //如果需要生成其他文件，只需要继承重写这个方法
            otherFileGenerate(introspectedTable, templateClient);


            System.out.println("Mcg Generates Success...");


        } catch (Exception e) {
            throw new RuntimeException("代码生成失败", e);
        }
    }

    protected void otherFileGenerate(IntrospectedTable introspectedTable, TemplateClient templateClient) {
        //do nothing ...

    }

    protected void xmlMapperGenerate(IntrospectedTable introspectedTable, TemplateClient templateClient) throws IOException {

        //将字段写入模板引擎
        XmlMapperGeneratedFile xmlMapper =
                templateClient.render(XmlMapperGeneratedFile.class, introspectedTable);

        //生成文件
        if (xmlMapper != null) {
            xmlMapper.generate();
        }
    }

    protected void javaModelGenerate(IntrospectedTable introspectedTable, TemplateClient templateClient) throws IOException {

        //将字段写入模板引擎
        JavaModelGeneratedFile javaModel =
                templateClient.render(JavaModelGeneratedFile.class, introspectedTable);

        //生成文件
        if (javaModel != null) {
            javaModel.generate();
        }
    }

    protected void javaMapperGenerate(IntrospectedTable introspectedTable,
                                      TemplateClient templateClient) throws IOException {

        //将字段写入模板引擎
        JavaMapperGeneratedFile javaMapper =
                templateClient.render(JavaMapperGeneratedFile.class, introspectedTable);

        //生成文件
        if (javaMapper != null) {
            javaMapper.generate();
        }
    }

}
