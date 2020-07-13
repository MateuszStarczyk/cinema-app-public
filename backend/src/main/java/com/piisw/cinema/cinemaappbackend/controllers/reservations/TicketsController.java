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

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piisw.cinema.cinemaappbackend.payload.response.reservations.ReservationResponse;
import com.piisw.cinema.cinemaappbackend.services.reservations.TicketsService;

@RestController
@RequestMapping(TICKETS)
public class TicketsController {

  private final TicketsService ticketsService;

  public TicketsController(TicketsService ticketsService) {
    this.ticketsService = ticketsService;
  }

  @GetMapping(MY_TICKETS)
  @RolesAllowed("USER")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Success",
            response = ReservationResponse.class,
            responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<Object> getMyTickets() {
    List<ReservationResponse> ticketResponse = ticketsService.getFeatureTicketsForUser();

    return ResponseEntity.ok(ticketResponse);
  }

  @GetMapping(HISTORY)
  @RolesAllowed("USER")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Success",
            response = ReservationResponse.class,
            responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<Object> getTicketsHistory() {
    List<ReservationResponse> ticketResponse = ticketsService.getPastTicketsForUser();
    return ResponseEntity.ok(ticketResponse);
  }
}
