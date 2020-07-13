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
package com.piisw.cinema.cinemaappbackend.models;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "MOVIES")
public class Movie {

  @Expose
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @ColumnDefault("random_uuid()")
  @Type(type = "uuid-char")
  @Getter
  @Setter
  private UUID id;

  @Expose
  @Getter
  @Setter
  @NotBlank
  @Size(max = 100)
  @Column(name = "TITLE")
  private String title;

  @Expose
  @Getter
  @Setter
  @PositiveOrZero
  @Column(name = "DURATION")
  private Integer duration;

  @Expose
  @Getter
  @Setter
  @Column(name = "POSTER_FILENAME")
  private String posterFilename;

  @Expose
  @Getter
  @Setter
  @Column(name = "TRAILER_URL")
  private String trailerUrl;

  @Expose
  @Getter
  @Setter
  @Column(name = "DESCRIPTION")
  private String description;

  @Expose(serialize = false, deserialize = false)
  @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
  private List<Screening> screenings;

  public Movie(
      String title,
      Integer duration,
      String posterFilename,
      String trailerUrl,
      String description) {
    this.title = title;
    this.duration = duration;
    this.posterFilename = posterFilename;
    this.trailerUrl = trailerUrl;
    this.description = description;
  }
}
