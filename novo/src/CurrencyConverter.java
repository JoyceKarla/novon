import org.json.JSONObject; // Importação da biblioteca JSON

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/a9e0451a5cc66dc64f1ab097/latest/USD"; // URL com a chave da API

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Conversor de Moedas ===");
            System.out.println("1. USD para EUR");
            System.out.println("2. USD para JPY");
            System.out.println("3. EUR para GBP");
            System.out.println("4. GBP para AUD");
            System.out.println("5. JPY para BRL");
            System.out.println("6. EUR para CAD");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("Saindo do programa.");
                break;
            }

            System.out.print("Digite o valor a ser convertido: ");
            double amount = scanner.nextDouble();

            String fromCurrency = getBaseCurrency(choice);
            String toCurrency = getTargetCurrency(choice);

            if (toCurrency != null) {
                double convertedAmount = convertCurrency(fromCurrency, toCurrency, amount);
                System.out.printf("%.2f %s = %.2f %s\n", amount, fromCurrency, convertedAmount, toCurrency);
            } else {
                System.out.println("Opção inválida.");
            }
        }
        scanner.close();
    }

    private static String getBaseCurrency(int choice) {
        switch (choice) {
            case 1:
            case 2:
                return "USD"; // Moeda base
            case 3:
                return "EUR"; // Moeda base para GBP
            case 4:
                return "GBP"; // Moeda base para AUD
            case 5:
                return "JPY"; // Moeda base para BRL
            case 6:
                return "EUR"; // Moeda base para CAD
            default:
                return null;
        }
    }

    private static String getTargetCurrency(int choice) {
        switch (choice) {
            case 1: return "EUR"; // USD para EUR
            case 2: return "JPY"; // USD para JPY
            case 3: return "GBP"; // EUR para GBP
            case 4: return "AUD"; // GBP para AUD
            case 5: return "BRL"; // JPY para BRL
            case 6: return "CAD"; // EUR para CAD
            default: return null;
        }
    }

    public static double convertCurrency(String from, String to, double amount) {
        try {
            // A URL da API já contém a moeda base (USD)
            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Exibe a resposta da API para depuração
            System.out.println("Resposta da API: " + response.toString());

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject rates = jsonResponse.getJSONObject("conversion_rates");

            if (rates.has(to)) {
                double rate = rates.getDouble(to);
                return amount * rate;
            } else {
                System.out.println("Moeda " + to + " não encontrada.");
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace(); // Imprime a pilha de erros
            return 0;
        }
    }
}
