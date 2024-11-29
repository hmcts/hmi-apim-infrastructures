resource "azurerm_api_management_policy_fragment" "snl_auth_token_generation" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-snl-auth-token-generation"
  format            = "rawxml"
  description       = "This fragment contains code to generate the authentication token to communicate with SNL"
  value             = replace(replace(file("${path.module}/resources/policy-fragments/snl-auth-token-generation-fragment.xml"),
    "#keyVaultHost#", var.key_vault_host),
    "#sAndLOauthUrl#", length(data.azurerm_key_vault_secret.snl_OAuth_url) > 0 ? data.azurerm_key_vault_secret.snl_OAuth_url[0].value : "")
}

resource "azurerm_api_management_policy_fragment" "mock_generic_response" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-mock-generic-response"
  format            = "rawxml"
  description       = "This fragment contains code which generate generic response for MOCK"
  value             = file("${path.module}/resources/policy-fragments/mock-generic-response-fragment.xml")
}