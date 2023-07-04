## Defaults
variable "product" {
  default = "hmi"
}
variable "component" {
  default = "apim"
}
variable "location" {
  default = "UK South"
}
variable "component" {
  default = "hmi"
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
