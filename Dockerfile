FROM adoptopenjdk/openjdk8:ubi
WORKDIR /home/
COPY target/*.jar  ./
RUN rm -f *-sources.jar

ENV JAVA_OPTS="-Xms256m -Xmx256m  -XX:+ExitOnOutOfMemoryError -Dlog4j2.formatMsgNoLookups=true"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar *.jar" ]