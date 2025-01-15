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
package labs.oauth2.device.code;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import labs.oauth2.device.code.dagger.DaggerDeviceCodeComponent;
import labs.oauth2.device.code.services.AuthenticationService;
import labs.oauth2.device.code.services.DeviceCodeFlowService;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DeviceCodeMain {

  public static void main(String[] args) throws Exception {

    try {
      log.info("device code started");
      AuthenticationService authenticationService = DaggerDeviceCodeComponent.create().authenticationService();

      IAuthenticationResult authResult = authenticationService.getAuthResult();
      log.info("Access token    : {}", authResult.accessToken());
      log.info("Id token        : {}", authResult.idToken());
      log.info("Account username: {}", authResult.account().username());
      log.info("scope           : {}", authResult.scopes());
      callEndpoint("http://localhost:9090/api/books", authResult);

      authResult.expiresOnDate().toInstant().atOffset(ZoneOffset.UTC).isAfter(OffsetDateTime.now().plus(1, ChronoUnit.MINUTES));

    } finally {

      Unirest.shutDown();
    }
  }



  private static void callEndpoint(String endpoint, IAuthenticationResult authResult) {
    log.info("calling: {}", endpoint);
    log.debug("token  : {}", authResult.accessToken());
    Map<String, String> headers = new HashMap<>();
    headers.put("accept", "application/json");
    headers.put("Authorization", String.join(" ", "Bearer", authResult.accessToken()));
    try {
      HttpResponse<String> response = Unirest.get(endpoint).headers(headers).asString();
      log.info("status code {} => {}", response.getStatusText(), response.getBody());
    } catch (Exception e) {
      log.error("error occured", e);
    }
  }

}
