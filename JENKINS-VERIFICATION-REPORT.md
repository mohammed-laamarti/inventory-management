# 📋 Rapport de Vérification - Jenkinsfiles et Configuration

## ✅ RÉSUMÉ DE LA VÉRIFICATION

### 1. **Cohérence avec Terraform** ✅
Tous les Jenkinsfiles sont parfaitement alignés avec `terraform/variables.tf` :
- **ACR_NAME**: `mohammed` ✅
- **AKS_CLUSTER**: `aks` ✅
- **AKS_RESOURCE_GROUP**: `aks-Cloud` ✅
- **Region**: Sweden Central ✅

### 2. **Cohérence avec Kubernetes** ✅
Tous les Jenkinsfiles correspondent aux fichiers de déploiement K8s :

| Service | Jenkinsfile | K8s Deployment | Image | Port | Namespace |
|---------|-------------|----------------|-------|------|-----------|
| Product | ✅ | ✅ product-deployment.yaml | mohammed.azurecr.io/product-service | 8090 | inventory |
| Supplier | ✅ | ✅ supplier-deployment.yaml | mohammed.azurecr.io/supplier-service | 8091 | inventory |
| Raw Material | ✅ | ✅ raw-material-deployment.yaml | mohammed.azurecr.io/raw-material-service | 8092 | inventory |

### 3. **Fichiers Créés** ✅
- ✅ `product/Jenkinsfile` - Existait déjà
- ✅ `supplier/Jenkinsfile` - **CRÉÉ**
- ✅ `raw-material/Jenkinsfile` - **CRÉÉ**

---

## 🔧 CONFIGURATION JENKINS REQUISE

### Étape 1: Installer les Plugins Jenkins
Dans **Manage Jenkins** → **Manage Plugins** → **Available**

```
✅ Azure Credentials
✅ Azure CLI Plugin
✅ Docker Pipeline
✅ Pipeline
✅ Git
✅ Kubernetes CLI Plugin
✅ Maven Integration
✅ Pipeline: Stage View
✅ Trivy (optionnel pour le scan de sécurité)
```

### Étape 2: Créer les Credentials Azure
Dans **Manage Jenkins** → **Manage Credentials** → **(global)** → **Add Credentials**

**Type**: Azure Service Principal
- **ID**: `azure-credentials` (IMPORTANT: doit correspondre au Jenkinsfile)
- **Subscription ID**: Votre Azure Subscription ID
- **Client ID**: Votre Service Principal App ID
- **Client Secret**: Votre Service Principal Password
- **Tenant ID**: Votre Azure Tenant ID

#### Comment obtenir ces informations:
```bash
# Créer un Service Principal
az ad sp create-for-rbac --name jenkins-sp --role contributor \
  --scopes /subscriptions/{subscription-id}/resourceGroups/aks-Cloud

# Output (GARDEZ CES INFORMATIONS):
{
  "appId": "xxxxx",           # → Client ID
  "password": "xxxxx",        # → Client Secret
  "tenant": "xxxxx"           # → Tenant ID
}
```

### Étape 3: Créer les Pipelines Jenkins

#### Option A: Pipeline Multibranch (RECOMMANDÉ)
Créez 3 pipelines multibranch (un par service):

**1. Product Service Pipeline**
- **Nom**: `product-service-pipeline`
- **Branch Sources**: Git
- **Repository URL**: Votre repo Git
- **Script Path**: `product/Jenkinsfile`
- **Scan Repository Triggers**: Periodically (15 minutes)

**2. Supplier Service Pipeline**
- **Nom**: `supplier-service-pipeline`
- **Branch Sources**: Git
- **Repository URL**: Votre repo Git
- **Script Path**: `supplier/Jenkinsfile`
- **Scan Repository Triggers**: Periodically (15 minutes)

**3. Raw Material Service Pipeline**
- **Nom**: `raw-material-service-pipeline`
- **Branch Sources**: Git
- **Repository URL**: Votre repo Git
- **Script Path**: `raw-material/Jenkinsfile`
- **Scan Repository Triggers**: Periodically (15 minutes)

#### Option B: Pipeline Simple
Pour chaque service, créez un pipeline:
- **Type**: Pipeline
- **Definition**: Pipeline script from SCM
- **SCM**: Git
- **Script Path**: `{service}/Jenkinsfile`

---

## 🎯 FONCTIONNALITÉS DES JENKINSFILES

### Chaque Jenkinsfile inclut:

1. **🔍 Smart Detection**
   - Détecte automatiquement les changements dans le dossier du service
   - Skip du build si aucun changement n'est détecté

2. **📦 Build Maven**
   - Compilation avec Maven
   - Skip des tests pour un build plus rapide

3. **🐳 Docker Build & Push**
   - Build de l'image Docker
   - Tag avec le commit Git (7 premiers caractères)
   - Push vers Azure Container Registry

4. **🔒 Security Scan**
   - Scan avec Trivy pour détecter les vulnérabilités
   - Génération de rapports SARIF
   - Archive des résultats

5. **☸️ Déploiement AKS**
   - Connexion automatique au cluster AKS
   - Mise à jour du deployment avec la nouvelle image
   - Rollout status pour vérifier le déploiement
   - Rollback automatique en cas d'échec

6. **✅ Vérification**
   - Affichage du statut des pods
   - Affichage du statut du deployment
   - Affichage du statut du service

7. **🧹 Nettoyage**
   - Suppression des images Docker locales
   - Logout Azure
   - Économie d'espace disque

---

## 📊 WORKFLOW DE DÉPLOIEMENT

```
┌─────────────────┐
│   Git Push      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Jenkins détecte │
│  les changements│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Build Maven    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Build Docker   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Scan Trivy     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Push to ACR    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Deploy to AKS  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Vérifier     │
└─────────────────┘
```

---

## ⚠️ POINTS IMPORTANTS

### 1. Prérequis sur l'Agent Jenkins
L'agent Jenkins doit avoir installé:
- ✅ Java 17 (pour Maven)
- ✅ Maven 3.6+
- ✅ Docker
- ✅ Azure CLI
- ✅ kubectl
- ✅ Trivy (optionnel)

### 2. Permissions Azure
Le Service Principal doit avoir:
- ✅ **AcrPull** sur l'ACR `mohammed`
- ✅ **Contributor** sur le Resource Group `aks-Cloud`
- ✅ **Azure Kubernetes Service Cluster User Role** sur le cluster AKS

### 3. Namespace Kubernetes
Assurez-vous que le namespace existe:
```bash
kubectl create namespace inventory
```

### 4. Secrets Kubernetes
Les deployments utilisent des secrets. Assurez-vous qu'ils existent:
```bash
# Pour Product et Raw Material (PostgreSQL)
kubectl create secret generic postgres-secret \
  --from-literal=username=admin \
  --from-literal=password=admin \
  -n inventory

# Pour Supplier (MongoDB)
kubectl create secret generic mongo-secret \
  --from-literal=username=admin \
  --from-literal=password=admin \
  -n inventory
```

---

## 🔄 DÉPLOIEMENT INITIAL

Pour déployer tous les services la première fois:

```bash
# 1. Déployez d'abord l'infrastructure K8s
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/secrets.yaml
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/mongo-deployment.yaml
kubectl apply -f k8s/kafka-deployment.yaml

# 2. Déployez les services de configuration
kubectl apply -f k8s/config-server-deployment.yaml
kubectl apply -f k8s/discovery-deployment.yaml
kubectl apply -f k8s/gateway-deployment.yaml

# 3. Déployez les microservices
kubectl apply -f k8s/product-deployment.yaml
kubectl apply -f k8s/supplier-deployment.yaml
kubectl apply -f k8s/raw-material-deployment.yaml
```

Ensuite, les Jenkinsfiles prendront le relais pour les mises à jour automatiques!

---

## 🐛 DÉPANNAGE

### Problème: "No such image" lors du push
**Solution**: Vérifiez que vous êtes bien connecté à ACR
```bash
az acr login --name mohammed
```

### Problème: "Forbidden" lors du deploy K8s
**Solution**: Vérifiez les credentials AKS
```bash
az aks get-credentials --resource-group aks-Cloud --name aks --overwrite-existing
```

### Problème: "Deployment not found"
**Solution**: Déployez d'abord le deployment initial
```bash
kubectl apply -f k8s/{service}-deployment.yaml
```

### Problème: Pipeline toujours skip
**Solution**: Forcez le build ou modifiez la détection de changements dans le Jenkinsfile

---

## 📝 MODIFICATIONS FUTURES

Si vous voulez modifier les noms ou configurations:

1. **Changer le nom du cluster AKS**: Modifiez `AKS_CLUSTER` dans les 3 Jenkinsfiles
2. **Changer le namespace**: Modifiez `K8S_NAMESPACE` dans les 3 Jenkinsfiles
3. **Changer l'ACR**: Modifiez `ACR_NAME` dans les 3 Jenkinsfiles
4. **Ajouter des tests**: Retirez `-DskipTests` dans la commande Maven

---

## ✅ CHECKLIST FINALE

Avant de lancer les pipelines, vérifiez:

- [ ] Plugins Jenkins installés
- [ ] Credentials Azure créés avec l'ID `azure-credentials`
- [ ] Service Principal a les bonnes permissions
- [ ] Namespace `inventory` existe dans K8s
- [ ] Secrets `postgres-secret` et `mongo-secret` créés
- [ ] Deployments K8s initiaux déployés
- [ ] Agent Jenkins a tous les outils installés (Java, Maven, Docker, Azure CLI, kubectl)
- [ ] Les 3 pipelines Jenkins créés

---

## 🎉 RÉSULTAT ATTENDU

Une fois tout configuré, chaque fois que vous faites un `git push`:
1. Jenkins détecte automatiquement les changements
2. Build et test du service concerné
3. Création de l'image Docker
4. Push vers Azure Container Registry
5. Déploiement automatique sur AKS
6. Vérification du déploiement

**Tout est automatisé! 🚀**

