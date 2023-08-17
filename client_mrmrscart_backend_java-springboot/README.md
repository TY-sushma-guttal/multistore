# MrMrsCart - Ecommerce based application

## Project is built using Java v8.x Sprint boot v2.6.x

## Project follows Micro services architecture 

## List of Services 
1. help-and-support-service
2. notification-report-log-service
3. orders-payments-service
4. product-categories-service 
5. auth-service
6. User Service 



## Configuration Details 

1. Location Transparency, Naming Server - Eureka
2. API Gateway - Spring Cloud Gateway 
3. Centralized Configuration Management - Spring Cloud config 
4. Load Balancer- Spring Cloud Load Balancer
5. Fault tolerance -Resilience4j
6. Visibility and Monitoring,Tracing - Zipkin with RabbitMQ
7. API Doc - Swagger OpenAPI 
8. Metrics - Spring Actuators for Dev,QA ENV
9. Rest Invoke - feign Client

## Application Port

1. Netflix Eureka Naming Server 8761
2. API Gateway 8765
3. Zipkin Distributed Tracing Server 9411
4. Spring Cloud Config Server 8888
5. Auth Microservice 8000, 8001, 8002, ..
6. Product Microservice 8100, 8101, 8102, ...
7. help-and-support-service 8200, 8201, 8202, ...
8. orders-payments-service 8300, 8301, 8302, ...
9. notification-report-log-service 8400, 8401, 8402, ...
10. User Service 8500, 8501, 8502, ...


## Configuration URLS

1. Eureka - http://localhost:8761
2. API GATEWAY - http://localhost:8765/
3. Zipkin - http://localhost:9411/


## What Is an Actuator?
In essence, Actuator brings production-ready features to our application.

Monitoring our app, gathering metrics, understanding traffic, or the state of our database become trivial with this dependency.

The main benefit of this library is that we can get production-grade tools without having to actually implement these features ourselves.

Actuator is mainly used to expose operational information about the running application — health, metrics, info, dump, env, etc. It uses HTTP endpoints or JMX beans to enable us to interact with it.

Getting Started To enable Spring Boot Actuator, we just need to add the spring-boot-actuator dependency to our package manager.

```
/auditevents lists security audit-related events such as user login/logout. Also, we can filter by principal or type among other fields.
/beans returns all available beans in our BeanFactory. Unlike /auditevents, it doesn't support filtering.
/conditions, formerly known as /autoconfig, builds a report of conditions around autoconfiguration.
/configprops allows us to fetch all @ConfigurationProperties beans.
/env returns the current environment properties. Additionally, we can retrieve single properties.
/flyway provides details about our Flyway database migrations.
/health summarizes the health status of our application.
/heapdump builds and returns a heap dump from the JVM used by our application.
/info returns general information. It might be custom data, build information or details about the latest commit.
/liquibase behaves like /flyway but for Liquibase.
/logfile returns ordinary application logs.
/loggers enables us to query and modify the logging level of our application.
/metrics details metrics of our application. This might include generic metrics as well as custom ones.
/prometheus returns metrics like the previous one, but formatted to work with a Prometheus server.
/scheduledtasks provides details about every scheduled task within our application.
/sessions lists HTTP sessions given we are using Spring Session.
/shutdown performs a graceful shutdown of the application.
/threaddump dumps the thread information of the underlying JVM.

```

In Maven:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

```
In Application.properties
```
management.endpoints.web.exposure.include=*

```
To access:

```
/health
/info
/metrics

```


## HAL explorer

Visualising with HAL explorer HAL and the HAL Browser JSON Hypertext Application Language, or HAL, is a simple format that gives a consistent and easy way to hyperlink between resources in our API. Including HAL within our REST API makes it much more explorable to users as well as being essentially self-documenting.

In Maven
```
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-rest-hal-explorer</artifactId>
    <version>3.4.1.RELEASE</version>
</dependency>

```

To access:

```

http://localhost:8080/

http://localhost:8080/explorer/index.html#
```
