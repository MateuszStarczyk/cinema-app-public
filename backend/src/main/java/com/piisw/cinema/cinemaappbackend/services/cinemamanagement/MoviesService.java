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

import org.springframework.stereotype.Service;

import com.piisw.cinema.cinemaappbackend.exceptions.MovieDeletingForbiddenException;
import com.piisw.cinema.cinemaappbackend.models.Movie;
import com.piisw.cinema.cinemaappbackend.payload.dtos.MovieDTO;
import com.piisw.cinema.cinemaappbackend.repository.MoviesRepository;
import com.piisw.cinema.cinemaappbackend.repository.TicketsRepository;
import com.piisw.cinema.cinemaappbackend.services.utils.MappingUtils;

@Service
public class MoviesService {

  private final MoviesRepository moviesRepository;

  private final TicketsRepository ticketsRepository;

  private final MappingUtils mappingUtils;

  public MoviesService(
      MoviesRepository moviesRepository,
      TicketsRepository ticketsRepository,
      MappingUtils mappingUtils) {

    this.moviesRepository = moviesRepository;
    this.ticketsRepository = ticketsRepository;
    this.mappingUtils = mappingUtils;
  }

  public List<Movie> getAll() {
    return moviesRepository.findAll();
  }

  public Optional<Movie> getById(UUID id) {
    return moviesRepository.findById(id);
  }

  public MovieDTO save(MovieDTO movieDTO) {
    Movie movie = mappingUtils.movieDTOToMovie(movieDTO);
    movie = moviesRepository.save(movie);
    return mappingUtils.movieToMovieDTO(movie);
  }

  public boolean update(UUID id, Movie movie) {
    if (moviesRepository.existsById(id)) {
      movie.setId(id);
      moviesRepository.save(movie);
      return true;
    } else {
      return false;
    }
  }

  public boolean delete(UUID id) {
    if (moviesRepository.existsById(id)) {
      if (ticketsRepository.existsByMovieId(id)) throw new MovieDeletingForbiddenException(id);
      moviesRepository.deleteById(id);
      return true;
    } else {
      return false;
    }
  }
}
