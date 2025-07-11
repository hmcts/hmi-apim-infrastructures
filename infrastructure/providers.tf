terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      version = "4.33.0"
    }
    azapi = {
      source  = "Azure/azapi"
      version = "~> 2.4.0"
    }
  }
}