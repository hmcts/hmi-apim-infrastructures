## Defaults
variable "department" {
  default = "sds"
}
variable "product" {
  default = "hmi"
}
variable "product_name" {
  default = "apim"
}
variable "component" {
  default = "api"
}
variable "location" {
  default = "UK South"
}
variable "env" {}
variable "subscription" {
  default = ""
}
variable "deployment_namespace" {
  default = ""
}
variable "common_tags" {
  type = map(string)
}
variable "key_vault_host" {
  type        = string
  description = "Keyvault host for a environment"
}
variable "rate-call-limit" {
  type        = string
  description = "rate limit for apim"
}
variable "oauth_role" {
  type        = string
  default = "hmiGatewayNonProd"
  description = "The oAuth role to use within the policy files"
}