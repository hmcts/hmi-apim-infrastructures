provider "azurerm" {
  features {}
}

locals {
  env = (var.env == "aat") ? "stg" : (var.env == "sandbox") ? "sbox" : "${(var.env == "perftest") ? "test" : "${var.env}"}"

  env_long_name = var.env == "sbox" ? "sandbox" : var.env == "stg" ? "staging" : var.env
  env_subdomain = local.env_long_name == "prod" ? "" : "${local.env_long_name}."

  apim_name = "sds-api-mgmt-${local.env}"
  apim_rg   = "ss-${local.env}-network-rg"


  deploy_apim = local.env == "demo" ||local.env == "ithc" || local.env == "test" || local.env == "stg" || local.env == "sbox" || local.env == "prod" ? 1 : 0

  prefix = var.product
}

data "azurerm_api_management" "sds_apim" {
  name                = local.apim_name
  resource_group_name = local.apim_rg
}

data "azurerm_user_assigned_identity" "hmi" {
  name                = "hmi-${var.env}-mi"
  resource_group_name = "managed-identities-${var.env}-rg"
}
