package com.ironhack.ironbank.configuration;


import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Value("${custom.is.test:false}")
    private String isTest;

    private final String superAdmin = "backend-super-admin";
    private final String admin = "backend-admin";
    private final String user = "backend-user";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider provider = new KeycloakAuthenticationProvider();
        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(provider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // TODO: Remove this when the tests are fixed
        // I tried to set a flag to disable the security for testing purposes, but it didn't work. I had CORS issues.

//        if (isTest.equals("false")) {
//            super.configure(http);
//            http.authorizeRequests()
//                    .antMatchers("/accounts/user/**").hasAnyRole(user)
//                    .antMatchers("/accounts/**").hasAnyRole(admin)
//                    .antMatchers("/accounts").hasAnyRole(admin)
//                    .antMatchers(HttpMethod.POST, "/account-holders").permitAll()
//                    .antMatchers(HttpMethod.PUT, "/account-holders/**").hasAnyRole(superAdmin, admin)
//                    .antMatchers(HttpMethod.DELETE, "/account-holders/**").hasAnyRole(superAdmin, admin)
//                    .antMatchers(HttpMethod.GET, "/account-holders/**").hasAnyRole(superAdmin, admin, user)
//                    .antMatchers(HttpMethod.GET, "/account-holders").hasAnyRole(superAdmin, admin)
//                    .antMatchers("/admins/**").hasAnyRole(superAdmin)
//                    .antMatchers("/admins").hasAnyRole(superAdmin)
//                    .antMatchers(HttpMethod.GET, "/credit-accounts/**").hasAnyRole(admin, user)
//                    .antMatchers("/credit-accounts/**").hasAnyRole(admin)
//                    .antMatchers("/credit-accounts").hasAnyRole(admin)
//                    .antMatchers(HttpMethod.GET, "/checking-accounts/**").hasAnyRole(admin, user)
//                    .antMatchers("/checking-accounts/**").hasAnyRole(admin)
//                    .antMatchers("/checking-accounts").hasAnyRole(admin)
//                    .antMatchers(HttpMethod.GET, "/saving-accounts/**").hasAnyRole(admin, user)
//                    .antMatchers("/saving-accounts/**").hasAnyRole(admin)
//                    .antMatchers("/saving-accounts").hasAnyRole(admin)
//                    .antMatchers(HttpMethod.GET, "/student-checking-accounts/**").hasAnyRole(admin, user)
//                    .antMatchers("/student-checking-accounts/**").hasAnyRole(admin)
//                    .antMatchers("/student-checking-accounts").hasAnyRole(admin)
//                    .antMatchers("/third-parties/**").hasAnyRole(admin)
//                    .antMatchers("/third-parties").hasAnyRole(admin)
//                    .antMatchers("transactions/iban/**").hasAnyRole(user)
//                    .antMatchers("transactions/account-holder/**").hasAnyRole(user)
//                    .antMatchers(HttpMethod.GET, "/transactions/**").hasAnyRole(admin, user)
//                    .antMatchers(HttpMethod.GET, "/transactions").hasAnyRole(admin)
//                    .antMatchers(HttpMethod.POST, "/transactions").hasAnyRole(admin)
//                    .antMatchers("/transactions").hasAnyRole(admin)
//                    .anyRequest().permitAll();
//            http.csrf().disable();
//        } else {
            super.configure(http);
            http.authorizeRequests()
                    .anyRequest().permitAll();
            http.csrf().disable();
//        }
    }

}
