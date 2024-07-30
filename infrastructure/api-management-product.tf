module "api_mgmt_product" {
  source                = "git@github.com:hmcts/cnp-module-api-mgmt-product?ref=master"
  name                  = local.api-mgmt-prod-name
  approval_required     = "false"
  subscription_required = "false"
  api_mgmt_name         = local.apim_name
  api_mgmt_rg           = local.api_resource_group
  //Setting this to 0 prevents terraform flagging this as a change each time, as the default is 20 which never gets set due to the subscription_required being false
  subscriptions_limit   = "0"
  product_policy = replace(replace(file("${path.module}/resources/policy-files/product-policy.xml"),
    "#keyVaultHost#", var.key_vault_host),
  "#rateCallLimit#", var.rate-call-limit)
}