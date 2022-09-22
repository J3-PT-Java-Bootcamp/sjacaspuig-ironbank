package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.response.UserSecurityCreateResponse;
import com.ironhack.ironbank.dto.UserSecurityDTO;
import com.ironhack.ironbank.enums.RealmGroup;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class SecurityServiceImpl implements SecurityService {


    @Value("${keycloak.auth-server-url}")
    private String server_url;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${custom.keycloak.realm-master}")
    private String realmMaster;

    @Value("${custom.keycloak.username}")
    private String username;

    @Value("${custom.keycloak.password}")
    private String password ;

    @Value("${custom.keycloak.client-id}")
    private String clientId;

    @Override
    public UserSecurityCreateResponse createUser(UserSecurityDTO user, RealmGroup group) {
        UserSecurityCreateResponse response = new UserSecurityCreateResponse();

         try {
             UsersResource usersResource = getUsersResource();
             UserRepresentation userRepresentation = new UserRepresentation();
             CredentialRepresentation passwordCredential = createPasswordCredentials(user.getPassword());
             userRepresentation.setCredentials(Collections.singletonList(passwordCredential));
             userRepresentation.setUsername(user.getUsername());
             userRepresentation.setEmail(user.getEmail());
             userRepresentation.setFirstName(user.getFirstName());
             userRepresentation.setLastName(user.getLastName());
             userRepresentation.setEnabled(true);
             userRepresentation.setGroups(List.of(group.toString()));

             Response result = usersResource.create(userRepresentation);
             response.setStatus(result.getStatus());

             if (response.getStatus() == 201) { // 201 Created: The request has been fulfilled and resulted in a new resource being created.
                 String path = result.getLocation().getPath();
                 String userId = path.substring(path.lastIndexOf("/") + 1);

                 var userRepresentationCreated = getUserRepresentationById(userId);
                 response.setUser(UserSecurityDTO.fromUserRepresentation(userRepresentationCreated));
             } else if(response.getStatus() == 409) { // 409 Conflict: The request could not be completed due to user already exists.

                 var userRepresentationCreated = getUserRepresentationByEmail(user.getUsername());
                 response.setUser(UserSecurityDTO.fromUserRepresentation(userRepresentationCreated));
             }

         } catch (Exception e) {
             System.out.println(e.getMessage());
             response.setStatus(400);
             return response;
         }

         return response;
    }

    @Override
    public void updateUser(String id, UserSecurityDTO userSecurityDTO){

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userSecurityDTO.getUsername());
        user.setFirstName(userSecurityDTO.getFirstName());
        user.setLastName(userSecurityDTO.getLastName());
        user.setEmail(userSecurityDTO.getEmail());

        UsersResource usersResource = getUsersResource();
        usersResource.get(id).update(user);
    }

    @Override
    public void deleteUser(String id){
        UsersResource usersResource = getUsersResource();
        usersResource.get(id).remove();
    }

    @Override
    public void sendVerificationLink(String userId) {
        UsersResource usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    @Override
    public void sendResetPassword(String id) {
        UsersResource usersResource = getUsersResource();
        usersResource.get(id).executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private RealmResource getRealmResource(){
        Keycloak kc = KeycloakBuilder.builder()
                .serverUrl(server_url)
                .realm(realmMaster)
                .username(username)
                .password(password)
                .clientId(clientId)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
        return kc.realm(realm);
    }

    private UsersResource getUsersResource(){
        RealmResource realmResource = getRealmResource();
        return realmResource.users();
    }

    private UserRepresentation getUserRepresentationById(String id){
        UsersResource usersResource = getUsersResource();
        return usersResource.get(id).toRepresentation();
    }

    private UserRepresentation getUserRepresentationByEmail(String username){
        UsersResource usersResource = getUsersResource();
        return usersResource.search(username).get(0);
    }
}
