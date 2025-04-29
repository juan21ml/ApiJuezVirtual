package com.tutorial.consumo_api.Service;

import com.tutorial.consumo_api.dto.PostDto;
import com.tutorial.consumo_api.model.Codigo;
import com.tutorial.consumo_api.repository.CodigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class PostService {

    @Autowired
    private CodigoRepository codigoRepository;

    private static final String API_URL = "https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=false&wait=true";
    private static final String API_KEY = "be1efce5a1mshb2526d1028d630ap11638fjsna2a4b46f1b7d";

    public ResultResponse ejecutarCodigo(PostDto postDto) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com");
        headers.set("X-RapidAPI-Key", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = "{\n" +
                "  \"source_code\": \"" + postDto.getSourceCode().replace("\"", "\\\"").replace("\n", "\\n") + "\",\n" +
                "  \"language_id\": " + postDto.getLanguageId() + ",\n" +
                "  \"stdin\": \"\",\n" +
                "  \"expected_output\": \"" + postDto.getExpectedOutput() + "\"\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

        // Guardar en la base de datos
        Codigo codigo = new Codigo();
        codigo.setSourceCode(postDto.getSourceCode());
        codigo.setExpectedOutput(postDto.getExpectedOutput());
        codigo.setOutputJudge0(response.getBody()); // Guardamos la respuesta de Judge0
        codigoRepository.save(codigo);

        // Lógica para analizar la respuesta y compararla con la salida esperada
        if (response.getStatusCode() == HttpStatus.OK) {
            // Analizar la respuesta de Judge0 para determinar si el código es correcto
            boolean isCorrect = response.getBody().contains("stdout") && response.getBody().contains(postDto.getExpectedOutput());
            return new ResultResponse(isCorrect ? "¡Código correcto!" : "Código incorrecto.");
        } else {
            return new ResultResponse("Error en la ejecución del código");
        }
    }

    // Clase para estructurar la respuesta como JSON
    public static class ResultResponse {
        private String message;

        public ResultResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}




