# Modern Android Architecture 🚀

A production-ready Android application demonstrating modern Android development practices using **Kotlin**, **Jetpack Compose**, **Navigation 3**, **Clean Architecture**, **MVVM**, **Android Modularization**, **Dagger-Hilt**, **Google Maps Compose**, and more.

This project is designed as a scalable architecture reference for real-world applications.

---

## ✨ Features

- ✅ Android Modularization
- ✅ Clean Architecture
- ✅ MVVM Architecture
- ✅ Use Cases
- ✅ Repository Pattern
- ✅ Jetpack Compose UI
- ✅ Navigation 3
- ✅ Google Maps Compose
- ✅ Dagger-Hilt Dependency Injection
- ✅ Kotlin Coroutines
- ✅ StateFlow
- ✅ Lifecycle-aware UI
- ✅ Material 3 Design
- ✅ SOLID Principles
- ✅ Scalable Feature-based Architecture

---

## 🏗️ Architecture

This project follows **Clean Architecture** with **feature-based modularization**.

```
                Presentation
                     │
                ViewModel (MVVM)
                     │
                 Use Cases
                     │
                 Repository
                     │
              Remote / Local Data
```

Each layer has a single responsibility and depends only on abstractions.

---

## 📦 Project Structure

```
app/
│
├── core/
│   ├── common
│   ├── ui
│   ├── network
│   ├── navigation
│   └── database
│
├── feature-home/
├── feature-location/
├── feature-profile/
├── feature-auth/
│
├── domain/
│
└── data/
```

> Each feature is completely isolated and communicates only through well-defined interfaces.

---

## 🛠 Tech Stack

| Technology | Purpose |
|------------|---------|
| Kotlin | Programming Language |
| Jetpack Compose | UI Toolkit |
| Navigation 3 | Type-safe Navigation |
| MVVM | Presentation Architecture |
| Clean Architecture | Layer Separation |
| Modularization | Scalable Codebase |
| Dagger-Hilt | Dependency Injection |
| Coroutines | Asynchronous Programming |
| StateFlow | UI State Management |
| Google Maps Compose | Maps Integration |
| Material 3 | Modern UI Components |

---

## 📱 Screens

- Home
- Location
- Maps
- Profile
- Authentication

*(Add screenshots here after uploading the project.)*

---

## 🚀 Why Modularization?

This project separates the application into independent modules which provides:

- Faster Gradle builds
- Better scalability
- Easier maintenance
- Improved code ownership
- Parallel team development
- Better testability
- Reduced coupling

---

## 🧩 Design Principles

- Single Responsibility Principle (SRP)
- Dependency Inversion Principle (DIP)
- Separation of Concerns
- Unidirectional Data Flow
- SOLID Principles

---

## 📍 Google Maps

The project demonstrates integration of **Google Maps Compose** including:

- Interactive Map
- Camera Position
- Marker Placement
- Compose-first Map APIs

---

## 💉 Dependency Injection

Dependency injection is implemented using **Dagger-Hilt**.

Benefits include:

- Lifecycle-aware components
- Easy testing
- Decoupled architecture
- Simplified dependency management

---

## 📂 Build Requirements

- Android Studio (Latest Stable)
- Kotlin
- JDK 17+
- Android SDK
- Gradle

---

## 🎯 Purpose

This repository serves as:

- Android Architecture Reference
- Learning Resource
- Interview Preparation
- Production-ready Starter Template

---

## 🤝 Contributions

Contributions, suggestions, and improvements are always welcome.

Feel free to fork the repository and submit a Pull Request.

---

## 📄 License

This project is available under the MIT License.
