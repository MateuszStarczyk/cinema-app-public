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
package com.piisw.cinema.cinemaappbackend.services.cinemamanagement;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.piisw.cinema.cinemaappbackend.models.Room;
import com.piisw.cinema.cinemaappbackend.repository.RoomsRepository;

@Service
public class RoomsService {

  private final RoomsRepository roomsRepository;

  public RoomsService(RoomsRepository roomsRepository) {
    this.roomsRepository = roomsRepository;
  }

  public List<Room> getAll() {
    return roomsRepository.findAll();
  }

  public Optional<Room> getById(Integer id) {
    return roomsRepository.findById(id);
  }

  public void save(Room room) {
    roomsRepository.save(room);
  }

  public boolean update(Integer id, Room room) {
    if (roomsRepository.existsById(id)) {
      roomsRepository.save(room);
      return true;
    } else {
      return false;
    }
  }

  public boolean delete(Integer id) {
    if (roomsRepository.existsById(id)) {
      roomsRepository.deleteById(id);
      return true;
    } else {
      return false;
    }
  }
}
