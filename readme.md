# 📦 Sistema de Cadastro de Produtos

Projeto backend desenvolvido com **Spring Boot**, utilizando **Maven**, **Java 21** e banco de dados **MySQL**. A aplicação oferece uma API RESTful com endpoint para cadastro de produtos.

---

## 🚀 Tecnologias Utilizadas

![Java](https://img.shields.io/badge/Java-21-blue?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen?logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8-blue?logo=mysql&logoColor=white)

- [Java 21](https://www.oracle.com/java/)
- [Spring Boot 3.5.3](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MySQL](https://www.mysql.com/)
- [Bean Validation (JSR-380)](https://beanvalidation.org/)
- [SpringDoc OpenAPI](https://springdoc.org/)

---

## 📌 Funcionalidades

- ✅ Cadastro de produtos via requisição **POST** no endpoint `/api/produtos`
- ✅ Validação de dados de entrada
- ✅ Integração com banco de dados MySQL
- ✅ Documentação automática com Swagger (OpenAPI)

---

## 📁 Estrutura do Projeto

src/
├── main/
│ ├── java/
│ │ └── br/com/e2etreinamentos/meu/sistema/
│ │ ├── controller/
│ │ ├── model/
│ │ ├── repository/
│ │ └── MeuSistemaApplication.java
│ └── resources/
│ ├── application.properties
│ └── static/
└── test/



---

## 🔗 Endpoint da API

### POST `/api/produtos`

Cadastra um novo produto no sistema.


## 📚 Documentação da API

http://localhost:8080/swagger-ui.html



## 🧪 Testes
Execute os testes com:
mvn test


## 🛠 Requisitos
Java 21

Maven 3.9+

MySQL 8+

