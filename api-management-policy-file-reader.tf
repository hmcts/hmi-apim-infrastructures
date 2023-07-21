locals {
  policies_data = jsondecode(file("${path.module}/resources/policy-files/policies.json"))
}