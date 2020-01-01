package com.github.vspro.cg.config.profile;

import com.github.vspro.cg.config.PropertyHolder;
import com.github.vspro.cg.config.Validator;
import com.github.vspro.cg.exception.CodeAutoGenException;
import com.github.vspro.cg.exception.InvalidException;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static com.github.vspro.cg.util.Messages.getString;
import static com.github.vspro.cg.util.StringUtil.stringHasValue;


public class DomainGeneratorConfiguration extends PropertyHolder implements Validator {


    //包名
    private String targetPackage;
    //项目名称
    private String targetProject;

    //使用类路径
    private boolean userClassPath;
    //指定基类
    private String rootClass;

    private Set<String> rootClassProperties;

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getTargetProject() {
        return targetProject;
    }

    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }

    public boolean isUserClassPath() {
        return userClassPath;
    }

    public void setUserClassPath(boolean userClassPath) {
        this.userClassPath = userClassPath;
    }

    public String getRootClass() {
        return rootClass;
    }

    public void setRootClass(String rootClass) {
        this.rootClass = rootClass;
    }

    public Set<String> getRootClassProperty() {
        if (!stringHasValue(rootClass)) {
            return null;
        }

        if (rootClassProperties == null) {
            try {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

                Class<?> clz = Class.forName(rootClass, true, contextClassLoader);
                while (clz != null && clz != Object.class) {
                    Field[] declaredFields = clz.getDeclaredFields();
                    if (declaredFields != null && declaredFields.length > 0) {
                        if (rootClassProperties == null) {
                            rootClassProperties = new HashSet<>();
                        }
                        for (int i = 0; i < declaredFields.length; i++) {
                            Field field = declaredFields[i];
                            String name = field.getName();
                            rootClassProperties.add(name);
                        }
                    }

                    clz = clz.getSuperclass();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new CodeAutoGenException(getString("ValidationError.4"));
            }
        }
        return rootClassProperties;
    }

    @Override
    public void validate() throws InvalidException {

        if (!stringHasValue(targetPackage)) {
            throw new InvalidException(getString("ValidationError.4"));
        }

        if (!stringHasValue(targetProject)) {
            throw new InvalidException(getString("ValidationError.4"));
        }
    }


}
