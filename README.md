# Stock Control

**Stock Control REST API + Interface Web | NEXDOM Challenge**

Desafio proposto pela NEXDOM, sistema de controle de estoque RESTful com API em Spring Boot + Web Interface em Vue 3.

---

## 1. Visão Geral do Projeto

Aplicação para controle de estoque que permite gerenciar produtos, registrar movimentações (entradas e saídas) e consultar relatórios de disponibilidade e lucratividade.

### Entidades

- **Produto**: código, descrição, tipo, valor do fornecedor, quantidade em estoque
- **Movimentação de Estoque**: produto, tipo (entrada/saída), quantidade, e para saídas: valor de venda e data de venda

### Funcionalidades Principais

- CRUD completo de produtos
- CRUD de movimentações de estoque com validação (estoque insuficiente, campos obrigatórios para saída)
- Relatório de resumo de estoque por tipo de produto
- Relatório de lucro por produto
- Documentação da API com Swagger/OpenAPI
- Testes unitários no backend

---

## 2. Stack Tecnológica

### Backend

- Java 21
- Spring Boot 4.0.2
- Spring Data JPA
- Spring Validation
- H2 (banco em memória)
- Springdoc OpenAPI (Swagger UI)

### Frontend

- Vue 3 + TypeScript + Composition API
- Vite 7
- Vue Router 4
- Pinia
- Axios
- Vee-Validate + Zod (validação de formulários)
- Tailwind CSS

### Ferramentas

- Maven (wrapper incluído)
- Node.js / npm

---

## 3. Requisitos

| Tecnologia | Versão |
|------------|--------|
| Java | 21 |
| Node.js | ^20.19.0 ou >=22.12.0 |
| Maven | via wrapper (`mvnw` / `mvnw.cmd`) |

---

## 4. Como Executar o Projeto

### Backend

1. Entre na pasta da API:
   ```bash
   cd stock-control-api/stock-control-api
   ```

2. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
   No Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

3. A API ficará disponível em **http://localhost:8080/api**

| Recurso | URL |
|---------|-----|
| API base | http://localhost:8080/api |
| Swagger UI | http://localhost:8080/api/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api/v3/api-docs |
| H2 Console | http://localhost:8080/api/h2-console |

**Configuração H2 Console:**
- **JDBC URL:** `jdbc:h2:mem:stockdb`
- **User Name:** `douglas`
- **Password:** `123`

### Frontend

1. Entre na pasta do frontend:
   ```bash
   cd stock-control-web
   ```

2. Instale as dependências:
   ```bash
   npm install
   ```

3. Crie o arquivo `.env` na raiz de `stock-control-web`:
   ```env
   VITE_API_BASE_URL=http://localhost:8080/api
   ```

4. Execute o servidor de desenvolvimento:
   ```bash
   npm run dev
   ```

5. O frontend ficará disponível em **http://localhost:5173**

---

## 5. Rotas da API

Base: `http://localhost:8080/api`

### Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/products` | Lista todos os produtos (query opcional: `productType`) |
| GET | `/products/{id}` | Retorna produto por ID |
| POST | `/products` | Cria produto |
| PUT | `/products/{id}` | Atualiza produto |
| DELETE | `/products/{id}` | Remove produto |

**ProductType:** `ELECTRONIC`, `HOME_APPLIANCE`, `FURNITURE`

### Movimentações de Estoque

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/stock-movement` | Lista todas as movimentações |
| GET | `/stock-movement/{id}` | Retorna movimentação por ID |
| POST | `/stock-movement` | Cria movimentação |
| PUT | `/stock-movement/{id}` | Atualiza movimentação |
| DELETE | `/stock-movement/{id}` | Remove movimentação |

**MovementType:** `ENTRY`, `EXIT` (para saídas, são obrigatórios `saleValue` e `saleDate`)

### Relatórios

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/reports/products` | Resumo de estoque por produto (query opcional: `productType`) |
| GET | `/reports/profit` | Lucro por produto |

### Health Check

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/health` | Retorna "OK" se a API está ativa |

---

## 7. Como Executar os Testes

### Backend

```bash
cd stock-control-api/stock-control-api
./mvnw test
```

No Windows: `mvnw.cmd test`