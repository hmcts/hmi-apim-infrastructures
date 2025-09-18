resource "azurerm_api_management_policy_fragment" "invalid_destination_response" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-invalid-destination-response"
  format            = "rawxml"
  description       = "This fragment contains code which send error response if request has invalid destination in header"
  value             = file("${path.module}/resources/policy-fragments/invalid-destination-response-fragment.xml")
}

resource "azurerm_api_management_policy_fragment" "failed_destination_authentication_response" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-failed-destination-authentication-response"
  format            = "rawxml"
  description       = "This fragment contains code which send error response if destination fails to authenticate HMI request"
  value             = file("${path.module}/resources/policy-fragments/failed-destination-authentication-response-fragment.xml")
}

resource "azurerm_api_management_policy_fragment" "elinks_auth_bearer" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-elinks-auth-bearer"
  format            = "rawxml"
  description       = "This fragment contains code to generate the authentication token to communicate with eLinks"
  value = replace(file("${path.module}/resources/policy-fragments/elinks-auth-bearer-fragment.xml"),
  "#keyVaultHost#", var.key_vault_host)
}

resource "azurerm_api_management_policy_fragment" "elinks_auth_token" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-elinks-auth-token"
  format            = "rawxml"
  description       = "This fragment contains code to generate the authentication token to communicate with eLinks"
  value = replace(file("${path.module}/resources/policy-fragments/elinks-auth-token-fragment.xml"),
  "#keyVaultHost#", var.key_vault_host)
}

resource "azurerm_api_management_policy_fragment" "cath-auth-token-generation" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-cath-auth-token-generation"
  format            = "rawxml"
  description       = "This fragment contains code to generate the authentication token to communicate with CaTH"
  value = replace(file("${path.module}/resources/policy-fragments/cath-auth-token-generation-fragment.xml"),
  "#keyVaultHost#", var.key_vault_host)
}

resource "azurerm_api_management_policy_fragment" "snl-auth-token-generation" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-snl-auth-token-generation"
  format            = "rawxml"
  description       = "This fragment contains code to generate the authentication token to communicate with SNL"
  value = replace(file("${path.module}/resources/policy-fragments/snl-auth-token-generation-fragment.xml"),
  "#keyVaultHost#", var.key_vault_host)
}

resource "azurerm_api_management_policy_fragment" "snl-headers" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-snl-headers"
  format            = "rawxml"
  description       = "This fragment contains code to set the headers to communicate with SNL"
  value = replace(file("${path.module}/resources/policy-fragments/snl-headers-fragment.xml"),
  "#keyVaultHost#", var.key_vault_host)
}