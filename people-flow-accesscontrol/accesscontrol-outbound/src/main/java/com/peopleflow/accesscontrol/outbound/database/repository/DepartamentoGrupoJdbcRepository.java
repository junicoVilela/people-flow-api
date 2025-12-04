package com.peopleflow.accesscontrol.outbound.database.repository;

import com.peopleflow.accesscontrol.outbound.database.entity.DepartamentoGrupoEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório JDBC para operações na tabela departamento_grupo
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class DepartamentoGrupoJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SCHEMA = "people_flow_rh";

    private final RowMapper<DepartamentoGrupoEntity> rowMapper = new RowMapper<>() {
        @Override
        public DepartamentoGrupoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DepartamentoGrupoEntity(
                rs.getLong("departamento_id"),
                rs.getString("keycloak_group_id"),
                rs.getString("keycloak_group_name"),
                rs.getTimestamp("criado_em").toLocalDateTime(),
                rs.getString("criado_por"),
                rs.getTimestamp("atualizado_em") != null ? 
                    rs.getTimestamp("atualizado_em").toLocalDateTime() : null,
                rs.getString("atualizado_por")
            );
        }
    };

    public Optional<String> findGroupIdByDepartamentoId(Long departamentoId) {
        String sql = String.format(
            "SELECT keycloak_group_id FROM %s.departamento_grupo WHERE departamento_id = ?", 
            SCHEMA
        );
        
        try {
            List<String> results = jdbcTemplate.queryForList(sql, String.class, departamentoId);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void upsert(Long departamentoId, String keycloakGroupId, String keycloakGroupName, String usuario) {
        String sql = String.format(
            "INSERT INTO %s.departamento_grupo " +
            "(departamento_id, keycloak_group_id, keycloak_group_name, criado_em, criado_por) " +
            "VALUES (?, ?, ?, ?, ?) " +
            "ON CONFLICT (departamento_id) DO UPDATE SET " +
            "keycloak_group_id = EXCLUDED.keycloak_group_id, " +
            "keycloak_group_name = EXCLUDED.keycloak_group_name, " +
            "atualizado_em = ?, " +
            "atualizado_por = ?",
            SCHEMA
        );
        
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(sql, 
            departamentoId, keycloakGroupId, keycloakGroupName, now, usuario,
            now, usuario
        );
    }

    public void delete(Long departamentoId) {
        String sql = String.format(
            "DELETE FROM %s.departamento_grupo WHERE departamento_id = ?", 
            SCHEMA
        );
        jdbcTemplate.update(sql, departamentoId);
    }
}

