# heroes-api
FROM heroes/jdk8-centos7:latest

COPY heroes-api-1.0.0.jar $HOME

CMD java -jar $HOME/heroes-api-1.0.0.jar