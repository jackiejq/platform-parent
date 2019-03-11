#360jr
### admin 打包以及启动
*  mvn clean package -P prod(prod代表生产环境)

*  在target目录找到生成的包文件（本项目可生产zip或tar.gz包）

*  解压包后进入并切换至bin文件目录，windows运行.bat脚本，Linux运行.sh脚本

*  Linux脚本命令说明：
    ```bash
    启动服务: sh admin.sh start
    ``` 
    ```bash
    停止服务: sh admin.sh stop
    ``` 
    ```bash
    重启服务: sh admin.sh restart
    ``` 
    ```bash
    查看状态: sh admin.sh status
    ``` 
*  Linux脚本配置说明：
 > -  配置项目名称：APP_NAME=platform-admin
 > -  JVM参数：
   ```bash
         JVM_OPTS="-server -Xms512M -Xmx512M -Xmn512m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=256m"
         JVM_OPTS="${JVM_OPTS} -XX:-OmitStackTraceInFastThrow"
   ```
 > - 启动方式：指定JVM参数→启动jar位置→配置文件路径→重定向日志
   ```bash
     nohup java -Xbootclasspath/a:${CONFIG_DIR} -jar $JVM_OPTS $JAR_FILE --spring.config.location=${CONFIG_DIR} > $LOG_PATH 2>&1 &
   ```

 