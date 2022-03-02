package com.example.cryptofx.utilidades;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static com.example.cryptofx.utilidades.Const.*;

public class CoinGecko {

    public static String getData(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getCoinPrice(String coin, String fiat) {
        String url = BASE_URL + SERVICE_GET_PRICE + PARAM_IDS + coin + AMPERSAND + PARAM_VS_CURRENCIES + fiat;
        String price = null;
        try {
             price = getData(url).replace("{", "").replace("}", "").replace(":", "").replace(fiat, "").replace(coin, "").replace("\"", "");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al obtener el precio de la moneda");
        }
        return price;
    }

    public List<String> getCoinsFormatted() {
        List<String> coins = List.of(COIN_ADA, COIN_BCH, COIN_BTC, COIN_DASH, COIN_EOS, COIN_ETC, COIN_ETH, COIN_LTC, COIN_NEO, COIN_XLM, COIN_XRP, COIN_ZEC, COIN_XMR, COIN_TRX, COIN_BTG);
        List<String> coinFormatted = new ArrayList<>();
        for (String coin : coins) {
            coinFormatted.add(upperCaseFirst(coin.trim().replace("-", " ")));
        }
        return coinFormatted;
    }

    public boolean checkConnection() {
        String response = null;
        try {
            response = getData(BASE_URL + SERVICE_PING);
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al comprobar la conexión");
        }
        return response != null && response.contains("Moon");
    }

    public String upperCaseFirst(String var) {
        return var != null ? var.substring(0, 1).toUpperCase() + var.substring(1) : "";
    }
}