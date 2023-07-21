## Defaults
variable "department" {
  default = "sds"
}
variable "product" {
  default = "HMI"
}
variable "product_name" {
  default = "APIM"
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
variable "pih_host" {
  type        = string
  description = "Publication (CaTH) hostname"
}
variable "snow_host" {
  type        = string
  description = "Snow Service hostname"
}
variable "policy_file_template" {
  type        = string
  description = "api policy file template to inject in apim module"
}