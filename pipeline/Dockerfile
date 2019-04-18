# jdk8-centos7
FROM centos:latest

RUN yum -y update && yum clean all

# Set the labels that are used for OpenShift to describe the builder image.
LABEL maintainer="Sun Jingchuan <jason@163.com>" \
      io.k8s.description="Oracle JDK 1.8.0_202 based on CentOS 7" \
      io.k8s.display-name="Oracle JDK 1.8.0_202" \
      io.openshift.expose-services="8080:http" \
      io.openshift.tags="jdk8"

ENV JAVA_HOME=/usr/lib/jdk1.8.0_202 \
    APP_ROOT=/opt/app-root
ENV PATH=${JAVA_HOME}/bin:${APP_ROOT}/bin:${PATH} HOME=${APP_ROOT}

COPY lib/jdk1.8.0_202 ${JAVA_HOME}
COPY bin ${APP_ROOT}/bin

RUN chmod -R u+x ${APP_ROOT}/bin && \
    chgrp -R 0 ${APP_ROOT} && \
    chmod -R g=u ${APP_ROOT} /etc/passwd

USER 10001
WORKDIR ${APP_ROOT}

ENTRYPOINT [ "uid_entrypoint" ]

EXPOSE 8080