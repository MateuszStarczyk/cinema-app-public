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
package com.piisw.cinema.cinemaappbackend.controllers;

import static com.piisw.cinema.cinemaappbackend.controllers.AddressesMapping.TICKETS_VALIDATION;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piisw.cinema.cinemaappbackend.payload.response.TicketValidationResponse;
import com.piisw.cinema.cinemaappbackend.services.reservations.TicketsService;

@RestController
@RequestMapping(TICKETS_VALIDATION)
public class TicketsValidationController {

  private final TicketsService ticketsService;

  public TicketsValidationController(TicketsService ticketsService) {
    this.ticketsService = ticketsService;
  }

  @GetMapping("/{ticketId}")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Success", response = TicketValidationResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<TicketValidationResponse> ticketValidation(@PathVariable UUID ticketId) {
    TicketValidationResponse ticketResponse = ticketsService.ticketValidation(ticketId);

    return ResponseEntity.ok(ticketResponse);
  }

  @GetMapping("/{ticketId}/screenings/{screeningId}")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Success", response = TicketValidationResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<TicketValidationResponse> ticketValidation(
      @PathVariable UUID ticketId, @PathVariable Integer screeningId) {
    TicketValidationResponse ticketValidation =
        ticketsService.ticketValidation(ticketId, screeningId);

    return ResponseEntity.ok(ticketValidation);
  }
}
