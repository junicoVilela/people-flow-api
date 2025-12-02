package com.peopleflow.accesscontrol.core.ports.output;

import java.util.List;
import java.util.Map;

public interface KeycloakRolePort {
    
    void createClientRole(String roleName, String description);
    
    List<Map<String, Object>> listClientRoles();
    
    Map<String, Object> findByName(String roleName);
    
    void updateRole(String roleName, String newDescription);
    
    void deleteRole(String roleName);
    
    String getClientUuid();
}

