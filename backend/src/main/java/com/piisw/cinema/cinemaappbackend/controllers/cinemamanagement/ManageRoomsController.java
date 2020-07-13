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
import static com.piisw.cinema.cinemaappbackend.controllers.AddressesMapping.ROOMS;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.piisw.cinema.cinemaappbackend.models.Room;
import com.piisw.cinema.cinemaappbackend.payload.request.cinemamanagement.RoomRequest;
import com.piisw.cinema.cinemaappbackend.payload.response.MessageResponse;
import com.piisw.cinema.cinemaappbackend.services.cinemamanagement.RoomsService;

@RestController
@RequestMapping(CINEMA_MANAGEMENT)
public class ManageRoomsController {

  private final RoomsService roomsService;

  public ManageRoomsController(RoomsService roomsService) {
    this.roomsService = roomsService;
  }

  @GetMapping(ROOMS)
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Success",
            response = Room.class,
            responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<List<Room>> getRooms() {
    return ResponseEntity.ok(roomsService.getAll());
  }

  @GetMapping(ROOMS + "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Success", response = Room.class),
        @ApiResponse(code = 400, message = "Room not found!", response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<Object> getRoom(@PathVariable Integer id) {
    return roomsService
        .getById(id)
        .<ResponseEntity<Object>>map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.badRequest().body(new MessageResponse("Room not found!")));
  }

  @PostMapping(ROOMS)
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Room saved successfully!",
            response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<MessageResponse> addRoom(@Valid @RequestBody RoomRequest request) {
    Room room = new Room(request.getSeats());
    roomsService.save(room);
    return ResponseEntity.ok(new MessageResponse("Room saved successfully!"));
  }

  @PutMapping(ROOMS + "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Room updated successfully",
            response = MessageResponse.class),
        @ApiResponse(code = 400, message = "Room not found!", response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<MessageResponse> updateRoom(
      @PathVariable Integer id, @Valid @RequestBody RoomRequest request) {
    Room room = new Room(request.getSeats());
    if (roomsService.update(id, room)) {
      return ResponseEntity.ok(new MessageResponse("Room updated successfully!"));
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Room not found!"));
    }
  }

  @DeleteMapping(ROOMS + "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Room deleted successfully",
            response = MessageResponse.class),
        @ApiResponse(code = 400, message = "Room not found!", response = MessageResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  public ResponseEntity<MessageResponse> deleteRoom(@PathVariable Integer id) {
    if (roomsService.delete(id)) {
      return ResponseEntity.ok(new MessageResponse("Room deleted successfully!"));
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Room not found!"));
    }
  }
}
