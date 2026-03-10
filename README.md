# 🚚 Portal Super Trans - Sistema de Cadastro de Empresas

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/postgresql-4169e1?style=for-the-badge&logo=postgresql&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-Integration-blue?style=for-the-badge)

## 📖 Sobre o Projeto
O **Portal Super Trans** é uma aplicação Java de linha de comando (CLI) desenvolvida para gerenciar o cadastro de diferentes tipos de parceiros comerciais (Fornecedores, Revendedores e Clientes Finais).

Este projeto foi construído com foco na aplicação de boas práticas de **Engenharia de Software**, utilizando **Programação Orientada a Objetos (POO)**, arquitetura em camadas e persistência de dados em um banco de dados relacional (PostgreSQL) através da API **JDBC**.



## 🚀 Funcionalidades
- **Cadastro Especializado:** Suporte para três entidades de negócios distintas usando herança:
    - `Empresa Jurídica` (Validação estrita de 14 dígitos para CNPJ).
    - `Empresa Física` (Validação estrita de 11 dígitos para CPF).
    - `Empresa Estrangeira` (Identificador internacional flexível).
- **Validação de Regras de Negócio (Fail-Fast):** Tratamento de erros e exceções customizadas no momento da instanciação dos objetos.
- **Persistência Relacional:** Salva e recupera informações dinamicamente do banco de dados PostgreSQL.
- **Tipagem Segura:** Utilização de `Enums` para garantir a integridade dos papéis corporativos no sistema.

## 🏗️ Arquitetura e Padrões Aplicados
O código-fonte está estruturado em pacotes lógicos, promovendo alta coesão e baixo acoplamento:
- `supertrans.model`: Classes de domínio, entidades e Enums (Aplicação de Herança e Encapsulamento).
- `supertrans.service`: Regras de negócio e lógica de persistência (Separação de Responsabilidades).
- `supertrans.database`: Gerenciamento e fábrica de conexões com o banco.
- `supertrans.Main`: Interface de interação com o usuário (CLI).

### Estratégia de Banco de Dados (Joined Tables)
Para lidar com o polimorfismo das classes no banco de dados, foi adotada a estratégia de *Tabelas Separadas* (Joined Strategy). Uma tabela "mãe" (`empresa`) guarda os dados comuns, enquanto tabelas "filhas" (`empresa_fisica`, `empresa_juridica`, etc.) guardam os dados específicos, ligadas por *Foreign Keys* (Chaves Estrangeiras).



## 🛠️ Tecnologias Utilizadas
- **Linguagem:** Java 11+
- **Banco de Dados:** PostgreSQL
- **Conectividade:** JDBC (Java Database Connectivity) Driver
- **IDE:** IntelliJ IDEA

## ⚙️ Como Executar o Projeto Localmente

### 1. Pré-requisitos
Certifique-se de ter instalado em sua máquina:
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/)
- [PostgreSQL](https://www.postgresql.org/download/) e pgAdmin

### 2. Configurando o Banco de Dados
Abra o pgAdmin, crie um banco de dados chamado `super_trans_db` e execute o seguinte script SQL na *Query Tool* para criar as tabelas:

```sql
CREATE TABLE empresa (
    id SERIAL PRIMARY KEY,
    nome_fantasia VARCHAR(100) NOT NULL,
    perfil VARCHAR(50) NOT NULL,
    faturamento_direto BOOLEAN NOT NULL,
    documento_anexo VARCHAR(255),
    aprovado BOOLEAN NOT NULL,
    tipo_empresa VARCHAR(20) NOT NULL
);

CREATE TABLE empresa_juridica (
    id_empresa INT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    razao_social VARCHAR(150) NOT NULL,
    cnpj VARCHAR(14) NOT NULL UNIQUE
);

CREATE TABLE empresa_fisica (
    id_empresa INT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE
);

CREATE TABLE empresa_estrangeira (
    id_empresa INT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    razao_social VARCHAR(150) NOT NULL,
    identificador_estrangeiro VARCHAR(50) NOT NULL
);