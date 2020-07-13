import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { MaterialModule } from 'src/app/material/material.module';
import { FormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { AppRoutingModule } from '../app-routing.module';
import { NotFoundComponent } from './not-found/not-found.component';
import { NoPermissionsComponent } from './no-permissions/no-permissions.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgxPermissionsModule } from 'ngx-permissions';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthInterceptor } from './helpers/auth.interceptor';
import { ErrorInterceptor } from './helpers/error.interceptor';
import { QRCodeModule } from 'angularx-qrcode';
import {YouTubePlayerModule} from '@angular/youtube-player';
import { ImgFallbackModule } from 'ngx-img-fallback';

@NgModule({
  imports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    FlexLayoutModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    NgxPermissionsModule.forRoot(),
    QRCodeModule,
    YouTubePlayerModule,
    ImgFallbackModule
  ],
  exports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    FlexLayoutModule,
    AppRoutingModule,
    NoPermissionsComponent,
    HttpClientModule,
    NgxPermissionsModule,
    QRCodeModule,
    YouTubePlayerModule,
    ImgFallbackModule
  ],
  declarations: [NotFoundComponent, NoPermissionsComponent],
  providers: [
    DatePipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true,
    },
  ],
})
export class SharedModule {}
