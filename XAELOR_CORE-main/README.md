# XAELOR CORE

API REST desenvolvida em **Java + Spring Boot** para gerenciamento de perfumes, matérias-primas e da composição de matérias-primas utilizadas em perfumes.

O projeto foi desenvolvido com arquitetura em camadas, utilizando **Spring Boot, Spring Data JPA, Hibernate, Oracle Database e Spring HATEOAS**.

---

## 📌 Sumário

* [Sobre o Projeto](#-sobre-o-projeto)
* [Tecnologias](#-tecnologias)
* [Arquitetura](#-arquitetura)
* [Modelo de Dados](#-modelo-de-dados)
* [Estrutura do Projeto](#-estrutura-do-projeto)
* [Como Executar](#-como-executar)
* [CRUD](#-crud)
* [CRUD de Matéria-Prima](#-crud-de-matéria-prima)
* [CRUD de Perfume](#-crud-de-perfume)
* [CRUD de Perfume x Matéria-Prima](#-crud-de-perfume-x-matéria-prima)
* [HATEOAS](#-hateoas)
* [Validações](#-validações)
* [Testes](#-testes)
* [Possíveis Melhorias](#-possíveis-melhorias)
* [Conclusão](#-conclusão)

---

# 🧴 Sobre o Projeto

O **XAELOR CORE** é uma API REST responsável pelo gerenciamento de perfumes e matérias-primas.

A aplicação possui três recursos principais:

### 1. Perfume

Responsável pelo cadastro dos perfumes.

Informações armazenadas:

* Nome;
* Gênero;
* Descrição.

### 2. Matéria-Prima

Responsável pelo cadastro dos ingredientes utilizados na criação dos perfumes.

Informações armazenadas:

* Nome;
* Tipo de unidade;
* Descrição.

### 3. Perfume x Matéria-Prima

Responsável por relacionar um perfume às matérias-primas utilizadas em sua composição.

Além da relação entre as entidades, esse recurso armazena informações como:

* Valor por unidade;
* Tipo de unidade;
* Custo total;
* Margem de lucro;
* Valor final.

---

# 🚀 Tecnologias

O projeto utiliza as seguintes tecnologias:

| Tecnologia         | Utilização                    |
| ------------------ | ----------------------------- |
| Java               | Linguagem de programação      |
| Spring Boot        | Framework principal           |
| Spring Web         | Desenvolvimento da API REST   |
| Spring Data JPA    | Persistência de dados         |
| Hibernate          | ORM                           |
| Oracle Database    | Banco de dados                |
| Oracle JDBC        | Conexão com Oracle            |
| Spring HATEOAS     | Links nas respostas da API    |
| Jakarta Validation | Validação dos dados           |
| Lombok             | Redução de código boilerplate |
| Maven              | Gerenciamento do projeto      |
| JUnit              | Testes                        |

---

# 🏗️ Arquitetura

O projeto utiliza uma arquitetura baseada em camadas:

```text
┌─────────────────────────┐
│       Cliente           │
│ Postman / Front-end     │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       Controller        │
│    Endpoints REST       │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│        Service          │
│   Regras de negócio     │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       Repository        │
│      Spring Data JPA    │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│      Oracle Database    │
└─────────────────────────┘
```

### Controller

É responsável por receber as requisições HTTP.

Exemplos:

```text
MateriaPrimaController
PerfumeController
PerfumeMateriaPrimaController
```

---

### Service

Responsável pelas regras de negócio.

Exemplos:

```text
MateriaPrimaService
PerfumeService
PerfumeMateriaPrimaService
```

É nessa camada que são realizadas validações antes de salvar ou atualizar informações.

---

### Repository

Responsável pelo acesso ao banco de dados utilizando Spring Data JPA.

Exemplo:

```java
public interface PerfumeRepository
        extends JpaRepository<Perfume, Long> {
}
```

Com isso, operações como:

```java
findAll()
findById()
save()
delete()
```

podem ser utilizadas sem a necessidade de implementar manualmente SQL para essas operações.

---

# 🗄️ Modelo de Dados

O projeto possui três entidades principais.

```text
                 ┌───────────────────┐
                 │      PERFUME       │
                 │───────────────────│
                 │ PERFUME_ID (PK)    │
                 │ PERFUME_NOME       │
                 │ PERFUME_GENERO     │
                 │ PERFUME_DESCRICAO  │
                 └─────────┬─────────┘
                           │
                           │
                           ▼
              ┌───────────────────────────┐
              │ TB_PERFUME_MATERIA_PRIMA  │
              │───────────────────────────│
              │ PERMAT_ID (PK)            │
              │ PERFUME_ID                │
              │ MATPRIMA_ID               │
              │ VALOR_POR_UNIDADE         │
              │ TIPO_UNIDADE              │
              │ CUSTO_TOTAL               │
              │ MARGEM_LUCRO              │
              │ VALOR_FINAL               │
              └─────────────┬─────────────┘
                            │
                            │
                            ▼
                 ┌─────────────────────┐
                 │    MATÉRIA-PRIMA    │
                 │─────────────────────│
                 │ MATPRIMA_ID (PK)     │
                 │ MATPRIMA_NOME        │
                 │ MATPRIMA_TIPOUNIDADE │
                 │ MATPRIMA_DESCRICAO   │
                 └─────────────────────┘
```

---

## 👃 Entidade Perfume

Tabela:

```text
TB_PERFUME
```

Campos principais:

| Campo             | Tipo   |
| ----------------- | ------ |
| PERFUME_ID        | Long   |
| PERFUME_NOME      | String |
| PERFUME_GENERO    | String |
| PERFUME_DESCRICAO | String |

---

## 🧪 Entidade Matéria-Prima

Tabela:

```text
TB_MATERIAPRIMA
```

Campos:

| Campo                | Tipo   |
| -------------------- | ------ |
| MATPRIMA_ID          | Long   |
| MATPRIMA_NOME        | String |
| MATPRIMA_TIPOUNIDADE | Enum   |
| MATPRIMA_DESCRICAO   | String |

Tipos de unidade utilizados:

```text
ML
L
MG
G
GOTA
unidade
```

---

## 🔗 Entidade PerfumeMateriaPrima

Tabela:

```text
TB_PERFUME_MATERIA_PRIMA
```

Campos principais:

| Campo             | Descrição                 |
| ----------------- | ------------------------- |
| PERMAT_ID         | ID da relação             |
| PERFUME_ID        | Perfume relacionado       |
| MATPRIMA_ID       | Matéria-prima relacionada |
| VALOR_POR_UNIDADE | Valor da matéria-prima    |
| TIPO_UNIDADE      | Unidade utilizada         |
| CUSTO_TOTAL       | Custo total               |
| MARGEM_LUCRO      | Margem aplicada           |
| VALOR_FINAL       | Valor final               |

---

# 📁 Estrutura do Projeto

```text
src
└── main
    ├── java
    │   └── br
    │       └── com
    │           └── fiap
    │               └── XAELOR_CORE
    │                   │
    │                   ├── Controller
    │                   │   ├── MateriaPrimaController.java
    │                   │   ├── PerfumeController.java
    │                   │   └── PerfumeMateriaPrimaController.java
    │                   │
    │                   ├── Repository
    │                   │   ├── MateriaPrimaRepository.java
    │                   │   ├── PerfumeRepository.java
    │                   │   └── PerfumeMateriaPrimaRepository.java
    │                   │
    │                   ├── Service
    │                   │   ├── MateriaPrimaService.java
    │                   │   ├── PerfumeService.java
    │                   │   └── PerfumeMateriaPrimaService.java
    │                   │
    │                   ├── model
    │                   │   ├── MateriaPrima.java
    │                   │   ├── Perfume.java
    │                   │   └── PerfumeMateriaPrima.java
    │                   │
    │                   ├── enums
    │                   │   └── TipoUnidade.java
    │                   │
    │                   └── XaelorCoreApplication.java
    │
    └── resources
        └── application.properties
```

---

# ⚙️ Como Executar

## Pré-requisitos

É necessário possuir:

* Java JDK;
* Maven;
* Banco Oracle configurado;
* IDE de sua preferência.

---

## Executando com Maven

No Windows:

```bash
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Ou utilizando Maven instalado:

```bash
mvn spring-boot:run
```

A aplicação está configurada para utilizar a porta:

```text
8082
```

Portanto:

```text
http://localhost:8082
```

---

# 🔄 CRUD

CRUD é uma sigla para as quatro principais operações realizadas em um sistema que trabalha com dados:

```text
C → Create  → Criar
R → Read    → Ler
U → Update  → Atualizar
D → Delete  → Excluir
```

No XAELOR CORE essas operações são implementadas através dos Controllers, Services e Repositories.

---

## 📌 Fluxo do CRUD

```text
POST /perfume
       │
       ▼
PerfumeController
       │
       ▼
PerfumeService
       │
       ▼
PerfumeRepository
       │
       ▼
Oracle
```

O mesmo conceito é utilizado para GET, PUT e DELETE.

---

# 🧪 CRUD de Matéria-Prima

Endpoint base:

```text
/materiaPrima
```

---

## CREATE — Criar

Para cadastrar uma matéria-prima utilizamos:

```http
POST /materiaPrima
```

### Exemplo

```json
{
  "nome": "Essência de Lavanda",
  "tipoUnidade": "ML",
  "descricao": "Essência utilizada na composição aromática."
}
```

### Exemplo utilizando cURL

```bash
curl -X POST http://localhost:8082/materiaPrima \
-H "Content-Type: application/json" \
-d '{
  "nome": "Essência de Lavanda",
  "tipoUnidade": "ML",
  "descricao": "Essência utilizada na composição aromática."
}'
```

### O que acontece?

1. O cliente envia o JSON.
2. O `MateriaPrimaController` recebe a requisição.
3. O Controller chama o `MateriaPrimaService`.
4. O Service valida os dados.
5. O `MateriaPrimaRepository` salva no banco.
6. O Oracle gera o ID.
7. A API retorna a matéria-prima cadastrada.

---

# READ — Listar

Para consultar todas as matérias-primas:

```http
GET /materiaPrima
```

Exemplo:

```bash
curl http://localhost:8082/materiaPrima
```

Resposta aproximada:

```json
{
  "_embedded": {
    "materiaPrimaList": [
      {
        "id": 1,
        "nome": "Essência de Lavanda",
        "tipoUnidade": "ML",
        "descricao": "Essência utilizada na composição aromática.",
        "_links": {
          "self": {
            "href": "http://localhost:8082/materiaPrima/1"
          }
        }
      }
    ]
  }
}
```

---

# READ — Buscar por ID

Para buscar uma matéria-prima específica:

```http
GET /materiaPrima/1
```

Exemplo:

```bash
curl http://localhost:8082/materiaPrima/1
```

O sistema utiliza:

```java
materiaPrimaRepository.findById(id)
```

Caso o ID não exista, o Service informa que a matéria-prima não está cadastrada.

---

# UPDATE — Atualizar

Para atualizar:

```http
PUT /materiaPrima/atualizar/1
```

Exemplo:

```json
{
  "nome": "Essência de Lavanda Premium",
  "tipoUnidade": "ML",
  "descricao": "Essência premium utilizada na composição aromática."
}
```

Exemplo:

```bash
curl -X PUT http://localhost:8082/materiaPrima/atualizar/1 \
-H "Content-Type: application/json" \
-d '{
  "nome": "Essência de Lavanda Premium",
  "tipoUnidade": "ML",
  "descricao": "Essência premium utilizada na composição aromática."
}'
```

O sistema busca primeiro a matéria-prima existente e depois altera seus dados.

---

# DELETE — Excluir

Para excluir:

```http
DELETE /materiaPrima/delete/1
```

Exemplo:

```bash
curl -X DELETE http://localhost:8082/materiaPrima/delete/1
```

Quando a exclusão é realizada com sucesso:

```http
204 No Content
```

---

# 🌸 CRUD de Perfume

Endpoint base:

```text
/perfume
```

---

# CREATE — Criar Perfume

```http
POST /perfume
```

Exemplo:

```json
{
  "nomePerfume": "XAELOR Intense",
  "generoPerfume": "Unissex",
  "descricaoPerfume": "Fragrância intensa e marcante."
}
```

Exemplo utilizando cURL:

```bash
curl -X POST http://localhost:8082/perfume \
-H "Content-Type: application/json" \
-d '{
  "nomePerfume": "XAELOR Intense",
  "generoPerfume": "Unissex",
  "descricaoPerfume": "Fragrância intensa e marcante."
}'
```

---

# READ — Listar Perfumes

```http
GET /perfume
```

Exemplo:

```bash
curl http://localhost:8082/perfume
```

---

# READ — Buscar Perfume

```http
GET /perfume/1
```

Exemplo:

```bash
curl http://localhost:8082/perfume/1
```

---

# UPDATE — Atualizar Perfume

```http
PUT /perfume/atualizar/1
```

Exemplo:

```json
{
  "nomePerfume": "XAELOR Intense Black",
  "generoPerfume": "Unissex",
  "descricaoPerfume": "Fragrância intensa de longa duração."
}
```

O sistema busca o perfume existente pelo ID e atualiza os dados previstos pelo Service.

---

# DELETE — Excluir Perfume

```http
DELETE /perfume/delete/1
```

Exemplo:

```bash
curl -X DELETE http://localhost:8082/perfume/delete/1
```

Resposta de sucesso:

```http
204 No Content
```

---

# 🔗 CRUD de Perfume x Matéria-Prima

Endpoint base:

```text
/perfumeMateriaPrima
```

Esse recurso representa a relação entre um perfume e uma matéria-prima.

Por exemplo:

```text
Perfume:
XAELOR Intense

        +

Matéria-Prima:
Essência de Lavanda

        ↓

Relação:
PerfumeMateriaPrima
```

---

# CREATE — Criar Relação

```http
POST /perfumeMateriaPrima
```

Exemplo:

```json
{
  "id_perfume": {
    "id": 1
  },
  "id_materiaPrima": {
    "id": 1
  },
  "valorPorUnidade": 12.50,
  "tipoUnidade": "ML",
  "custoTotal": 125.00,
  "margemLucro": 30,
  "valorFinal": 162.50
}
```

O sistema recebe o perfume, a matéria-prima e os dados de custo.

---

# READ — Listar Relações

```http
GET /perfumeMateriaPrima
```

Retorna todas as relações cadastradas.

---

# READ — Buscar Relação

```http
GET /perfumeMateriaPrima/1
```

Retorna a relação correspondente ao ID informado.

---

# UPDATE — Atualizar Relação

```http
PUT /perfumeMateriaPrima/atualizar/1
```

Exemplo:

```json
{
  "valorPorUnidade": 15.00,
  "tipoUnidade": "ML",
  "custoTotal": 150.00,
  "margemLucro": 35,
  "valorFinal": 202.50
}
```

A atualização utiliza o ID da relação para localizar o registro.

> **Observação:** na implementação atual do projeto, o método de atualização de `PerfumeMateriaPrima` possui um ponto que deve ser revisado: os novos valores são validados, mas não são atribuídos à entidade existente antes do `save()`. Para que o UPDATE funcione efetivamente, os campos devem ser copiados para a entidade encontrada.

Exemplo da correção esperada:

```java
existente.setValorPorUnidade(
    perfumeMateriaPrima.getValorPorUnidade()
);

existente.setTipoUnidade(
    perfumeMateriaPrima.getTipoUnidade()
);

existente.setCustoTotal(
    perfumeMateriaPrima.getCustoTotal()
);

existente.setMargemLucro(
    perfumeMateriaPrima.getMargemLucro()
);

existente.setValorFinal(
    perfumeMateriaPrima.getValorFinal()
);

return perfumeMateriaPrimaRepository.save(existente);
```

---

# DELETE — Excluir Relação

```http
DELETE /perfumeMateriaPrima/delete/1
```

Em caso de sucesso:

```http
204 No Content
```

---

# 🔗 HATEOAS

O projeto utiliza **Spring HATEOAS**.

HATEOAS permite que a API forneça links relacionados aos recursos retornados.

Exemplo:

```json
{
  "id": 1,
  "nomePerfume": "XAELOR Intense",
  "generoPerfume": "Unissex",
  "_links": {
    "self": {
      "href": "http://localhost:8082/perfume/1"
    },
    "perfumes": {
      "href": "http://localhost:8082/perfume"
    },
    "deletar": {
      "href": "http://localhost:8082/perfume/delete/1"
    }
  }
}
```

Isso permite que o consumidor da API navegue pelos recursos através dos próprios links retornados.

Nos Controllers são utilizados recursos como:

```java
EntityModel.of(...)
```

e:

```java
linkTo(methodOn(...))
```

---

# ✅ Validações

O projeto possui validações implementadas na camada Service.

## Matéria-Prima

O nome é obrigatório.

```text
Nome é obrigatorio
```

O tipo de unidade também é obrigatório.

```text
Tipo Unidade é obrigatório para cadastrar Materias Primas
```

---

## Perfume

O nome é obrigatório.

Além disso, nomes vazios ou contendo apenas espaços são rejeitados.

O gênero também é obrigatório.

---

## PerfumeMateriaPrima

São realizadas verificações relacionadas a:

* valor por unidade;
* tipo de unidade;
* margem de lucro;
* valores positivos.

A entidade utiliza também validações como:

```java
@Positive
```

para campos que não podem possuir valores negativos ou iguais a zero.

---

# 🧪 Testes

O projeto possui teste de carregamento do contexto da aplicação:

```java
@SpringBootTest
class XaelorCoreApplicationTests {

    @Test
    void contextLoads() {
    }
}
```

Esse teste verifica se a aplicação consegue iniciar corretamente.

Para aumentar a cobertura, podem ser adicionados testes para:

* criação de perfume;
* criação de matéria-prima;
* busca por ID;
* listagem;
* atualização;
* exclusão;
* validações;
* IDs inexistentes;
* respostas HTTP;
* HATEOAS.

---

# ⚠️ Possíveis Melhorias

## 1. Credenciais do banco

As credenciais do banco não devem ficar diretamente no código em um projeto publicado.

O ideal é utilizar variáveis de ambiente:

```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

---

## 2. Tratamento de exceções

Atualmente algumas situações utilizam:

```java
throw new RuntimeException(...)
```

Uma melhoria seria criar exceções específicas e utilizar:

```java
@RestControllerAdvice
```

para padronizar as respostas de erro.

Exemplo:

```json
{
  "status": 404,
  "message": "Perfume não encontrado"
}
```

---

## 3. Atualização do Perfume

O método de atualização do perfume deve garantir que todos os campos esperados sejam atualizados, incluindo:

```text
nomePerfume
generoPerfume
descricaoPerfume
```

---

## 4. Atualização de PerfumeMateriaPrima

O método atual precisa atribuir os novos valores à entidade existente antes de executar o `save()`.

---

## 5. Relacionamentos

A entidade `PerfumeMateriaPrima` possui referências relacionadas a perfume e matéria-prima que podem ser simplificadas.

Uma modelagem mais limpa seria:

```text
PerfumeMateriaPrima
│
├── Perfume
│
└── MateriaPrima
```

Assim, os nomes poderiam ser obtidos diretamente das entidades relacionadas.

---

# 📊 Resumo dos Endpoints

| Método | Endpoint                              | Operação                |
| ------ | ------------------------------------- | ----------------------- |
| POST   | `/materiaPrima`                       | Criar matéria-prima     |
| GET    | `/materiaPrima`                       | Listar matérias-primas  |
| GET    | `/materiaPrima/{id}`                  | Buscar matéria-prima    |
| PUT    | `/materiaPrima/atualizar/{id}`        | Atualizar matéria-prima |
| DELETE | `/materiaPrima/delete/{id}`           | Excluir matéria-prima   |
| POST   | `/perfume`                            | Criar perfume           |
| GET    | `/perfume`                            | Listar perfumes         |
| GET    | `/perfume/{id}`                       | Buscar perfume          |
| PUT    | `/perfume/atualizar/{id}`             | Atualizar perfume       |
| DELETE | `/perfume/delete/{id}`                | Excluir perfume         |
| POST   | `/perfumeMateriaPrima`                | Criar relação           |
| GET    | `/perfumeMateriaPrima`                | Listar relações         |
| GET    | `/perfumeMateriaPrima/{id}`           | Buscar relação          |
| PUT    | `/perfumeMateriaPrima/atualizar/{id}` | Atualizar relação       |
| DELETE | `/perfumeMateriaPrima/delete/{id}`    | Excluir relação         |

---

# 🏁 Conclusão

O **XAELOR CORE** é uma API REST desenvolvida utilizando **Java e Spring Boot**, com foco no gerenciamento de perfumes e matérias-primas.

O projeto apresenta uma arquitetura organizada em:

```text
Controller
     ↓
Service
     ↓
Repository
     ↓
Oracle Database
```

Os principais recursos disponíveis são:

* 👃 Perfumes;
* 🧪 Matérias-primas;
* 🔗 Relação entre perfumes e matérias-primas.

A API implementa as operações fundamentais de um CRUD:

```text
CREATE
READ
UPDATE
DELETE
```

Além disso, possui:

* Spring Data JPA;
* Hibernate;
* Oracle;
* HATEOAS;
* validações;
* testes;
* separação de responsabilidades por camadas.

Dessa forma, o projeto fornece uma base completa para gerenciamento dos dados relacionados à criação, composição e custeio de perfumes.
