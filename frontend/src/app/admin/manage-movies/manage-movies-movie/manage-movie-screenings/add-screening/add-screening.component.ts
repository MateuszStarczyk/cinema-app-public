import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ManageMoviesService } from 'src/app/admin/shared/services/manage-movies.service';
import { Screening } from 'src/app/admin/shared/models/screening';
import { Room } from 'src/app/admin/shared/models/room';

@Component({
  selector: 'app-add-screening',
  templateUrl: './add-screening.component.html',
  styleUrls: ['./add-screening.component.scss'],
})
export class AddScreeningComponent implements OnInit {
  isLoading = false;
  rooms: Room[] = [];
  newScreening = new Screening();
  @Input() movieId: string;
  @Output() save: EventEmitter<any> = new EventEmitter();

  constructor(private readonly manageMoviesService: ManageMoviesService) {}

  ngOnInit(): void {
    this.newScreening.movieId = this.movieId;
    this.loadRooms();
  }

  loadRooms() {
    this.manageMoviesService
      .getRooms()
      .subscribe(rooms => {
        this.rooms = rooms;
      });
  }

  addScreening() {
    this.isLoading = true;
    this.manageMoviesService
      .addScreening(this.newScreening)
      .subscribe(() => {
        this.newScreening.startDate = new Date();
        this.newScreening.roomId = null;
        this.save.emit();
      })
      .add(() => (this.isLoading = false));
  }
}
