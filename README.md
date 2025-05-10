# scf

### 🚀 Status do Projeto

> 🏗️ Em desenvolvimento — funcionalidades básicas sendo implementadas.

## 💰 Sistema de Controle Financeiro Pessoal

Este é o sistema backend de um controle financeiro pessoal. Desenvolvimento com linguagem Java e Framework Spring com objetivo de estudar as tecnologias envolvidas.

### 🎯 Objetivo

Permitir que o usuário acompanhe suas finanças com:

* Cadastro de contas (carteira, banco, etc.)
* Registro de receitas e despesas
* Classificação por categorias
* Consulta de saldo por período
* Relatórios básicos financeiros

### 🛠️ Tecnologias Utilizadas

* **Linux** Debian 12
* **Java 21+**
* **Spring Boot**
* **Spring Data JPA**
* **Spring Security + JWT**
* **PostgreSQL** (ou H2 para desenvolvimento)
* **Swagger/OpenAPI**
* **SonarQube**
* **Docker** ou **Podman**

### 📦 Funcionalidades

* Autenticação e registro de usuários
* CRUD de contas, categorias e transações
* Regras de negócio para atualização de saldo
* Relatórios por período e categoria
* API RESTful documentada com Swagger

### Swagger (documentação da API)

O projeto conta com Swagger que disponibiliza todas as APIs presente na aplicação.

Para visualizar, rode o projeto e acessa a URL: <i>http://localhost:8080/swagger-ui.html</i>

### Configuração

* **docker-compose**

Para o uso de algumas ferramentas (sonarqube, postgresql, etc) é importante ter o Docker instalado para que possamos subir de forma prática e fácil essas ferramenta através do docker-compose ou podman-compose.

Antes, vamos criar a rede para que o Banco de Dados e o Sistema se comuniquem, caso seja containerizado a aplicação com o profile PROD, a rede já esteja criada. Execute:

<i>docker network create scf-network</i> - para rodar no docker
ou
<i>podman network create scf-network</i> - para rodar no podman

Agora, para o docker compose, execute o comando abaixo:

<i>docker-compose up -d</i>
ou
<i>podman-compose up -d</i>

Ao rodar o comando <i>docker ps -a</i> verá na lista as seguintes ferramentas:
* **SonarQube**
* **PostgreSQL**

Garanta que todas iniciaram antes de seguir.

### SonarQube (profile:dev - desenvolvimento)

Acessível pela URL <i>http://localhost:9000</i>
Ao efetuar o login padrão (admin/admin), será solicitado a alteração da senha.

* Gerar Token:
Clique no seu avatar (canto superior direito) > My Account > Security
Gere um novo Token, ex: scf-token
Guarde esse token

* Rodando SonarQube

Via terminal, na raíz do projeto, rode o comando abaixo:

`mvn clean verify sonar:sonar \

  -Dspring.profiles.active=dev \

  -Dsonar.projectKey=scf \

  -Dsonar.host.url=http://localhost:9000 \

  -Dsonar.login=seu-token-aqui`

Após execução, acesse http://localhost:9000
Veja o projeto scf listado com métricas como:
    Cobertura de testes
    Bugs
    Vulnerabilidades
    Code smells
    Complexidade


### PostgreSQL (profile:prod - produção)

Após subir o PostgreSQL com o Docker, você pode acessá-lo de várias formas — tanto via terminal, cliente gráfico (como DBeaver, pgAdmin), quanto por linha de comando.

Execute o comando abaixo para entrar no container:

<i>docker exec -it scf-postgres bash</i>
ou
<i>podman exec -it scf-postgres bash</i>

Dentro dele:

<i>PGPASSWORD=postgresql123 psql -h localhost -U postgres -d finance-db</i>

Alguns comandos para executar após acessar o banco:

<i>\l</i> - Ver bancos disponíveis

<i>\dt</i> - Ver tabelas existentes

<i>SELECT * FROM nome_da_tabela;</i> - Faz um select na tabela especificada

<i>\q</i> - Sair do postgreSQL

### Dockerfile

Caso queira rodar o aplicativo no Docker ou Podman, pode ser criada a imagem via Dockerfile.

No arquivo <i>application.properties</i> altere a propriedade <i>spring.profiles.active</i> para <i>prod</i>.
Via terminal, na raíz do projeto, execute o comando que irá executar o script Dockerfile, realizando a criação da imagem da aplicação:

<i>podman build -t scf-app .</i>

Agora, vamos subir a aplicação para o rodar na porta 8080 e na rede scf-network:

<i>podman run -p 8080:8080 --network scf-network scf-app</i>

Pronto, a aplicação estará sendo executada e os logs exibidos no terminal.

Acesse o swagger (http://localhost:8080/swagger-ui/index.html)  para visualizar as APIs ou o Actuator (http://localhost:8080/actuator/health/) para verificar a saúde da aplicação.

Para finalizar aperte </i>CTRL + C</i> simultaneamente. 