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
package poc.oidc.resource.server.rest.keycloak;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import poc.oidc.resource.server.dto.BookDto;
import poc.oidc.resource.server.exception.BookNotFoundException;
import poc.oidc.resource.server.service.BookService;

import java.util.Collection;

@Profile("keycloak")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Slf4j
public class BookRestController {


    private final BookService bookService;

    // @formatter:off
    @Operation(summary = "Get a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)})
    // @formatter:on
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('editor')")
    public BookDto findById(@Parameter(description = "id of book to be searched") @PathVariable long id) throws BookNotFoundException {
        log.info("finding book with id: {}", id);
        return bookService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('editor')")
    public Collection<BookDto> findBooks( ) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("finding all books for principal: {} claims: {}", jwt.getSubject(), jwt.getClaims());
        return bookService.findBooks();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('editor')")
    @ResponseStatus(HttpStatus.OK)
    public BookDto updateBook(@PathVariable("id") final Integer id, @NotNull @RequestBody @Valid final BookDto book) {
        log.info("updating book with id: {} book: {}", id, book);
        return bookService.updateBook(id, book);
    }

    // @PatchMapping("/{id}")
    // @ResponseStatus(HttpStatus.OK)
    // public BookDto patchBook(@PathVariable("id") final String id, @RequestBody final BookDto book) {
    //     return book;
    // }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('editor')")
    public BookDto postBook(@NotNull @Valid @RequestBody final BookDto book) {
        log.info("creating  book: {}", book);
        return bookService.createBook(book);
    }


    @DeleteMapping("/{id}")@PreAuthorize("hasRole('editor')")
    @ResponseStatus(HttpStatus.OK)
    public long deleteBook(@PathVariable final long id) {
        log.info("deleting book with id: {}", id);
        return id;
    }

}
