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
package com.piisw.cinema.cinemaappbackend.controllers.reservations;

import static com.piisw.cinema.cinemaappbackend.controllers.AddressesMapping.*;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.piisw.cinema.cinemaappbackend.models.Movie;
import com.piisw.cinema.cinemaappbackend.payload.dtos.ScreeningDetailedDTO;
import com.piisw.cinema.cinemaappbackend.payload.request.reservations.ReservationRequest;
import com.piisw.cinema.cinemaappbackend.payload.response.MessageResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.MovieResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.ReservationResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.ScreeningSeatResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.TicketResponse;
import com.piisw.cinema.cinemaappbackend.services.reservations.ReservationsService;

@RestController
@RequestMapping(RESERVATIONS)
public class ReservationsController {

  private final ReservationsService reservationsService;

  public ReservationsController(ReservationsService reservationsService) {
    this.reservationsService = reservationsService;
  }

  @GetMapping(MOVIES_WITH_SCREENINGS)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Success",
            response = Movie.class,
            responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<List<MovieResponse>> getMoviesWithScreenings(
      @RequestParam("date") Date date) {
    return ResponseEntity.ok(reservationsService.getAllMovieScreeningsForDate(date));
  }

  @GetMapping(SCREENINGS + "/{id}")
  @RolesAllowed("USER")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Success", response = ScreeningDetailedDTO.class),
        @ApiResponse(
            code = 400,
            message = "Screening not found!",
            response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<Object> getScreening(@PathVariable Integer id) {
    return ResponseEntity.ok(reservationsService.getScreeningDetailed(id));
  }

  @GetMapping(SCREENING_SEATS + "/{id}")
  @RolesAllowed("USER")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Success",
            response = ScreeningSeatResponse.class,
            responseContainer = "List"),
        @ApiResponse(
            code = 400,
            message = "Screening not found!",
            response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<Object> getScreeningSeats(@PathVariable Integer id) {
    return ResponseEntity.ok(reservationsService.getScreeningSeats(id));
  }

  @PostMapping(RESERVE)
  @RolesAllowed("USER")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Reservation saved successfully!",
            response = TicketResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<Object> addReservation(@Valid @RequestBody ReservationRequest request) {
    Optional<TicketResponse> ticketResponse = reservationsService.addReservation(request);
    if (ticketResponse.isPresent()) {
      return ResponseEntity.ok(ticketResponse.get());
    }
    return ResponseEntity.badRequest()
        .body(new MessageResponse("Screening, user or ticket kind not found!"));
  }

  @GetMapping("/{id}")
  @RolesAllowed("USER")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Success", response = ReservationResponse.class),
        @ApiResponse(
            code = 400,
            message = "Reservation not found!",
            response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<Object> getReservation(@PathVariable UUID id) {
    return ResponseEntity.ok(reservationsService.getReservation(id));
  }
}
