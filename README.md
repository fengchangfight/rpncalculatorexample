### 如何build出可执行Jar包？
运行： 

```
./gradew build
```
执行完毕后可在build/libs/下找到calculator-1.0-SNAPSHOT.jar文件

### 如何运行？
对上面build出来的jar包执行如下命令：

```
java -jar calculator-1.0-SNAPSHOT.jar
```

一个执行样例：

```
请输入RPN格式的表达式(可多行输入),输入q退出：
ab
非法输入...
stack:
1 2 +
stack: 3
ka
非法输入...
stack: 3
2 *
stack: 6
q
退出计算器
```

### 如何执行test case?

```
./gradlew test
```


