package client;

import exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private final String url;
    private String token;

    public KVTaskClient(String url) {
        client = HttpClient.newHttpClient();
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
            throw new ManagerSaveException(e.getMessage());
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
            throw new ManagerSaveException(e.getMessage());
        }

    }

    public String load(String key) {
        URI uri = URI.create(url + "/load/" + key + "/?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println(uri);
                throw new ManagerSaveException("Ошибка при получении данных. Код статуса: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }
}
