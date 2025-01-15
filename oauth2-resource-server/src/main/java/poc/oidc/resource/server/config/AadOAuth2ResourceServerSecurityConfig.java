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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;

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

    @Profile("!azure")
    @Bean
    public SecurityFilterChain htmlFilterChain(HttpSecurity http) throws Exception {
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



}
