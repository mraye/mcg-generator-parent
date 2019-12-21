package com.github.vspro.cg.codegen;

import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;
import com.github.vspro.cg.template.context.TplContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.github.vspro.cg.util.StringUtil.stringHasValue;

public abstract class GeneratedFile {

    protected ContextHolder contextHolder;
    protected TplContext tplContext;
    private String content;

    public GeneratedFile(ContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }


    public final void generate() throws IOException {
        doGenerate();
    }

    protected void doGenerate() throws IOException {
        String fileName = getFileName();
        String packageName = getPackage();
        String targetProject = getTargetProject();
        if (targetProject.startsWith("./")) {
            targetProject = targetProject.substring(1);
        }
        if (!targetProject.startsWith("/")) {
            targetProject = "/" + targetProject;
        }
        if (stringHasValue(contextHolder.getBaseTargetProject())) {
            //deal with plugin
            String baseTargetProject = contextHolder.getBaseTargetProject();
            targetProject = baseTargetProject + targetProject;

        } else if (useClassPath()) {
            String cp = Thread.currentThread().getContextClassLoader()
                    .getResource("").getPath();
            int index = cp.lastIndexOf("/target/classes");
            cp = cp.substring(0, index);
            targetProject = cp + targetProject;
        }

        packageName = packageName.replaceAll("\\.", "/");
        if (!targetProject.endsWith("/")) {
            targetProject = targetProject + "/";
        }
        String path = (targetProject + packageName).replaceAll("\\\\", "/");

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(path + "/" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.append(content);
        fileWriter.flush();
        fileWriter.close();
    }


    public abstract String getTargetProject();

    public abstract String getPackage();

    public abstract boolean useClassPath();

    public abstract String getTplLocation();

    public abstract String getFileName();

    /**
     * introspectedTable中有数据库中反射出来的字段
     * 向模板传递数据
     *
     * @param introspectedTable
     */
    public abstract void loadData(IntrospectedTable introspectedTable);


    public TplContext getTplContext() {
        return this.tplContext;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
