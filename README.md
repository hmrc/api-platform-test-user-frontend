api-platform-test-user-frontend
=================================

[![Build Status](https://travis-ci.org/hmrc/api-platform-test-user-frontend.svg)](https://travis-ci.org/hmrc/api-platform-test-user-frontend) [ ![Download](https://api.bintray.com/packages/hmrc/releases/api-platform-test-user-frontend/images/download.svg) ](https://bintray.com/hmrc/releases/api-platform-test-user-frontend/_latestVersion)

## Starting the service locally

Run it using service manager:

`sm --start API_PLATFORM_TEST_USER_FRONTEND`

Note: This doesn’t start up downstream dependencies e.g api-platform-test-user


## Running tests

Run all of the unit tests with `sbt test`

Run all of the integration tests with `sbt it:test`

## Service responsibilities

This is the front-end for the functionality to create test Individuals and Organisations that can subsequently be used for testing of API microservices in the External Test environment.

## Who uses this service?

Third party developers who want to test their application on external test environment.

## What does this service use?

It calls out to the [api-platform-test-user](https://github.com/hmrc/api-platform-test-user) microservice to create the user.

## Test in QA

It is only accessible in External Test.