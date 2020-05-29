package com.rpn.manager;

import java.util.Scanner;

/**
 * @author xiefengchang
 * 本类负责command-line的输入输出
 */
public class IoManager {
    /**
     * 从标准输入获取一行并返回内容
     * @return
     */
    public static String acceptLine(){
        Scanner scanner = new Scanner(System.in);
        String row = scanner.nextLine();
        return row;
    }
}
