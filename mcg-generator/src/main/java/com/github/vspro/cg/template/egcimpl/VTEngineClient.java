package com.github.vspro.cg.template.egcimpl;

import com.github.vspro.cg.codegen.GeneratedFile;
import com.github.vspro.cg.template.EngineClient;
import com.github.vspro.cg.template.context.TplContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

public class VTEngineClient implements EngineClient {

	private VelocityEngine ve;
	
	@Override
	public void init() {
		ve = new VelocityEngine();
		ve.setProperty(org.apache.velocity.runtime.RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
	}


	@Override
	public <T extends GeneratedFile> String render(T gf) {

		Template template = ve.getTemplate(gf.getTplLocation());
		VelocityContext ctx = new VelocityContext();

		TplContext tplContext = gf.getTplContext();
		if (tplContext != null && tplContext.size() > 0) {
			Set<Map.Entry<String, Object>> entries = tplContext.entrySet();
			for (Map.Entry<String, Object> entry: entries){
				ctx.put(entry.getKey(), entry.getValue());
			}
			StringWriter sw = new StringWriter();
			template.merge(ctx, sw);
			return sw.toString();
		}
		return null;
	}
}
