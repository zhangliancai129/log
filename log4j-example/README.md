# log4j参数配置

**能不用Log4j就不要用Log4j，高并发情况下，性能不是很好。推荐使用logback或log4j2。**

## 1. rootLogger
**语法结构：**<br>
log4j.rootLogger=logLevel, appenderName1, appenderName2, ...<br>
+ logLevel：日志级别，从高到低分为OFF、FATAL、ERROR、WARN、INFO、DEBUG、ALL。
+ appenderName1, appenderName2：指定日志信息输出到哪里。可以同时指定多个输出目的地。

## 2. Appender，配置日志信息输出目的地
**语法结构：**<br>
log4j.appender.自定义的appenderName=Appender类全名

### 2.1. Log4j提供的Appender有以下几种
+ org.apache.log4j.AsyncAppender（异步打印日志。虽然是异步打印日志，但还是存在一些性能问题。）
+ org.apache.log4j.WriterAppender（将日志以流格式发送到指定的地方，下面几个类都是它的子类）
+ org.apache.log4j.ConsoleAppender（控制台）
+ org.apache.log4j.FileAppender（磁盘文件）
+ org.apache.log4j.DailyRollingFileAppender（按指定时间产生一个新的日志文件，FileAppender的子类）
+ org.apache.log4j.RollingFileAppender（文件大小到达指定值的时候产生一个新的文件，FileAppender的子类）

### 2.2. Appender的相关参数
**语法结构：**<br>
log4j.appender.appenderName.optionName=optionValue

#### 2.2.1. 指定日志输出的最低级别
log4j.appender.appenderName.Threshold=DEBUG

#### 2.2.2. 是否立即输出日志
默认值是true，所有的日志都会被立即输出。<br>
log4j.appender.appenderName.ImmediateFlush=true

#### 2.2.3. 日志输出目的地
控制台（ConsoleAppender，默认值：System.out）：<br>
log4j.appender.appenderName.Target=System.err<br>
日志文件：<br>
log4j.appender.appenderName.File=/u1/logs/log.log

#### 2.2.4. 日志添加方式
log4j.appender.appenderName.Append=true<br>
true将消息增加到指定文件的末尾<br>
false将消息覆盖文件中的内容

#### 2.2.5. 日志文件生成规则
**DailyRollingFileAppender 的特有参数。**<br>
log4j.appender.appenderName.DatePattern='.'yyyy-MM-dd-a<br>
**按照指定的格式生成日志文件。**
+ '.'yyyy-MM: 每月产生一个新的文件
+ '.'yyyy-ww: 每周产生一个新的文件
+ '.'yyyy-MM-dd: 每天产生一个新的文件
+ '.'yyyy-MM-dd-a: 每天产生两个新的文件（午夜和中午各生成一个）
+ '.'yyyy-MM-dd-HH: 每小时产生一个新的文件
+ '.'yyyy-MM-dd-HH-mm: 每分钟产生一个新的文件

**RollingFileAppender的特有参数**<br>
在日志文件达到指定大小时，将原来文件重命名为log.log.1，然后新建文件log.log。<br>
log4j.appender.appenderName.MaxFileSize=1024MB<br>
单位可以是KB, MB 或者是 GB。

#### 2.2.6. 日志文件可以备份的最大数 
**RollingFileAppender的特有参数。**<br>
log4j.appender.appenderName.MaxBackupIndex=20

#### 2.2.7. 编码格式
log4j.appender.appenderName.Encoding=UTF-8

[参考文档](https://www.yiibai.com/log4j/log4j_logging_files.html)

## 3. 配置日志信息的布局
**语法结构：**<br>
log4j.appender.appenderName.layout=Layout类全名<br>
log4j.appender.appenderName.layout.ConversionPattern=日志输出格式

### 3.1. Log4j提供的layout有以下几种
+ org.apache.log4j.HTMLLayout（以HTML表格形式布局）
+ org.apache.log4j.PatternLayout（可以灵活地指定布局模式）
+ org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串）
+ org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息）

### 3.2. 日志内置环境信息
+ %c: 日志信息所属的类目，通常就是所在类的全名。
+ %C: 完全限定类名。
+ %d: 打印日志的时间。例如：%d{yyyy-MM-dd HH:mm:ss.SSS}，输出：2008-08-08 08:08:08.888。
+ %F: 所在的文件名称。
+ %l: 输出日志事件的发生位置。相当于%C.%M(%F:%L)的组合，包括类名、方法名，以及在代码中的行数。
+ %L: 代码中的行号。
+ %m: 代码中指定的日志消息。
+ %M: 方法名称。
+ %n: 输出一个回车换行符，Windows平台为＂\r\n＂，UNIX和Linux平台为＂\n＂，将输出日志信息换行。
+ %p: 日志级别，即DEBUG，INFO，WARN，ERROR等。
+ %r: 输出自应用启动到输出该Log信息所用的毫秒数。
+ %t: 线程名。
+ %x: 输出和当前线程相关的org.apache.log4j.NDC(嵌套诊断环境)，主要用于Servlet这样的多客户、多线程的Web应用中。
+ %X: X后面的是org.apache.log4j.MDC的键，表示在日志中打印键对应的值。例如：%X{traceId}，将打印存储在MDC中键为traceId对应的值信息。
+ %%: 输出一个”%”字符。

**可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如:**
+ %20c：指定最小的宽度是20个字符，如果少于20个字符的话，在左边补空格，右对齐。
+ %-20c：指定最小的宽度是20个字符，如果少于20个字符的话，在右边补空格，左对齐。
+ %.30c：指定最大的宽度是30个字符，如果多于30个字符的话，则会将左边多出的字符截掉，少于30个字符时也不会有空格。
+ %20.30c：如果少于20个字符就在左边补空格，右对齐，如果多于30个字符的话，则把左边多出的字符截掉。

[参考文档](https://www.yiibai.com/log4j/log4j_patternlayout.html)

## 4. 配置示例
```text/plain
log4j.rootLogger=DEBUG, CONSOLE, FILE, ROLLING_FILE, DAILY_FILE

# 子Logger 是否继承 父Logger 的 输出源（appender） 的标志位。
# 具体说，默认情况下子Logger会继承父Logger的appender，也就是说子Logger会在父Logger的appender里输出。
# 若是additivity设为false，则子Logger只会在自己的appender里输出，而不会在父Logger的appender里输出。
log4j.additivity.org.apache=true

# 控制台
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.Threshold=DEBUG
log4j.appender.CONSOLE.ImmediateFlush=true
log4j.appender.CONSOLE.Target=System.out
log4j.appender.CONSOLE.Encoding=UTF-8
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss.SSS} %5p [%t] %c.%M:%L %m%n

# 根据文件大小新增文件，这种方式用的最多
log4j.appender.ROLLING_FILE=org.apache.log4j.RollingFileAppender
log4j.appender.ROLLING_FILE.Threshold=DEBUG
log4j.appender.ROLLING_FILE.ImmediateFlush=true
log4j.appender.ROLLING_FILE.File=/u1/logs/log4j-example/rolling-log.log
log4j.appender.ROLLING_FILE.Append=true
log4j.appender.ROLLING_FILE.MaxFileSize=10MB
log4j.appender.ROLLING_FILE.MaxBackupIndex=20
log4j.appender.ROLLING_FILE.Encoding=UTF-8
log4j.appender.ROLLING_FILE.layout=org.apache.log4j.PatternLayout
log4j.appender.ROLLING_FILE.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss.SSS} %5p [%t] %F.%M %m%n

# 根据时间新增文件，这种方式用的比较少
log4j.appender.DAILY_FILE=org.apache.log4j.DailyRollingFileAppender
log4j.appender.DAILY_FILE.Threshold=DEBUG
log4j.appender.DAILY_FILE.ImmediateFlush=true
log4j.appender.DAILY_FILE.File=/u1/logs/log4j-example/daily-log.log
log4j.appender.DAILY_FILE.Append=true
log4j.appender.DAILY_FILE.DatePattern='.'yyyy-MM-dd-a
log4j.appender.DAILY_FILE.Encoding=UTF-8
log4j.appender.DAILY_FILE.layout=org.apache.log4j.PatternLayout
log4j.appender.DAILY_FILE.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss.SSS} %5p [%t] %F.%M %m%n

# 文件 这种方式几乎不用
log4j.appender.FILE=org.apache.log4j.FileAppender
log4j.appender.FILE.Threshold=DEBUG
log4j.appender.FILE.ImmediateFlush=true
log4j.appender.FILE.File=/u1/logs/log4j-example/file-log.log
log4j.appender.FILE.Append=true
log4j.appender.FILE.Encoding=UTF-8
log4j.appender.FILE.layout=org.apache.log4j.PatternLayout
log4j.appender.FILE.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss.SSS} %5p [%t] %F.%M %m%n

```