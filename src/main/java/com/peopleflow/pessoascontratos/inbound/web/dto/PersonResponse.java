package com.peopleflow.pessoascontratos.inbound.web.dto;

import java.time.Instant;
import java.util.UUID;

public class PersonResponse {
    private UUID id;
    private String name;
    private String document;
    private String status;
    private Instant createdAt;

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDocument() { return document; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDocument(String document) { this.document = document; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
