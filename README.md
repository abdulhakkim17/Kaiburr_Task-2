# Kaiburr_Task-2

# Java Backend REST API - Kubernetes Deployment

#Screenshots of implementation is inside: Task 2/Screenshots

#Source code is inside: Task 2/task-manager/k8s

This project contains a Java-based REST API deployed in a Kubernetes cluster with MongoDB as the database. The deployment includes Docker containerization, Kubernetes manifests. 

The integration with the Kubernetes API for dynamic pod execution is done in the application code. However when executing it, the dynamic pod creation alone causes troble.

#Features:
- Application and MongoDB images using Docker and images are pused to the Docker hub for pod creation
- Kubernetes deployment with MongoDB in a separate pod, which replica set of 1
- Application is also deployed in a seperate pod using kubernetes deployment, which has replica set of 1
- MongoDB pod is internal pod used by the application, thus created a service type as Node Port
- Application endpoints exposed using a LoadBalancer Service
- Persistent volume to create volume which can be claimed for Mongodb pod and then Persistent volume claim was able to mount the volume to the Mongodb pod
- Dynamic pod creation for executing shell commands inside Kubernetes pods [Error] 

---

#Prerequisites
Ensure you have the following installed:
- Java 21, Maven
- Docker (for containerization)
- Kubectl (for Kubernetes CLI)
- Docker Desktop (Kubernetes cluster)


