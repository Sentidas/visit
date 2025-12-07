# Используем официальный образ с Java
FROM eclipse-temurin:21-jdk

# Рабочая директория
WORKDIR /app

# Копируем проект в контейнер
COPY . .

WORKDIR /app/country

# Делаем скрипты Gradle Wrapper исполняемыми
RUN chmod +x gradlew

# Устанавливаем Python для простого веб-сервера
RUN apt-get update && apt-get install -y python3


# Генерируем отчет и запускаем веб-сервер для Allure
CMD ["sh", "-c", "./gradlew clean test allureReport || true; exec python3 -m http.server 8081  --directory build/reports/allure-report/allureReport"]


