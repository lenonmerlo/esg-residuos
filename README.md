# ESG Resíduos API

API RESTful para gerenciamento de resíduos sólidos, pontos de coleta, coletas e destinações — desenvolvida com **Spring Boot** como atividade prática da disciplina de Microsserviços (Cap. 8) da FIAP ADS.

---

## Sumário

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Modelos de Dados](#modelos-de-dados)
- [Endpoints da API](#endpoints-da-api)
  - [Resíduos](#resíduos-wastes)
  - [Pontos de Coleta](#pontos-de-coleta-collection-points)
  - [Coletas](#coletas-collections)
  - [Destinações](#destinações-destinations)
  - [Alertas](#alertas-alerts)
- [Regras de Negócio](#regras-de-negócio)
- [Segurança](#segurança)
- [Documentação Interativa (Swagger)](#documentação-interativa-swagger)
- [Configuração de Ambiente](#configuração-de-ambiente)
- [Executando com Docker](#executando-com-docker)
- [Executando Localmente](#executando-localmente)
- [Migrações de Banco de Dados](#migrações-de-banco-de-dados)
- [Estrutura do Projeto](#estrutura-do-projeto)

---

## Sobre o Projeto

O **ESG Resíduos** é uma API para suporte a iniciativas ESG (Environmental, Social and Governance) na gestão de resíduos sólidos. A plataforma permite:

- Cadastrar tipos de **resíduos** com suas descrições
- Gerenciar **pontos de coleta** com capacidade e limiares de alerta
- Registrar **coletas** vinculando resíduos a pontos de coleta
- Registrar **destinações** (tratamento, reciclagem, descarte) para coletas concluídas
- Consultar **alertas** gerados quando pontos de coleta atingem limites críticos

---

## Tecnologias Utilizadas

| Tecnologia              | Versão | Finalidade                                       |
| ----------------------- | ------ | ------------------------------------------------ |
| Java                    | 21     | Linguagem principal                              |
| Spring Boot             | 4.0.6  | Framework base                                   |
| Spring Web MVC          | —      | Camada REST                                      |
| Spring Data JPA         | —      | ORM e acesso a dados                             |
| Spring Security         | —      | Autenticação HTTP Basic                          |
| Spring Validation       | —      | Validação de entradas                            |
| Flyway                  | —      | Migração e versionamento do banco                |
| Oracle Database         | —      | Banco de dados relacional                        |
| ojdbc11                 | —      | Driver JDBC Oracle (runtime)                     |
| Lombok                  | —      | Redução de boilerplate (getters, builders, etc.) |
| springdoc-openapi       | 2.8.6  | Geração automática de Swagger UI                 |
| Docker / Docker Compose | —      | Containerização                                  |

---

## Arquitetura

O projeto segue a arquitetura em camadas padrão do Spring Boot:

```
Controller  →  Service  →  Repository  →  Database (Oracle)
     ↕               ↕
    DTO           Model (Entity)
```

| Camada         | Responsabilidade                                                       |
| -------------- | ---------------------------------------------------------------------- |
| **Controller** | Recebe requisições HTTP, valida entrada, delega ao Service             |
| **Service**    | Contém as regras de negócio e orquestra operações                      |
| **Repository** | Abstração de acesso ao banco via Spring Data JPA                       |
| **Model**      | Entidades JPA mapeadas para tabelas do Oracle                          |
| **DTO**        | Objetos de transferência (request/response), sem acoplamento ao modelo |
| **Exception**  | Tratamento centralizado de erros via `@ControllerAdvice`               |
| **Config**     | Configurações de segurança e documentação OpenAPI                      |

---

## Modelos de Dados

### Residuo (`RESIDUO`)

| Campo         | Tipo      | Restrições               |
| ------------- | --------- | ------------------------ |
| `id`          | Long (PK) | Auto-gerado              |
| `wasteType`   | String    | Único, máx. 30 chars     |
| `description` | String    | Opcional, máx. 200 chars |

### Ponto de Coleta (`PONTO_COLETA`)

| Campo              | Tipo       | Restrições                                |
| ------------------ | ---------- | ----------------------------------------- |
| `id`               | Long (PK)  | Auto-gerado                               |
| `name`             | String     | Máx. 100 chars                            |
| `capacityKg`       | BigDecimal | Capacidade máxima em kg                   |
| `alertVolumeKg`    | BigDecimal | Volume de alerta em kg                    |
| `occupiedVolumeKg` | BigDecimal | Volume atualmente ocupado                 |
| `status`           | String     | `DISPONIVEL`, `PROXIMO_LIMITE`, `CRITICO` |
| `updatedAt`        | LocalDate  | Data da última atualização                |

### Coleta (`COLETA`)

| Campo                | Tipo                | Restrições                                  |
| -------------------- | ------------------- | ------------------------------------------- |
| `id`                 | Long (PK)           | Auto-gerado                                 |
| `collectionPoint`    | FK → `PONTO_COLETA` | ManyToOne (LAZY)                            |
| `waste`              | FK → `RESIDUO`      | ManyToOne (LAZY)                            |
| `collectionDate`     | LocalDate           | Data da coleta                              |
| `volumeKg`           | BigDecimal          | Volume coletado                             |
| `status`             | String              | `ABERTA`, `DESTINADA`, `CANCELADA`          |
| `destinationDate`    | LocalDate           | Data de envio à destinação                  |
| `destinationHistory` | String              | Histórico de processamento, máx. 4000 chars |

### Destinação (`DESTINACAO`)

| Campo              | Tipo          | Restrições                           |
| ------------------ | ------------- | ------------------------------------ |
| `id`               | Long (PK)     | Auto-gerado                          |
| `collection`       | FK → `COLETA` | ManyToOne (LAZY)                     |
| `destinationDate`  | LocalDate     | Data de destinação                   |
| `destinationName`  | String        | Nome da instalação, máx. 120 chars   |
| `processingType`   | String        | Tipo de processamento, máx. 60 chars |
| `destinedVolumeKg` | BigDecimal    | Volume destinado                     |

### Alerta de Coleta (`ALERTA_COLETA`)

| Campo             | Tipo                | Restrições                                             |
| ----------------- | ------------------- | ------------------------------------------------------ |
| `id`              | Long (PK)           | Auto-gerado                                            |
| `collectionPoint` | FK → `PONTO_COLETA` | ManyToOne (LAZY)                                       |
| `collection`      | FK → `COLETA`       | ManyToOne (LAZY), opcional                             |
| `alertDate`       | LocalDate           | Data do alerta                                         |
| `alertType`       | String              | `LIMITE_ATINGIDO`, `CAPACIDADE_EXCEDIDA`, `INFORMACAO` |
| `message`         | String              | Mensagem descritiva, máx. 300 chars                    |

---

## Endpoints da API

Base URL: `http://localhost:8080`

### Resíduos (`/wastes`)

| Método   | Endpoint       | Auth    | Descrição               |
| -------- | -------------- | ------- | ----------------------- |
| `GET`    | `/wastes`      | Não     | Lista todos os resíduos |
| `GET`    | `/wastes/{id}` | Não     | Busca resíduo por ID    |
| `POST`   | `/wastes`      | **Sim** | Cria novo resíduo       |
| `PUT`    | `/wastes/{id}` | **Sim** | Atualiza resíduo        |
| `DELETE` | `/wastes/{id}` | **Sim** | Remove resíduo          |

#### POST `/wastes` — Request Body

```json
{
  "wasteType": "Plástico Rígido",
  "description": "Garrafas, caixas e utensílios plásticos"
}
```

#### Response (`200 OK` / `201 Created`)

```json
{
  "id": 1,
  "wasteType": "Plástico Rígido",
  "description": "Garrafas, caixas e utensílios plásticos"
}
```

---

### Pontos de Coleta (`/collection-points`)

| Método   | Endpoint                  | Auth    | Descrição                       |
| -------- | ------------------------- | ------- | ------------------------------- |
| `GET`    | `/collection-points`      | Não     | Lista todos os pontos de coleta |
| `GET`    | `/collection-points/{id}` | Não     | Busca ponto de coleta por ID    |
| `POST`   | `/collection-points`      | **Sim** | Cria novo ponto de coleta       |
| `PUT`    | `/collection-points/{id}` | **Sim** | Atualiza ponto de coleta        |
| `DELETE` | `/collection-points/{id}` | **Sim** | Remove ponto de coleta          |

#### POST `/collection-points` — Request Body

```json
{
  "name": "Ponto Central - Bloco A",
  "capacityKg": 500.0,
  "alertVolumeKg": 400.0
}
```

#### Response

```json
{
  "id": 1,
  "name": "Ponto Central - Bloco A",
  "capacityKg": 500.0,
  "alertVolumeKg": 400.0,
  "occupiedVolumeKg": 0.0,
  "status": "DISPONIVEL",
  "updatedAt": "2026-04-29"
}
```

---

### Coletas (`/collections`)

| Método   | Endpoint            | Auth    | Descrição                                       |
| -------- | ------------------- | ------- | ----------------------------------------------- |
| `GET`    | `/collections`      | Não     | Lista todas as coletas                          |
| `GET`    | `/collections/{id}` | Não     | Busca coleta por ID                             |
| `POST`   | `/collections`      | **Sim** | Registra nova coleta                            |
| `PUT`    | `/collections/{id}` | **Sim** | Atualiza coleta (somente se `ABERTA`)           |
| `DELETE` | `/collections/{id}` | **Sim** | Cancela coleta (altera status para `CANCELADA`) |

#### POST `/collections` — Request Body

```json
{
  "collectionPointId": 1,
  "wasteId": 2,
  "volumeKg": 80.0
}
```

#### Response

```json
{
  "id": 1,
  "collectionPointId": 1,
  "collectionPointName": "Ponto Central - Bloco A",
  "wasteId": 2,
  "wasteType": "Plástico Rígido",
  "collectionDate": "2026-04-29",
  "volumeKg": 80.0,
  "status": "ABERTA",
  "destinationDate": null,
  "destinationHistory": null
}
```

---

### Destinações (`/destinations`)

| Método | Endpoint             | Auth    | Descrição                         |
| ------ | -------------------- | ------- | --------------------------------- |
| `GET`  | `/destinations`      | Não     | Lista todas as destinações        |
| `GET`  | `/destinations/{id}` | Não     | Busca destinação por ID           |
| `POST` | `/destinations`      | **Sim** | Registra destinação de uma coleta |
| `PUT`  | `/destinations/{id}` | **Sim** | Atualiza destinação               |

#### POST `/destinations` — Request Body

```json
{
  "collectionId": 1,
  "destinationName": "Cooperativa Verde Vida",
  "processingType": "Reciclagem",
  "destinedVolumeKg": 80.0
}
```

#### Response

```json
{
  "id": 1,
  "collectionId": 1,
  "destinationDate": "2026-04-29",
  "destinationName": "Cooperativa Verde Vida",
  "processingType": "Reciclagem",
  "destinedVolumeKg": 80.0
}
```

---

### Alertas (`/alerts`)

| Método | Endpoint                               | Auth | Descrição                           |
| ------ | -------------------------------------- | ---- | ----------------------------------- |
| `GET`  | `/alerts`                              | Não  | Lista todos os alertas              |
| `GET`  | `/alerts/{id}`                         | Não  | Busca alerta por ID                 |
| `GET`  | `/alerts/by-point/{collectionPointId}` | Não  | Lista alertas de um ponto de coleta |

#### Response

```json
{
  "id": 1,
  "collectionPointId": 1,
  "collectionPointName": "Ponto Central - Bloco A",
  "collectionId": 3,
  "alertDate": "2026-04-29",
  "alertType": "LIMITE_ATINGIDO",
  "message": "Ponto de coleta atingiu o volume de alerta."
}
```

---

## Regras de Negócio

### Coletas

- Ao criar uma coleta, o sistema valida se `ocupado + novo volume ≤ capacidade` do ponto de coleta. Caso contrário, lança `BusinessException`.
- Uma coleta só pode ser atualizada enquanto estiver com `status = "ABERTA"`.
- Deletar uma coleta não a remove — altera o `status` para `"CANCELADA"`.
- A `collectionDate` é definida automaticamente como a data atual no momento da criação.

### Destinações

- Só é possível criar uma destinação para uma coleta com `status = "ABERTA"`.
- O `destinedVolumeKg` não pode ser maior que o `volumeKg` da coleta.
- Ao criar a destinação, o status da coleta é alterado automaticamente para `"DESTINADA"` e a `destinationDate` é preenchida com a data atual.

### Pontos de Coleta

- O `status` inicial de um ponto de coleta é sempre `"DISPONIVEL"`.
- O `occupiedVolumeKg` começa em `0` e o `updatedAt` é definido na data de criação.

---

## Segurança

A API utiliza **HTTP Basic Authentication** para as operações de escrita.

| Operação                   | Autenticação                  |
| -------------------------- | ----------------------------- |
| `GET` em qualquer endpoint | Pública (sem autenticação)    |
| `POST`, `PUT`, `DELETE`    | Requer credenciais Basic Auth |

As credenciais são configuradas via `application.properties` (ou variáveis de ambiente) no módulo de segurança do Spring Security.

**Credenciais de acesso (desenvolvimento):**

| Campo    | Valor      |
| -------- | ---------- |
| Username | `admin`    |
| Password | `admin123` |

> **Nota:** A autenticação Basic Auth transmite credenciais codificadas em Base64. Em produção, utilize sempre HTTPS para proteger o tráfego.

---

## Documentação Interativa (Swagger)

A documentação interativa da API está disponível via **Swagger UI** após iniciar a aplicação:

```
http://localhost:8080/swagger-ui.html
```

A especificação OpenAPI (JSON) pode ser acessada em:

```
http://localhost:8080/v3/api-docs
```

Para testar endpoints protegidos via Swagger UI, clique em **Authorize** e informe as credenciais Basic Auth.

---

## Configuração de Ambiente

A aplicação requer as seguintes variáveis de ambiente para conexão com o banco Oracle:

| Variável  | Descrição          | Exemplo                                |
| --------- | ------------------ | -------------------------------------- |
| `DB_URL`  | URL JDBC do Oracle | `jdbc:oracle:thin:@//host:1521/XEPDB1` |
| `DB_USER` | Usuário do banco   | `esg_user`                             |
| `DB_PASS` | Senha do banco     | `esg_pass`                             |

### Exemplo de arquivo `.env`

```env
DB_URL=jdbc:oracle:thin:@//localhost:1521/XEPDB1
DB_USER=esg_user
DB_PASS=esg_pass
```

---

## Executando com Docker

O projeto inclui `Dockerfile` e `docker-compose.yml` para facilitar a execução em container.

### Pré-requisitos

- [Docker](https://www.docker.com/) instalado
- [Docker Compose](https://docs.docker.com/compose/) instalado

### Subindo a aplicação

```bash
docker-compose up --build
```

A aplicação ficará disponível em `http://localhost:8080`.

### Parando os containers

```bash
docker-compose down
```

---

## Executando Localmente

### Pré-requisitos

- Java 21+
- Maven 3.9+
- Oracle Database acessível

### Passos

1. Clone o repositório:

```bash
git clone <url-do-repositorio>
cd esg-residuos
```

2. Configure as variáveis de ambiente:

**Windows (PowerShell):**

```powershell
$env:DB_URL="jdbc:oracle:thin:@oracle.fiap.com.br:1521/ORCL"
$env:DB_USER="RM564471"
$env:DB_PASS="sua_senha"
```

**Linux/Mac:**

```bash
export DB_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521/ORCL
export DB_USER=RM564471
export DB_PASS=sua_senha
```

3. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

Ou, no Windows:

```cmd
mvnw.cmd spring-boot:run
```

4. Acesse em: `http://localhost:8080`

---

## Migrações de Banco de Dados

O projeto usa **Flyway** para controle de versão do banco de dados. As migrações ficam em `src/main/resources/db/migration/`:

| Arquivo                 | Descrição                                                                              |
| ----------------------- | -------------------------------------------------------------------------------------- |
| `V2__create_tables.sql` | Criação das tabelas `RESIDUO`, `PONTO_COLETA`, `COLETA`, `DESTINACAO`, `ALERTA_COLETA` |
| `V3__seed_data.sql`     | Dados iniciais para testes e demonstração                                              |

As migrações são aplicadas automaticamente na inicialização da aplicação (`spring.flyway.enabled=true`).

---

## Estrutura do Projeto

```
esg-residuos/
├── src/
│   ├── main/
│   │   ├── java/br/com/fiap/esg_residuos/
│   │   │   ├── EsgResiduosApplication.java       # Classe principal Spring Boot
│   │   │   ├── config/
│   │   │   │   ├── OpenApiConfig.java             # Configuração do Swagger/OpenAPI
│   │   │   │   └── SecurityConfig.java            # Configuração do Spring Security
│   │   │   ├── controller/
│   │   │   │   ├── WasteController.java
│   │   │   │   ├── CollectionPointController.java
│   │   │   │   ├── CollectionController.java
│   │   │   │   ├── DestinationController.java
│   │   │   │   └── CollectionAlertController.java
│   │   │   ├── dto/
│   │   │   │   ├── request/                       # DTOs de entrada (Records com validação)
│   │   │   │   └── response/                      # DTOs de saída (Records com método from())
│   │   │   ├── exception/
│   │   │   │   ├── BusinessException.java         # Exceção para violações de regra de negócio
│   │   │   │   ├── ResourceNotFoundException.java # Exceção para recurso não encontrado (404)
│   │   │   │   └── GlobalExceptionHandler.java    # Tratamento centralizado de erros
│   │   │   ├── model/
│   │   │   │   ├── Waste.java
│   │   │   │   ├── CollectionPoint.java
│   │   │   │   ├── Collection.java
│   │   │   │   ├── Destination.java
│   │   │   │   └── CollectionAlert.java
│   │   │   ├── repository/
│   │   │   │   ├── WasteRepository.java
│   │   │   │   ├── CollectionPointRepository.java
│   │   │   │   ├── CollectionRepository.java
│   │   │   │   ├── DestinationRepository.java
│   │   │   │   └── CollectionAlertRepository.java
│   │   │   └── service/
│   │   │       ├── WasteService.java
│   │   │       ├── CollectionPointService.java
│   │   │       ├── CollectionService.java
│   │   │       ├── DestinationService.java
│   │   │       └── CollectionAlertService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   │           ├── V2__create_tables.sql
│   │           └── V3__seed_data.sql
│   └── test/
│       └── java/br/com/fiap/esg_residuos/
│           └── EsgResiduosApplicationTests.java
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Tratamento de Erros

A API retorna respostas de erro padronizadas via `GlobalExceptionHandler`:

| Situação                       | HTTP Status                                       |
| ------------------------------ | ------------------------------------------------- |
| Recurso não encontrado         | `404 Not Found`                                   |
| Violação de regra de negócio   | `422 Unprocessable Entity` (ou `400 Bad Request`) |
| Campos inválidos na requisição | `400 Bad Request`                                 |
| Credenciais ausentes/inválidas | `401 Unauthorized`                                |

---

_Projeto desenvolvido para a disciplina de Microsserviços com Spring — FIAP ADS, 2026._
