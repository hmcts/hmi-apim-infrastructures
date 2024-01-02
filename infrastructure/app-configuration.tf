locals {
  api_resource_group = "ss-${var.env}-network-rg"
  app_config_name = "app-config-${var.product}-${var.env}"
}

resource "azurerm_app_configuration" "app_conf" {
  name                = local.app_config_name
  resource_group_name = local.api_resource_group
  location            = var.location
}