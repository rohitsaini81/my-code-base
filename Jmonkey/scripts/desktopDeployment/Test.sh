#!/bin/bash
jre/bin/java -XX:MaxRAMPercentage=60 -classpath "lib/*" testjmonkey.Test
exit 0
