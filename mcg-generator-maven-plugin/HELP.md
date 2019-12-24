

## mcg-generator-maven-plugin使用

可以通过maven插件的形式生成代码


### 引入

在你自己maven工程的pom文件中加入:  

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


在加入这个插件后，在idea中的右侧中的maven中plugins，可以找到`mcg-generator`->`mcg-generator:generate`

### 配置文件


在你的maven工程目录`src/main/resources`加入`mcg-config.xml`配置文件，注意，一定是在`src/main/resources`根目录下
加入名称为`mcg-config.xml`的配置文件:  

```xml

<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE configuration PUBLIC "-//vspro.github.com//DTD Mcg Generator 1.0//EN"
        "http://vspro.github.com/dtd/mcg-config_1_0.dtd">

<configuration>

    <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                    connectionUrl="jdbc:mysql://localhost:3306/test?serverTimezone=UTC&amp;characterEncoding=utf8&amp;allowMultiQueries=true"
                    userName="root" password="****"/>

<!--    默认是相对路径(类路径)-->
    <domainGenerator
            targetPackage="com.github.vspro.persistent.domain"
            targetProject="src/main/java"/>

    <sqlmapGenerator
            targetPackage="mappings"
            targetProject="src/main/resources"/>

    <mapperGenerator
            targetPackage="com.github.vspro.persistent.dao"
            targetProject="src/main/java"/>


    <!--table标签要放在最后！！-->
    <table tableName="t_sys_user"
           domainObjectName="SysUserDo"
           mapperInterfaceName="SysUserDao"
           mapperXmlName="sys-user"/>

</configuration>

```

#### 运行

直接在idea右侧中双击`mcg-generator:generate`即可




### 加载自定义模板文件

***注意，使用插件形式生成代码，不支持从类路径加载自定义文件，不管使用freemarker不是velocity模板引擎***


**只支持从文件系统加载自定义模板文件**


```xml

<!--     freemarker加载文件系统中的自定义模板文件-->
    <templateGenerator type="fm" useClassPath="false"
                       rootDir="E:\code-gen\"
        domainTplLocation="domain.ftl"
        sqlMapTplLocation="sqlmap.ftl"
        mapperTplLocation="mapper.ftl"/>

<!--        velocity加载文件系统中的自定义模板文件-->
<!--        <templateGenerator type="vt" useClassPath="false"-->
<!--               rootDir="E:\code-gen\"-->
<!--               domainTplLocation="domain.vm"-->
<!--               sqlMapTplLocation="sqlmap.vm"-->
<!--               mapperTplLocation="mapper.vm"/>-->

```







