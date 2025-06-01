package br.com.challenge.conversormoedas.modelo;

import br.com.challenge.conversormoedas.service.ConsumoApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Gerencia o menu e as operações de conversão de moedas.
 * Permite ao usuário selecionar diferentes tipos de conversão e salva o histórico em arquivo JSON.
 */

public class MenuConversor {
    private Scanner leitura = new Scanner(System.in);
    private final String API_KEY = "bfc4810ce7af94fd1b359849";
    private final String ENDERECO = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    // Objeto para consumir a API de conversão
    private ConsumoApi consumoApi = new ConsumoApi();
    // Lista para armazenar o histórico de conversões
    private List<Conversao> listaDeConsulta = new ArrayList<>();

    public void exibeMenu() {
        while (true) {
            //while (!opcao.equalsIgnoreCase("sair")) {
            System.out.println("""
                    *** CONVERSOR DE MOEDAS ***
                    Escolha uma opção:
                    
                    1 - Converter de Dolar para Real
                    2 - Converter de Euro para Real
                    3 - Converter de Peso Argentino para Real
                    4 - Converter de Real para Dolar
                    5 - Converter de Real para Euro
                    6 - Converter de Real para Peso Argentino
                    7 - Converter moeda personalizada
                    
                    Digite SAIR para encerrar: 
                    """);

            String opcao = leitura.nextLine();

            if (opcao.equalsIgnoreCase("sair")) {
                gerarArquivoConsulta();
                System.out.println("ENCERRANDO O PROGRAMA");
                leitura.close();
                break;
            }

            switch (opcao) {
                case "1" -> consultarMoeda("USD", "BRL");
                case "2" -> consultarMoeda("EUR", "BRL");
                case "3" -> consultarMoeda("ARS", "BRL");
                case "4" -> consultarMoeda("BRL", "USD");
                case "5" -> consultarMoeda("BRL", "EUR");
                case "6" -> consultarMoeda("BRL", "ARS");
                case "7" -> {
                    System.out.println("Digite a moeda a ser convertida (Ex: BRL )");
                    String moeda = leitura.nextLine().toUpperCase().trim();
                    System.out.println("Digite a moeda destino (Ex: USD): ");
                    String moedaConvertida = leitura.nextLine().toUpperCase().trim();
                    consultarMoeda(moeda, moedaConvertida);
                }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private double lerValor(String mensagem) {
        while (true) {
            System.out.println(mensagem);
            // Substitui vírgula por ponto para aceitar ambos como separador decimal
            String input = leitura.nextLine().replace(",", ".").trim();

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, digite um número válido (exemplo: 100 ou 100.50).");
            }
        }
    }

     //Realiza a conversão de moedas usando a API e exibe o resultado.
    private void consultarMoeda(String moeda, String moedaConvertida) {
        double valor = lerValor("Digite o valor que deseja converter: ");
        var json = consumoApi.obterDados(ENDERECO + moeda);

        // Converte o JSON para um objeto Java
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        // pega as taxas de conversão do objeto JSON
        JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

        if (!jsonObject.has("conversion_rates") || !conversionRates.has(moedaConvertida)) {
            System.out.println("\nMoeda inválida. Tente novamente.");
            return;
        }

        // pega a taxa de conversão e calcula o valor convertido
        double taxa = conversionRates.get(moedaConvertida).getAsDouble();
        double resultado = valor * taxa;

        System.out.printf("Valor convertido: %.2f %s é igual a %.2f %s", valor, moeda, resultado, moedaConvertida + "\n");

        // guarda a conversão na listaDeConsulta
        Conversao conversao = new Conversao( valor, moeda, resultado, moedaConvertida);
        listaDeConsulta.add(conversao);
    }

    private void gerarArquivoConsulta() {
        // Cria um objeto Gson com formatação JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(listaDeConsulta);

        // Escreve o JSON no arquivo
        try (FileWriter arquivo = new FileWriter("lista_conversor.json", true)) {
            arquivo.write(json);
            arquivo.write(System.lineSeparator());
            listaDeConsulta.clear();
            System.out.println("Arquivo gerado com sucesso");
        } catch (IOException e) {
            System.out.println(MessageFormat.format("Erro ao salvar arquivo", e.getMessage()));
        }
    }
}
