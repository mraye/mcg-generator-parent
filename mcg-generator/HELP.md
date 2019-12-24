
## mcg-generator使用方法

### 引入依赖

```xml
<dependency>
    <groupId>com.github.vspro</groupId>
    <artifactId>mcg-generator</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```


### 极简配置

在你的maven工程目录`src/main/resources`加入`mcg-config.xml`配置文件:  

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




### 生成代码

在你的工程下任意一个类中加入`main`方法，如下:  

```java
public class App {

    public static void main(String[] args) throws IOException, XMLParseException {

        String path = App.class.getClassLoader().getResource("mcg-config.xml").getPath();
        ConfigurationParser parser = new ConfigurationParser();
		Configuration configuration = parser.parseConfiguration(new File(path));
		McgGenerator generator = new McgGenerator(configuration);
		generator.generate();

    }
}

```
看到`Mcg Generates Success...`,说明代码生成成功了

### 配置文件`mcg-config.xml`说明

#### jdbcConnection Node

这个节点是配置连接数据库信息：  

```xml
    <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                    connectionUrl="jdbc:mysql://localhost:3306/test?serverTimezone=UTC&amp;characterEncoding=utf8&amp;allowMultiQueries=true"
                    userName="root" password="****"/>
```


#### domainGenerator Node

这个节点用来配置生成Do类的路径，如`SysUserDo.java`

```xml
    <domainGenerator useClassPath="true"
            targetPackage="com.github.vspro.persistent.domain"
            targetProject="src/main/java"/>
```

其中`userClassPath`默认是`true`,可以不写，代表使用类路径；如果是`false`,代表使用文件路径:  

+ 当`userClassPath`=`true`: 代表要在该工程中生成，`targetProject`为`src/main/java/`,那么生成的Do类应在该工程的`src/main/java/com/github/vspro/persistent/domain/xxxDo.java`
+ 当`userClassPath`=`false`: 代表要使用文件路径(绝对路径)，如`targetProject`为`E:/code-gen`,那么生成的Do类就在`E:/code-gen/com/github/vspro/persistent/domain/xxxDo.java`



#### sqlmapGenerator Node

这个节点用来配置生成`sqlmap.xml`的路径，如`sys-user.xml`

```xml
    <sqlmapGenerator userClassPath="true"
            targetPackage="mappings"
            targetProject="src/main/resources"/>
```

其中`userClassPath`默认是`true`,可以不写，代表使用类路径；如果是`false`,代表使用文件路径:  

+ 当`userClassPath`=`true`: 代表要在该工程中生成，`targetProject`为`src/main/resources/`,那么生成的Do类应在该工程的`src/main/resources/mappings/sys-user.xml`
+ 当`userClassPath`=`false`: 代表要使用文件路径(绝对路径)，如`targetProject`为`E:/code-gen`,那么生成的Do类就在`E:/code-gen/mappings/sys-user.xml`


#### mapperGenerator Node

这个节点用来配置生成Dao的路径，如`SysUserDao.java`

```xml
     <mapperGenerator userClassPath="true"
            targetPackage="com.github.vspro.persistent.dao"
            targetProject="src/main/java"/>

```

其中`userClassPath`默认是`true`,可以不写，代表使用类路径；如果是`false`,代表使用文件路径:  

+ 当`userClassPath`=`true`: 代表要在该工程中生成，`targetProject`为`src/main/java/`,那么生成的Do类应在该工程的`src/main/java/com/github/vspro/persistent/dao/xxxDao.java`
+ 当`userClassPath`=`false`: 代表要使用文件路径(绝对路径)，如`targetProject`为`E:/code-gen`,那么生成的Do类就在`E:/code-gen/com/github/vspro/persistent/domain/xxxDao.java`



#### table Node

这个节点用来配置表生成对应的Do,Dao和`sqlmap`名称

```xml

    <table tableName="t_sys_user"
           domainObjectName="SysUserDo"
           mapperInterfaceName="SysUserDao"
           mapperXmlName="sys-user"
           enableLogicalDel="false"
           logicalDelColName="has_removed"/>
```

其中：
+ `enableLogicalDel`: 是否使用逻辑删除字段，true:代表使用逻辑删除字段，反之，亦然
+ `logicalDelColName`: 逻辑删除字段名称。仅在`enableLogicalDel`=`true`的情况有效

#### templateGenerator Node

这个节点用来配置模板使用的渲染引擎和加载自定义的生成Do,Dao和`sqlmap`的模板文件。

如果配置文件没有写这个节点，**默认使用`velocity`渲染引擎，渲染的模板在本工程的`src/resources/com/github/vspro/cg/config/tpl/vt/`目录下**,

支持使用`freemarker`模板文件或者`velocity`模板文件

字段说明：
+ type: 模板引擎类型
    - fm: 使用`freemarker`模板文件，以`.ftl`结尾文件
    - vt: 使用`velocity`模板文件，以`.vm`结尾文件
+ useClassPath: 在类路径加载模板文件还是在文件系统中加载模板文件
    - true: 默认值，代表在类路径加载模板文件
    - false: 代表使用文件系统加载模板文件
+ rootDir: 模板放置的根路径。当`userClassPath=false`时必填，使用绝对路径,如`E:/code-gen/`,Do,Dao和`sqlmap`模板文件都放在这个路径
+ domainTplLocation: 生成Do类的模板文件
+ sqlMapTplLocation: 生成`sqlmap`的模板文件
+ mapperTplLocation: 生成Dao类的模板文件

##### 加载默认模板文件

```xml

<!--    使用freemarker模板引擎-->
   <templateGenerator type="fm"/>

<!--    使用velocity模板引擎-->
   <templateGenerator type="vt"/>
```

##### 加载自定义模板文件

1.从文件中加载自定义模板文件
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

2.从类路径中加载自定义模板文件

当`userClasspath=true`时，则将模板文件放置`src/main/resources/tpl`下，其中`tpl`目录任意，则配置文件：  

```xml

<!--     freemarker加载类路径中的自定义模板文件-->
    <templateGenerator type="fm" useClassPath="true"
                       rootDir="/tpl/"
        domainTplLocation="domain.ftl"
        sqlMapTplLocation="sqlmap.ftl"
        mapperTplLocation="mapper.ftl"/>

<!--    velocity加载类路径中的自定义模板文件-->
<!--    <templateGenerator type="vt" useClassPath="true"-->
<!--          rootDir="/tpl/"-->
<!--          domainTplLocation="domain.vm"-->
<!--          sqlMapTplLocation="sqlmap.vm"-->
<!--          mapperTplLocation="mapper.vm"/>-->
```



