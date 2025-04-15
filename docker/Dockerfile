FROM openjdk:21

MAINTAINER tinycloud

# 设置其他额外参数，用于直接传递springboot的启动参数给容器
ENV PARAMS=""

# 设置默认的 JDK 启动参数
ENV JAVA_OPTS="-Xms256m -Xmx512m"

ADD target/tiny-mock-*.jar /app.jar
ADD config/application.yml config/application.yml

# 更新时区
RUN sh -c 'touch $jarFile; ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime; echo "Asia/Shanghai" > /etc/timezone'

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /app.jar --spring.config.location=/config/application.yml $PARAMS"]