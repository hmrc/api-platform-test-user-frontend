#!/bin/bash

sbt -Dtest_driver=chrome clean compile coverage test it:test coverageReport
python dependencyReport.py api-platform-test-user-frontend
