# PI - Sistema de Gestão

## Descrição
Este projeto é um sistema de gestão desenvolvido em **Java** com **Spring Boot** e segue a arquitetura **MVC**. O sistema tem funcionalidades para gerenciar usuários, produtos e carrinho de compras, além de contar com autenticação e autorização via **Spring Security**.

## Tecnologias Utilizadas
- **Java 11+**
- **Spring Boot 2.7.5**
- **Maven**
- **Thymeleaf**
- **Spring Security**
- **MySQL** (Gerenciado via **phpMyAdmin**)
- **Tailwind CSS** (Para estilização)
- **JavaScript** (Para interatividade no front-end)
- **Tomcat** (Servidor de aplicação)

## Estrutura do Projeto

```
src
┣ main
┃  ┣ java/com/pi/vip4
┃  ┃  ┣ config
┃  ┃  ┃  ┗ SecurityConfig.java    # Configuração de segurança do Spring Security
┃  ┃  ┣ controller
┃  ┃  ┃  ┣ LoginController.java          # Controlador para autenticação e login
┃  ┃  ┃  ┣ PainelController.java          # Controlador do painel administrativo
┃  ┃  ┃  ┣ ProdutoController.java      # Controlador para operações com produtos
┃  ┃  ┃  ┣ TesteController.java                          # Controlador de testes
┃  ┃  ┃  ┗ UserController.java      # Controlador para gerenciamento de usuários
┃  ┃  ┣ exception
┃  ┃  ┃  ┣ ErrorResponse.java                    # Modelo para respostas de erro
┃  ┃  ┃  ┣ GlobalExceptionHandler.java          # Manipulador global de exceções
┃  ┃  ┃  ┗ ResourceNotFoundException.java  # Exceção para recursos não encontrados
┃  ┃  ┣ model
┃  ┃  ┃  ┣ Carrinho.java                      # Representa o carrinho de compras
┃  ┃  ┃  ┣ Cliente.java                     # Modelo para informações do cliente
┃  ┃  ┃  ┣ ImgProduto.java            # Modelo para URLs de imagens dos produtos
┃  ┃  ┃  ┣ Produto.java                                      # Modelo de produto
┃  ┃  ┃  ┗ User.java                                         # Modelo de usuário
┃  ┃  ┣ repository
┃  ┃  ┃  ┣ ImgProdutoRepository.java  # Repositório para imagens de produtos
┃  ┃  ┃  ┣ ProdutoRepository.java  # Repositório de produtos
┃  ┃  ┃  ┗ UserRepository.java  # Repositório de usuários
┃  ┃  ┣ service
┃  ┃  ┃  ┣ CustomUserDetailsService.java  # Serviço de autenticação do usuário
┃  ┃  ┃  ┗ ProdutoService.java  # Regras de negócio para produtos
┃  ┃  ┣ validation
┃  ┃  ┃  ┣ CPFValid.java  # Anotação personalizada para validação de CPF
┃  ┃  ┃  ┗ CPFValidatorCaelum.java  # Implementação da validação de CPF
┃  ┃  ┗ Application.java  # Classe principal da aplicação
┃  ┣ resources
┃  ┃  ┣ static
┃  ┃  ┃  ┣ css/input.css  # Estilizações personalizadas
┃  ┃  ┃  ┣ js/alerts.js  # Scripts para exibir alertas
┃  ┃  ┃  ┣ uploads/  # Imagens armazenadas localmente
┃  ┃  ┣ templates
┃  ┃  ┃  ┣ fragments/menu.html  # Fragmento de menu
┃  ┃  ┃  ┣ create-produto-form.html  # Formulário de criação de produto
┃  ┃  ┃  ┣ edit-produto-form.html  # Formulário de edição de produto
┃  ┃  ┃  ┣ login.html  # Página de login
┃  ┃  ┃  ┣ painel.html  # Página do painel administrativo
┃  ┃  ┃  ┗ index.html  # Página inicial
┃  ┃  ┗ application.properties  # Configurações da aplicação
┃  ┗ test/java/com/staxrt/tutorial/ApplicationTests.java  # Teste básico
┣ target/  # Diretório gerado após build do projeto
┣ .gitignore  # Arquivo para ignorar arquivos desnecessários no Git
┣ pom.xml  # Configuração do Maven
┣ README.md  # Documentação do projeto
```

## Configuração e Execução

### Pré-requisitos
- **JDK 11+**
- **Maven**
- **MySQL**
- **Tomcat**

### Passos para rodar o projeto
1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   ```
2. Acesse a pasta do projeto:
   ```bash
   cd seu-repositorio
   ```
3. Configure o banco de dados no arquivo `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```
4. Instale as dependências:
   ```bash
   mvn clean install
   ```
5. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

A aplicação estará disponível em `http://localhost:8080/`.

## Licença
Este projeto está licenciado sob a **MIT License**. Consulte o arquivo `LICENSE` para mais detalhes.

---

Esse README fornece uma visão geral do projeto, suas tecnologias e instruções para configuração. Caso precise de mais detalhes ou ajustes, é só me avisar! 🚀

