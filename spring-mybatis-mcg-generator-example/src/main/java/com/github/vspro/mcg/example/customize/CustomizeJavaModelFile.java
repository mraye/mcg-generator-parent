package com.github.vspro.mcg.example.customize;

import com.github.vspro.cg.codegen.JavaModelGeneratedFile;
import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;
import com.github.vspro.cg.template.context.TplContext;

public class CustomizeJavaModelFile extends JavaModelGeneratedFile {
    public CustomizeJavaModelFile(ContextHolder contextHolder) {
        super(contextHolder);
    }

    @Override
    public void loadData(IntrospectedTable introspectedTable) {
        super.loadData(introspectedTable);
        TplContext tplContext = getTplContext();
        tplContext.put("customize_key", "hello_customize");
    }
}
