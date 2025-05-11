package com.tutorial.consumo_api.controller;

import com.tutorial.consumo_api.dto.PostDto;
import com.tutorial.consumo_api.Service.PostService;
import com.tutorial.consumo_api.Service.PostService.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:63342")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/runCode")
    public ResponseEntity<ResultResponse> runCode(@RequestBody PostDto postDto) {
        // Cambié String por ResultResponse en la respuesta
        ResultResponse resultResponse = postService.ejecutarCodigo(postDto);
        return ResponseEntity.ok(resultResponse);  // Devolvemos el objeto como JSON
    }

}

