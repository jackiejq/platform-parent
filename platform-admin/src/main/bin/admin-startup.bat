rem ======================================================================
rem windows启动脚本
rem
rem author: uncle.quentin
rem date: 2018-12-15
rem ======================================================================
rem Open in a browser
rem start "" "http://localhost:8084/swagger-ui.html"
rem --logging.config=../config/log4j2.xml
rem startup jar
javaw -Xbootclasspath/a:../config/ -jar ../boot/platform-admin.jar --spring.config.location=../config/

pause