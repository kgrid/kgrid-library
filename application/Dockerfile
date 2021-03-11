FROM openjdk:11

MAINTAINER KGrid Team "kgrid-developers@umich.edu"

ENV KGRID_CONFIG=""

WORKDIR /home/kgrid
ARG JAR_FILE

COPY target/${JAR_FILE} .

EXPOSE 8080
CMD java -jar kgrid-library*.jar $KGRID_CONFIG