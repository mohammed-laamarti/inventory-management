output "aks_name" {
  description = "Nom du cluster AKS"
  value       = azurerm_kubernetes_cluster.aks.name
}

output "kube_config" {
  description = "Configuration Kubeconfig du cluster AKS"
  value       = azurerm_kubernetes_cluster.aks.kube_config_raw
  sensitive   = true
}

output "acr_login_server" {
  description = "URL du ACR existant"
  value       = data.azurerm_container_registry.existing_acr.login_server
}
