#!/usr/bin/env bash
set -e

SUBSCRIPTION_ID="b13a5623-ba4a-4d10-9956-8d4d4cea833c"
RESOURCE_GROUP="rg-healthhelp"
ACR_NAME="acrhealthhelprm558024"
ACI_NAME="aci-healthhelp-app"
DNS_NAME_LABEL="healthhelp-app-gs"
IMAGE_REPO="healthhelp-gs"
LOCATION="brazilsouth"

# Tag vem da variável de ambiente IMAGE_TAG; se não vier usa 'latest'
IMAGE_TAG="${IMAGE_TAG:-latest}"

echo ">> [APP-ACI] Selecionando subscription..."
az account set --subscription "$SUBSCRIPTION_ID"

echo ">> [APP-ACI] Coletando informações do ACR..."
ACR_LOGIN_SERVER=$(az acr show -n "$ACR_NAME" --query loginServer -o tsv)

echo ">> [APP-ACI] Removendo container antigo (se existir)..."
az container delete \
  --resource-group "$RESOURCE_GROUP" \
  --name "$ACI_NAME" \
  --yes || true

echo ">> [APP-ACI] Criando container ACI para a aplicação..."
az container create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$ACI_NAME" \
  --location "$LOCATION" \
  --os-type Linux \
  --image "${ACR_LOGIN_SERVER}/${IMAGE_REPO}:${IMAGE_TAG}" \
  --cpu 2 \
  --memory 4 \
  --registry-login-server "$ACR_LOGIN_SERVER" \
  --registry-username "$ACR_NAME" \
  --registry-password "$(az acr credential show -n "$ACR_NAME" --query passwords[0].value -o tsv)" \
  --dns-name-label "$DNS_NAME_LABEL" \
  --ports 8080 \
  --environment-variables \
  SPRING_DATASOURCE_URL="jdbc:sqlserver://healthhelp-sql-gs.br...abaseName=HealthHelp;encrypt=true;trustServerCertificate=true" \
  SPRING_DATASOURCE_USERNAME="Global" \
  SPRING_DATASOURCE_PASSWORD="Healthhelp2025!"


echo ">> [APP-ACI] Container criado com sucesso."
echo "   App:    http://${DNS_NAME_LABEL}.brazilsouth.azurecontainer.io:8080"
echo "   Swagger (Spring):"
echo "   -> http://${DNS_NAME_LABEL}.brazilsouth.azurecontainer.io:8080/swagger-ui.html"
echo "   ou"
echo "   -> http://${DNS_NAME_LABEL}.brazilsouth.azurecontainer.io:8080/swagger-ui/index.html"
