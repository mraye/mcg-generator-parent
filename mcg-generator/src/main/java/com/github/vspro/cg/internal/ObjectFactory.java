package com.github.vspro.cg.internal;

import com.github.vspro.cg.config.constant.CfgNodeConstants;
import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.internal.db.table.IntrospectedTable;
import com.github.vspro.cg.template.egcimpl.FMEngineClient;
import com.github.vspro.cg.util.Messages;
import com.github.vspro.cg.internal.db.table.ActualTableName;
import com.github.vspro.cg.internal.db.table.IntrospectedColumn;
import com.github.vspro.cg.internal.types.DefaultJavaTypeResolver;
import com.github.vspro.cg.internal.types.JavaTypeResolver;
import com.github.vspro.cg.template.EngineClient;
import com.github.vspro.cg.template.egcimpl.VTEngineClient;

import static com.github.vspro.cg.util.Messages.getString;

/**
 * 反射创建对象
 */
public class ObjectFactory {


    public static Class<?> extClassForName(String clz) throws ClassNotFoundException {

        //TODO 指定classLoader
        return internalClassForName(clz);

    }

    private static Class<?> internalClassForName(String clz) throws ClassNotFoundException {
        Class<?> clzz = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            clzz = Class.forName(clz, true, classLoader);
        } catch (Exception e) {
            e.printStackTrace();
            //do nothing...
        }

        //如果线程上下文加载器没有加载到类，就使用当前类的加载器
        if (clzz == null) {
            clzz = Class.forName(clz, true, ObjectFactory.class.getClassLoader());
        }

        return clzz;
    }


    public static JavaTypeResolver createJavaTypeResolver(ContextHolder contextHolder) {
        String clz = DefaultJavaTypeResolver.class.getName();
        JavaTypeResolver resolver = (JavaTypeResolver) createInternalObject(clz);
        resolver.setContext(contextHolder);
        return resolver;
    }

    public static Object createInternalObject(String type) {
        Object answer;

        try {
            Class<?> clazz = internalClassForName(type);

            answer = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(Messages.getString("ObjectReflectiveError.0"), e); //$NON-NLS-1$
        }

        return answer;
    }

    public static IntrospectedColumn createIntrospectedColumn(ContextHolder contextHolder) {
        String clz = IntrospectedColumn.class.getName();
        IntrospectedColumn column = (IntrospectedColumn) createInternalObject(clz);
        return column;
    }

    public static IntrospectedTable createIntrospectedTable(ActualTableName atn, ContextHolder contextHolder) {
        String clz = IntrospectedTable.class.getName();
        IntrospectedTable table = (IntrospectedTable) createInternalObject(clz);
        table.setContextHolder(contextHolder);
        table.setTableName(atn);
        return table;
    }

    public static EngineClient createEngineClient(ContextHolder contextHolder) {
        String type = contextHolder.getTemplateGeneratorConfiguration().getType();
        String clz = null;
        if (type.equalsIgnoreCase(CfgNodeConstants.TPL_ENGINE_TYPE_VT)) {
            clz = VTEngineClient.class.getName();
        } else if (type.equalsIgnoreCase(CfgNodeConstants.TPL_ENGINE_TYPE_FM)) {
            clz = FMEngineClient.class.getName();
        }
        EngineClient engineClient = (EngineClient) createInternalObject(clz);
        return engineClient;
    }
}
