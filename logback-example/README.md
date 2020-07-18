# logback

+ logback是spring boot默认的日志框架，logback比log4j的性能要好上几十倍。
+ 如果不自定义《logback.xml》文件，会使用spring boot的默认配置，实际应用中几乎都是自定义《logback.xml》文件。
>如果想要使用spring boot的默认配置打印日志，那么在application.yml文件中的配置可以从如下几个类中查看：
>org.springframework.boot.context.logging.LoggingApplicationListener
>org.springframework.boot.logging.LogFile
>org.springframework.boot.logging.LoggingSystemProperties
>org.springframework.boot.logging.logback.DefaultLogbackConfiguration
>org.springframework.boot.logging.logback.SpringBootJoranConfigurator
>org.springframework.boot.logging.logback.LogbackLoggingSystem

**默认配置方式示例：**
```yaml
logging:
  file:
#    path: E:/spring-boot-example/logs/logback-example # logging.file.path，logging.file.name 只有一个会生效，logging.file.name的优先级高
    name: logs/logback-example/logback-example.log # 这个好像只能是相对路径
    max-size: 1024MB # 默认10MB
    max-history: 30  # 默认7天
  pattern:
    file: '[%d{yyyy-MM-dd HH:mm:ss.SSS,Asia/Shanghai}] [%5level] [%20.30thread] [%X{traceId},%X{spanId}] %30.50logger{50} - %msg%n'
    rolling-file-name: E:/spring-boot-example/logs/logback-example/logback-example.%d{yyyy-MM-dd}.%i.log
  level:
    root: DEBUG
```

### 自定义配置说明
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- scan: 当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。
     scanPeriod: 设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟。
     debug: 当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。 -->
<configuration scan="true" scanPeriod="60 seconds" debug="false">

    <!-- Spring boot 有几个默认的日志配置文件，都在spring-boot.jar包的org.springframework.boot.logging.logback路径下面 -->
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>

    <!-- 获取application.properties文件中的配置参数，在后续文件中可以使用${name}使用 -->
    <!-- 源码：org.springframework.boot.logging.logback.SpringPropertyAction -->
    <springProperty scope="context" name="APPLICATION_NAME" source="spring.application.name" />

    <!-- 设置变量，在文件中可以使用${name}来引用定义的变量，如果存在name属性相同的配置，以最后的配置为准 -->
    <!--定义日志文件的存储地址，需要使用绝对路径-->
    <property name="LOG_HOME" value="/u1/logs" />

    <!-- 上下文名称 -->
    <contextName>${APPLICATION_NAME}</contextName>

    <!-- 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder"> 
            <!-- CONSOLE_LOG_PATTERN 一般使用defaults.xml文件中定义的即可，也可以自己定义 -->
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
            <charset>utf-8</charset>
        </encoder> 
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>debug</level>
        </filter>
        <!-- 字符串 System.out 或者 System.err，默认 System.out -->
        <target>System.out</target>
    </appender>

    <!-- 滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件。 -->
    <appender name="ALL_LOG"  class="ch.qos.logback.core.rolling.RollingFileAppender">   
        <!-- 正在记录的日志文件的路径及文件名 -->
        <file>${LOG_HOME}/${APPLICATION_NAME}/${APPLICATION_NAME}.log</file>
        <!-- true: 日志被追加到文件末尾，false: 清空现有文件重新开始。默认是true -->
        <append>true</append>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder"> 
            <!--格式化输出--> 
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%20.30thread] [%5level] %30.50logger{50} : %msg%n</pattern>   
            <charset>utf-8</charset>
        </encoder>
        <!-- 当发生滚动时，决定 RollingFileAppender 的行为，涉及文件移动和重命名 -->
        <!-- TimeBasedRollingPolicy： 最常用的滚动策略，它根据时间和文件大小来制定滚动策略。 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 必要节点，归档日志文件的路径和文件名-->
            <FileNamePattern>${LOG_HOME}/${APPLICATION_NAME}/${APPLICATION_NAME}-%d{yyyy-MM-dd}.%i.log</FileNamePattern>
            <!-- 告知 RollingFileAppender 何时激活滚动 -->
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>1024MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <!-- ThresholdFilter：日志级别临界值过滤器，过滤掉低于指定临界值的日志。 -->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <!-- INFO及以上级别的日志才会被记录 -->
            <level>info</level>
        </filter>
    </appender>

    <!-- 只记录错误日志 -->
    <appender name="ERROR_LOG"  class="ch.qos.logback.core.rolling.RollingFileAppender">   
        <file>${LOG_HOME}/${APPLICATION_NAME}/${APPLICATION_NAME}-error.log</file>
        <append>true</append>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder"> 
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [%5level] %class.%method [%L] : %msg%n</pattern>   
            <charset>utf-8</charset>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${LOG_HOME}/${APPLICATION_NAME}/${APPLICATION_NAME}-error-%d{yyyy-MM-dd}.%i.log</FileNamePattern> 
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>1024MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <!-- LevelFilter：日志级别过滤器，如果日志级别等于配置级别，过滤器会根据onMath 和 onMismatch接收或拒绝日志。 -->
        <!-- 过滤器的返回值只能是ACCEPT（事件被立即处理，不再经过剩余过滤器）、DENY（事件立即被抛弃，不再经过剩余过滤器）和NEUTRAL（有序列表里的下一个过滤器会接着处理记录事件）的其中一个 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <!-- 日志级别，这里只会记录ERROR级别的日志 -->
            <level>error</level>
            <!-- 符合过滤条件的操作 -->
            <onMatch>ACCEPT</onMatch>
            <!-- 不符合过滤条件的操作 -->
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

     <!--日志异步到数据库，还有其它很多Appender：SocketAppender、SMTPAppender、SyslogAppender、SiftingAppender -->  
    <appender name="DB" class="ch.qos.logback.classic.db.DBAppender">
        <!--日志异步到数据库 --> 
        <connectionSource class="ch.qos.logback.core.db.DriverManagerConnectionSource">
           <!--连接池 --> 
           <dataSource class="com.mchange.v2.c3p0.ComboPooledDataSource">
              <driverClass>com.mysql.jdbc.Driver</driverClass>
              <url>jdbc:mysql://127.0.0.1:3306/logbackExample</url>
              <user>root</user>
              <password>root</password>
            </dataSource>
        </connectionSource>
    </appender>

    <!-- 异步打印日志，这个东西吧，看着挺有用，实际存在性能问题，不建议使用，除非自己改写一下 -->
    <appender name="ASYNC_ALL_LOG" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="ALL_LOG" />
        <queueSize>500</queueSize>
        <discardingThreshold>0</discardingThreshold>
    </appender>

    <appender name="ASYNC_ERROR_LOG" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="ERROR_LOG" />
        <queueSize>500</queueSize>
        <discardingThreshold>0</discardingThreshold>
        <includeCallerData>true</includeCallerData>
    </appender>

    <!-- 根据Spring配置适配。源码：org.springframework.boot.logging.logback.SpringProfileAction -->
    <springProfile name="dev">
       <!-- 用来设置某一个包或者具体的某一个类的日志打印级别、以及指定<appender> --> 
        <logger name="com.apache.ibatis" level="TRACE"/>
        <logger name="java.sql.Connection" level="DEBUG"/>
        <logger name="java.sql.Statement" level="DEBUG"/>
        <logger name="java.sql.PreparedStatement" level="DEBUG"/>
        <root level="DEBUG">
            <appender-ref ref="STDOUT"/>
        </root>
    </springProfile>

    <springProfile name="pro">
        <!-- 如果addtivity=true，则会将打印信息传递到root；如果addtivity=false，则只会在<logger>中<appender-ref>打印信息，不会向上传递 -->
        <logger name="java.sql.PreparedStatement" level="DEBUG" addtivity="false" >
            <appender-ref ref="ALL_LOG" />
        </logger>
        <!-- root是一个特殊的logger，是根logger -->
        <root level="INFO">
            <appender-ref ref="ALL_LOG"/>
            <appender-ref ref="ERROR_LOG" />
            <appender-ref ref="DB"/>
        </root>
    </springProfile>

</configuration>
```

### 日志内置变量，与log4j的差不多：
 + %logger{length } 或 %lo{length } 或 %c{length }：日志logger名称（也可以看做是类名），如果超出长度限制，会使用缩写的形式打印。
 + %C{length} 或 %class{length}：类全限定名称，不建议使用。
 + %contextName 或 %cn：输出上下文名称。
 + %d{pattern} 或 $date{pattern}：时间。
 + %F 或 %file：java源文件名，不建议使用。
 + %L 或 %line：行号，不建议使用。
 + %m 或 %msg 或 %message：代码中需要打印的日志内容。
 + %M 或 %method：方法名，不建议使用。
 + %n：换行符，Windows是\r\n，Linux是\n。
 + %p 或 %le 或 %level：日志级别。
 + %r 或 %relative：程序启动到打印日志的时间，单位是毫秒。
 + %t 或 %thread：线程名称。
 + %replace(p){r, t}：p 为日志内容，r 是正则表达式，将p 中符合r 的内容替换为t 。例如替换空白字符："%replace(%msg){'\s', ''}"。
 + %X: X后面的是org.slf4j.MDC的键，表示在日志中打印键对应的值。例如：%X{traceId}，将打印存储在MDC中键为traceId对应的值信息。

#### 可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如:
 + %20c：指定最小的宽度是20个字符，如果少于20个字符的话，在左边补空格，右对齐。
 + %-20c：指定最小的宽度是20个字符，如果少于20个字符的话，在右边补空格，左对齐。
 + %.30c：指定最大的宽度是30个字符，如果多于30个字符的话，则会将左边多出的字符截掉，少于30个字符时也不会有空格。
 + %20.30c：如果少于20个字符就在左边补空格，右对齐，如果多于30个字符的话，则把左边多出的字符截掉。

### 参考资料：
  + [配置示例](https://www.cnblogs.com/zhangjianbing/p/8992897.html)
  + [logback介绍](https://www.cnblogs.com/lixuwu/p/5804793.html)
  + [logback配置分析(configuration)](https://www.cnblogs.com/lixuwu/p/5810912.html)
  + [logback配置分析(appender)](https://www.cnblogs.com/lixuwu/p/5811273.html)
  + [logback配置分析(filter)](https://www.cnblogs.com/lixuwu/p/5816814.html)
  + [安装彩色日志插件](https://blog.csdn.net/ruglcc/article/details/73844044)

