#!/usr/bin/env bash
set -e

# SCRIPT 03 - APP HEALTHHELP EM ACI

SUBSCRIPTION="2TDSPW-RM558024-CauãMarceloMachado"
RG_NAME="rg-healthhelp"
LOCATION="brazilsouth"

ACR_NAME="acrhealthhelprm558024"
APP_ACI_NAME="aci-healthhelp-app"
DNS_APP="healthhelp-app-gs"

IMAGE_REPOSITORY="healthhelp-gs"
IMAGE_TAG="${IMAGE_TAG:-latest}"   # pode sobrescrever via variável

# Dados do banco
DB_SERVER_FQDN="healthhelp-sql-gs.brazilsouth.azurecontainer.io"
DB_NAME="HealthHelp"
DB_USER="Global"
DB_PASSWORD="Healthhelp2025!"

echo ">> [APP-ACI] Selecionando subscription..."
az account set --subscription "$SUBSCRIPTION"

echo ">> [APP-ACI] Coletando informações do ACR..."
ACR_LOGIN_SERVER=$(az acr show -n "$ACR_NAME" --query loginServer -o tsv)
ACR_USERNAME=$(az acr credential show -n "$ACR_NAME" --query username -o tsv)
ACR_PASSWORD=$(az acr credential show -n "$ACR_NAME" --query passwords[0].value -o tsv)

IMAGE_FULL="${ACR_LOGIN_SERVER}/${IMAGE_REPOSITORY}:${IMAGE_TAG}"

echo ">> [APP-ACI] Removendo container antigo (se existir)..."
az container delete \
  --resource-group "$RG_NAME" \
  --name "$APP_ACI_NAME" \
  --yes || echo "Nenhum container anterior para remover."

echo ">> [APP-ACI] Criando container ACI para a aplicação..."
az container create \
  --resource-group "$RG_NAME" \
  --name "$APP_ACI_NAME" \
  --image "$IMAGE_FULL" \
  --os-type Linux \
  --cpu 2 \
  --memory 4 \
  --ports 8080 \
  --dns-name-label "$DNS_APP" \
  --ip-address Public \
  --registry-login-server "$ACR_LOGIN_SERVER" \
  --registry-username "$ACR_USERNAME" \
  --registry-password "$ACR_PASSWORD" \
  --environment-variables \
    "SPRING_DATASOURCE_URL=jdbc:sqlserver://${DB_SERVER_FQDN}:1433;databaseName=${DB_NAME};encrypt=true;trustServerCertificate=true" \
    "SPRING_DATASOURCE_USERNAME=${DB_USER}" \
    "SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}"

echo ">> [APP-ACI] Container criado."
echo "   URL: http://${DNS_APP}.${LOCATION}.azurecontainer.io:8080"
