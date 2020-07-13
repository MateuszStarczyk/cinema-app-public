# Cinema app 
Cinema ticketing system

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![CircleCI](https://circleci.com/gh/pwr-piisw/cinema-app.svg?style=svg&circle-token=93a34822c6b9c6d509a0251a5b496a9fded5e441)](https://circleci.com/gh/pwr-piisw/cinema-app)

### How to build:
```shell script
mvn clean install
```

### Backend
[Spring Boot](https://github.com/spring-projects/spring-boot) version 2.2.6
#### Run: 
```shell script
mvn spring-boot:run -pl backend
```
__Note:__ now frontend is running in production mode on [http://localhost:8080](http://localhost:8080)

### Frontend
[Angular CLI](https://github.com/angular/angular-cli) version 9.1.1.
#### Run:
Go into frontend directory and run:
```shell script
ng serve
```
__Note:__ now frontend is running in developer mode on [http://localhost:4200](http://localhost:4200)

### Ticket validation via REST API
REST API offers two ticket validation endpoints:

#### 1. Ticket information: 
  Returns json data about a ticket.

* **URL**

  /tickets-validation/:ticketId

* **Method:**

  `GET`
  
*  **URL Params**

   **Required:**
 
   `ticketId=[UUID]`

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{
    "selectedSeats": [
        "D-6"
    ],
    "ticketKinds": [
        {
            "id": 1,
            "name": "CHILD / STUDENT",
            "price": 15.99,
            "qty": 1
        }
    ],
    "startDate": "2020-06-14T20:20:00.000+0200",
    "roomId": 2,
    "movieId": "f18d1a59-8718-4b77-9000-fffc32a3a53a",
    "movieTitle": "Harry Potter",
    "duration": 120
}`
 
* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** `Ticket with UUID b94255ac-9480-46e9-90e5-012dc43b7225e not found!`

* **Sample Call:**

  `http://localhost:8080/api/tickets-validation/7e1d801a-ebad-48ec-a898-73dc61acd32a`
  

#### 2. Ticket validation for screening + ticket information: 
  Returns json data about a ticket and an information if this ticket is valid for a given screening.

* **URL**

  /tickets-validation/:ticketId/screenings/:screeningId

* **Method:**

  `GET`
  
*  **URL Params**

   **Required:**
 
   `ticketId=[UUID]`
   
   `screeningId=[Integer]`

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{
    "isValid": true,
    "selectedSeats": [
        "D-6"
    ],
    "ticketKinds": [
        {
            "id": 1,
            "name": "CHILD / STUDENT",
            "price": 15.99,
            "qty": 1
        }
    ],
    "startDate": "2020-06-14T20:20:00.000+0200",
    "roomId": 2,
    "movieId": "f18d1a59-8718-4b77-9000-fffc32a3a53a",
    "movieTitle": "Harry Potter",
    "duration": 120
}`
 
* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** `Ticket with UUID b94255ac-9480-46e9-90e5-012dc43b7225e not found!`

* **Sample Call:**

  `http://localhost:8080/api/tickets-validation/7e1d801a-ebad-48ec-a898-73dc61acd32a/screenings/15`
  
__Note:__ REST API is running in production and developer modes on [http://localhost:8080/api](http://localhost:8080/api)
