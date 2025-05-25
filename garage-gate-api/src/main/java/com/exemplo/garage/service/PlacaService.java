package com.exemplo.garage.service;

import com.exemplo.garage.controller.MultipartBodyPublisher;
import com.exemplo.garage.model.PlacaAutorizada;
import com.exemplo.garage.repository.PlacaAutorizadaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.util.List;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class PlacaService {

    private final PlacaAutorizadaRepository placaRepository;

    public PlacaService(PlacaAutorizadaRepository placaRepository) {
        this.placaRepository = placaRepository;
    }

    public List<PlacaAutorizada> listarTodas() {
        return placaRepository.findAll();
    }

    public void salvar(PlacaAutorizada placa) {
        placaRepository.save(placa);
    }

    public boolean placaEstaAutorizada(String placa) {
        return placaRepository.existsById(placa);
    }

    public String capturarEReconhecerPlaca(String urlIgnorado) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8000/ler-placa"))  // agora é GET
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Erro da API Python: " + response.body());
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());

            return json.get("placa").asText();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
  


}
