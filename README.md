# Max Header Size Issue Demo

This repository has been created to demonstrate an issue I'm experiencing at the minute.

### Context
Despite me configuring the application to have a max header size of 40KB, anything over 8KB results in a response status of 431.

### How to run

A simple `./gradlew test` will run the test which reproduces the scenario. If the test passes,
it means a 431 was returned.