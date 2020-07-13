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
package com.piisw.cinema.cinemaappbackend.payload.dtos;

import java.util.Objects;

import javax.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

public class MovieDTO {

  @Getter @Setter private String id;

  @Getter
  @Setter
  @NotEmpty
  @Size(max = 100)
  private String title;

  @Getter @Setter @PositiveOrZero private Integer duration;

  @Getter @Setter private String posterFilename;

  @Getter @Setter private String trailerUrl;

  @Getter @Setter private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MovieDTO movieDTO = (MovieDTO) o;
    return id.equals(movieDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
