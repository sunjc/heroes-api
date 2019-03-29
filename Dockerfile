# heroes-api-centos7
FROM centos:latest

RUN yum -y update && yum clean all

# Set the labels that are used for OpenShift to describe the builder image.
LABEL maintainer="Sun Jingchuan <jason@163.com>" \
      io.k8s.description="Heroes API" \
      io.k8s.display-name="Heroes API" \
      io.openshift.expose-services="8080:http" \
      io.openshift.tags="spring-boot,heroes-api" \
      # this label tells s2i where to find its mandatory scripts(run, assemble, save-artifacts)
      # io.openshift.s2i.scripts-url="image:///usr/libexec/s2i" \
      io.openshift.s2i.scripts-url="image:///tmp/scripts" \
      io.openshift.s2i.destination="/tmp"

ENV JAVA_HOME=/usr/lib/jdk1.8.0_202 \
    MAVEN_HOME=/usr/lib/apache-maven-3.6.0 \
    APP_ROOT=/opt/heroes
ENV PATH=${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${APP_ROOT}/bin:${PATH} HOME=${APP_ROOT}

# Include jdk and maven in lib
COPY lib /usr/lib
COPY bin ${APP_ROOT}/bin
# Copy the S2I scripts to /usr/libexec/s2i
# COPY .s2i/bin /usr/libexec/s2i

RUN chmod -R u+x ${APP_ROOT}/bin && \
    chgrp -R 0 ${APP_ROOT} && \
    chmod -R g=u ${APP_ROOT} /etc/passwd

USER 10001
WORKDIR ${APP_ROOT}

ENTRYPOINT [ "uid_entrypoint" ]

EXPOSE 8080

# Inform the user how to run this image.
# CMD ["/usr/libexec/s2i/usage"]