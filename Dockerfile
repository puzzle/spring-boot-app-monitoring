FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift

COPY . /tmp/src

RUN [ -s /tmp/src/app.jar ] && mvn dependency:resolve

RUN mkdir /deployment; \
  if [ -s /tmp/src/app.jar ]; then \
    mvn package \
    cp target/app.jar /deployment \
  else \
    cp /tmp/src/app.jar /deployment \
  fi \
  rm -rf /tmp/src

CMD java -jar /deployment/app.jar
