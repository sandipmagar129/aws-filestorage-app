FROM maven as build 
MAINTAINER Vaibhav

WORKDIR /awsdemo
COPY . .

RUN mvn package 


FROM java:8-jre-alpine

WORKDIR /

COPY --from=build /awsdemo/target/awsdemo*.jar app

EXPOSE 8080 

CMD java -jar app 