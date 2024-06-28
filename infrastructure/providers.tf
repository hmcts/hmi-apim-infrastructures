terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      version = "3.110.0"
    }
  }
}