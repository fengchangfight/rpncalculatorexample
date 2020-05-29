package com.rpn.manager;

import com.rpn.operator.BaseBinaryOperation;
import com.rpn.operator.BaseUnaryOperation;
import com.rpn.operator.OperatorFactory;
import javafx.util.Pair;

import java.math.BigDecimal;

import java.util.*;

/**
 * @author xiefengchang
 * 本类作为计算器的状态管理，记录着所有的历史输入和操作，等价于可推演出当前计算器的状态，本状态用于后序计算，也可用于反推前序某种状态
 */
public class StateManager {

    /**
     * 资瓷的运算符
     */
    public static final Set<String> binaryOperators = new HashSet<>(Arrays.asList("+","-","*","/"));
    public static final Set<String> unaryOperators = new HashSet<>(Arrays.asList("sqrt"));
    public static final Set<String> commands = new HashSet<>(Arrays.asList("clear", "undo"));

    /**
     * 本数据结构记录每一次向前解析的反操作，利用反操作，来实现undo逻辑，主要内容为一个或多个栈结构变更
     */
    static class UndoLog{
        /**
         * action只有两种类型： 1， - 代表出栈, 2, +{number}代表入栈某个数字，有可能在一步parsing中包含多个出入栈动作，所有用List串起来
         */

        List<Pair<Character, BigDecimal> > actions = new ArrayList<>();
        public void addAction(Character sign,  BigDecimal val){
            //注意插入顺序每次在list头部插入
            actions.add(0,new Pair<>(sign, val));
        }
    }

    /**
     * 这个栈用来做undo日志，通过执行undo日志可以复原之前的状态
     */
    Stack<UndoLog> undoLogStack = new Stack<>();

    /**
     * 用于存放全局的计算数栈
     */
    private Stack<BigDecimal> numberStack = new Stack<>();

    private String currentInputLine="";

    private String message = "";

    public String getMessage(){
        return message;
    }

    /**
     * 解析器扫描游标记录,目前在行内解析到了第几个字符
     */
    private Integer rowIndex=0;

    public void resetAll(){
        clear();
        rowIndex = 0;
        currentInputLine = "";
        message = "";
    }
    /**
     * 应用undo log, 更新numstack，弹出undo log本身栈顶元素
     */
    public void applyLastUndoLog(){
        if(undoLogStack.isEmpty()){
            //如果没有undo内容了，就啥也不做了
            return;
        }
        UndoLog work2undo = undoLogStack.pop();

        for(Pair<Character,BigDecimal> work: work2undo.actions){
            if('-'==work.getKey()){
                numberStack.pop();
            }else if('+'==work.getKey()){
                BigDecimal val = work.getValue();
                numberStack.push(val);
            }else{
                throw new RuntimeException("Unsupported undo action");
            }
        }
    }

    /**
     * 在用户新接收一行输入时更新这个记录
     * @param row
     */
    public void addNewLine(String row){
        currentInputLine = row;
        rowIndex = 0;
    }

    /**
     * 清空栈，undolog历史和状态复原
     */
    public void clear(){
        numberStack.clear();
        undoLogStack.clear();
    }

    /**
     * 打印当前stack的内容(借助临时对称栈，不改变栈内容)
     */
    public void printCurrentStack(){
        System.out.println(getStackContent());
    }

    public String getStackContent(){
        StringBuilder sb = new StringBuilder();
        sb.append("stack: ");
        if(numberStack.isEmpty()){
            return sb.toString().trim();
        }
        Stack<BigDecimal> tmpStack = new Stack<>();
        while(!numberStack.isEmpty()){
            tmpStack.push(numberStack.pop());
        }
        while(!tmpStack.isEmpty()){
            BigDecimal out = tmpStack.pop();
            //打印精度设为10（原存储精度15)
            BigDecimal numafter = out.setScale(10,BigDecimal.ROUND_DOWN);
            sb.append(numafter.stripTrailingZeros().toPlainString()+" ");
            numberStack.push(out);
        }

        return sb.toString().trim();
    }

    /**
     * 用parser向前解析一步，解析当前游标所指的操作数或运算符，并游标移到下一处symbol，所谓一步，比如: 3 12 -, 可以理解为游标解析3这个数字是一步
     * 并更新相应栈状态和游标状态，下一步解析12这个数字，并更新栈状态和游标状态，再下一步解析 - 这个符号并更新栈状态和游标状态
     * 这个解析过程，有可能发生问题，包括非法输入，操作数不够等，要分别处理
     * 返回下一个元素起点index
     */
    public int parseOneStepForward(String currentLine){
        //从rowIndex为起点，找到连续非空字符的最后一位index，相当于截出一个输入元素（可能是操作符或操作数）
        int p = rowIndex;
        while(p<=currentLine.length()-1 && currentLine.charAt(p)!=' '){
            p++;
        }
        int lastIndex = p-1;

        String element = currentLine.substring(rowIndex, lastIndex+1);

        int type = checkElementType(element);

        //以上解析出本轮的token，下面根据token的类型（例如数字，运算符，操作命令，非法输入，分别进行操作)

        if(type==1){
            int code = handleUnaryOperation(currentLine, element);
            if(code==-1){
                return currentLine.length();
            }
        }else if(type==2){
            int code = handleBinaryOperation(currentLine, element);
            if(code==-1){
                return currentLine.length();
            }
        }else if(type==5)
        {
            handleCommands(element);
        }else if(type==0){
            handleNumbers(element);
        }else{
            // 非法输入,直接返回lastIndex+1;
            message = "非法输入...";
            System.out.println(message);
            return currentLine.length();
        }

        //如果上面没有因为非法输入等原因提前返回，这里就计算下一轮计算的起始index
        if(lastIndex==currentLine.length()-1){
            return currentLine.length();
        }else{
            int i = lastIndex+1;
            while(i<=currentLine.length()-1 && currentLine.charAt(i)==' '){
                i++;
            }
            return i;
        }
    }
    private void handleNumbers(String element){
        //操作数,操作数入栈，更新反操作到undolog栈的栈顶
        BigDecimal val = (new BigDecimal(element)).setScale(15, BigDecimal.ROUND_DOWN);
        numberStack.push(val);
        UndoLog undo = new UndoLog();
        undo.addAction('-', null);
        undoLogStack.push(undo);
    }
    private void handleCommands(String element){
        //操作指令， 如clear, undo
        if("clear".equals(element)){
            this.clear();
        }else if("undo".equals(element)){
            applyLastUndoLog();
        }else{
            throw new RuntimeException("Unsupported command");
        }
    }
    private int handleUnaryOperation(String currentLine, String element){
        // 一元运算符
        //操作符, 判定操作符类型，取相应栈顶元素，弹出并计算结果值，压回栈，把相应的反操作记录到undolog栈顶，undolog栈元素可以是一系列操作，但是这

        if(numberStack.isEmpty()){
            // 这里有可能出现操作数不足的情况，这是打印出错误信息，并返回lastIndex + 1
            message = "operator "+currentLine.charAt(rowIndex)+" (position: "+(rowIndex+1)+"): insucient parameters";
            System.out.println(message);
            // 这样返回可在上层终止parsing
            rowIndex = 0;
            return -1;
        }
        UndoLog undo = new UndoLog();
        BaseUnaryOperation uop = OperatorFactory.makeUnaryOperation(element);
        //取栈顶元素，计算
        BigDecimal out = numberStack.pop();
        //将逆操作加入undo
        undo.addAction('+', out);
        //将本组undo操作集合加入undoLogStack
        undoLogStack.add(undo);
        try{
            BigDecimal ret = uop.doOperation(out);
            //逆操作，出栈
            undo.addAction('-', null);
            //算完推回栈
            numberStack.push(ret);
        }catch (RuntimeException e){
            message = e.getMessage();
            System.out.println(message);
            applyLastUndoLog();
        }
        return 0;
    }
    private int handleBinaryOperation(String currentLine, String element){
        //二元运算符
        if(numberStack.isEmpty() || numberStack.size()<2){
            //参数不够的情况
            message = "operator "+currentLine.charAt(rowIndex)+" (position: "+(rowIndex+1)+"): insucient parameters";
            System.out.println(message);
            rowIndex = 0;
            return -1;
        }
        //弹出两个操作数
        UndoLog undo = new UndoLog();
        BigDecimal secondVal = numberStack.pop();
        BigDecimal firstVal  = numberStack.pop();
        undo.addAction('+', secondVal);
        undo.addAction('+', firstVal);

        BaseBinaryOperation binop = OperatorFactory.makeBinaryOperation(element);
        undoLogStack.push(undo);

        try{
            BigDecimal ret = binop.doOperation(firstVal, secondVal);
            numberStack.push(ret);
            undo.addAction('-', null);
        }catch (RuntimeException e){
            message = e.getMessage();
            //复原两个弹出参数
            applyLastUndoLog();
        }

        return 0;
    }

    /**
     * 解析当前输入的一整行，当然要结合之前的状态, 如果出现非法输入或者操作数不足的情况，要打印出警告信息，并停止继续解析
     */
    public void parseLine(){
        if(currentInputLine==null || currentInputLine.length()<1){
            //空串不处理
            return;
        }
        //解析当前输入行
        //预处理，本循环旨在谨防起始case时rowIndex指向空符的情况，连续跳过，直到碰到第一个非空字符
        while(currentInputLine.charAt(rowIndex)==' '){
            rowIndex++;
        }
        if(rowIndex>currentInputLine.length()-1){
            //说明整个串都是空串，不用解析了
            return;
        }

        //连续解析直到末尾或者碰到异常
        while(rowIndex<=currentInputLine.length()-1){
            rowIndex = parseOneStepForward(currentInputLine);
        }
    }

    /**
     * 检查单个输入元素是操作符(0表示)还是操作数(1表示)
     * @param word
     * @return
     */
    private int checkElementType(String word){
        try{
            BigDecimal num = new BigDecimal(word);
            //代表数字
            return 0;
        }catch (NumberFormatException e){
            //检查word 是否属于合法的运算符，如果是
            int t = checkLegalOperator(word);
            if(t==2){
                //代表二元运算符
                return 2;
            }else if(t==1){
                //代表一元运算符
                return 1;
            }else if(t==5){
                // 操作指令，如undo, clear等
                return 5;
            }else{
                //不是操作数，又不是合法运算符，即为非法输入
                return -1;
            }
        }
    }

    /**
     * 检查输入的是否是一个合法的运算符
     * @param word
     * @return
     */
    private int checkLegalOperator(String word){
        if(binaryOperators.contains(word)){
            //二元运算符
            return 2;
        }else if(unaryOperators.contains(word)){
            //一元运算符
            return 1;
        }else if(commands.contains(word)){
            //操作指令
            return 5;
        }
        //非法输入串
        return  -1;
    }
}
