# 🛒 Store Product App

A modern Android application developed with **Jetpack Compose**, **Kotlin**, and **MVVM architecture**.  
This app displays a list of products fetched from an external API and cached locally for offline access.

## 📱 Features

- 🔍 Product listing with image, name, rating, and price
- 📦 Product detail screen with all specifications
- 📲 Onboarding experience using HorizontalPager
- ☁️ API integration using Retrofit
- 📁 Local caching with DataStore (JSON + SharedPreferences)
- 📶 Offline access with automatic retry on reconnection
- 🎨 UI built entirely with Jetpack Compose
- ✅ Persistent onboarding flag using Flow
- 🔐 Koin for dependency injection

## 🧰 Technologies

- Kotlin
- Jetpack Compose
- ViewModel + StateFlow
- Retrofit
- DataStore (Preferences)
- Coil (AsyncImage)
- Koin
- Material3 Design
- Navigation Compose

## 🚀 Architecture

The app follows the **MVVM** pattern with separation of concerns:
- `ViewModel` handles API and local caching
- `Composable screens` are reactive and clean
- `ProductCache` stores data locally and validates cache expiration

## 📦 Caching Strategy

- The app caches products locally using `DataStore` (Gson + JSON).
- If a cache exists and is not expired (10 min), data is loaded from local storage.
- If the internet is restored after being offline, the app attempts a background fetch.

## 📂 Project Structure

📁 ui/
├─ screen/
└─ components/
📁 viewmodel/
📁 local_data/
📁 network/
📁 model/


## 🧪 How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/robsonalves04/store_product

✍️ Author
Robson Alves
LinkedIn: https://www.linkedin.com/in/robson-alves04/| GitHub: https://github.com/robsonalves04

