package com.github.vspro.cg.config.profile;

import com.github.vspro.cg.config.PropertyHolder;
import com.github.vspro.cg.config.Validator;
import com.github.vspro.cg.exception.CodeAutoGenException;
import com.github.vspro.cg.exception.InvalidException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.vspro.cg.util.Messages.getString;
import static com.github.vspro.cg.util.StringUtil.stringHasValue;


public class MapperGeneratorConfiguration extends PropertyHolder implements Validator {

    //包名
    private String targetPackage;
    //项目名称
    private String targetProject;
    //使用相对路径
    private boolean userClassPath;
    //父接口
    private String rootInterface;

    private Set<String> rootInterfaceMethods;

    private Set<String> defaultInterfaceMethods = new HashSet() {{
        add("insert");
        add("insertOrUpdate");
        add("selectByPrimaryKey");
        add("selectOneSelective");
        add("updateByPrimaryKeySelective");
        add("updateByPrimaryKey");
        add("deleteByPrimaryKey");
        add("deleteLogicalByPrimaryKey");
        add("batchInsert");
    }};

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

    public String getRootInterface() {
        return rootInterface;
    }

    public void setRootInterface(String rootInterface) {
        this.rootInterface = rootInterface;
    }

    public Set<String> mergeInterfaceMethod() {
        if (!stringHasValue(rootInterface)) {
            return defaultInterfaceMethods;
        } else {
            try {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                Class<?> clz = Class.forName(rootInterface, true, contextClassLoader);
                Method[] declaredMethods = clz.getDeclaredMethods();
                if (declaredMethods != null && declaredMethods.length > 0) {
                    rootInterfaceMethods = new HashSet<>();
                    for (int i = 0; i < declaredMethods.length; i++) {
                        Method method = declaredMethods[i];
                        String name = method.getName();
                        rootInterfaceMethods.add(name);
                    }
                }

                if (rootInterfaceMethods == null || rootInterfaceMethods.isEmpty()) {
                    return defaultInterfaceMethods;
                }
                return defaultInterfaceMethods.stream()
                        .filter(md -> {
                            return !rootInterfaceMethods.contains(md);
                        }).collect(Collectors.toSet());

            } catch (ClassNotFoundException e) {
                throw new CodeAutoGenException(getString("ValidationError.4"));
            }
        }

    }

    /**
     * 是否需要重写sqlmap模板
     * @return
     */
    public boolean needOverrideSqlMap(){
        Set<String> collect = rootInterfaceMethods.stream()
                .filter(md -> {
                    return !defaultInterfaceMethods.contains(md);
                }).collect(Collectors.toSet());

        if (collect != null && !collect.isEmpty()){
            return true;
        }else {
            return false;
        }
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
