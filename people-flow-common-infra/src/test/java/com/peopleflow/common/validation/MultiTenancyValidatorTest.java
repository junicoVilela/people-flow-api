package com.peopleflow.common.validation;

import com.peopleflow.common.security.SecurityContextHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("MultiTenancyValidator - Testes Unitários")
class MultiTenancyValidatorTest {

    @Mock
    private SecurityContextHelper securityHelper;

    private MultiTenancyValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MultiTenancyValidator(securityHelper);
    }

    @Test
    @DisplayName("Deve permitir acesso quando empresaId corresponde")
    void devePermitirAcessoQuandoEmpresaIdCorresponde() {
        // Arrange
        Long empresaId = 5L;
        when(securityHelper.getEmpresaId()).thenReturn(empresaId);

        // Act & Assert
        assertThatCode(() -> validator.validarAcessoEmpresa(empresaId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve bloquear acesso quando empresaId não corresponde")
    void deveBloquearyAcessoQuandoEmpresaIdNaoCorresponde() {
        // Arrange
        Long userEmpresaId = 5L;
        Long targetEmpresaId = 15L;
        when(securityHelper.getEmpresaId()).thenReturn(userEmpresaId);
        when(securityHelper.getUsername()).thenReturn("joao");

        // Act & Assert
        assertThatThrownBy(() -> validator.validarAcessoEmpresa(targetEmpresaId))
                .isInstanceOf(MultiTenancyViolationException.class)
                .hasMessageContaining("Acesso negado");
    }

    @Test
    @DisplayName("Deve verificar se usuário é admin")
    void deveVerificarSeUsuarioEhAdmin() {
        // Arrange
        when(securityHelper.hasRole("admin")).thenReturn(true);

        // Act & Assert
        assertThatCode(() -> {
            boolean isAdmin = validator.isAdmin();
            assert isAdmin;
        }).doesNotThrowAnyException();
    }
}

