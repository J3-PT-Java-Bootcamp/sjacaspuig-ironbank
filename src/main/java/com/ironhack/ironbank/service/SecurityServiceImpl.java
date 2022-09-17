package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.ResponseMessage;
import com.ironhack.ironbank.dto.UserKeycloakDTO;
import com.ironhack.ironbank.enums.RealmGroup;
import com.ironhack.ironbank.model.UserKeycloak;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
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
    public Object[] createUser(UserKeycloak user, RealmGroup group) {
        ResponseMessage message = new ResponseMessage();
        int statusId = 0;
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
             statusId = result.getStatus();

             if(statusId == 201){
                 String path = result.getLocation().getPath();
                 String userId = path.substring(path.lastIndexOf("/") + 1);

                 RealmResource realmResource = getRealmResource();
                 RoleRepresentation roleRepresentation = realmResource.roles().get("realm-user").toRepresentation();
                 realmResource.users().get(userId).roles().realmLevel().add(Arrays.asList(roleRepresentation));
                 message.setMessage("usuario creado con éxito");
             }else if(statusId == 409){
                 message.setMessage("ese usuario ya existe");
             }else{
                 message.setMessage("error creando el usuario");
             }
         }catch (Exception e){
             e.printStackTrace();
         }

         return new Object[]{statusId, message};
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
