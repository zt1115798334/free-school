FROM java:8
MAINTAINER zt
VOLUME /tmp
ADD /target/school-app-0.0.1-SNAPSHOT.jar school-app-0.0.1-docker.jar
RUN export LC_ALL=zh_CN.UTF-8
RUN echo "export LC_ALL=zh_CN.UTF-8"  >>  /etc/profile
RUN echo "Asia/shanghai" > /etc/timezone
RUN bash -c 'touch school-app-0.0.1-SNAPSHOT.jar'
EXPOSE 8081
ENTRYPOINT ["nohup","java","-jar","school-app-0.0.1-docker.jar"," > /dev/null 2>&1 &"]
