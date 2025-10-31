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
package labs.oauth2.device.code.dagger;


import dagger.Component;
import jakarta.inject.Singleton;
import labs.oauth2.device.code.services.AuthenticationService;
import labs.oauth2.device.code.services.DeviceCodeFlowService;

@Singleton
@Component( modules = {
    DeviceCodeModule.class
})
public interface DeviceCodeComponent {

  AuthenticationService authenticationService();

}
