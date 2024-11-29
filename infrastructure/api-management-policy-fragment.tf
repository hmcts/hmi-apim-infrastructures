resource "azurerm_api_management_policy_fragment" "example" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-snl-token-generation-policy-fragment"
  format            = "rawxml"
  description       = "This fragment contains code to generate the snl auth token"
  value             = replace(replace(file("${path.module}/resources/policy-fragments/generate-snl-auth-token.xml"),
    "#keyVaultHost#", var.key_vault_host),
    "#sAndLOauthUrl#", length(data.azurerm_key_vault_secret.snl_OAuth_url) > 0 ? data.azurerm_key_vault_secret.snl_OAuth_url[0].value : "")
}