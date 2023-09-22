resource "azurerm_api_management_certificate" "api_management_crime_cert" {
  name                = "hmi-crime-cert"
  api_management_name = data.azurerm_api_management.sds_apim.name
  resource_group_name = local.apim_rg
  data                = data.azurerm_key_vault_secret.crime_cert_base.value
  password            = data.azurerm_key_vault_secret.crime_cert_password.value
}