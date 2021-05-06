# Payment Application Server


### Description
This is the server-side code for a venmo-like payment application. A user can send payments, request other users to send them money, view their balance, past payments, pending payments, and more. The front-end is CLI based and in another repository.

### Motivation
This was a bootcamp project. After completing the requirements I wanted to host it on a cloud server rather than my machine, so I deployed it to Heroku.

### Methods
The postgres DB contains tables for users, accounts, transfers, and others. Model classes were made in Java for the respective objects. Spring Boot was used to build the Controller. 12 API endpoints were created and used JbdcDAOs to interact with the DB.
<br />
<br />

To put the server on Heroku the application.properties file had to be slightly tweaked. After deploying it to Heroku a Heroku postgreSQL DB was attached. The database was then seeded with DbVisualizer.

### Next Steps & Contributing

After learning Vue.js I'm considering making a front-end application rather than the current CLI and hosting that on Heroku.
