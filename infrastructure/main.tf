provider "azurerm" {
  features {}
}

locals {
  env = (var.env == "aat") ? "stg" : (var.env == "sandbox") ? "sbox" : "${(var.env == "perftest") ? "test" : "${var.env}"}"

  env_long_name = var.env == "sbox" ? "sandbox" : var.env == "stg" ? "staging" : var.env
  env_subdomain = local.env_long_name == "prod" ? "" : "${local.env_long_name}."

  base_url      = "sds-api-mgmt.${local.env_subdomain}platform.hmcts.net/${var.product}/"

  apim_name     = "sds-api-mgmt-${local.env}"
  apim_rg       = "ss-${local.env}-network-rg"


  deploy_apim = local.env == "test" ||local.env == "stg" || local.env == "sbox" || local.env == "prod" ? 1 : 0

  prefix            = var.product
}
