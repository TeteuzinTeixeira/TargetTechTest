# Tech Test - API de Processamento e Comissão de Vendas

API REST desenvolvida em **Java** e **Spring Boot** responsável pelo recebimento, cálculo e consolidação das comissões de vendas de uma equipe comercial. O projeto foi construído seguindo os princípios de **Clean / Hexagonal Architecture** e utiliza **PostgreSQL** rodando via **Docker**.

---

## 🛠️ Tecnologias e Ferramentas

* **Java 25+**
* **Spring Boot 4+** (Spring Web)
* **PostgreSQL** (via Docker Compose)
* **Maven** (Gerenciamento de dependências)

---

## 📐 Arquitetura do Projeto

O projeto utiliza uma estrutura desacoplada em camadas baseada nos conceitos de *Ports and Adapters* / *Clean Architecture*:

```
src/main/java/com/target/TechTest/
│
├── adapter/                     # Camada de Adaptadores (Interfaces Externas)
│   └── controller/              # Endpoints REST e DTOs da API
│       ├── dto/                 # Objetos de requisição/resposta (Request/Response)
│       └── SellsController.java # Entrypoint do HTTP POST /vendas
│
├── core/                        # Camada Central (Regras de Negócio Puras)
│   ├── entity/                  # Entidades de Domínio anêmicas e puros objetos
│   └── useCase/                 # Casos de uso com a lógica de negócio encapsulada
│       └── ProcessSellUseCase.java
│
└── TechTestApplication.java     # Classe principal de inicialização do Spring

```

## ⚙️ Regra de Negócio de Comissão

O cálculo de comissão sobre cada venda individual segue a seguinte regra tarifária:

| Valor da Venda | % de Comissão |
| :--- | :--- |
| **Abaixo de R$ 100,00** | 0% (Isento) |
| **R$ 100,00 até R$ 499,99** | 1% |
| **A partir de R$ 500,00** | 5% |

No processamento, a aplicação recebe uma lista de vendas, aplica os percentuais individualmente por transação e consolida o valor total devido a cada vendedor.


## 🚀 Como Executar o Projeto

#### Pré-requisitos
* **JDK 25** ou superior instalado.
* **Docker** e **Docker Compose**.

#### 1. Subir o Banco de Dados (PostgreSQL)
Execute o Docker Compose para provisionar a instância do PostgreSQL:

```bash
docker-compose up -d
```

## 2. Executar a Aplicação Spring Boot

Utilize o Maven Wrapper para iniciar o servidor:

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível, por padrão, na porta `8080`.

### 📌 Documentação da API

#### `POST /api/sell`

Processa o lote de vendas e retorna o total consolidado de comissão por vendedor.

**Request Body (`application/json`)**

```json
{
  "vendas": [
    {
      "vendedor": "João Silva",
      "valor": 1200.50
    },
    {
      "vendedor": "João Silva",
      "valor": 80.00
    },
    {
      "vendedor": "Maria Souza",
      "valor": 300.00
    }
  ]
}
```

**Response Body (`200 OK`)**

```json
[
  {
    "vendedor": "João Silva",
    "totalComissao": 60.03
  },
  {
    "vendedor": "Maria Souza",
    "totalComissao": 3.00
  }
]
```

#### `POST /api/stock/movimentation`

Realiza uma entrada ou saída de mercadoria do depósito e retorna a quantidade final em estoque.

**Request Body (`application/json`)**

```json
{
  "codigoProduto": 101,
  "tipo": "SAIDA",
  "descricao": "Venda de balcão",
  "quantidade": 20
}
```

**Response Body (`200 OK`)**

```json
{
  "codigoProduto": 101,
  "descricaoProduto": "Caneta Azul",
  "tipoMovimentacao": "SAIDA",
  "descricaoMovimentacao": "Venda de balcão",
  "quantidadeMovimentada": 20,
  "quantidadeEstoqueFinal": 130
}
```

#### `POST /api/fees/calculate`

Calcula o valor dos juros com base na data de vencimento e valor informado, aplicando 2,5% ao dia de atraso.

**Request Body (`application/json`)**

```json
{
  "valor": 100.00,
  "dataVencimento": "2026-10-01"
}
```

**Response Body (`200 OK`)**

```json
{
  "valorOriginal": 100.00,
  "dataVencimento": "2026-10-01",
  "dataCalculo": "2026-10-05",
  "diasAtraso": 4,
  "valorMulta": 10.00,
  "valorTotal": 110.00
}
```

## 🧪 Testes Unitários

A camada de Casos de Uso (`core/useCase`) foi amplamente testada para garantir a integridade de todas as regras de negócio de forma isolada, rápida e independente de dependências externas (como banco de dados ou redes).

Os testes foram construídos utilizando **JUnit 5**, **AssertJ** para asserções fluentes e **Mockito** para isolamento de dependências nos repositories.

### 📊 Cobertura de Testes (`core/useCase`)

| Métrica | Cobertura | Status |
| :--- | :---: | :---: |
| **Linhas (Line Coverage)** | **100%** | 🟢 |
| **Ramificações (Branch Coverage)** | **100%** | 🟢 |

---

### 🛡️ Cenários Cobertos

* **`ProcessSellUseCase`**:
    * Validação de listas nulas, vazias ou objetos com campos incompletos.
    * Faixas de comissão de 0% (abaixo de R$ 100), 1% (R$ 100 a R$ 499,99) e 5% (R$ 500+).
    * Consolidação e agrupamento correto de totais por vendedor.
* **`MovimentProductUseCase`**:
    * Sucesso nas movimentações de `ENTRADA` e `SAIDA`.
    * Lançamento de exceção para produtos não encontrados (`404/400`).
    * Validação de consistência e exceção ao tentar realizar saída sem saldo suficiente.
* **`CalculateFeeUseCase`**:
    * Contas no prazo ou com vencimento futuro (0% de multa).
    * Cálculo exato da multa diária de 2,5% ao dia conforme os dias de atraso.
    * Precisão e arredondamento financeiro de casas decimais (`HALF_UP`).

Para executar a suíte de testes unitários da aplicação, utilize o comando:

```bash
./mvnw test