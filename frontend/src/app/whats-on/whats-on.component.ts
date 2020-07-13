import { Component, OnInit, Input } from '@angular/core';
import { Movie } from '../shared/models/movie';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-whats-on',
  templateUrl: './whats-on.component.html',
  styleUrls: ['./whats-on.component.scss'],
})
export class WhatsOnComponent implements OnInit {
  movies: Movie[];
  @Input() isFull = true;

  constructor(private readonly route: ActivatedRoute) {}

  ngOnInit() {
    this.movies = this.route.snapshot.data.movies;
  }
}
