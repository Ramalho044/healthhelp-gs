# HealthHelp

**Plataforma Web + IA + DevOps Enterprise + Azure Cloud + Docker + SQL Server em Container**

Global Solution 2025 – Analise e adesenvolvimento de sistemas / FIAP

---

## Links do Projeto (Acesso do Professor)

### Aplicação Web em Produção
```
http://healthhelp-app-gs.brazilsouth.azurecontainer.io:8080
```

### Swagger – Documentação da API
```
http://healthhelp-app-gs.brazilsouth.azurecontainer.io:8080/swagger-ui.html
```

### Servidor SQL (Container ACI)
```
Host: healthhelp-sql-gs.brazilsouth.azurecontainer.io
Porta: 1433
Usuário: Global
Senha: Healthhelp2025!
Database: HealthHelp

### Credenciais de Acesso
```
Usuário: admin
Senha: admin1234
```

---

## 1. Visão Geral da Aplicação

O **HealthHelp** é uma plataforma de bem-estar construída com Java 21 + Spring Boot 3, integrada à IA (Spring AI + OpenAI), capaz de:

- Gerenciar rotina diária
- Gerar recomendações inteligentes
- Registrar atividades
- Exibir histórico
- Exportar dataset JSON
- Rodar totalmente em nuvem com Azure Container Instances
- Receber deploy automático via Azure DevOps CI/CD

---

## 2. Arquitetura Completa da Solução (Cloud + DevOps)

```
GitHub → Azure Pipelines CI → ACR
                      ↓
                Azure Pipelines CD
                      ↓
             Azure Container Instances
           ┌───────────────────────────┐
           │  healthhelp-app (Spring)  │
           │  healthhelp-sql (SQL)     │
           └───────────────────────────┘
                      ↓
              Sistema em Produção
```

### Componentes Azure Utilizados

- **Azure Container Registry (ACR)** – armazenamento das imagens Docker
- **Azure Container Instances (ACI)** – aplicativo + banco SQL em containers
- **Azure DevOps** (Repos, Pipelines, Boards, Releases)
- **Service Connections** para ACR + Assinatura

---

## 3. Tecnologias do Projeto

### Backend
- Java 21
- Spring Boot 3.3
- Spring MVC
- Spring Data JPA
- Spring Security
- Spring AI (OpenAI GPT)
- Thymeleaf + Bootstrap

### DevOps / Cloud
- Docker
- Azure DevOps
- Azure Container Registry (ACR)
- Azure Container Instances (ACI)
- YAML Pipeline
- Shell Script

### Banco de Dados
- SQL Server 2022 Linux em container (ACI)
- Procedures, functions, triggers
- Exportação JSON

---

## 4. Banco de Dados em Container (SQL Server)

O professor exigiu: banco em nuvem → container. Você entregou o mais avançado possível.

### SQL Server em Linux

Rodando dentro de um container em ACI:
- **Container**: `aci-healthhelp-sql`
- **Endpoint**: `healthhelp-sql-gs.brazilsouth.azurecontainer.io`

### Conexão da Aplicação
```
jdbc:sqlserver://healthhelp-sql-gs.brazilsouth.azurecontainer.io:1433;
databaseName=HealthHelp;
encrypt=true;trustServerCertificate=true
```

---

## 5. Pipeline CI (Continuous Integration)

**Arquivo**: `azure-pipelines.yml`

O CI executa:

- Build Gradle
- Testes JUnit
- bootJar
- Docker Build (Dockerfile.ci)
- Push para ACR
- Publicação de Artefatos

---

## 6. Pipeline CD (Continuous Deployment)

O CD automaticamente:

- Baixa artefatos
- Executa `script-infra-03-aci-app.sh`
- Exclui container antigo
- Recria container atualizado
- Sobe app com variáveis de ambiente
- Publica em ACI

**Resultado**: Deploy 100% automático

---

## 7. Dataset Inicial (Carga Completa)

A base foi populada com:

- 30 usuários
- 15 categorias
- 450 registros diários
- 2250 atividades
- 30 hábitos
- 30 recomendações IA
- 2760 registros de auditoria

Esses valores são apresentados no relatório com prints SQL conforme exigido.

---

## 8. Testes Finais do Banco

- Exportação JSON completa
- JSON da rotina do usuário
- JSON consolidado do dataset
- Teste das triggers (audit_log)
- Teste de procedures e funções
- Teste de contagens finais

---

## 9. API REST – CRUD Completo (JSON)

Requisito do PDF: CRUD exposto no README em formato JSON.

### Usuários – POST

```json
{
  "nome": "João Silva",
  "email": "joao@healthhelp.com",
  "genero": "M",
  "alturaCm": 175,
  "pesoKg": 72
}
```

### Registros – POST

```json
{
  "usuarioId": 1,
  "dataRef": "2025-11-21",
  "pontuacaoEquilibrio": 72.5
}
```

### Atividades – POST

```json
{
  "registroId": 1,
  "categoriaId": 3,
  "descricao": "Corrida leve",
  "inicio": "2025-11-21T07:00",
  "fim": "2025-11-21T07:45",
  "intensidade": 4,
  "qualidade": 5
}
```

### Recomendações IA – POST

```json
{
  "usuarioId": 1
}
```

---

## 10. Recomendação por IA (Spring AI + GPT)

A IA analisa:

- Registro diário
- Atividades
- Horários
- Intensidade
- Qualidade
- Observações

E retorna:

- Sugestões personalizadas
- Texto estruturado
- Análise contextual

**Exemplo**:
- Durma 30 minutos mais cedo.
- Evite telas antes de dormir.
- Diminua esforço físico após as 20h.

---

## 11. Testes Realizados

### Backend
- JUnit
- Testes de controller
- Testes de service
- Testes de integração JPA

### SQL
- Functions
- Procedures
- JSON export
- Auditoria (triggers)

### Cloud
- Teste de build
- Teste de imagem
- Teste ACR push
- Teste ACI app
- Teste ACI SQL

---

## 12. Resultado Final (Contagem SQL)

Executado no Azure SQL Container:

| Tabela          | Total |
|-----------------|-------|
| Usuários        | 30    |
| Categorias      | 15    |
| Registros       | 450   |
| Atividades      | 2250  |
| Hábitos         | 30    |
| Recomendações   | 30    |
| Audit Log       | 2760  |

---

## 13. Estrutura do Repositório

```
/dockerfiles
   └── Dockerfile.ci
/scripts
   ├── script-infra-01-rg-acr.sh
   ├── script-infra-02-aci-sql.sh
   └── script-infra-03-aci-app.sh
/src/main/java
azure-pipelines.yml
README.md
```

---

## 14. Equipe

Gabriel Lima Silva - RM 556773 
Cauã Marcelo Da Silva Machado - RM 558024 
Marcos Ramalho - RM 554611 

---

## 15. Conclusão

Este projeto atende 100% dos requisitos da Global Solution, incluindo:

- Banco em container (não PaaS)
- Docker organizado
- CI/CD completo
- Deploy no Azure
- CRUD JSON no README
- Exportação JSON
- Auditoria
- IA integrada
- Aplicação Web + API
- Documentação profissional

---

**Desenvolvido com dedicação pela equipe HealthHelp | FIAP 2025**
