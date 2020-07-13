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

import static com.piisw.cinema.cinemaappbackend.controllers.AddressesMapping.*;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.time.temporal.ChronoUnit;
import java.util.*;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.piisw.cinema.cinemaappbackend.models.Screening;
import com.piisw.cinema.cinemaappbackend.payload.request.cinemamanagement.ScreeningRequest;
import com.piisw.cinema.cinemaappbackend.payload.response.MessageResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.ScreeningResponse;
import com.piisw.cinema.cinemaappbackend.services.cinemamanagement.ScreeningsService;

@RestController
@RequestMapping(CINEMA_MANAGEMENT)
public class ManageScreeningsController {

  private final ScreeningsService screeningsService;

  public ManageScreeningsController(ScreeningsService screeningsService) {
    this.screeningsService = screeningsService;
  }

  @GetMapping(SCREENINGS)
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Success",
            response = ScreeningResponse.class,
            responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<List<ScreeningResponse>> getScreenings() {
    List<ScreeningResponse> screeningResponses = new ArrayList<>();
    List<Screening> screenings = screeningsService.getAll();
    screenings.forEach(
        s ->
            screeningResponses.add(
                new ScreeningResponse(
                    s.getId(),
                    s.getStartDate().toString(),
                    s.getRoom().getId(),
                    s.getMovie().getId())));

    return ResponseEntity.ok(screeningResponses);
  }

  @GetMapping(SCREENINGS + "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Success", response = ScreeningResponse.class),
        @ApiResponse(
            code = 400,
            message = "Screening not found!",
            response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<Object> getScreening(@PathVariable Integer id) {
    Optional<Screening> s = screeningsService.getById(id);
    if (s.isPresent()) {
      ScreeningResponse screeningResponse =
          new ScreeningResponse(
              s.get().getId(),
              s.get().getStartDate().toInstant().truncatedTo(ChronoUnit.SECONDS).toString(),
              s.get().getRoom().getId(),
              s.get().getMovie().getId());
      return ResponseEntity.ok(screeningResponse);
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Screening not found!"));
    }
  }

  @GetMapping(MOVIES + "/{id}" + SCREENINGS)
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Success",
            response = Screening.class,
            responseContainer = "List"),
        @ApiResponse(code = 400, message = "Movie not found!", response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<Object> getScreeningsForMovie(@PathVariable UUID id) {
    List<Screening> screenings = screeningsService.getScreeningsForMovie(id);
    if (screenings != null) {
      List<ScreeningResponse> screeningResponses = new ArrayList<>();
      screenings.forEach(
          s ->
              screeningResponses.add(
                  new ScreeningResponse(
                      s.getId(),
                      s.getStartDate().toString(),
                      s.getRoom().getId(),
                      s.getMovie().getId())));

      return ResponseEntity.ok(screeningResponses);
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Movie not found!"));
    }
  }

  @PostMapping(SCREENINGS)
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Screening saved successfully!",
            response = MessageResponse.class),
        @ApiResponse(
            code = 400,
            message = "Movie of attached id doesn't exist!",
            response = MessageResponse.class),
        @ApiResponse(
            code = 400,
            message = "Room of attached id doesn't exist!",
            response = MessageResponse.class),
        @ApiResponse(
            code = 409,
            message = "Another screening is being broadcast during these dates!",
            response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<MessageResponse> addScreening(
      @Valid @RequestBody ScreeningRequest request) {
    Screening screening = new Screening(Date.from(request.getStartDate().toInstant()));
    switch (screeningsService.save(screening, request.getRoomId(), request.getMovieId())) {
      case MOVIE_NOT_EXIST:
        return ResponseEntity.badRequest()
            .body(new MessageResponse("Movie of attached id doesn't exist!"));
      case ROOM_NOT_EXIST:
        return ResponseEntity.badRequest()
            .body(new MessageResponse("Room of attached id doesn't exist!"));
      case SCREENING_OVERLAPS:
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new MessageResponse("Another screening is being broadcast during these dates!"));
      default:
        return ResponseEntity.ok(new MessageResponse("Screening saved successfully!"));
    }
  }

  @DeleteMapping(SCREENINGS + "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Screening deleted successfully",
            response = MessageResponse.class),
        @ApiResponse(
            code = 400,
            message = "Screening not found!",
            response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<MessageResponse> deleteScreening(@PathVariable Integer id) {
    if (screeningsService.delete(id)) {
      return ResponseEntity.ok(new MessageResponse("Screening deleted successfully!"));
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Screening not found!"));
    }
  }
}
