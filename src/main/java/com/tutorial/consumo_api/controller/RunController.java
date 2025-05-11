package com.tutorial.consumo_api.controller;

import com.tutorial.consumo_api.Service.PostService;
import com.tutorial.consumo_api.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RunController {

    @Autowired
    private PostService postService;

    @PostMapping("/runCodeFromRunController")  // Cambié la ruta aquí
    public ResponseEntity<Map<String, String>> runCode(@RequestBody PostDto postDto) {
        PostService.ResultResponse resultado = postService.ejecutarCodigo(postDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", resultado.getMessage());

        return ResponseEntity.ok(response);
    }
}
