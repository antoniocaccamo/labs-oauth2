/*
 * Copyright 2017-2025 original caccamo.antonio@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package poc.oidc.resource.server.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;
import poc.oidc.resource.server.config.keycloak.KeycloakAuthenticationConverter;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j @RequiredArgsConstructor
@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class AadOAuth2ResourceServerSecurityConfig {

    private static final String[] patterns = {
        "swagger-ui.html",
        "/swagger-ui/*",
        "/v3/*", "/v3/*/*",
        "/", "index.html",
        "/*.js", "/*.css"
    };

    @Autowired
    KeycloakAuthenticationConverter keycloakAuthenticationConverter;

    @Profile("!(azure || keycloak)")
    @Bean
    public SecurityFilterChain htmlFilterChain(HttpSecurity http) throws Exception {
        log.info("SecurityFilterChain for others");
        // @formatter:off
        http.authorizeHttpRequests(
                    authz ->
                            authz.requestMatchers(patterns).permitAll()
                                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer((oauth) -> oauth.jwt(Customizer.withDefaults()))
        ;
        // @formatter:on
        return http.build();
    }

    @Profile("azure")
    @Bean
    public SecurityFilterChain azureHtmlFilterChain(HttpSecurity http) throws Exception {
        log.info("SecurityFilterChain for azure");
        // @formatter:off
        http.with(
                AadResourceServerHttpSecurityConfigurer.aadResourceServer(),
                Customizer.withDefaults()
            )
            .authorizeHttpRequests(authz-> authz.requestMatchers(patterns).permitAll()
                                        .anyRequest().authenticated()
            );
        // @formatter:on
        return http.build();
    }

    @Profile("keycloak")
    @Bean
    public SecurityFilterChain keycloakHtmlFilterChain(HttpSecurity http) throws Exception {
        log.info("SecurityFilterChain for keycloak");
        // @formatter:off
        http.authorizeHttpRequests(
                    authz ->
                            authz.requestMatchers(patterns).permitAll()
                                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer((oauth) -> oauth.jwt( jwt -> jwt.jwtAuthenticationConverter(keycloakAuthenticationConverter)
            )
        );
        // @formatter:on
        return http.build();
    }

}

