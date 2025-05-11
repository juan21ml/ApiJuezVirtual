package com.tutorial.consumo_api.Service;

import com.tutorial.consumo_api.dto.PostDto;
import com.tutorial.consumo_api.repository.CodigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PostService {

    @Autowired
    private CodigoRepository codigoRepository;

    private static final String API_URL = "https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=false&wait=true";
    private static final String API_KEY = "be1efce5a1mshb2526d1028d630ap11638fjsna2a4b46f1b7d";

    public ResultResponse ejecutarCodigo(PostDto postDto) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-RapidAPI-Key", API_KEY);
            headers.add("Content-Type", "application/json");

            String body = String.format("{\"source_code\": \"%s\", \"language_id\": %d, \"expected_output\": \"%s\"}",
                    postDto.getSourceCode(), postDto.getLanguageId(), postDto.getExpectedOutput());

            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String result = response.getBody();
                // Imprime la respuesta completa para inspección
                System.out.println("Resultado de la API: " + result);

                // Usar Jackson para parsear el JSON
                JsonNode rootNode = new ObjectMapper().readTree(result);

                String output = extractOutput(rootNode);
                String error = extractError(rootNode);

                // Normalizar las salidas para eliminar saltos de línea y espacios extra
                String normalizedOutput = output.replaceAll("\\s+", " ").trim();
                String normalizedExpectedOutput = postDto.getExpectedOutput().replaceAll("\\s+", " ").trim();

                // Log de la salida y comparación
                System.out.println("stdout (normalizado): '" + normalizedOutput + "'");
                System.out.println("stderr: '" + error + "'");
                System.out.println("Expected Output (normalizado): '" + normalizedExpectedOutput + "'");

                // Comparar la salida normalizada con la salida esperada normalizada
                if (normalizedOutput.equals(normalizedExpectedOutput)) {
                    return new ResultResponse("Ejecución exitosa: El resultado es correcto.", output, error);
                } else {
                    return new ResultResponse("❌ Error: La salida no coincide con la esperada.", output, error);
                }
            } else {
                return new ResultResponse("❌ Error en la ejecución del código: " + response.getStatusCode().toString(), "", "");
            }
        } catch (Exception e) {
            return new ResultResponse("❌ Error en el procesamiento: " + e.getMessage(), "", "");
        }
    }


    private String extractOutput(JsonNode responseNode) {
        // Extraer el valor de stdout de manera más robusta usando Jackson
        return responseNode.path("stdout").asText("");  // Retorna un string vacío si stdout no existe
    }

    private String extractError(JsonNode responseNode) {
        // Extraer el valor de stderr de manera más robusta usando Jackson
        return responseNode.path("stderr").asText("");  // Retorna un string vacío si stderr no existe
    }

    public static class ResultResponse {
        private String message;
        private String stdout;
        private String stderr;

        public ResultResponse(String message, String stdout, String stderr) {
            this.message = message;
            this.stdout = stdout;
            this.stderr = stderr;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStdout() {
            return stdout;
        }

        public void setStdout(String stdout) {
            this.stdout = stdout;
        }

        public String getStderr() {
            return stderr;
        }

        public void setStderr(String stderr) {
            this.stderr = stderr;
        }
    }
}
