package com.github.vspro.cg.template.egcimpl;

import com.github.vspro.cg.codegen.GeneratedFile;
import com.github.vspro.cg.config.constant.CfgNodeConstants;
import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.exception.TplRenderException;
import com.github.vspro.cg.template.EngineClient;
import com.github.vspro.cg.template.context.TplContext;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

import static com.github.vspro.cg.util.Messages.getString;

public class FMEngineClient extends AbstractEngineClient {

    Configuration configuration;

    public FMEngineClient(ContextHolder contextHolder) {
        super(contextHolder);
    }

    @Override
    public void init() {
        configuration = new Configuration(Configuration.VERSION_2_3_22);
        String rootDir = contextHolder.getTemplateGeneratorConfiguration().getRootDir();
        if (contextHolder.getTemplateGeneratorConfiguration().isUserClassPath()) {
            configuration.setTemplateLoader(new ClassTemplateLoader(Thread.currentThread().getContextClassLoader(), rootDir));
        } else {
            try {
                configuration.setDirectoryForTemplateLoading(new File(rootDir));
            } catch (IOException e) {
                e.printStackTrace();
                throw new TplRenderException(getString("TplRenderError.0"));
            }
        }
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }


    @Override
    public <T extends GeneratedFile> String render(T gf) {
        try {
            Template template = configuration.getTemplate(gf.getTplLocation());
            VelocityContext ctx = new VelocityContext();

            TplContext tplContext = gf.getTplContext();
            if (tplContext != null && tplContext.size() > 0) {
                Set<Map.Entry<String, Object>> entries = tplContext.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    ctx.put(entry.getKey(), entry.getValue());
                }
                StringWriter sw = new StringWriter();
                template.process(ctx, sw);
                return sw.toString();
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new TplRenderException(getString("TplRenderError.0"));
        }
        return null;
    }
}
