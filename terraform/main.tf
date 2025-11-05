##########################################
# Resource Group existant (récupéré depuis Azure)
##########################################
data "azurerm_resource_group" "aks_rg" {
  name = var.resource_group_name
}

##########################################
# ACR existant (récupéré depuis Azure)
##########################################
data "azurerm_container_registry" "existing_acr" {
  name                = var.acr_name
  resource_group_name = var.resource_group_name
}

##########################################
# AKS Cluster
##########################################
resource "azurerm_kubernetes_cluster" "aks" {
  name                = var.cluster_name
  location            = data.azurerm_resource_group.aks_rg.location
  resource_group_name = data.azurerm_resource_group.aks_rg.name
  dns_prefix          = "inventory"

  default_node_pool {
    name       = "default"
    node_count = var.node_count
    vm_size    = var.node_vm_size
  }

  identity {
    type = "SystemAssigned"
  }

  role_based_access_control_enabled = true

  tags = {
    environment = var.environment
    project     = "inventory-management"
  }
}

##########################################
# Donner à AKS le droit de puller depuis ton ACR
##########################################
resource "azurerm_role_assignment" "aks_acr_pull" {
  principal_id         = azurerm_kubernetes_cluster.aks.kubelet_identity[0].object_id
  role_definition_name = "AcrPull"
  scope                = data.azurerm_container_registry.existing_acr.id
}
