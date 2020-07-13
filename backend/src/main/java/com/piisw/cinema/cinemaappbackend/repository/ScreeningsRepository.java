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

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.piisw.cinema.cinemaappbackend.models.Screening;

@Repository
public interface ScreeningsRepository extends JpaRepository<Screening, Integer> {

  @Query("SELECT s FROM Screening s WHERE s.movie.id = :movieId")
  List<Screening> findAllByMovieId(@Param("movieId") UUID movieId);

  @Query(
      "SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Screening s WHERE s.startDate <= :#{#screening.endDate} AND s.endDate >= :#{#screening.startDate} AND s.room.id = :#{#screening.room.id}")
  boolean isOverlapping(@Param("screening") Screening screening);

  @Query(
      "select s from Screening s where EXTRACT(YEAR FROM s.startDate) = :sYear and EXTRACT(MONTH FROM s.startDate) = :sMonth and EXTRACT(DAY FROM s.startDate) = :sDay")
  List<Screening> findAllByYearAndMonthAndDay(
      @Param("sYear") int year, @Param("sMonth") int month, @Param("sDay") int day);

  List<Screening> findAllByStartDateAfter(Date date);
}
