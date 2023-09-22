resource "azurerm_api_management_certificate" "api_management_crime_cert" {
  name                         = "hmi-crime-cert"
  api_management_name          = data.azurerm_api_management.sds_apim.name
  resource_group_name          = local.apim_rg
  key_vault_secret_id          = data.azurerm_key_vault_certificate.crime_cert.secret_id
  key_vault_identity_client_id = data.azurerm_user_assigned_identity.hmi.client_id
}
