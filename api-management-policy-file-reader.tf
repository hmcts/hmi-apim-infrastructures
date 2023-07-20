locals {
  policies_data = jsondecode(file("${path.module}/resources/policy-files/policies.json"))
  policies = [for policy in local.policies_data.policies : policy]
}