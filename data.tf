data "azurerm_api_management_product" "apim_product" {
  count               = local.deploy_apim
  product_id          = "${var.product}-product-${local.env}"
  resource_group_name = local.apim_rg
  api_management_name = local.apim_name
}