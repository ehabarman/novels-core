# TODO: Still need to work on this shit, can't reach to database in localhost and maybe has other issues yet to be discovered

FROM maven:3.8.1-openjdk-15-slim as build
LABEL maintainer="ehab arman"

# Build layer
ARG SERVICE_NAME="novelCore"
ARG GIT_COMMIT
ARG ENVIRONEMNT="dev"
ARG WORKDIR="/novel-core"

LABEL service=${SERVICE_NAME} \
      stage=build \
      tag=${GIT_COMMIT} \
      env=${ENVIRONEMNT}

RUN mkdir ${WORKDIR}
WORKDIR ${WORKDIR}

COPY ./pom.xml ./pom.xml
COPY ./src ./src
COPY ./src/main/resources ./

RUN  mvn package && mv ./target/novel-core-0.0.1.jar ./${SERVICE_NAME}.jar


FROM maven:3.8.1-openjdk-15-slim
LABEL maintainer="Ehab Arman"

# Deployment layer
ARG SERVICE_NAME="novelCore"
ARG GIT_COMMIT
ARG ENVIRONEMNT="dev"
ARG WORKDIR="/novel-core"

LABEL service=${SERVICE_NAME} \
      stage=build \
      tag=${GIT_COMMIT} \
      env=${ENVIRONEMNT}

RUN groupadd -g 1001 novel && adduser -u 1001 --gid 1001 novel &&  mkdir -p ${WORKDIR} && chown -R novel:novel ${WORKDIR}


WORKDIR ${WORKDIR}

COPY --chown=novel:novel --from=build ${WORKDIR}/${SERVICE_NAME}.jar ./${SERVICE_NAME}.jar
USER novel
EXPOSE 6969

ENTRYPOINT ["java","-jar","./novelCore.jar"]