
## mcg-generator 

***mybatis代码生成***

> 基于mybatis-generator造的轮子，可以生成java代码。如domain,dao,sql-map文件

features:
+ 通过加载模板文件渲染，可以使用自定义模板生成java代码
+ 支持velocity、freemarker模板引擎
+ 提供类似mybatis-generator-plugin插件运行
+ 方便修改模板，生成自己的模板代码

#### 工程结构

```bash
mcg-generator-parent                      父工程
---- mcg-generator                        主工程
---- mcg-generator-maven-plugin           mcg-generator插件
---- spring-mybatis-mcg-generator-example 生成代码示例
```


### 使用mcg-generator-maven-plugin插件生成代码

1. 将配置文件`mcg-config.xml`放置在`src/main/resources`下

**注意，文件名称一定要是`mcg-config.xml``**

#### 引入插件

```xml
<plugins>
    <plugin>
        <groupId>com.github.vspro</groupId>
        <artifactId>mcg-generator-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <dependencies>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>8.0.13</version>
            </dependency>
        </dependencies>
    </plugin>
</plugins>
```

在maven工程中的plugins中可以看到`mcg-generator`,**点击运行`mcg-generator:generate`**


### 使用自定义模板

如果要使用自定义模板

#### 引入依赖

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.13</version>
</dependency>
<!--        如果需要自定义模板文件，需要加入mcg-generator-->
<dependency>
    <groupId>com.github.vspro</groupId>
    <artifactId>mcg-generator</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### 加入自定义模板

放在maven工程类路径`src/main/resources`下，这里是`src/main/resources/tpl/`:

```bash
src/main/resources
    ---tpl
        --- domain.vm
        --- mapper.vm
        --- sqlmap.vm
```

#### 修改配置文件

这里是`customize-mcg-config.xml`:  

```xml

<templateGenerator type="vt" useClassPath="true"
                   rootDir="/tpl/"
                   domainTplLocation="domain.vm"
                   sqlMapTplLocation="sqlmap.vm"
                   mapperTplLocation="mapper.vm"/>
```

#### 自定义domain(dao,sqlmap)模板

如果要自定义domain,需要继承`JavaModelGeneratedFile`:  

```java

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
```


+ 如果重写dao,需要继承`JavaMapperGeneratedFile`
+ 如果重写sql-map文件，需要继承`XmlMapperGeneratedFile`

#### 重写McgGenerator

```java

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
```


#### 生成模板

在工程下新建一个类，这里是`App.java`:  

```java

public class App {


    public static void main(String[] args) throws IOException, XMLParseException {
        ConfigurationParser parser = new ConfigurationParser();
        //当前类的加载器或者线程加载器
        File file = new File(App.class.getClassLoader().getResource("customize-mcg-config.xml").getPath());
        Configuration configuration = parser.parseConfiguration(file);
        McgGenerator generator = new CustomizeMcgGenerator(configuration);
        generator.generate();
    }
}
```


示例在`spring-mybatis-mcg-generator-example`中，包括标准的`mcg-config.xml`也包涵在这里
