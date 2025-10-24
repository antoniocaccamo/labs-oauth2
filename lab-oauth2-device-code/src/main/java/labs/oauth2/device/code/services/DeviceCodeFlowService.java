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

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

import com.microsoft.aad.msal4j.*;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import labs.oauth2.device.code.cache.TokenCacheAspect;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;


@Singleton
@Slf4j
public class DeviceCodeFlowService {

   private final ConfigService configService;

  @Inject
  public DeviceCodeFlowService(ConfigService configService) {
    this.configService = configService;
  }



  public  IAuthenticationResult acquireTokenDeviceCode() throws Exception {

    // Load token cache from file and initialize token cache aspect. The token cache will have
    // dummy data, so the acquireTokenSilently call will fail.
    TokenCacheAspect tokenCacheAspect = new TokenCacheAspect("sample_cache.json");

    PublicClientApplication pca = PublicClientApplication.builder(configService.getClientId())
                                      .authority(configService.getAuthority())
                                      .setTokenCacheAccessAspect(tokenCacheAspect)
                                      .build();

    Set<IAccount> accountsInCache = pca.getAccounts().join();
    // Take first account in the cache. In a production application, you would filter
    // accountsInCache to get the right account for the user authenticating.
    IAccount account = accountsInCache.iterator().next();

    IAuthenticationResult result;
    try {
      SilentParameters silentParameters =
          SilentParameters
              .builder(configService.getScopeAsSet(), account)
              .build();

      // try to acquire token silently. This call will fail since the token cache
      // does not have any data for the user you are trying to acquire a token for
      result = pca.acquireTokenSilently(silentParameters).join();
    } catch (Exception ex) {
      if (ex.getCause() instanceof MsalException) {

        Consumer<DeviceCode> deviceCodeConsumer = (DeviceCode deviceCode) ->
                                                      System.out.println(deviceCode.message());

        DeviceCodeFlowParameters parameters =
            DeviceCodeFlowParameters
                .builder(configService.getScopeAsSet(), deviceCodeConsumer)
                .build();

        // Try to acquire a token via device code flow. If successful, you should see
        // the token and account information printed out to console, and the sample_cache.json
        // file should have been updated with the latest tokens.
        result = pca.acquireToken(parameters).join();
      } else {
        // Handle other exceptions accordingly
        throw ex;
      }
    }
    return result;
  }
}