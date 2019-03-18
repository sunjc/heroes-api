# heroes-api-centos7
FROM centos:latest

RUN yum -y update && yum -y clean all

COPY lib /usr/lib
COPY .m2 /root/.m2

ENV JAVA_HOME=/usr/lib/jdk1.8.0_202 \
    MAVEN_HOME=/usr/lib/apache-maven-3.6.0 \
    PATH=/usr/lib/jdk1.8.0_202/bin:/usr/lib/apache-maven-3.6.0/bin:$PATH

# Here you can specify the maintainer for the image that you're building
LABEL maintainer="Sun Jingchuan <jason@163.com>"

# Set the labels that are used for OpenShift to describe the builder image.
LABEL io.k8s.description="Heroes API" \
    io.k8s.display-name="Heroes API" \
    io.openshift.expose-services="8080:http" \
    io.openshift.tags="spring boot" \
    # this label tells s2i where to find its mandatory scripts(run, assemble, save-artifacts)
    io.openshift.s2i.scripts-url="image:///usr/libexec/s2i" \
    io.openshift.s2i.destination="/tmp"

# Copy the S2I scripts to /usr/libexec/s2i since we set the label that way
COPY .s2i/bin /usr/libexec/s2i

# Set the default port for applications built using this image
EXPOSE 8080

# Inform the user how to run this image.
CMD ["/usr/libexec/s2i/usage"]