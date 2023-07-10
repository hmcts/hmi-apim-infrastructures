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
