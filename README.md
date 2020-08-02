# desafio-b2w

[![Build Status](https://travis-ci.org/codecentric/springboot-sample-app.svg?branch=master)](https://travis-ci.org/codecentric/springboot-sample-app)

Desafio de back-end da B2W com [Spring Boot](http://projects.spring.io/spring-boot/).

## Requerimentos

Para buildar e rodar a aplicação é preciso:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Rodando a aplicação localmente

Existem várias maneiras de executar um aplicativo Spring Boot em sua máquina local. Uma maneira é executar o método `main` na classe` DesafioB2wApplication` do IDE.

Como alternativa, você pode usar o [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) como:

```shell
mvn spring-boot:run
```
Uma outra alternativa é, via linha de comando, acessar a pasta do projeto e executar mvn clean package.  Este comando irá empacotar a aplicação como um arquivo .jar.  Em seguida,
acessar a pasta target dentro do projeto e executar o comando java -jar desafio-b2w-0.0.1-SNAPSHOT.jar.
