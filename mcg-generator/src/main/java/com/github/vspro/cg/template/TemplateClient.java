package com.github.vspro.cg.template;

import com.github.vspro.cg.codegen.GeneratedFile;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;

public interface TemplateClient {

	<T extends GeneratedFile>T render(Class<T> clz, IntrospectedTable introspectedTable);

}
