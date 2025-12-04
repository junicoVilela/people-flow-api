package com.peopleflow.accesscontrol.outbound.database.repository;

import com.peopleflow.accesscontrol.outbound.database.entity.CargoRoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CargoRoleJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SCHEMA = "people_flow_rh";

    private final RowMapper<CargoRoleEntity> rowMapper = new RowMapper<>() {
        @Override
        public CargoRoleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CargoRoleEntity(
                rs.getLong("cargo_id"),
                rs.getString("role_name"),
                rs.getString("descricao"),
                rs.getTimestamp("criado_em").toLocalDateTime(),
                rs.getString("criado_por")
            );
        }
    };

    public List<String> findRolesByCargoId(Long cargoId) {
        String sql = String.format(
            "SELECT role_name FROM %s.cargo_role WHERE cargo_id = ?", 
            SCHEMA
        );
        return jdbcTemplate.queryForList(sql, String.class, cargoId);
    }

    public void insert(Long cargoId, String roleName, String descricao, String criadoPor) {
        String sql = String.format(
            "INSERT INTO %s.cargo_role (cargo_id, role_name, descricao, criado_em, criado_por) " +
            "VALUES (?, ?, ?, ?, ?)", 
            SCHEMA
        );
        jdbcTemplate.update(sql, cargoId, roleName, descricao, LocalDateTime.now(), criadoPor);
    }

    public void delete(Long cargoId, String roleName) {
        String sql = String.format(
            "DELETE FROM %s.cargo_role WHERE cargo_id = ? AND role_name = ?", 
            SCHEMA
        );
        jdbcTemplate.update(sql, cargoId, roleName);
    }

    public void deleteAllByCargoId(Long cargoId) {
        String sql = String.format(
            "DELETE FROM %s.cargo_role WHERE cargo_id = ?", 
            SCHEMA
        );
        jdbcTemplate.update(sql, cargoId);
    }
}

