resource "azurerm_api_management_certificate" "api_management_crime_cert" {
  name                = data.azurerm_key_vault_certificate.crime_cert.name
  api_management_name = data.azurerm_api_management.sds_apim.name
  resource_group_name = local.apim_rg
  data                = data.azurerm_key_vault_certificate.crime_cert.secret_id
}
