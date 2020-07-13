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
package com.piisw.cinema.cinemaappbackend.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piisw.cinema.cinemaappbackend.models.Screening;
import com.piisw.cinema.cinemaappbackend.payload.dtos.MovieDTO;
import com.piisw.cinema.cinemaappbackend.repository.ScreeningsRepository;
import com.piisw.cinema.cinemaappbackend.services.utils.MappingUtils;

@Service
public class WhatsOnService {

  private final ScreeningsRepository screeningsRepository;
  private final MappingUtils mappingUtils;

  public WhatsOnService(ScreeningsRepository screeningsRepository, MappingUtils mappingUtils) {

    this.screeningsRepository = screeningsRepository;
    this.mappingUtils = mappingUtils;
  }

  public List<MovieDTO> getWhatsOnMovies() {
    List<Screening> screeningsForDate = screeningsRepository.findAllByStartDateAfter(new Date());

    return screeningsForDate.stream()
        .map(mappingUtils::screeningToMovieDTO)
        .distinct()
        .collect(Collectors.toList());
  }
}
