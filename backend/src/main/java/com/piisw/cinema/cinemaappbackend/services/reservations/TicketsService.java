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
package com.piisw.cinema.cinemaappbackend.services.reservations;

import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.piisw.cinema.cinemaappbackend.exceptions.TicketNotFoundException;
import com.piisw.cinema.cinemaappbackend.models.Ticket;
import com.piisw.cinema.cinemaappbackend.models.User;
import com.piisw.cinema.cinemaappbackend.payload.response.TicketValidationResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.ReservationResponse;
import com.piisw.cinema.cinemaappbackend.repository.TicketsRepository;
import com.piisw.cinema.cinemaappbackend.repository.UserRepository;
import com.piisw.cinema.cinemaappbackend.services.utils.MappingUtils;

@Service
public class TicketsService {

  private final TicketsRepository ticketsRepository;
  private final UserRepository userRepository;
  private final MappingUtils mappingUtils;

  public TicketsService(
      TicketsRepository ticketsRepository,
      UserRepository userRepository,
      MappingUtils mappingUtils) {

    this.ticketsRepository = ticketsRepository;
    this.userRepository = userRepository;
    this.mappingUtils = mappingUtils;
  }

  public List<ReservationResponse> getPastTicketsForUser() {
    List<Ticket> tickets =
        ticketsRepository.findAllByUserIdBeforeStartDate(getCurrentUserId(), new Date());
    return mappingUtils.ticketsToReservationResponses(tickets);
  }

  public List<ReservationResponse> getFeatureTicketsForUser() {
    List<Ticket> tickets =
        ticketsRepository.findAllByUserIdAfterStartDate(getCurrentUserId(), new Date());
    tickets.sort(Comparator.comparing(t -> t.getScreening().getStartDate()));
    return mappingUtils.ticketsToReservationResponses(tickets);
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Optional<User> userOptional = userRepository.findByEmail(authentication.getName());
    return userOptional.isPresent() ? userOptional.get().getId() : -1L;
  }

  public TicketValidationResponse ticketValidation(UUID id) {
    Ticket ticket =
        ticketsRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));

    return mappingUtils.ticketToTicketValidationResponse(ticket);
  }

  public TicketValidationResponse ticketValidation(UUID id, Integer screeningId) {
    Ticket ticket =
        ticketsRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));

    TicketValidationResponse ticketResponse = mappingUtils.ticketToTicketValidationResponse(ticket);
    ticketResponse.setIsValid(ticket.getScreening().getId().equals(screeningId));
    return ticketResponse;
  }
}
