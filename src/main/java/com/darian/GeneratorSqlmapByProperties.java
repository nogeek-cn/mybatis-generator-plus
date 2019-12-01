package com.darian;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.FileInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 读取 peroperties ，配置信息
 * 读取 StaticSource 拼接成 generatorConfig.xml
 * 配置信息放进去，
 * 然后重载，生成
 *
 * <br>Darian
 **/
public class GeneratorSqlmapByProperties {
    public void generator(String tableString) throws Exception {

        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        ConfigurationParser configurationParser = new ConfigurationParser(warnings);

        String configString = StaticSource.START_STRING + tableString + StaticSource.END_STRING;

        Configuration config = configurationParser.parseConfiguration(new StringReader(configString));
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);

    }

    public static void main(String[] args) throws Exception {
        try {
            Properties pro = new Properties();
            String path = System.getProperty("user.dir") + "\\src\\main\\resources\\application.properties";
            FileInputStream in = new FileInputStream(path);
            pro.load(in);
            String tableList = pro.getProperty("tableList");

            List<String> tableNameList = Stream.of(tableList.split(",")).collect(Collectors.toList());

            GeneratorSqlmapByProperties generatorSqlmap = new GeneratorSqlmapByProperties();
            generatorSqlmap.generator(generatorTableName(tableNameList));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***
     * CharSequence delimiter,
     *    CharSequence prefix,
     *    CharSequence suffix
     * @param tableNameList
     * @return
     */
    private static String generatorTableName(List<String> tableNameList) {
        return tableNameList.stream().collect(Collectors.joining("\"></table>  <table schema=\"\" tableName=\"",
                " <table schema=\"\" tableName=\"",
                "\"></table> "));
    }

}




class StaticSource {


    public final static String START_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE generatorConfiguration\n" +
            "  PUBLIC \"-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN\"\n" +
            "  \"http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd\">\n" +
            "\n" +
            "<generatorConfiguration>\n" +
            "    <properties  resource=\"application.properties\"/>\n" +
            "\n" +
            "    <context id=\"testTables\" targetRuntime=\"MyBatis3\">\n" +
            "\n" +
            "        <commentGenerator>\n" +
            "            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->\n" +
            "            <property name=\"suppressAllComments\" value=\"true\" />\n" +
            "        </commentGenerator>\n" +
            "        <!--数据库连接的信息：驱动类、连接地址、用户名、密码 -->\n" +
            "        <jdbcConnection driverClass=\"${driverClass}\"\n" +
            "                        connectionURL=\"${connectionURL}\"\n" +
            "                        userId=\"${userId}\"\n" +
            "                        password=\"${password}\">\n" +
            "        </jdbcConnection>\n" +
            "\n" +
            "        <!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL 和 \n" +
            "            NUMERIC 类型解析为java.math.BigDecimal -->\n" +
            "        <javaTypeResolver>\n" +
            "            <property name=\"forceBigDecimals\" value=\"false\" />\n" +
            "        </javaTypeResolver>\n" +
            "\n" +
            "        <!-- targetProject:生成PO类的位置 -->\n" +
            "        <javaModelGenerator targetPackage=\"${entitypackage}\"\n" +
            "            targetProject=\"${user.dir}\\src\\main\\java\">\n" +
            "            <!-- enableSubPackages:是否让schema作为包的后缀 -->\n" +
            "            <property name=\"enableSubPackages\" value=\"false\" />\n" +
            "            <!-- 从数据库返回的值被清理前后的空格 -->\n" +
            "            <property name=\"trimStrings\" value=\"true\" />\n" +
            "        </javaModelGenerator>\n" +
            "\n" +
            "\n" +
            "        <!-- targetProject:mapper映射文件生成的位置 -->\n" +
            "        <sqlMapGenerator targetPackage=\"${mapperxmlpackage}\"\n" +
            "                         targetProject=\"${user.dir}\\src\\main\\java\">\n" +
            "            <!-- enableSubPackages:是否让schema作为包的后缀 -->\n" +
            "            <property name=\"enableSubPackages\" value=\"false\" />\n" +
            "        </sqlMapGenerator>\n" +
            "\n" +
            "\n" +
            "        <!-- targetPackage：mapper接口生成的位置 -->\n" +
            "        <javaClientGenerator type=\"XMLMAPPER\" targetPackage=\"${mapperpackage}\"\n" +
            "                             targetProject=\"${user.dir}\\src\\main\\java\">\n" +
            "            <!-- enableSubPackages:是否让schema作为包的后缀 -->\n" +
            "            <property name=\"enableSubPackages\" value=\"false\" />\n" +
            "        </javaClientGenerator>\n";


    public final static String END_STRING = "</context>\n" +
            "\n" +
            "</generatorConfiguration>";

}
