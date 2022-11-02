#!/bin/bash

sbt -Dbrowser=chrome clean compile coverage test it:test coverageReport
