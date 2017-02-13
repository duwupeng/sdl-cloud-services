package com.talebase.cloud.common.db.reverser;

/**
 * Created by Shadow on 2016/8/2.
 */
public class Main {
    private static String help = "-----------------------------------------------------------------------\n" +
            " |    指定数据库以及表,生成相应的scala对象，thrift文件: \n" +
            " |    java -jar code-reverser.jar reverse:[po|enum|enumFmt|sql|all|conf] [reverse.conf] \n" +
            "-----------------------------------------------------------------------";
    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println(help);
            System.exit(0);
        }

        if(args[0].startsWith("reverse:")){
            DbReverserHelper.gen(args);
            System.exit(0);
        }else{
            System.out.println(help);
        }
    }
}
