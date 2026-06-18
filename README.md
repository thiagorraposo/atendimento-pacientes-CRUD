# Atendimento de Pacientes - CRUD Web

Sistema web academico para gerenciamento de pacientes e seus atendimentos.

O projeto permite cadastrar, listar, editar, excluir e consultar pacientes, alem de registrar atendimentos vinculados a cada paciente. As telas foram desenvolvidas com JSF / Jakarta Faces, a persistencia usa JPA com Spring Data JPA, o banco de dados utilizado e PostgreSQL, o relatorio geral e gerado por um Servlet proprio e o gerenciamento do projeto e feito com Maven.

Spring Boot e utilizado como apoio para configuracao, execucao, injecao de dependencia e integracao das tecnologias.

## Tecnologias

- Java 17
- Maven
- Spring Boot
- JSF / Jakarta Faces
- JPA / Spring Data JPA
- PostgreSQL
- Servlet
- HTML/XHTML
- CSS
- Git

## Arquitetura MVC

O projeto segue a organizacao MVC, separando responsabilidades entre visualizacao, controle, regras de negocio e persistencia.

- **View**: paginas `.xhtml` localizadas em `src/main/resources/META-INF/resources` e a pagina inicial `src/main/webapp/index.xhtml`.
- **Controller**: Beans JSF no pacote `br.com.thiago.atendimentopacientes.controller`, responsaveis por receber acoes da View e chamar os Services.
- **Model**: entidades JPA no pacote `br.com.thiago.atendimentopacientes.model`, como `Paciente` e `Atendimento`.
- **Service**: classes no pacote `br.com.thiago.atendimentopacientes.service`, onde ficam validacoes, regras de negocio e chamadas aos repositories.
- **Repository**: interfaces no pacote `br.com.thiago.atendimentopacientes.repository`, responsaveis pela persistencia com Spring Data JPA.
- **Servlet**: Servlet proprio no pacote `br.com.thiago.atendimentopacientes.servlet`, usado para gerar um relatorio auxiliar por requisicao HTTP direta.

Fluxo principal:

```text
View XHTML -> Bean JSF -> Service -> Repository -> Banco PostgreSQL
```

Fluxo do relatorio:

```text
Servlet -> Service -> Repository -> Banco PostgreSQL
```

## Funcionalidades

- Cadastro de pacientes
- Listagem de pacientes
- Edicao de pacientes
- Exclusao de pacientes
- Busca de pacientes por nome
- Cadastro de atendimentos
- Listagem de atendimentos
- Edicao de atendimentos
- Exclusao de atendimentos
- Filtro de atendimentos por status
- Relatorio geral via Servlet
- Navegacao padronizada entre as telas
- Mensagens de sucesso e erro
- Confirmacao antes de excluir registros

## Configuracao do Banco PostgreSQL

O projeto esta configurado para usar o banco `atendimento_pacientes_crud` no PostgreSQL.

Comandos SQL sugeridos:

```sql
CREATE DATABASE atendimento_pacientes_crud;

CREATE USER atendimento_user WITH PASSWORD '123456';

GRANT ALL PRIVILEGES ON DATABASE atendimento_pacientes_crud TO atendimento_user;
```

Depois de conectar no banco criado, conceda permissoes no schema publico:

```sql
\c atendimento_pacientes_crud

GRANT ALL ON SCHEMA public TO atendimento_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO atendimento_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO atendimento_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL PRIVILEGES ON TABLES TO atendimento_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL PRIVILEGES ON SEQUENCES TO atendimento_user;
```

As configuracoes de conexao ficam em `src/main/resources/application.properties`.

Configuracao padrao esperada:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/atendimento_pacientes_crud
spring.datasource.username=atendimento_user
spring.datasource.password=123456
```

O arquivo tambem aceita variaveis de ambiente:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/atendimento_pacientes_crud
export DB_USERNAME=atendimento_user
export DB_PASSWORD=123456
```

## Como Rodar

Compile e instale as dependencias:

```bash
mvn clean install
```

Execute a aplicacao:

```bash
mvn spring-boot:run
```

Por padrao, o sistema sobe na porta `8080`.

## URLs Principais

- Pagina inicial: `http://localhost:8080`
- Pacientes: `http://localhost:8080/pacientes/listar.xhtml`
- Atendimentos: `http://localhost:8080/atendimentos/listar.xhtml`
- Relatorio do sistema: `http://localhost:8080/relatorio-sistema`

## Estrutura de Pastas

```text
src/main/java/br/com/thiago/atendimentopacientes
├── controller
├── exception
├── model
├── repository
├── service
└── servlet

src/main/resources/META-INF/resources
├── atendimentos
├── pacientes
└── resources/css

src/main/webapp
└── index.xhtml
```

Descricao dos principais pacotes:

- `model`: entidades JPA do sistema.
- `repository`: interfaces Spring Data JPA para acesso ao banco.
- `service`: regras de negocio, validacoes e controle de transacoes.
- `controller`: Beans JSF usados pelas telas XHTML.
- `servlet`: Servlets proprios do projeto, como o relatorio geral.
- `exception`: excecoes de regra de negocio.
- `resources/META-INF/resources`: telas JSF, arquivos XHTML e recursos estaticos.

## Papel de Cada Tecnologia

- **Java 17**: linguagem principal do projeto.
- **Maven**: gerenciamento de dependencias, build e execucao.
- **Spring Boot**: apoio para configuracao, injecao de dependencias, execucao e integracao.
- **JSF / Jakarta Faces**: construcao das telas web com arquivos XHTML e Beans.
- **JPA / Spring Data JPA**: mapeamento das entidades e acesso ao banco de dados.
- **PostgreSQL**: banco de dados relacional usado para armazenar pacientes e atendimentos.
- **Servlet**: geracao do relatorio geral por uma requisicao HTTP direta.
- **HTML/XHTML**: estrutura das paginas.
- **CSS**: padronizacao visual das telas.
- **Git**: controle de versao.

## Melhorias Futuras

- Autenticacao de usuarios
- Controle de permissoes
- Paginacao nas listagens
- Dashboard com indicadores
- Exportacao CSV/PDF mais completa
- Validacao de CPF
- Filtros avancados para atendimentos
- Historico detalhado de alteracoes
