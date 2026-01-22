locals {

  resource_group_name = "${local.prefix}-sharedinfra-sds-${var.env}-rg"
  key_vault_name      = "${local.prefix}-sds-kv-${var.env}"
  pip_client_host     = "pip-client-host"
  cft_client_host     = "cft-client-host"
  cft_OAuth_url       = "cft-OAuth-url"
  elinks_client_host  = "elinks-client-host"
  snl_client_host     = "snl-client-host"
  snl_OAuth_url       = "snl-OAuth-url"
  apim_resource_name  = "ss-${var.env}-network-rg"
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

data "azurerm_application_insights" "sds_app_insights" {
  name                = "sds-api-mgmt-${var.env}"
  resource_group_name = "ss-${var.env}-network-rg"
}
