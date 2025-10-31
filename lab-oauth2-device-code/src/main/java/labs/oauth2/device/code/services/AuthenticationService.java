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

import com.microsoft.aad.msal4j.IAuthenticationResult;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * @auhtor antonio.caccamo on 2025-01-15 @ 10:21
 */
@Slf4j
@Singleton
public class AuthenticationService {

  private final DeviceCodeFlowService deviceCodeFlowService;

  private IAuthenticationResult authResult;

  @Inject
  public AuthenticationService(DeviceCodeFlowService deviceCodeFlowService) {
    this.deviceCodeFlowService = deviceCodeFlowService;
  }


  public IAuthenticationResult getAuthResult() throws Exception {
    if (Objects.isNull(authResult) || isNearExpiring(authResult)) {
      authResult = deviceCodeFlowService.acquireTokenDeviceCode();
    }
    return authResult;
  }

  private boolean isNearExpiring(IAuthenticationResult authResult) {
    boolean isNearExpiring = false;
    if (Objects.isNull(authResult)) {
      isNearExpiring = true;
    }
    isNearExpiring =  authResult.expiresOnDate().toInstant().atOffset(ZoneOffset.UTC)
                .isAfter(
                    OffsetDateTime.now()
                        .plus(1, ChronoUnit.MINUTES)
                );
    log.info("toke is near expiring: {}", isNearExpiring);
    return isNearExpiring;
  }
}
