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
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import com.piisw.cinema.cinemaappbackend.exceptions.ScreeningDeletingForbiddenException;
import com.piisw.cinema.cinemaappbackend.models.Movie;
import com.piisw.cinema.cinemaappbackend.models.Room;
import com.piisw.cinema.cinemaappbackend.models.Screening;
import com.piisw.cinema.cinemaappbackend.repository.MoviesRepository;
import com.piisw.cinema.cinemaappbackend.repository.RoomsRepository;
import com.piisw.cinema.cinemaappbackend.repository.ScreeningsRepository;
import com.piisw.cinema.cinemaappbackend.repository.TicketsRepository;

@Service
public class ScreeningsService {

  private final ScreeningsRepository screeningsRepository;

  private final MoviesRepository moviesRepository;

  private final RoomsRepository roomsRepository;

  private final TicketsRepository ticketsRepository;

  public ScreeningsService(
      ScreeningsRepository screeningsRepository,
      MoviesRepository moviesRepository,
      RoomsRepository roomsRepository,
      TicketsRepository ticketsRepository) {

    this.screeningsRepository = screeningsRepository;
    this.moviesRepository = moviesRepository;
    this.roomsRepository = roomsRepository;
    this.ticketsRepository = ticketsRepository;
  }

  public List<Screening> getAll() {
    return screeningsRepository.findAll();
  }

  public Optional<Screening> getById(Integer id) {
    return screeningsRepository.findById(id);
  }

  public List<Screening> getScreeningsForMovie(UUID id) {
    if (moviesRepository.existsById(id)) {
      return screeningsRepository.findAllByMovieId(id);
    } else {
      return null;
    }
  }

  public SaveScreeningResultEnum save(Screening screening, Integer roomId, UUID movieId) {

    Optional<Movie> movie = moviesRepository.findById(movieId);
    if (!movie.isPresent()) {
      return SaveScreeningResultEnum.MOVIE_NOT_EXIST;
    }

    Optional<Room> room = roomsRepository.findById(roomId);
    if (!room.isPresent()) {
      return SaveScreeningResultEnum.ROOM_NOT_EXIST;
    }

    screening.setEndDate(DateUtils.addMinutes(screening.getStartDate(), movie.get().getDuration()));
    screening.setMovie(movie.get());
    screening.setRoom(room.get());

    if (screeningsRepository.isOverlapping(screening)) {
      return SaveScreeningResultEnum.SCREENING_OVERLAPS;
    }

    screeningsRepository.save(screening);
    return SaveScreeningResultEnum.SUCCESS;
  }

  public boolean delete(Integer id) {
    if (screeningsRepository.existsById(id)) {
      if (ticketsRepository.existsByScreeningId(id))
        throw new ScreeningDeletingForbiddenException(id);
      screeningsRepository.deleteById(id);
      return true;
    } else {
      return false;
    }
  }
}
