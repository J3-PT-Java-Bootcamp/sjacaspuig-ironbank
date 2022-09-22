import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { HttpClientModule } from '@angular/common/http';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NbDatepickerModule, NbLayoutModule, NbThemeModule } from '@nebular/theme';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import { FormsModule } from '@angular/forms';
import { OAuthModule } from 'angular-oauth2-oidc';
import { FooterModule } from './components/footer/footer.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    NbThemeModule.forRoot({ name: 'corporate' }),
    NbEvaIconsModule,
    NbLayoutModule,
    OAuthModule.forRoot({
      resourceServer: {
          allowedUrls: ['http://localhost:8080'],
          sendAccessToken: true
      }
    }),
    FooterModule,
    NbDatepickerModule.forRoot(),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
