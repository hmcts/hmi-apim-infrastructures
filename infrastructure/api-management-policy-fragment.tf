resource "azurerm_api_management_policy_fragment" "vh_auth_token_generation" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-vh-auth-token-generation"
  format            = "rawxml"
  description       = "This fragment contains code to generate the authentication token to communicate with Video Hearing"
  value = replace(replace(replace(file("${path.module}/resources/policy-fragments/vh-auth-token-generation-fragment.xml"),
    "#keyVaultHost#", var.key_vault_host),
    "#vhOauthUrl#", length(data.azurerm_key_vault_secret.vh_OAuth_url) > 0 ? data.azurerm_key_vault_secret.vh_OAuth_url[0].value : ""),
  "#vhHost#", length(data.azurerm_key_vault_secret.vh_client_host) > 0 ? data.azurerm_key_vault_secret.vh_client_host[0].value : "")
}

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

resource "azurerm_api_management_policy_fragment" "vh_get_api_version" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-vh-get-api-version"
  format            = "rawxml"
  description       = "This fragment contains code which is used by video hearing to extract api version from url"
  value             = file("${path.module}/resources/policy-fragments/vh-get-api-version-fragment.xml")
}

resource "azurerm_api_management_policy_fragment" "invalid_query_parameters_response" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-invalid-query-parameters-response"
  format            = "rawxml"
  description       = "This fragment contains code which send error response if request contains invalid query parameters"
  value             = file("${path.module}/resources/policy-fragments/invalid-query-parameters-response-fragment.xml")
}

resource "azurerm_api_management_policy_fragment" "missing_mandatory_updated_since_response" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-missing-mandatory-updated-since-response"
  format            = "rawxml"
  description       = "This fragment contains code which send error response if request contains invalid query parameters"
  value             = file("${path.module}/resources/policy-fragments/missing-mandatory-updated-since-response-fragment.xml")
}

resource "azurerm_api_management_policy_fragment" "elinks_mock_person_response" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-elinks-mock-person-response"
  format            = "rawxml"
  description       = "This fragment contains code which send error response if request contains invalid query parameters"
  value             = file("${path.module}/resources/policy-fragments/elinks-mock-person-response-fragment.xml")
}

resource "azurerm_api_management_policy_fragment" "elinks_mock_multi_person_response" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-elinks-mock-multi-person-response"
  format            = "rawxml"
  description       = "This fragment contains code which send error response if request contains invalid query parameters"
  value             = file("${path.module}/resources/policy-fragments/elinks-mock-multi-person-response-fragment.xml")
}

resource "azurerm_api_management_policy_fragment" "elinks_auth_token_generation" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-elinks-auth-token-generation"
  format            = "rawxml"
  description       = "This fragment contains code to generate the authentication token to communicate with Video Hearing"
  value = replace(replace(file("${path.module}/resources/policy-fragments/elinks-auth-token-generation-fragment.xml"),
    "#keyVaultHost#", var.key_vault_host),
    "#elinksHost#", var.elinks_host)
}