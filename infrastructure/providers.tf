terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      version = "4.32.0"
    }
    azapi = {
      source  = "Azure/azapi"
      version = "~> 2.3.0"
    }
  }
}