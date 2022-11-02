#!/usr/bin/env bash
sbt -Dbrowser=chrome -Daccessibility.test='true' it:test