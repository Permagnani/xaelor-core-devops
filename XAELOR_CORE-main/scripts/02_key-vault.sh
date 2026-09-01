#!/bin/bash

# =========================================================
# 02_key-vault.sh
# Cria o Key Vault e armazena todos os dados sensiveis:
# credenciais do MySQL e credenciais do ACR
# =========================================================

# Variaveis
# ALTERE PARA SEU RM
rm=564995
resourceGroup="rg-xaelor-core"
location="canadacentral"

MYSQL_ROOT_PASSWORD=senha-root-xaelor
MYSQL_DATABASE=db_xaelor
MYSQL_USER=user-xaelor
MYSQL_PASSWORD=senha-xaelor

# O host "564995-mysql-xaelor" e um placeholder.
# Na hora de subir a API (script 04), o IP/FQDN real do
# container MySQL vai substituir esse valor via comando sed.
SPRING_DATASOURCE_URL=jdbc:mysql://564995-mysql-xaelor:3306/db_xaelor
SPRING_DATASOURCE_USERNAME=$MYSQL_USER
SPRING_DATASOURCE_PASSWORD=$MYSQL_PASSWORD

acrName="acrxaelor$rm"
ACRUSERNAME=$(az acr credential show --name $acrName --resource-group $resourceGroup --query username --output tsv)
ACRPASSWORD=$(az acr credential show --name $acrName --resource-group $resourceGroup --query passwords[0].value --output tsv)
keyVaultName="kv-xaelor-$rm"

# Registra o Servico do Key Vault na Assinatura
az provider register --namespace Microsoft.KeyVault

# Criar o Key Vault
if ! az keyvault show --name "$keyVaultName" --resource-group "$resourceGroup" &> /dev/null; then
  az keyvault create --name "$keyVaultName" --resource-group "$resourceGroup" --location "$location"
else
  echo "Key Vault '$keyVaultName' ja existe no Grupo de Recurso '$resourceGroup'."
fi

# Conceder acesso de ADM no Key Vault para nossa Assinatura
az role assignment create \
  --assignee $(az account show --query user.name -o tsv) \
  --role "Key Vault Administrator" \
  --scope /subscriptions/$(az account show --query id -o tsv)/resourceGroups/$resourceGroup/providers/Microsoft.KeyVault/vaults/$keyVaultName

sleep 15

# Armazenar os dados sensiveis
az keyvault secret set --vault-name $keyVaultName --name mysql-root-password --value "$MYSQL_ROOT_PASSWORD"
az keyvault secret set --vault-name $keyVaultName --name mysql-database --value "$MYSQL_DATABASE"
az keyvault secret set --vault-name $keyVaultName --name mysql-user --value "$MYSQL_USER"
az keyvault secret set --vault-name $keyVaultName --name mysql-password --value "$MYSQL_PASSWORD"
az keyvault secret set --vault-name $keyVaultName --name spring-datasource-url --value "$SPRING_DATASOURCE_URL"
az keyvault secret set --vault-name $keyVaultName --name spring-datasource-username --value "$SPRING_DATASOURCE_USERNAME"
az keyvault secret set --vault-name $keyVaultName --name spring-datasource-password --value "$SPRING_DATASOURCE_PASSWORD"
az keyvault secret set --vault-name $keyVaultName --name acr-username --value "$ACRUSERNAME"
az keyvault secret set --vault-name $keyVaultName --name acr-password --value "$ACRPASSWORD"