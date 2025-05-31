package br.com.challenge.conversormoedas.menu;

import br.com.challenge.conversormoedas.service.ConsumoApi;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Scanner;

public class MenuConversor {
    private Scanner leitura = new Scanner(System.in);
    private final String API_KEY = "bfc4810ce7af94fd1b359849";
    private final String ENDERECO = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";
    private ConsumoApi consumoApi = new ConsumoApi();

    public void exibeMenu() {
        String opcao = "";

        while (!opcao.equals("sair")) {
            System.out.println("\n*** CONVERSOR DE MOEDAS ***");
            System.out.println("1 - Converter de USD para outra moeda");
            System.out.println("2 - Converter de EUR para outra moeda");
            System.out.println("3 - Converter de BRL para outra moeda");
            System.out.println("4 - Converter moeda personalizada");

            System.out.println("Digite 'sair' para encerrar.");
            System.out.print("Escolha uma opção: ");

            opcao = leitura.nextLine();

            switch (opcao) {
                case "1" -> consultarMoeda("USD");
                case "2" -> consultarMoeda("EUR");
                case "3" -> consultarMoeda("BRL");
                case "4" -> {
                    System.out.println
                            ("Digite a moeda base (Ex: ARS )");
                    String moeda = leitura.nextLine().toUpperCase().trim();
                    consultarMoeda(moeda);
                }
                case "sair" -> System.out.println("ENCERRANDO O PROGRMA");
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void consultarMoeda(String moeda) {
        System.out.println("Digite a moeda que quer converter (EX: BRL): ");
        String moedaConvertida = leitura.nextLine().toUpperCase().trim();

        System.out.println("Digite o valor que deseja converter: ");
        double valor = Double.parseDouble(leitura.nextLine());

        var json = consumoApi.obterDados(ENDERECO + moeda);

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        //dentro de conversion_rates contem um objeto com todas as moedas e suas taxas de conversão
        JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

        //pega a taxa da moeda a ser convertida
        double taxa = conversionRates.get(moedaConvertida).getAsDouble();
        double resultado = valor * taxa;

        System.out.printf("Valor convertido: %.2f %s = %.2f %s\n",
                valor, moeda, resultado, moedaConvertida);


        /*
        var json = consumoApi.obterDados(ENDERECO + moeda);
        System.out.println("Resposta da API: ");
        System.out.println(json);

         */
    }
}
