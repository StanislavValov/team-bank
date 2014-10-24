#!/bin/bash
cd back-end
mvn clean package
cd ..
cd front-end
grunt package
cd ..
cp -r frontend back-end/target/jarjar
cd back-end
cp configuration.properties target/jarjar
cd target/jarjar
java -jar uber-team-bank.jar