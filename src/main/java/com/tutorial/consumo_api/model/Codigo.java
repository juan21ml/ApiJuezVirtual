package com.tutorial.consumo_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "codigos")
public class Codigo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String sourceCode;


    private String expectedOutput;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String outputJudge0; // Guardamos también la respuesta de Judge0

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public String getOutputJudge0() {
        return outputJudge0;
    }

    public void setOutputJudge0(String outputJudge0) {
        this.outputJudge0 = outputJudge0;
    }
}
