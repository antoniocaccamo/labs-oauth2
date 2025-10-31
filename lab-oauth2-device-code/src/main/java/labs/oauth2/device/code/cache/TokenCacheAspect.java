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
package labs.oauth2.device.code.cache;

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import com.microsoft.aad.msal4j.ITokenCacheAccessAspect;
import com.microsoft.aad.msal4j.ITokenCacheAccessContext;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenCacheAspect implements ITokenCacheAccessAspect {

  @Getter
  protected final Path path;
  private String data;

  public TokenCacheAspect(String fileName) {
    this.path = Path.of(fileName);
    try {
      this.data = new String(Files.readAllBytes(path.toAbsolutePath()));
    } catch (NoSuchFileException noSuchFileException){
      path.toAbsolutePath().getParent().toFile().mkdirs();
      try {
        path.toAbsolutePath().toFile().createNewFile();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void beforeCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
    log.info("reading cached data from file: " + data);
    iTokenCacheAccessContext.tokenCache().deserialize(data);
  }

  @Override
  public void afterCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
    data = iTokenCacheAccessContext.tokenCache().serialize();

    // you could implement logic here to write changes to file
    try {
      log.info("has cache changed?  {}", iTokenCacheAccessContext.hasCacheChanged());
      log.info("writing data to cache file:{} ", data);
      Files.write(path.toAbsolutePath(), data.getBytes());
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }



}