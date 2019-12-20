# MUMEAProject
Altimetrik Playground
I used Spring Family to build the project which support centrical config, load balance and fault tolerance
please start as following steps: (it's better to import the project in STS)
1. config-service 
2. discovery-service
3. gateway-service
4. vincheckerMicroService
5. VinUI
6. springboot-admin-ea

you can use swagger to invoke the API as well. input the vin number
http://localhost:3211/swagger-ui.html#/

```
vincheckerMicroService and VinUI are the workshop project

you could start mysql instance, or remove following configuration in config project
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ea-project?useSSL=false&serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    database-platform: org.hibernate.dialect.MySQL5Dialect
    hibernate:
      ddl-auto: update
  boot:
    admin:
      client:
        url: http://localhost:8180
```
