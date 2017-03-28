FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/sshop-demo.jar /sshop-demo/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/sshop-demo/app.jar"]
