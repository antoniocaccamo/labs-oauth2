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
package labs.oauth2.device.code.services;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @auhtor antonio.caccamo on 2024-05-03 @ 18:20
 */
@Slf4j
@Singleton
public class ConfigService {

    private static final String AAD_PROPERTIES = "aad.properties";

    private static final String KEY_AUTHORITY = "aad.authority";
    private static final String KEY_TENANT_ID = "aad.tenant_id";
    private static final String KEY_CLIENT_ID = "aad.client_id";
    private static final String KEY_SCOPE     = "aad.scope";
    private static final String KEY_STORAGE_ACCOUNT = "storage.account.name";

    private final Properties props;
    private final Path path;


    @Inject
    public ConfigService() {

        this.path = Path.of(AAD_PROPERTIES);
        try {
            props = new Properties();
            props.load(new FileReader(path.toAbsolutePath().toFile()));
            log.debug("props: {}", props);
            //Objects.requireNonNull(props.getProperty(KEY_AUTHORITY));
            Objects.requireNonNull(props.getProperty(KEY_TENANT_ID));
            Objects.requireNonNull(props.getProperty(KEY_CLIENT_ID));
            Objects.requireNonNull(props.getProperty(KEY_SCOPE));
            Objects.requireNonNull(props.getProperty(KEY_STORAGE_ACCOUNT));
            log.debug("{}", this);
        } catch (IOException e) {
            log.error("error occurred", e);
            throw new RuntimeException(e);
        }
    }


    public  String getAuthority() {
        return "https://login.microsoftonline.com/" + getTenant();

    }

    public  String getTenant() {
        return   props.getProperty(KEY_TENANT_ID).trim();
    }

    public  String getClientId() {
        return props.getProperty(KEY_CLIENT_ID);
    }

    public String getStorageAccountName() {
        return props.getProperty(KEY_STORAGE_ACCOUNT).trim();
    }

    public Set<String> getScopeAsSet() {

        return getScopeAsList().stream().collect(Collectors.toSet());
    }

    public List<String> getScopeAsList() {
        return Arrays.asList( props.getProperty(KEY_SCOPE).trim().split(","));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConfigService.class.getSimpleName() + "[", "]")
                       .add(KEY_TENANT_ID + " = " + getTenant())
                       .add(KEY_AUTHORITY + " = " + getAuthority())
                       .add(KEY_CLIENT_ID + " = " + getClientId())
                       .add(KEY_STORAGE_ACCOUNT + " = " + getStorageAccountName())
                       .add(KEY_SCOPE + "=" + getScopeAsList())
                       .toString();
    }
}
