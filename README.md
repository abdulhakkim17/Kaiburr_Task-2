# Kaiburr_Task-2

# Java Backend REST API - Kubernetes Deployment 🚀

This project contains a Java-based REST API deployed in a Kubernetes cluster with MongoDB as the database. The deployment includes Docker containerization, Kubernetes manifests, and integration with the Kubernetes API for dynamic pod execution.

## **Features**
- Containerized Java backend using Docker 🐳  
- Kubernetes deployment with **MongoDB in a separate pod**  
- Application endpoints exposed using a **LoadBalancer/NodePort/Ingress**  
- **Persistent volume** for MongoDB to ensure data retention  
- **Dynamic pod creation** for executing shell commands inside Kubernetes pods [Error] 

---

## **1. Prerequisites**
Ensure you have the following installed:
- **Java 21**, **Maven**
- **Docker** (for containerization)
- **Kubectl** (for Kubernetes CLI)
- **Minikube / Docker Desktop / AKS / EKS / GKE** (Kubernetes cluster)
- **Helm** (for MongoDB deployment)  [Not used]

---


