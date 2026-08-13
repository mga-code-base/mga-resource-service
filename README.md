# 🏋️‍♂️ mga-resource-service

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4%2F4.0-6DB33F?logo=springboot)](https://spring.io/)
[![Java Version](https://img.shields.io/badge/Java-25-007396?logo=openjdk)](https://openjdk.org/)
[![Build Tool](https://img.shields.io/badge/Build-Gradle%209.4.1-066DA5?logo=gradle)](https://gradle.org/)
[![GraalVM](https://img.shields.io/badge/GraalVM-Native%20Image-orange?logo=graalvm)](https://www.graalvm.org/)

## 📌 Service Overview

`mga-resource-service` handles business workflows for class schedule management, trainer allocation, capacity validation, slot bookings, and gym equipment state management.

It sits behind the Kubernetes Gateway (Porkbun DNS → Cloudflare → K8s Gateway) and delegates persistence calls downstream to `mga-resource-crud-service`.

### 🏛️ Architectural Features
* Edge Ingress & Authorization: Receives public ingress traffic routed through K8s Gateway and enforces user role authorization checks.
* CQRS Orchestration: Validates availability and business rules before delegating data mutations and queries down to `mga-resource-crud-service` via internal RestClient calls.
* GraalVM Native Image Packaging: Compiled Ahead-Of-Time (AOT) using Java 25 & Gradle 9.4.1 for instant container startup and optimized Kubernetes cluster resource utilization.

---

## 🛠️ Technical Specifications
* Port: 8085 (Kubernetes Gateway Ingress)
* Language/Build: Java 25 | Gradle 9.4.1
* Image Runtime: GraalVM Native Image AOT
* Downstream Integration: mga-resource-crud-service (Port 8081)
* Shared Contract: mga-all-models-jars

---

## 📡 Core API Controller Endpoints

### Classes & Schedules Controller (`/api/v1/resources/classes`)
* `GET /api/v1/resources/classes` - View upcoming fitness classes and schedules
* `GET /api/v1/resources/classes/{id}` - Retrieve detailed schedule for a specific class
* `POST /api/v1/resources/classes` - Create new fitness class schedule (Admin/Trainer)
* `PUT /api/v1/resources/classes/{id}` - Modify existing class schedule or capacity limits
* `DELETE /api/v1/resources/classes/{id}` - Cancel/remove class schedule

### Bookings Controller (`/api/v1/resources/bookings`)
* `POST /api/v1/resources/bookings` - Reserve a slot in an available class
* `GET /api/v1/resources/bookings/me` - List active class bookings for authenticated member
* `DELETE /api/v1/resources/bookings/{bookingId}` - Cancel existing class reservation

### Equipment Management Controller (`/api/v1/resources/equipment`)
* `GET /api/v1/resources/equipment` - Retrieve status and availability of gym equipment
* `PUT /api/v1/resources/equipment/{id}/maintenance` - Flag equipment for maintenance or update operational status

---

## ⚙️ Build & Deployment Instructions
# Standard JVM Run
./gradlew bootRun

# GraalVM Native Image Compilation
./gradlew nativeCompile

# Paketo OCI Native Container Image
./gradlew bootBuildImage
