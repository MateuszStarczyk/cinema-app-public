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
package com.piisw.cinema.cinemaappbackend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.piisw.cinema.cinemaappbackend.models.Movie;
import com.piisw.cinema.cinemaappbackend.models.Room;
import com.piisw.cinema.cinemaappbackend.models.Screening;

@DataJpaTest
class ScreeningsRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private ScreeningsRepository screeningsRepository;

  @Test
  void findAllByYearAndMonthAndDay() throws ParseException {
    // given
    Movie movie = new Movie("Movie", 120, "poster", "https://trailer.com", "description");
    entityManager.persist(movie);
    Room room = new Room(45);
    entityManager.persist(room);
    String sStringDate1 = "23-05-2020 20:00:00";
    String eStringDate1 = "23-05-2020 22:00:00";
    String sStringDate2 = "24-05-2020 20:00:00";
    String eStringDate2 = "24-05-2020 22:00:00";
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    Date sDate1 = formatter.parse(sStringDate1);
    Date eDate1 = formatter.parse(eStringDate1);
    Date sDate2 = formatter.parse(sStringDate2);
    Date eDate2 = formatter.parse(eStringDate2);

    Screening screening1 = new Screening(sDate1, eDate1, room, movie);
    Screening screening2 = new Screening(sDate2, eDate2, room, movie);
    entityManager.persist(screening1);
    entityManager.persist(screening2);
    entityManager.flush();

    // when
    List<Screening> found =
        new ArrayList<>(screeningsRepository.findAllByYearAndMonthAndDay(2020, 5, 23));

    // then
    assertThat(found.size()).isEqualTo(1);
    assertThat(found.get(0).getStartDate()).isEqualTo(sDate1);
    assertThat(found.get(0).getEndDate()).isEqualTo(eDate1);
    assertThat(found.get(0).getMovie().getTitle()).isEqualTo(movie.getTitle());
    assertThat(found.get(0).getRoom().getSeats()).isEqualTo(room.getSeats());
  }
}
