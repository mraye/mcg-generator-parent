package com.github.vspro.mcg.example.customize;

import com.github.vspro.cg.api.McgGenerator;
import com.github.vspro.cg.codegen.JavaModelGeneratedFile;
import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;
import com.github.vspro.cg.template.TemplateClient;

import java.io.IOException;

public class CustomizeMcgGenerator extends McgGenerator {

    public CustomizeMcgGenerator(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void javaModelGenerate(IntrospectedTable introspectedTable, TemplateClient templateClient) throws IOException {

        //将字段写入模板引擎
        CustomizeJavaModelFile javaModel =
                templateClient.render(CustomizeJavaModelFile.class, introspectedTable);

        //生成文件
        if (javaModel != null) {
            javaModel.generate();
        }
    }

}
