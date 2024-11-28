resource "azurerm_api_management_policy_fragment" "example" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-product-policy-fragment"
  format            = "rawxml"
  value             = replace(file("${path.module}/resources/policy-fragments/generate-snl-auth-token.xml"),
                        "#keyVaultHost#", var.key_vault_host)
}