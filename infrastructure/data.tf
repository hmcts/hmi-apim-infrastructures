locals {

  resource_group_name = "${local.prefix}-sharedinfra-${var.env}-rg"
  key_vault_name      = "${local.prefix}-kv-${var.env}"
  pip_client_host     = "pip-client-host"
  servicenow_host     = "hmi-servicenow-host"
  vh_client_host      = "vh-client-host"
  vh_OAuth_url        = "vh-OAuth-url"
  cft_client_host     = "cft-client-host"
  cft_OAuth_url       = "cft-OAuth-url"
  crime_client_host   = "crime-client-host"
  elinks_client_host  = "elinks-client-host"
  snl_client_host     = "snl-client-host"
  snl_OAuth_url       = "snl-OAuth-url"
  crime_cert = "hmi-crime-cert-${var.env}"
}

data "azurerm_client_config" "current" {}

data "azurerm_key_vault" "kv" {
  name                = local.key_vault_name
  resource_group_name = local.resource_group_name
}

data "azurerm_key_vault_secret" "pip_client_host" {
  count        = local.deploy_apim
  name         = local.pip_client_host
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_secret" "servicenow_host" {
  count        = local.deploy_apim
  name         = local.servicenow_host
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_secret" "vh_client_host" {
  count        = local.deploy_apim
  name         = local.vh_client_host
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_secret" "vh_OAuth_url" {
  count        = local.deploy_apim
  name         = local.vh_OAuth_url
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_secret" "cft_client_host" {
  count        = local.deploy_apim
  name         = local.cft_client_host
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_secret" "cft_OAuth_url" {
  count        = local.deploy_apim
  name         = local.cft_OAuth_url
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_secret" "crime_client_host" {
  count        = local.deploy_apim
  name         = local.crime_client_host
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_secret" "elinks_client_host" {
  count        = local.deploy_apim
  name         = local.elinks_client_host
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_secret" "snl_client_host" {
  count        = local.deploy_apim
  name         = local.snl_client_host
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_secret" "snl_OAuth_url" {
  count        = local.deploy_apim
  name         = local.snl_OAuth_url
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_certificate" "crime_cert" {
  name         = local.crime_cert
  key_vault_id = data.azurerm_key_vault.kv.id
}
