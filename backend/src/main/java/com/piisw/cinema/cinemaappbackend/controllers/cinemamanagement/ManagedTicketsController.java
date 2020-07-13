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
import static com.piisw.cinema.cinemaappbackend.controllers.AddressesMapping.MANAGED_TICKETS;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.piisw.cinema.cinemaappbackend.models.cinemamanagement.ManagedTicket;
import com.piisw.cinema.cinemaappbackend.payload.request.cinemamanagement.ManagedTicketRequest;
import com.piisw.cinema.cinemaappbackend.payload.response.MessageResponse;
import com.piisw.cinema.cinemaappbackend.services.cinemamanagement.ManagedTicketsService;

@RestController
@RequestMapping(CINEMA_MANAGEMENT)
public class ManagedTicketsController {

  private final ManagedTicketsService managedTicketsService;

  public ManagedTicketsController(ManagedTicketsService managedTicketsService) {
    this.managedTicketsService = managedTicketsService;
  }

  @GetMapping(MANAGED_TICKETS)
  @RolesAllowed({"ADMIN", "USER"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Success",
            response = ManagedTicket.class,
            responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<List<ManagedTicket>> getManagedTickets() {
    return ResponseEntity.ok(managedTicketsService.getAllActive());
  }

  @GetMapping(MANAGED_TICKETS + "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Success", response = ManagedTicket.class),
        @ApiResponse(
            code = 400,
            message = "Managed ticket not found!",
            response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<Object> getManagedTicket(@PathVariable Integer id) {
    return managedTicketsService
        .getById(id)
        .<ResponseEntity<Object>>map(ResponseEntity::ok)
        .orElseGet(
            () ->
                ResponseEntity.badRequest().body(new MessageResponse("Managed ticket not found!")));
  }

  @PostMapping(MANAGED_TICKETS)
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Managed ticket saved successfully!",
            response = MessageResponse.class),
        @ApiResponse(
            code = 409,
            message = "Name is already in use!",
            response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<MessageResponse> addManagedTicket(
      @Valid @RequestBody ManagedTicketRequest request) {
    ManagedTicket managedTicket = new ManagedTicket(request.getName(), request.getPrice());
    if (managedTicketsService.save(managedTicket)) {
      return ResponseEntity.ok(new MessageResponse("Managed ticket saved successfully!"));
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(new MessageResponse("Name is already in use!"));
    }
  }

  @PutMapping(MANAGED_TICKETS + "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Managed ticket updated successfully",
            response = MessageResponse.class),
        @ApiResponse(
            code = 400,
            message = "Managed ticket not found!",
            response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<MessageResponse> updateManagedTicket(
      @PathVariable Integer id, @Valid @RequestBody ManagedTicketRequest request) {
    ManagedTicket managedTicket = new ManagedTicket(request.getName(), request.getPrice());
    if (managedTicketsService.update(id, managedTicket)) {
      return ResponseEntity.ok(new MessageResponse("Managed ticket updated successfully!"));
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Managed ticket not found!"));
    }
  }

  @DeleteMapping(MANAGED_TICKETS + "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Managed ticket deleted successfully",
            response = MessageResponse.class),
        @ApiResponse(
            code = 400,
            message = "Managed ticket not found!",
            response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<MessageResponse> deleteManagedTicket(@PathVariable Integer id) {
    if (managedTicketsService.delete(id)) {
      return ResponseEntity.ok(new MessageResponse("Managed ticket deleted successfully!"));
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Managed ticket not found!"));
    }
  }
}
