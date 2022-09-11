package com.ironhack.ironbank.service;

import com.ironhack.ironbank.configuration.security.KeycloakProvider;
import com.ironhack.ironbank.dto.UserKeycloakDTO;
import com.ironhack.ironbank.enums.RealmGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log
public class SecurityServiceImpl implements SecurityService {

    private final KeycloakProvider keycloakProvider;

    @Override
    public Object[] createUser(UserKeycloakDTO user, RealmGroup group) {
        UserKeycloakDTO userKeycloakDTO = null;
        int status;

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
        status = result.getStatus();

        if(status == 201){ // 201 Created: The request has been fulfilled and resulted in a new resource being created.
            String path = result.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);
            var userRepresentationCreated= getUserRepresentationById(userId);
            userKeycloakDTO = UserKeycloakDTO.fromUserRepresentation(userRepresentationCreated);
        } else if (status == 409) { // 409 Conflict: User already exists
            var userRepresentationCreated= getUserRepresentationByEmail(user.getUsername());
            userKeycloakDTO = UserKeycloakDTO.fromUserRepresentation(userRepresentationCreated);
        }

        return new Object[]{status, userKeycloakDTO};
    }

    @Override
    public void updateUser(String id, UserKeycloakDTO userKeycloakDTO){

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userKeycloakDTO.getUsername());
        user.setFirstName(userKeycloakDTO.getFirstName());
        user.setLastName(userKeycloakDTO.getLastName());
        user.setEmail(userKeycloakDTO.getEmail());

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

    private UsersResource getUsersResource(){
        RealmResource realmResource = keycloakProvider.getRealmResource();
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
