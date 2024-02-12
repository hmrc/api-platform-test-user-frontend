#!/bin/bash -e

BROWSER=$1
ENVIRONMENT=$2

sbt -Dbrowser="${BROWSER:=chrome}" -Denvironment="${ENVIRONMENT:=local}" pre-commit
