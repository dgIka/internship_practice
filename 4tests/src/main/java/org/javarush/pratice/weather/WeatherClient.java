package org.javarush.pratice.weather;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLOutput;
import java.time.Duration;
import java.util.List;

public class WeatherClient {
    private static final String API_KEY = "b634f3ff5e0c4ad094f101348251806";
    private static final String CITY = "Moscow";

    private static CookieManager cookieManager = new CookieManager();

    public static void main(String[] args) {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .cookieHandler(cookieManager)
                .build();

        String apiURI = "http://api.weatherapi.com/v1/current.json?key="
                + API_KEY + "&q="
                + CITY + "&aqi=no";

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(apiURI))
                .version(HttpClient.Version.HTTP_1_1)
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    System.out.println("Полный ответ API:\n: " + responseBody);

                    try{
                        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

                        if(jsonObject.has("error")) {
                            JsonObject error = jsonObject.getAsJsonObject("error");
                            System.out.println("Ошибка API: " + error.get("message").getAsString());

                            return;
                        }

                        JsonObject current = jsonObject.getAsJsonObject("current");
                        JsonObject location = jsonObject.getAsJsonObject("location");
                        double tempC = current.get("temp_c").getAsDouble();
                        String condition = current.getAsJsonObject("condition").get("text").getAsString();
                        String country = location.get("country").getAsString();

                        System.out.println("Текущая погода в " + CITY + " " + country + ": ");
                        System.out.println("Температура: " + tempC);
                        System.out.println("Облачность: " + condition);

                        List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
                        if (cookies.isEmpty()) {
                            System.out.println("Куки отсутствуют");
                        } else {
                            for (HttpCookie cookie : cookies) {
                                System.out.println("Cookie: " + cookie.getName() + ": " + cookie.getValue());
                            }
                        }


                    } catch (Exception e) {
                        System.out.println("Ошибка парсинга данных" + e.getMessage());
                    }
                }).exceptionally(e -> {
                    System.err.println("Ошибка HTTP Request: " + e.getMessage());
                    return null;
                }).join();
    }
}
