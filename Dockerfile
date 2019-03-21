FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift

SHELL ["/usr/bin/scl", "enable", "rh-maven35"]

WORKDIR /tmp/src

COPY . /tmp/src

USER 0

RUN if [ ! -s /tmp/src/app.jar ]; then \
      mvn dependency:resolve; \
    fi

RUN mkdir /deployment; \
    if [ -s /tmp/src/app.jar ]; then \

      mvn package; \
      cp target/app.jar /deployment; \
    else \
      cp /tmp/src/app.jar /deployment; \
    fi; \
    rm -rf /tmp/src

EXPOSE 8080

USER 1001

CMD java -jar /deployment/app.jar
