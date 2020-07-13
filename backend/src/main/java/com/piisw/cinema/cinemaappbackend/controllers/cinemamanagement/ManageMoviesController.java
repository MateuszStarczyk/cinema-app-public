/*
 * MIT License
 *
 * Copyright (c) 2020 Politechnika Wrocławska - Projektowanie i implementacja systemów webowych
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.piisw.cinema.cinemaappbackend.controllers.cinemamanagement;

import static com.piisw.cinema.cinemaappbackend.controllers.AddressesMapping.CINEMA_MANAGEMENT;
import static com.piisw.cinema.cinemaappbackend.controllers.AddressesMapping.MOVIES;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.piisw.cinema.cinemaappbackend.models.Movie;
import com.piisw.cinema.cinemaappbackend.payload.dtos.MovieDTO;
import com.piisw.cinema.cinemaappbackend.payload.response.MessageResponse;
import com.piisw.cinema.cinemaappbackend.services.cinemamanagement.MoviesService;

@RestController
@RequestMapping(CINEMA_MANAGEMENT)
public class ManageMoviesController {

  private final MoviesService moviesService;

  public ManageMoviesController(MoviesService moviesService) {
    this.moviesService = moviesService;
  }

  @GetMapping(MOVIES)
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Success",
            response = Movie.class,
            responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<List<Movie>> getMovies() {
    return ResponseEntity.ok(moviesService.getAll());
  }

  @GetMapping(MOVIES + "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Success", response = Movie.class),
        @ApiResponse(code = 400, message = "Movie not found!", response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<Object> getMovie(@PathVariable UUID id) {
    return moviesService
        .getById(id)
        .<ResponseEntity<Object>>map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.badRequest().body(new MessageResponse("Movie not found!")));
  }

  @PostMapping(MOVIES)
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Movie saved successfully!", response = MovieDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<MovieDTO> addMovie(@Valid @RequestBody MovieDTO request) {

    MovieDTO saved = moviesService.save(request);
    return ResponseEntity.ok(saved);
  }

  @PutMapping(MOVIES + "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Movie updated successfully",
            response = MessageResponse.class),
        @ApiResponse(code = 400, message = "Movie not found!", response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<MessageResponse> updateMovie(
      @PathVariable UUID id, @Valid @RequestBody MovieDTO request) {
    Movie movie =
        new Movie(
            request.getTitle(),
            request.getDuration(),
            request.getPosterFilename(),
            request.getTrailerUrl(),
            request.getDescription());
    if (moviesService.update(id, movie)) {
      return ResponseEntity.ok(new MessageResponse("Movie updated successfully!"));
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Movie not found!"));
    }
  }

  @DeleteMapping(MOVIES + "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Movie deleted successfully",
            response = MessageResponse.class),
        @ApiResponse(code = 400, message = "Movie not found!", response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<MessageResponse> deleteMovie(@PathVariable UUID id) {
    if (moviesService.delete(id)) {
      return ResponseEntity.ok(new MessageResponse("Movie deleted successfully!"));
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Movie not found!"));
    }
  }
}
