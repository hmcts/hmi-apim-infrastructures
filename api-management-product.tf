module "api_mgmt_product" {
  source                = "git@github.com:hmcts/cnp-module-api-mgmt-product?ref=master"
  name                  = local.api-mgmt-prod-name
  approval_required     = "false"
  subscription_required = "false"
  api_mgmt_name         = local.apim_name
  api_mgmt_rg           = local.api_resource_group
  product_policy        = replace(file("${path.module}/resources/policy-files/product-policy.xml"), "#keyVaultHost#", var.key_vault_host)
}