locals {
  policies_data = jsondecode(file("${path.module}/resources/policy-files/policies.json"))
}

output "policy_template_text" {
  value = [for policy in local.policies_data.policies :
    {
      operation_id = policy.operationId
    }
  ]
}