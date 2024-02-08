api-platform-test-user-frontend
=================================

[ ![Download](https://api.bintray.com/packages/hmrc/releases/api-platform-test-user-frontend/images/download.svg) ](https://bintray.com/hmrc/releases/api-platform-test-user-frontend/_latestVersion)

## Starting the service locally

Run it using the included shell script:

`./run_local_with_dependencies.sh`

This should start this service and any dependent services.

Once the script has finished, the service will be available on http://localhost:9618/api-test-user


## Running tests

Run all of the unit tests with `sbt test`

Integration tests make use of docker-selenium-grid to spin up grid, chrome, firefox and edge.  See that repo for more details.
N.B. The docker image start script will need port 6001 adding to TARGET_PORTS and any firewall you have must permit this port.

Run all of the integration tests with `sbt -Dbrowser=chrome it:test`

Run the unit and integration tests with code coverage reporting using `./run_all_tests.sh`

## Service responsibilities

This is the front-end for the functionality to create test Individuals and Organisations that can subsequently be used for testing of API microservices in the External Test environment. 
The `api-platform-test-user-frontend` runs in the primary environment (ie Production and QA) but is configured to talk, via a proxy, to the `api-platform-test-user` backend service - which handles the work of actually creating the test users in a Mongo database - in the sandbox environment (ie External Test and Development, respectively). This is to allow testing to take place in External Test and keep Production systems free of test data.
Because of this, if running `api-platform-test-user-frontend` in Staging or Integration is desired, since neither environment has a sandbox. both `api-platform-test-user-frontend` and `api-platform-test-user` must be deployed in the same environment but the configuration must cause the frontend to talk to the backend using a proxy, as this arrangement is relied on in certain parts of the frontend code.

## Who uses this service?

Third party developers who want to test their application on external test environment.

## What does this service use?

It calls out to the [api-platform-test-user](https://github.com/hmrc/api-platform-test-user) microservice to create the user.

## Test in QA

It is only accessible in External Test.
