import { Component, OnInit } from '@angular/core';
import { MovieWithScreenings } from 'src/app/reservation/shared/models/movie-with-screenings';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-today-screenings',
  templateUrl: './today-screenings.component.html',
  styleUrls: ['./today-screenings.component.scss'],
})
export class TodayScreeningsComponent implements OnInit {
  isLoading = false;
  screenings: MovieWithScreenings[];

  constructor(private readonly route: ActivatedRoute) { }

  ngOnInit() {
    this.screenings = this.route.snapshot.data.screenings;
  }
}
