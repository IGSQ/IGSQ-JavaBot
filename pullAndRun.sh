#!/bin/bash

git pull
gradle clean shadowJar
clear
java -jar build/libs/IGSQBot-0.0.1-all.jar