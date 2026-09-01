#!/bin/bash

# =========================================================
# 03_aci-mysql.sh
# Sobe o Container do MySQL no ACI, buscando as senhas no
# Key Vault e persistindo os dados no Storage Account
# =========================================================

# Variaveis
# ALTERE PARA SEU RM E SUA REGIAO (Politica)
rm=564995
location="canadacentral"
resourceGroup="rg-xaelor-core"
acrName="acrxaelor$rm"
aciName="564995-mysql-xaelor"
imageName="564995-mysql-xaelor"
tag="v1"
storageAccountName="stgxaelor$rm"
file_share_name="mysql-xaelor-volume"
storage_key=$(az storage account keys list --resource-group $resourceGroup --account-name $storageAccountName --query "[0].value" --output tsv)
keyVaultName="kv-xaelor-$rm"

# Registra o Servico de ACI na Assinatura
az provider register --namespace Microsoft.ContainerInstance

# Deploy do Container MySQL
az container create \
  --resource-group $resourceGroup \
  --name $aciName \
  --location $location \
  --image $acrName.azurecr.io/$imageName:$tag \
  --cpu 1 \
  --memory 1 \
  --os-type Linux \
  --dns-name-label mysql-xaelor-$rm \
  --ports 3306 \
  --registry-login-server $acrName.azurecr.io \
  --registry-username $(az keyvault secret show --vault-name $keyVaultName --name acr-username --query value -o tsv) \
  --registry-password $(az keyvault secret show --vault-name $keyVaultName --name acr-password --query value -o tsv) \
  --azure-file-volume-account-name $storageAccountName \
  --azure-file-volume-account-key $storage_key \
  --azure-file-volume-share-name $file_share_name \
  --azure-file-volume-mount-path /var/lib/mysql \
  --environment-variables \
    MYSQL_ROOT_PASSWORD=$(az keyvault secret show --vault-name $keyVaultName --name mysql-root-password --query value -o tsv) \
    MYSQL_DATABASE=$(az keyvault secret show --vault-name $keyVaultName --name mysql-database --query value -o tsv) \
    MYSQL_USER=$(az keyvault secret show --vault-name $keyVaultName --name mysql-user --query value -o tsv) \
    MYSQL_PASSWORD=$(az keyvault secret show --vault-name $keyVaultName --name mysql-password --query value -o tsv) \
  --restart-policy Always

# Testes apos a criacao (descomente para usar)
#
# az container logs --resource-group rg-xaelor-core --name 564995-mysql-xaelor
#
# az container exec --resource-group rg-xaelor-core --name 564995-mysql-xaelor \
#   --exec-command "mysql -uuser-xaelor -psenha-xaelor"
#
# use db_xaelor;
# select * from TB_PERFUME;