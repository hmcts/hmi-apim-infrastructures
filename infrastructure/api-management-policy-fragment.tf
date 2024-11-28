resource "azurerm_api_management_policy_fragment" "example" {
  api_management_id = data.azurerm_api_management.sds_apim.id
  name              = "${var.product}-product-policy-fragment"
  format            = "rawxml"
  value             = "<fragment><set-method>GET</set-method></fragment>"
}