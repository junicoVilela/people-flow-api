package com.peopleflow.accesscontrol.core.ports.output;

import java.util.List;
import java.util.Map;

public interface KeycloakGrupoPort {
    
    String createGroup(String name);
    
    List<Map<String, Object>> listAll();
    
    Map<String, Object> findById(String groupId);
    
    List<Map<String, Object>> findByName(String name);
    
    void updateGroup(String groupId, String newName);
    
    void deleteGroup(String groupId);
    
    List<Map<String, Object>> getMembers(String groupId);
    
    void assignRoleToGroup(String groupId, String roleName);
    
    void removeRoleFromGroup(String groupId, String roleName);
}

