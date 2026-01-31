package com.dizzyz7.skycast

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Scanner

// Модель данных для JSON (только нужные поля)
data class WeatherResponse(
    val name: String,
    val main: MainData,
    val weather: List<WeatherDescription>
)

data class MainData(val temp: Double, val humidity: Int, val pressure: Int)
data class WeatherDescription(val description: String)

fun main() {
    val scanner = Scanner(System.`in`)
    val client = OkHttpClient()
    val gson = Gson()
    val apiKey = "YOUR_API_KEY_HERE" // Сюда вставляется ключ от OpenWeatherMap

    println("--- 🌤️ SkyCast Weather App ---")
    print("Введите название города: ")
    val city = scanner.nextLine()

    val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric&lang=ru"

    val request = Request.Builder().url(url).build()

    try {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println("❌ Ошибка: Город не найден или сервис недоступен.")
                return
            }

            val body = response.body?.string()
            val weatherData = gson.fromJson(body, WeatherResponse::class.java)

            println("\n🌍 Погода в городе: ${weatherData.name}")
            println("🌡️ Температура: ${weatherData.temp}°C")
            println("☁️ Состояние: ${weatherData.weather[0].description.replaceFirstChar { it.uppercase() }}")
            println("💧 Влажность: ${weatherData.humidity}%")
            println("⏲️ Давление: ${weatherData.pressure} hPa")
        }
    } catch (e: Exception) {
        println("📡 Ошибка сети: Не удалось подключиться к серверу.")
    }
}
