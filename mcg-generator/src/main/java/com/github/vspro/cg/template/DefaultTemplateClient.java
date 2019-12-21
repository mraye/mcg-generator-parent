package com.github.vspro.cg.template;

import com.github.vspro.cg.codegen.GeneratedFile;
import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.internal.ObjectFactory;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;

public  class DefaultTemplateClient implements TemplateClient{

	private ContextHolder contextHolder;

	private EngineClient engineClient;

	public DefaultTemplateClient(ContextHolder contextHolder) {
		this.contextHolder = contextHolder;
		initEngineClient();
	}

	protected void initEngineClient() {
		engineClient = ObjectFactory.createEngineClient(contextHolder);
		engineClient.init();
	}


	@Override
	public <T extends GeneratedFile> T render(Class<T> clz, IntrospectedTable introspectedTable) {
		try {
			T t = clz.getConstructor(ContextHolder.class).newInstance(contextHolder);
			t.loadData(introspectedTable);
			t.setContent(engineClient.render(t));
			return t;
		} catch (Exception e) {

		}
		return null;
	}
}
