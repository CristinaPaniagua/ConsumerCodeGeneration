# Arrowhead Client Skeletons (Java Spring-Boot)
##### The project provides client skeletons for the Arrowhead Framework 4.1.3

These skeletons are to be extended with your own application code.

### Requirements

The project has the following dependencies:
* JRE/JDK 11 [Download from here](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
* Maven 3.5+ [Download from here](http://maven.apache.org/download.cgi) | [Install guide](https://www.baeldung.com/install-maven-on-windows-linux-mac)

### Project structure

This is a multi module maven project relying on the [parent `pom.xml`](https://github.com/arrowhead-f/client-java-spring/blob/master/pom.xml) which lists all the modules and common dependencies.

* **client-skeleton-common**: a common library module for the other maven modules. Contains all the data transfer objects, an 'ArrowheadService' class with the purpose of easily interacting with the framework and also the common arrowhead dependencies.

* **client-skeleton-consumer**: client skeleton module with the purpose of initiating an orchestration request and consume the service from the chosen provider. This consumer project also contains a simple example about how to orchestrate and consume the service afterward.

* **client-skeleton-provider**: client skeleton module with the purpose of registrating a specific service into the Service Registry and running a web server where the service is available.

Both client skeleton have a default 'ApplicationInitListener' and a defult 'SecurityConfig' what you can change or extend. The essential configuration has to be managed by customizing the `application.properties` file, located in `src/main/resources` folder.
