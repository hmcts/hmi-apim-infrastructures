terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      version = "4.71.0"
    }
    azapi = {
      source  = "Azure/azapi"
      version = "~> 2.8.0"
    }
  }
}