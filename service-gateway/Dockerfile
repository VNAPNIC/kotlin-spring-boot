FROM openjdk:8-jre-alpine
ADD ./target/service-com.vnapnic.discovery-1.0.0.jar service-com.vnapnic.discovery-1.0.0.jar
EXPOSE 8762
ENTRYPOINT ["java","-jar","-Dfile.encoding=UTF-8","/service-com.vnapnic.discovery-1.0.0.jar"]