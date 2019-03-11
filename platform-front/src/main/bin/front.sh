#!/bin/bash
#
# ayo,this is uncle.quentin,dj drop the beat

APP_NAME=platform-front
APP_HOME=`pwd`
CONFIG_DIR=$APP_HOME/../config/
RUNNING_USER=root
ADATE=`date +%Y%m%d%H%M%S`


dirname $0|grep "^/" >/dev/null
if [ $? -eq 0 ];then
   APP_HOME=`dirname $0`
else
    dirname $0|grep "^\." >/dev/null
    retval=$?
    if [ $retval -eq 0 ];then
        APP_HOME=`dirname $0|sed "s#^.#$APP_HOME#"`
    else
        APP_HOME=`dirname $0|sed "s#^#$APP_HOME/#"`
    fi
fi

if [ ! -d "$APP_HOME/logs" ];then
  mkdir $APP_HOME/logs
fi

LOG_PATH=$APP_HOME/logs/$APP_NAME.log
GC_LOG_PATH=$APP_HOME/logs/gc-$APP_NAME-$ADATE.log
#JVM参数
JVM_OPTS="-server -Xms512M -Xmx512M -Xmn512m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=256m"
JVM_OPTS="${JVM_OPTS} -XX:-OmitStackTraceInFastThrow"

JAR_FILE=$APP_HOME/../$APP_NAME.jar
pid=0
start(){
  checkpid
  if [ ! -n "$pid" ]; then
    JAVA_CMD="nohup java -Xbootclasspath/a:${CONFIG_DIR} -jar $JVM_OPTS $JAR_FILE --spring.config.location=${CONFIG_DIR} > $LOG_PATH 2>&1 &"
    su - $RUNNING_USER -c "$JAVA_CMD"
    echo "---------------------------------"
    echo "admin启动完成，按CTRL+C退出日志界面即可😊😊😊"
    echo "---------------------------------"
  else
      echo "$APP_NAME is runing PID: $pid"   
  fi

}


status(){
   checkpid
   if [ ! -n "$pid" ]; then
     echo "$APP_NAME not runing"
   else
     echo "$APP_NAME runing PID: $pid"
   fi 
}

checkpid(){
    pid=`ps -ef |grep $JAR_FILE |grep -v grep |awk '{print $2}'`
}

stop(){
    checkpid
    if [ ! -n "$pid" ]; then
     echo "$APP_NAME not runing"
    else
      echo "$APP_NAME stop..."
      kill -9 $pid
    fi 
}

restart(){
    stop 
    sleep 1s
    start
}

case $1 in  
          start) start;;  
          stop)  stop;; 
          restart)  restart;;  
          status)  status;;   
              *)  echo "require start|stop|restart|status"  ;;  
esac 