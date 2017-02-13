package com.talebase.cloud.common.db.reverser;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.*;

/**
 * 指定数据库以及表,生成相应的scala对象，thrift文件" +
 * @author Eric on 2016-06-30
 */
public class DbReverserHelper {


  public static Properties properties;

  private static String tables;
  private static String db;
  private static String mode;
  private static String packageName;


  private static List<String> tablesToReverse = new ArrayList();
  private static Map<String, Map<String, String>> tablesToReverseMeta = new HashMap();

  public static final String arraySeperator = ">";

  public static Connection conn ;

  public static String entityName ;
  public static String instanceName ;
  private static String desktopDir = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

  private static String command ;
  /**
   * 檢查參數
   * @param args
   */
  private static boolean checkArg(String... args){
    command = args[0].split(":")[1].trim().toUpperCase();
    boolean checkResult = false;
    if(command.equalsIgnoreCase("conf")){
      StringBuffer confBuf = new StringBuffer();
      confBuf.append("dataBaseDriver=com.mysql.jdbc.Driver\n\n");
      confBuf.append("## 数据库连接信息\n");
      confBuf.append("url=jdbc:mysql://localhost:3306/@module?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull\n");
      confBuf.append("username=iplastest\n");
      confBuf.append("password=123456\n\n");
      confBuf.append("## 生成结构体的时候，中间的包名\n");
      confBuf.append("package = promotion\n\n");
      confBuf.append("## 将要访问的db\n");
      confBuf.append("db = promotiondb\n\n");
      confBuf.append("##scanAll：反射整个库,  specify: 反转指定表\n");
      confBuf.append("mode = scanAll\n\n");
      confBuf.append("## specify模式下， 反转列表\n");
      confBuf.append("tables = coupons,use_condition");
      Utils.write(confBuf.toString(), ContentType.CONF,null);
      checkResult = false;
    } else if(command.equalsIgnoreCase("enumFmt")){
      StringBuffer enumFmtBuf = new StringBuffer();
      enumFmtBuf.append("枚举例子\n `enum_type` smallint(1) NOT NULL COMMENT '枚举描述,1:枚举1(enumOne);2:枚举2(enumTwo);3:枚举3(enumThree)'\n");
      enumFmtBuf.append("結果 :\n  /**\n  *抵扣用途\n  **/");
      enumFmtBuf.append("\nenum enumType{\n" +
              "   /**\n  *枚举1\n  **/\n" +
              "   ENUM_ONE=1,\n\n" +
              "   /**\n   *枚举2\n   **/\n" +
              "   ENUM_TWO=2,\n\n" +
              "   /**\n   *枚举3\n   **/\n" +
              "   ENUM_THREE=3\n" +
              "}");
      System.out.println(enumFmtBuf.toString());
      checkResult = false;
    }else{
      for(ContentType type : ContentType.values()){
        if(type.name().endsWith(command)){
          if(args.length>=2 && new File(args[1]).isFile()){
            checkResult=true;
          }
        }
      }
      if(!checkResult){
        System.out.println("java -jar dapeng.jar reverse:[po|struct|enum|sql|all|enumFmt|conf] [reverse.conf] \n");
      }
    }
    return checkResult;
  }

  /**
   * 生成代碼
   * @param args
   */
  public static void gen(String... args){
    if(!checkArg(args))
      return;

    readConf(args[1]);
    readMode();

    try{
      JdbcUtils.getConnection();
      intReverseTables();
      intMetaInfos();

      if (tablesToReverseMeta.size() == 0) {
        System.out.println("No new created table has been detected");
      }
      for (Map.Entry<String, Map<String, String>> entry : tablesToReverseMeta.entrySet()) {
        initCurrentEntityNames(entry.getKey());
        switch (ContentType.valueOf(command)) {
          case PO: genPo(entry); break;
//          case STRUCT: genStruct(entry); break;
          case ENUM:
            genEnums(entry); break;
          case SQL:
            genSql(entry); break;
          case ALL:
            genPo(entry);
//            genStruct(entry);
            genEnums(entry);
            genSql(entry);
            break;
          default:
            System.out.println("example: java -jar dapeng.jar reverseConf");
        }
      }
    }finally{
      try{
        if(conn!=null&&!conn.isClosed())
          conn.close();
      }catch (SQLException e){
        e.printStackTrace();
      }
    }


  }

  public static void initCurrentEntityNames(String tableName){
    entityName = Utils.underlineToCamel(true, tableName, true);
    if (entityName.endsWith("s")) {
      entityName = entityName.substring(0, entityName.length() - 1);
    }
    if (entityName.endsWith("ies")) {
      entityName = entityName.substring(0, entityName.length() - 3) + "y";
    }
    instanceName = Utils.underlineToCamel(true, entityName, false);
  }
  /**
   * 读取配置
   * @param file
   */
  public static void readConf(String file){
    try {
      InputStream input = new FileInputStream(new File(file));
      properties = new Properties();
      properties.load(input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 读取模式
   */
  public static void readMode(){
    try {
      db= properties.getProperty("db");
      tables= properties.getProperty("tables");
      mode = properties.getProperty("mode");
      packageName = properties.getProperty("package");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public static void intReverseTables(){
    try{
      if (mode.equalsIgnoreCase("scanAll")) {
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        ResultSet rs = null;
        String[] typeList = {"TABLE"};
        rs = databaseMetaData.getTables(null, null, null, typeList);
        for (boolean more = rs.next(); more; more = rs.next()) {
          String tableName = rs.getString("TABLE_NAME");
          String type = rs.getString("TABLE_TYPE");
          if ((type.equalsIgnoreCase("table")) && (tableName.indexOf("$") == -1)) {
            tablesToReverse.add(tableName);
          }
        }
      } else if (mode.equalsIgnoreCase("specify")) {
        tablesToReverse = Arrays.asList(tables.split(","));
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }
  public static void intMetaInfos(){
    for (String table : tablesToReverse) {
      try {
        ResultSet rs = conn.getMetaData().getColumns(null, "%", table.trim(), "%");
        LinkedHashMap<String, String> map = new LinkedHashMap();
        String colname;
        String typeName;
        String remark;
        int nullAble;
        boolean nullable = true;
        while (rs.next()) {
          colname = rs.getString("COLUMN_NAME");
          typeName = rs.getString("TYPE_NAME");
          remark = rs.getString("REMARKS");
          nullAble = rs.getInt("NULLABLE");
          switch (nullAble) {
            case 0:
              nullable = false;
              break;
            case 1:
              nullable = true;
              break;
            default:
              nullable = true;
          }
          String colmunSize = rs.getString("COLUMN_SIZE");
          if (colmunSize != null) {
            map.putIfAbsent(colname, typeName + arraySeperator + nullable + arraySeperator + remark + arraySeperator + colmunSize);
          } else {
            map.putIfAbsent(colname, typeName + arraySeperator + nullable + arraySeperator + remark);
          }
        }
        tablesToReverseMeta.putIfAbsent(table, map);
      }catch (Exception e){
        e.printStackTrace();
      }
    }
  }

  /**
   * 生成scala對象
   * @param tableMeta
   */
  public static void genPo(Map.Entry<String, Map<String, String>> tableMeta){
    StringBuffer poBuf = new StringBuffer();
    poBuf.append(String.format("package com.beizhi.cloud.%s.entity;\n",packageName.trim()));
    poBuf.append("  /**\n" +
            "  * @author auto-tool\n" +
            "  */");
    poBuf.append(String.format("public class %s {\n", entityName));
    Iterator<Map.Entry<String, String>> it = tableMeta.getValue().entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, String> entryTemp = (Map.Entry) it.next();
      poBuf.append("   /**\n" +
              "   * " + (String) entryTemp.getValue().split(arraySeperator)[2] + "\n" +
              "   */\n");

      poBuf.append("   "+Utils.toJavaType((entryTemp.getValue()).split(arraySeperator)[0]) + " "+Utils.underlineToCamel(true, entryTemp.getKey(), false)+";" )
              .append("\r\n\r\n");
    }
    poBuf.append("}\n");
    Utils.write(poBuf.toString(), ContentType.PO,null);
  }

  /**
   * 生成thrift结构体
   * @param tableMeta
   */
  public static void genStruct(Map.Entry<String, Map<String, String>> tableMeta){
    StringBuffer structBuf = new StringBuffer();
    structBuf.append(String.format("namespace java com.kuaisu.%s.dto\n\n",packageName.trim()));
    structBuf.append(String.format("struct T%s{\n", entityName));
    Iterator<Map.Entry<String, String>> it = tableMeta.getValue().entrySet().iterator();
    int i = 0;
    while (it.hasNext()) {
      i++;
      Map.Entry<String, String> entryTemp = (Map.Entry) it.next();
      structBuf.append("   /**\n" +
              "   * " + (String) entryTemp.getValue().split(arraySeperator)[2] + "\n" +
              "   */\n");
      structBuf.append(" " + i + " : " + (!(Boolean.valueOf(entryTemp.getValue().split(arraySeperator)[1])) ? "" : "optional") + " "
              + Utils.toThriftType(((String) entryTemp.getValue()).split(arraySeperator)[0])
              + " " + Utils.underlineToCamel(false, entryTemp.getKey(), false))
              .append(",\r\n");
    }
    structBuf.append("}\n");
    Utils.write(structBuf.toString(), ContentType.STRUCT,null);
  }
  /**
   * 生成thrift枚举
   * @param tableMeta
   */
  public static void genEnums(Map.Entry<String, Map<String, String>> tableMeta){
    Iterator<Map.Entry<String, String>> it = tableMeta.getValue().entrySet().iterator();
    String enumeComment = null;
    String enumeName = null;
    String[] enumeItems = null;
    boolean hasEnum = false;
    int i = 0;
    StringBuffer enumBuf =null;
    while (it.hasNext()) {
      i++;
      Map.Entry<String, String> entryTemp = (Map.Entry) it.next();
      if ((entryTemp.getValue().startsWith("SMALLINT"))) {
        enumBuf = new StringBuffer();
        hasEnum=true;
        if(hasEnum){
          enumBuf.append(String.format("package com.beizhi.cloud.%s.enums;\n", packageName.trim()));
        }
        String currentEnumesToGen = entryTemp.getValue();
        try {
          enumeComment = currentEnumesToGen.split(",")[0].split(">")[2];
          enumeName = entityName + Utils.underlineToCamel(false, entryTemp.getKey(), true);
          enumeItems = currentEnumesToGen.split(",")[1].split(";");

          enumBuf.append("/**").append("\r\n")
                  .append("*").append(enumeComment).append("\r\n")
                  .append("**/").append("\r\n");
          enumBuf.append("public enum ").append(enumeName).append("{");
          String itemComment;
          String itemName;
          String itemValue;

          for (int j = 0; j < enumeItems.length; j++) {
            itemComment = enumeItems[j].substring(enumeItems[j].indexOf(":") + 1, enumeItems[j].indexOf("("));
            itemName = enumeItems[j].substring(enumeItems[j].indexOf("(") + 1, enumeItems[j].indexOf(")"));
            itemValue = enumeItems[j].split(":")[0];
            enumBuf.append("\r\n").append("   /**").append("\r\n")
                    .append("   *").append(itemComment).append("\r\n")
                    .append("   **/").append("\r\n")
                    .append("   " + Utils.underlineToCamel(itemName).toUpperCase() + "(" + itemValue+")");
            if (j < enumeItems.length - 1) {
              enumBuf.append(",").append("\r\n");
            }
            if (j == enumeItems.length - 1) {
              enumBuf.append(";\r\n}").append("\r\n");
            }
          }
        } catch (Exception e) {
          System.err.println(String.format("instanceName-> %s enumeName:%s: enumeComment:%s 不符合规范 skipped",instanceName,enumeName,enumeComment));
          continue;
        }
        Utils.write(enumBuf.toString(), ContentType.ENUMSINGLE,Utils.underlineToCamel(false, entryTemp.getKey(), true));
      }
    }
  }

  /**
   * 生成thrift结构体
   * @param tableMeta
   */
  public static void genSql(Map.Entry<String, Map<String, String>> tableMeta){
    StringBuffer structBuf = new StringBuffer();
    structBuf.append(String.format("//%s\n",packageName));

    Iterator<Map.Entry<String, String>> it = tableMeta.getValue().entrySet().iterator();
    structBuf.append("//insert sql\n");
    structBuf.append(String.format(" insert into %s set\n", tableMeta.getKey()));
    while (it.hasNext()) {
      Map.Entry<String, String> entryTemp = (Map.Entry) it.next();
      String fieldName = entryTemp.getKey();
      structBuf.append(String.format("      %s='' ,\n", fieldName));
    }
    structBuf.replace(structBuf.length() - 2, structBuf.length() - 1, ";");

    structBuf.append("//delete sql\n");
    it = tableMeta.getValue().entrySet().iterator();
    structBuf.append(String.format(" delete from %s where\n", tableMeta.getKey()));
    while (it.hasNext()) {
      Map.Entry<String, String> entryTemp = (Map.Entry) it.next();
      String fieldName = entryTemp.getKey();
      structBuf.append(String.format("      %s='' and\n", fieldName));
    }
    structBuf.replace(structBuf.length() - 5, structBuf.length() - 1, ";");

    structBuf.append("//update sql\n");
    structBuf.append(String.format("  update %s set\n", tableMeta.getKey()));
    it = tableMeta.getValue().entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, String> entryTemp = (Map.Entry) it.next();
      String fieldName = entryTemp.getKey();
      structBuf.append(String.format("      %s='' and\n", fieldName));
    }
    structBuf.replace(structBuf.length() - 5, structBuf.length() - 1, ";");

    structBuf.append("//select sql\n");
    it = tableMeta.getValue().entrySet().iterator();
    structBuf.append(" select\n");
    while (it.hasNext()) {
      Map.Entry<String, String> entryTemp = (Map.Entry) it.next();
      String fieldName = entryTemp.getKey();
      structBuf.append(String.format("      %s,\n", fieldName));
    }
    structBuf.delete(structBuf.length() - 2, structBuf.length() - 1);
    structBuf.append(String.format(" from %s where\n", tableMeta.getKey()));
    it = tableMeta.getValue().entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, String> entryTemp = (Map.Entry) it.next();
      String fieldName = entryTemp.getKey();
      structBuf.append(String.format("      %s='' and\n", fieldName));
    }
    structBuf.replace(structBuf.length() - 5, structBuf.length() - 1, ";");

    Utils.write(structBuf.toString(), ContentType.SQL,null);
  }


  public static enum ContentType{
    CONF,
    STRUCT,
    ENUM,
    ENUMSINGLE,
    PO,
    SQL,
    ALL
  }
  /**
   * JdbcUtils
   */
  public static class JdbcUtils {

    public static void getConnection() {
      try {
        Class.forName(properties.getProperty("dataBaseDriver"));
        String databaseConnectUrl = properties.getProperty("url");
        databaseConnectUrl = databaseConnectUrl.replace("@module", db);
        conn = DriverManager.getConnection(databaseConnectUrl, properties.getProperty("username"), properties.getProperty("password"));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  /**
   * 工具
   */
  public static class Utils {

    public static final char UNDERLINE = '_';

    public static String underlineToCamel(boolean transferFiled, String param, boolean firstLetterToUpper) {
      if (transferFiled && param.equals("type")) {
        return "`type`";
      }
      if ((param == null) || ("".equals(param.trim()))) {
        return "";
      }
      int len = param.length();
      StringBuilder sb = new StringBuilder(len);
      for (int i = 0; i < len; i++) {
        char c = param.charAt(i);
        if (i == 0) {
          if ((firstLetterToUpper) && (c >= 'a') && (c <= 'z')) {
            sb.append((char) (c - 32));
          } else if ((!firstLetterToUpper) && (c >= 'A') && (c <= 'Z')) {
            sb.append((char) (c + 32));
          } else {
            sb.append(c);
          }
        } else if (c == UNDERLINE) {
          i++;
          if (i < len) {
            sb.append(Character.toUpperCase(param.charAt(i)));
          }
        } else {
          sb.append(c);
        }
      }

      return sb.toString();
    }

    public static String underlineToCamel(String param) {
      if ((param == null) || ("".equals(param.trim()))) {
        return "";
      }
      int len = param.length();
      StringBuilder sb = new StringBuilder(len);
      char c;
      for (int i = 0; i < len; i++) {
        c = param.charAt(i);
        if ((c >= 'A') && (c <= 'Z')) {
          sb.append("_").append((char) (c + 32));
        } else {
          sb.append(c);
        }
      }
      return sb.toString();
    }

    public static String toJavaType(String type) {
      if (type.equalsIgnoreCase("CHAR")
              || type.equalsIgnoreCase("VARCHAR")
              || type.equalsIgnoreCase("TINYBLOB")
              || type.equalsIgnoreCase("TINYTEXT")
              || type.equalsIgnoreCase("BLOB")
              || type.equalsIgnoreCase("TEXT")
              || type.equalsIgnoreCase("MEDIUMBLOB")
              || type.equalsIgnoreCase("MEDIUMTEXT")
              || type.equalsIgnoreCase("LOGNGBLOB")
              || type.equalsIgnoreCase("LONGTEXT")) {
        return "String";
      }
      if (type.equalsIgnoreCase("DOUBLE")) {
        return "double";
      }
      if (type.equalsIgnoreCase("ENUM")) {
        return "String";
      }
      if (type.equalsIgnoreCase("DECIMAL")) {
        return "java.math.BigDecimal";
      }
      if (type.equalsIgnoreCase("TINYINT")
              || type.equalsIgnoreCase("SMALLINT")
              || type.equalsIgnoreCase("MEDIUMINT")
              || type.equalsIgnoreCase("INT")
              || type.equalsIgnoreCase("BIT")
              || type.equalsIgnoreCase("BIGINT")) {
        return "int";
      }

      if (type.equalsIgnoreCase("TIME") || type.equalsIgnoreCase("YEAR")) {
        return "String";
      }
      if (type.equalsIgnoreCase("DATE")) {
        return "java.sql.Date";
      }
      if (type.equalsIgnoreCase("DATETIME")) {
        return "java.sql.Timestamp";
      }
      if (type.equalsIgnoreCase("TIMESTAMP")) {
        return "java.sql.Timestamp";
      }
      if (type.equalsIgnoreCase("LONGBLOB")) {
        return "Array[Byte]";
      }
      return type;
    }

    public static String toScalaType(String type) {
      if (type.equalsIgnoreCase("CHAR")
              || type.equalsIgnoreCase("VARCHAR")
              || type.equalsIgnoreCase("TINYBLOB")
              || type.equalsIgnoreCase("TINYTEXT")
              || type.equalsIgnoreCase("BLOB")
              || type.equalsIgnoreCase("TEXT")
              || type.equalsIgnoreCase("MEDIUMBLOB")
              || type.equalsIgnoreCase("MEDIUMTEXT")
              || type.equalsIgnoreCase("LOGNGBLOB")
              || type.equalsIgnoreCase("LONGTEXT")) {
        return "String";
      }
      if (type.equalsIgnoreCase("DOUBLE")) {
        return "Double";
      }
      if (type.equalsIgnoreCase("ENUM")) {
        return "String";
      }
      if (type.equalsIgnoreCase("DECIMAL")) {
        return "BigDecimal";
      }
      if (type.equalsIgnoreCase("TINYINT")
              || type.equalsIgnoreCase("SMALLINT")
              || type.equalsIgnoreCase("MEDIUMINT")
              || type.equalsIgnoreCase("INT")
              || type.equalsIgnoreCase("BIT")
              || type.equalsIgnoreCase("BIGINT")) {
        return "Int";
      }

      if (type.equalsIgnoreCase("TIME") || type.equalsIgnoreCase("YEAR")) {
        return "String";
      }
      if (type.equalsIgnoreCase("DATE")) {
        return "java.sql.Date";
      }
      if (type.equalsIgnoreCase("DATETIME")) {
        return "java.sql.Timestamp";
      }
      if (type.equalsIgnoreCase("TIMESTAMP")) {
        return "java.sql.Timestamp";
      }
      if (type.equalsIgnoreCase("LONGBLOB")) {
        return "Array[Byte]";
      }

      return type;
    }

    public static String toThriftType(String type) {
      if (type.equalsIgnoreCase("CHAR")
              || type.equalsIgnoreCase("VARCHAR")
              || type.equalsIgnoreCase("TINYBLOB")
              || type.equalsIgnoreCase("TINYTEXT")
              || type.equalsIgnoreCase("BLOB")
              || type.equalsIgnoreCase("TEXT")
              || type.equalsIgnoreCase("MEDIUMBLOB")
              || type.equalsIgnoreCase("MEDIUMTEXT")
              || type.equalsIgnoreCase("LOGNGBLOB")
              || type.equalsIgnoreCase("LONGTEXT")) {
        return "string";
      }
      if (type.equalsIgnoreCase("DOUBLE")) {
        return "double";
      }
      if (type.equalsIgnoreCase("DECIMAL")) {
        return "double";
      }
      if (type.equalsIgnoreCase("TINYINT")
              || type.equalsIgnoreCase("BIT")
              || type.equalsIgnoreCase("SMALLINT")
              || type.equalsIgnoreCase("MEDIUMINT")
              || type.equalsIgnoreCase("INT")
              || type.equalsIgnoreCase("BIGINT")) {
        return "i32";
      }

      if (type.equalsIgnoreCase("TIME") || type.equalsIgnoreCase("YEAR")) {
        return "string";
      }
      if (type.equalsIgnoreCase("DATE")) {
        return "i64";
      }
      if (type.equalsIgnoreCase("DATETIME")) {
        return "i64";
      }
      if (type.equalsIgnoreCase("Timestamp")) {
        return "i64";
      }
      if (type.equalsIgnoreCase("java.sql.Timestamp")) {
        return "i64";
      }
      if (type.equalsIgnoreCase("ENUM")) {
        return "string";
      }
      if (type.equalsIgnoreCase("LONGBLOB")) {
        return "binary";
      }
      return type;
    }

    public static void write(String content, ContentType contentType, String fileName){
      String fileAbsolutePath = "";
      String fileExtension ="";
      switch (contentType){
        case PO:
          fileAbsolutePath = desktopDir+File.separator+ "reverse-result/PO/";
          fileExtension = ".java";
          break;
        case STRUCT: fileAbsolutePath = desktopDir+File.separator+ "reverse-result/dto/";
          fileExtension = "Domain.thrift";
          break;
        case ENUM: fileAbsolutePath = desktopDir+File.separator+ "reverse-result/enum/";
          fileExtension = "Enums.java";
          break;
        case ENUMSINGLE: fileAbsolutePath = desktopDir+File.separator+ "reverse-result/enum/";
          fileExtension = fileName;
          break;
        case SQL: fileAbsolutePath = desktopDir+File.separator+ "reverse-result/sql/";
          fileExtension = ".sql";
          break;
        case CONF: fileAbsolutePath = desktopDir+File.separator+ "reverse-conf/";
          entityName = "reverse";
          fileExtension = ".conf";
          break;
        default:
      }

      File direcrory = new File(fileAbsolutePath);
      if(!direcrory.exists()){
        direcrory.mkdirs();
      }
      File file = new File(fileAbsolutePath + entityName + fileExtension);
      FileWriter fw = null;
      BufferedWriter writer = null;
      try{
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
        writer.write(content);
        writer.flush();
      } catch(IOException ioe){
        ioe.printStackTrace();
      }
      System.out.println(String.format("%s--> %s generated",contentType.name(),entityName));
    }
  }
}
