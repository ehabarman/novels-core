# novels-core

## Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.2/maven-plugin/reference/html/#build-image)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#howto-use-exposing-spring-data-repositories-rest-endpoint)
* [OAuth2 Client](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-security-oauth2-client)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-mongodb)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [JDBC API](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-sql)
* [Spring Data JDBC](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-security)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-developing-web-applications)
* [Jersey](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-jersey)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#using-boot-devtools)
* [Spring Web Services](https://docs.spring.io/spring-boot/docs/2.4.2/reference/htmlsingle/#boot-features-webservices)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Neo4j Data with REST](https://spring.io/guides/gs/accessing-neo4j-data-rest/)
* [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing Relational Data using JDBC with Spring](https://spring.io/guides/gs/relational-data-access/)
* [Managing Transactions](https://spring.io/guides/gs/managing-transactions/)
* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Producing a SOAP web service](https://spring.io/guides/gs/producing-web-service/)

### Build docker image
Use command "*docker image build -t novel-core .*"
To run image in container use "*docker container run --name novel-core -p 6969:6969 -d novel-core*"
To show logs use "*docker container logs novel-core*"