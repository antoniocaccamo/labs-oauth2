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

import com.microsoft.aad.msal4j.ITokenCacheAccessAspect;
import com.microsoft.aad.msal4j.ITokenCacheAccessContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TokenCacheAspect implements ITokenCacheAccessAspect {

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

  private static String readDataFromFile(String resource) {
    try {
      //Determine if sample running from IDE (resource URI starts with 'file') or from a .jar (resource URI starts with 'jar'),
      //  so that sample_cache.json is read properly
      if (TokenCacheAspect.class.getResource("TokenCacheAspect.class").toString().startsWith("file")) {
        URL path = TokenCacheAspect.class.getResource(resource);
        return new String(
            Files.readAllBytes(
                Paths.get(path.toURI())));
      }
      else {
        URI uri = TokenCacheAspect.class.getResource(resource).toURI();
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        FileSystem fs = FileSystems.newFileSystem(uri, env);
        Path myFolderPath = Paths.get(uri);

        return new String(Files.readAllBytes(myFolderPath));
      }
    } catch (Exception ex){
      System.out.println("Error reading data from file: " + ex.getMessage());
      throw new RuntimeException(ex);
    }
  }
}