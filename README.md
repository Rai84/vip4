# 📌 Documentação do Projeto PI

## 📂 1. Visão Geral do Projeto  
**Nome do Projeto:** FiveMarket
**Descrição:**  Sistema de compra e administração de um mercado.
**Tecnologias utilizadas:**  
- **Back-end:** Java 11, Spring Boot 2.7.5, Maven  
- **Front-end:** Thymeleaf com tailwind
- **Banco de dados:** MySQL (Gerenciado pelo phpMyAdmin)  
- **Servidor:** Tomcat  

---

## 📑 2. Estrutura do Projeto  
   ### 📂 Pacotes principais  
   - `com.pi.vip4.controller` → Controladores (Gerenciam as requisições HTTP)  
   - `com.pi.vip4.model` → Modelos das entidades (Representa as classes de dados)  
   - `com.pi.vip4.repository` → Classes de acesso ao banco de dados (Interação com o banco usando JPA)  
   - `com.pi.vip4.service` → Regras de negócio (Lógica da aplicação e serviços)
    
   ### 📂 Estrutura de Diretórios do Projeto

    -`📂 vip4 `
    -` ┣ 📂 config`           # Configurações de segurança e aplicação
    -` ┣ 📂 controller`       # Controladores que gerenciam as requisições
    -` ┣ 📂 exception`        # Tratamento de exceções
    -` ┣ 📂 model`            # Modelos de dados (entidades)
    -` ┣ 📂 repository`       # Repositórios para interação com o banco de dados
    -` ┣ 📂 service`          # Serviços de negócios (lógica de aplicação)
       ┣ 📂 validation         # Novo pacote para validações personalizadas
    -` ┗ 📜 Application.java` # Classe principal do Spring Boot

    -`📂 resources`
    -` ┣ 📂 static`
    -` ┃  ┣ 📂 css`                 # Arquivos CSS
    -` ┃  ┗ 📂 js`                  # Arquivos JavaScript
    -` ┣ 📂 templates`              # Templates do Thymeleaf
    -` ┃  ┣ 📂 fragments`           # Fragmentos reutilizáveis de HTML (ex: menu)
    -` ┃  ┗ 📜 htmls`               # Arquivos HTML para as páginas do aplicativo
    -` ┗ 📜 application.properties` # Arquivo de configuração do Spring Boot



📌 **Fluxo de dados:**  
Usuário → Controller → Service → DAO → Banco de Dados  

---

## 📦 3. Dependências do `pom.xml`  
Aqui estão as principais dependências utilizadas no projeto e suas versões:  

```xml
<dependencies>
    <!-- Spring Boot Starter Web (Controladores REST) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>2.7.5</version>
    </dependency>

    <!-- Spring Boot Starter Thymeleaf (Templates HTML) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
        <version>2.7.5</version>
    </dependency>

    <!-- Spring Boot Starter Data JPA (Acesso ao Banco) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
        <version>2.7.5</version>
    </dependency>

    <!-- Driver MySQL -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>

    <!-- Spring Boot Starter Test (Testes) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <version>2.7.5</version>
        <scope>test</scope>
    </dependency>

    <!-- Lombok (Elimina código boilerplate) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.26</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
