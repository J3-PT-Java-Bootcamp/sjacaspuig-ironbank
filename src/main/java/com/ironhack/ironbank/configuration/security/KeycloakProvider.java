package com.ironhack.ironbank.configuration.security;

import lombok.Getter;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KeycloakProvider {

    private static Keycloak keycloak = null;
    @Value("${keycloak.auth-server-url}")
    private String serverURL;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${custom.keycloak.realm.master}")
    private String masterRealm;
    @Value("${custom.keycloak.master.username}")
    private String masterUsername;
    @Value("${custom.keycloak.master.password}")
    private String masterPassword;
    @Value("${custom.keycloak.master.resource}")
    private String masterClientId;


    public KeycloakProvider() {
    }

    public Keycloak getInstance() {
        if (keycloak == null) {

            return KeycloakBuilder.builder()
                    .serverUrl(serverURL)
                    .realm(masterRealm)
                    .username(masterUsername)
                    .password(masterPassword)
                    .clientId(masterClientId)
                    .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                    .build();
        }

        return keycloak;
    }

    public RealmResource getRealmResource(){
        return getInstance().realm(realm);
    }
}