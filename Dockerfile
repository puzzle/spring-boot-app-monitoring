FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift

SHELL ["/usr/bin/scl", "enable", "rh-maven35"]

WORKDIR /tmp/src

COPY . /tmp/src

USER 0

RUN if [ ! -s /tmp/src/app.jar ]; then \
      mvn package; \
      cp target/app.jar /home/jboss; \
    else \
      cp /tmp/src/app.jar /home/jboss; \
    fi; \
    rm -rf /tmp/src

USER 1001

EXPOSE 8080

CMD java -jar /home/jboss/app.jar
