#!/bin/sh
cd back-end
mvn clean package
scp target/jarjar/uber-team-bank.jar clouway@dev.telcong.com:/opt/telcong/team-bank
cd ..
cd front-end
grunt package
cd ..
scp -r frontend/ clouway@dev.telcong.com:/opt/telcong/team-bank

ssh clouway@dev.telcong.com sudo stop team-bank
ssh clouway@dev.telcong.com sudo start team-bank