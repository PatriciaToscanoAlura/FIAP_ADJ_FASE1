# FIAD_ADJT_FASE1
Trabalho Fase 1 - Postech Fiap ADJT - 2026

API "TodosRestaurantes" - O projeto completo está na pasta TodosRestaurantes.

Depois de baixar o projeto, será necessário criar na raiz (pasta TodosRestaurantes) o aquivo '.env' e configurar a variável JWT_SECRET nele. Essa variável deve conter a chave para o ENCODER. Depois disso, basta subir o container utilizando o docker compose. 

Na pasta "documentacao" (dentro de TodosRestaurantes) há um documento com informações sobre a API, incluindo as instruções e exemplo para criação da variável JWT_SECRET. Também há um arquivo 'TodosRestaurantes.yaml' com o Swagger projetado para API (gerado através da ferramenta Apicurito). O swagger do que está implementado na aplicação pode ser visualizado através da URI /swagger-ui/index.html. 

## 🛠️ Como Testar a API
Na raiz do projeto (TodosRestaurantes), você encontrará a pasta `collection` com o arquivo `TodosRestaurantes.json`. 
Basta importá-lo no Postman para ter acesso a todos os endpoints configurados.

