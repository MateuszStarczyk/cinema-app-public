import { Component, Input, EventEmitter, Output } from '@angular/core';
import { Screening } from 'src/app/admin/shared/models/screening';
import { ManageMoviesService } from 'src/app/admin/shared/services/manage-movies.service';
import { AlertService } from 'src/app/shared/services/alerts/alert.service';

@Component({
  selector: 'app-screening-item',
  templateUrl: './screening-item.component.html',
  styleUrls: ['./screening-item.component.scss'],
})
export class ScreeningItemComponent {
  isLoading = false;
  @Input() screening: Screening;
  @Output() deleted: EventEmitter<any> = new EventEmitter();

  constructor(
    private readonly manageMoviesService: ManageMoviesService,
    private readonly alerts: AlertService
  ) {}

  askToConfirmDeleteScreening() {
    this.alerts
      .showYesNoDialog(`Do you want to delete screening?`)
      .then((result) => {
        if (result.value) {
          this.deleteScreening();
        }
      });
  }

  deleteScreening() {
    this.isLoading = true;
    this.manageMoviesService.deleteScreening(this.screening.id).subscribe(
      () => this.deleted.emit()
    ).add(() => this.isLoading = false);
  }
}
