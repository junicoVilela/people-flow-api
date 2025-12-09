package com.peopleflow.pessoascontratos.outbound.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "AUDITORIA", schema = "PEOPLE_FLOW_RH")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ENTIDADE", nullable = false)
    private String entidade;

    @Column(name = "ENTIDADE_ID", nullable = false)
    private Long entidadeId;

    @Column(name = "ACAO", nullable = false)
    private String acao;

    @Column(name = "USUARIO_ID", length = 36)
    private String usuarioId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "DADOS_ANTIGOS", columnDefinition = "jsonb")
    private String dadosAntigos;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "DADOS_NOVOS", columnDefinition = "jsonb")
    private String dadosNovos;

    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "IP_ADDRESS", length = 45)
    private String ipAddress;

    @Column(name = "USER_AGENT", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "EMPRESA_ID")
    private Long empresaId;
}

