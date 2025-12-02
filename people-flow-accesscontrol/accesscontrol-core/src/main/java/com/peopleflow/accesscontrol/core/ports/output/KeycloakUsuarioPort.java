package com.peopleflow.accesscontrol.core.ports.output;

import java.util.List;
import java.util.Map;

public interface KeycloakUsuarioPort {
    
    String createUser(String username, String email, String firstName, String lastName,
                     Map<String, List<String>> attributes);
    
    void setPassword(String userId, String password, boolean temporary);
    
    Map<String, Object> findByUsername(String username);
    
    Map<String, Object> findById(String userId);
    
    List<Map<String, Object>> listAll();
    
    List<Map<String, Object>> findByAttribute(String attributeName, String attributeValue);
    
    void updateUser(String userId, Map<String, Object> userData);
    
    void enableUser(String userId);
    
    void disableUser(String userId);
    
    void deleteUser(String userId);
    
    void addToGroup(String userId, String groupId);
    
    void removeFromGroup(String userId, String groupId);
    
    List<Map<String, Object>> getUserGroups(String userId);
    
    void logoutUser(String userId);
    
    void updateUserAttribute(String userId, String attributeName, String attributeValue);
    
    void assignClientRoles(String userId, List<String> roleNames);
    
    void sendPasswordSetupEmail(String userId);
}

