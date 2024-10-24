terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      version = "3.113.0"
    }
  }
}