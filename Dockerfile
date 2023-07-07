FROM alpine:latest

LABEL maintainer="nalajalaravi99@gmail.com"

# JDK Installation
RUN apk update && apk add --no-cache openjdk11 curl tar && mkdir /opt/tomcat && rm /var/cache/apk/*

#Setting JAVA PATH
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk
ENV PATH=$PATH:$JAVA_HOME/bin

#Tomcat Installation
RUN curl -O https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.10/bin/apache-tomcat-10.1.10.tar.gz 

#Extract the tomcat
RUN tar xvzf apache-tomcat-10.1.10.tar.gz --strip-components 1 --directory /opt/tomcat

#Copy war file to webapps
COPY target/*.war /opt/tomcat/webapps/hello.war

EXPOSE 8080

#Run the tomcat
CMD [ "/opt/tomcat/bin/catalina.sh", "run" ]
