package client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    HttpClient client = HttpClient.newHttpClient();
    String url;
    String token;

    public KVTaskClient(String url) {
        this.url = url;
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                token = response.body();
                System.out.println(token);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес");
        }
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "/save/" + key + "/?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Добавлен новый элемент!");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес");
        }

    }

    public String load(String key) {
        URI uri = URI.create(url + "/load/" + key + "/?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        String result;
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                result = response.body();
                return result;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес");
        }
        return null;
    }
}
