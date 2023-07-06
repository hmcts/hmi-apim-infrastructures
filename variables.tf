## Defaults
variable "product" {
  default = "hmi-apim"
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
