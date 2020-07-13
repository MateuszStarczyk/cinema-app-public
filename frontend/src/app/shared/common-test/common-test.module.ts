import { NgModule } from '@angular/core';
import { SharedModule } from '../shared.module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgxPermissionsModule } from 'ngx-permissions';
import { RouterTestingModule } from '@angular/router/testing';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AppModule } from 'src/app/app.module';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@NgModule({
  imports: [
    SharedModule,
    AppModule,
    RouterTestingModule,
    AppRoutingModule,
    HttpClientTestingModule,
    MatDialogModule,
    NgxPermissionsModule.forRoot(),
  ],
  exports: [
    SharedModule,
    RouterTestingModule,
    AppRoutingModule,
    HttpClientTestingModule,
    NgxPermissionsModule,
  ],
  declarations: [],
  providers: [
    {
      provide: MatDialogRef,
      useValue: {}
    },
    {
      provide: MAT_DIALOG_DATA,
      useValue: {
        seatsQty: 0,
        selectedSeats: [],
        screeningId: 1,
      }
    }
 ]
})
export class CommonTestModule {}
