# Place the MySQL JDBC driver here

This application needs the MySQL Connector/J JAR on the classpath.

**Add the driver:** copy `mysql-connector-java-8.0.30.jar` (or any
MySQL Connector/J 8.x jar) into this folder:

```
src/main/webapp/WEB-INF/lib/mysql-connector-java-8.0.30.jar
```

Eclipse's *Web App Libraries* container picks up every jar in this
folder automatically for both compilation and deployment, so no
`.classpath` edit is required.

> Tip: you already have this jar in your Tomcat `lib/` folder
> (`apache-tomcat-9.0.x/lib/mysql-connector-java-8.0.30.jar`).
> Just copy it here to make the project self-contained and portable.
