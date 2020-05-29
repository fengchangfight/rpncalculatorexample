package com.rpn;

import com.rpn.manager.IoManager;
import com.rpn.manager.StateManager;

/**
 * @author xiefengchang
 * 本类为计算程序入口类
 */
public class Calculator {

    private StateManager stateManager;

    public StateManager getStateManager(){
        return stateManager;
    }

    /**
     * 初始化本计算器的状态管理器和IO管理器
     */
    public void initMyState(){
        stateManager = new StateManager();
    }

    public void reset(){
        stateManager.resetAll();
    }

    public static void main(String[] args) {
        Calculator rpnCalculator = new Calculator();

        //初始化本计算器的状态
        rpnCalculator.initMyState();

        //通过无限循环接收参数: IoManager开始接受输入
        System.out.println("请输入RPN格式的表达式(可多行输入),输入q退出：");
        while(true){
            //接收新行
            String input = IoManager.acceptLine();
            if(input.equals("q")){
                //退出计算器
                System.out.println("退出计算器");
                break;
            }
            //更新状态
            rpnCalculator.stateManager.addNewLine(input);
            //尝试结合之前的全局状态，极限解析当前行的内容，直到解析完整当前行或者碰到不合法输入或者操作数不够，则停止解析，并打印更新后的栈状态
            rpnCalculator.stateManager.parseLine();
            rpnCalculator.stateManager.printCurrentStack();
        }
    }
}
