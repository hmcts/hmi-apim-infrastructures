locals {
  policies_data = jsondecode(file("${path.module}/resources/policy-files/environments/${local.env}.json"))

  policy_file_template = [for policy in local.policies_data.policies :
    {
      operation_id = policy.operationId
      xml_content = replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(file("${path.module}/resources/policy-files/${policy.templateFile}"),
        "#keyVaultHost#", var.key_vault_host),
        "#pihHost#", length(data.azurerm_key_vault_secret.pip_client_host) > 0 ? data.azurerm_key_vault_secret.pip_client_host[0].value : ""),
        "#vhHost#", length(data.azurerm_key_vault_secret.vh_client_host) > 0 ? data.azurerm_key_vault_secret.vh_client_host[0].value : ""),
        "#cftHost#", length(data.azurerm_key_vault_secret.cft_client_host) > 0 ? data.azurerm_key_vault_secret.cft_client_host[0].value : ""),
        "#cftOauthUrl#", length(data.azurerm_key_vault_secret.cft_OAuth_url) > 0 ? data.azurerm_key_vault_secret.cft_OAuth_url[0].value : ""),
        "#crimeHost#", length(data.azurerm_key_vault_secret.crime_client_host) > 0 ? data.azurerm_key_vault_secret.crime_client_host[0].value : ""),
        "#elinksHost#", length(data.azurerm_key_vault_secret.elinks_client_host) > 0 ? data.azurerm_key_vault_secret.elinks_client_host[0].value : ""),
        "#sAndLHost#", length(data.azurerm_key_vault_secret.snl_client_host) > 0 ? data.azurerm_key_vault_secret.snl_client_host[0].value : ""),
        "#sAndLOauthUrl#", length(data.azurerm_key_vault_secret.snl_OAuth_url) > 0 ? data.azurerm_key_vault_secret.snl_OAuth_url[0].value : ""),
      "#enableCrimeNewHealthCheck#", var.enable-crime-new-health-check)
      display_name = policy.display_name
      method       = policy.method
      url_template = policy.url_template
      description  = policy.description
      headers = can(policy.headers) ? [for header in policy.headers :
        {
          name          = header.name
          required      = header.required
          type          = header.type
          default_value = header.default_value
        }
      ] : null
      response = can(policy.response) ? [for endPointResponse in policy.response :
        {
          status_code = endPointResponse.status_code
          description = endPointResponse.description
        }
      ]: null
      query_parameters = can(policy.query_parameters) ? [for query_parameter in policy.query_parameters :
        {
          name          = query_parameter.name
          required      = query_parameter.required
          type          = query_parameter.type
          default_value = query_parameter.default_value
        }
      ] : null
      template_parameters = can(policy.template_parameters) ? [for template_parameter in policy.template_parameters :
        {
          name          = template_parameter.name
          required      = template_parameter.required
          type          = template_parameter.type
          default_value = template_parameter.default_value
        }
      ] : null
      tag = can(policy.tag) ? {
        name         = policy.tag.name
        display_name = policy.tag.display_name
      } : null
    }
  ]
}