#!/bin/sh
cd back-end

mvn clean package

scp target/jarjar/uber-team-bank.jar clouway@dev.telcong.com:/opt/telcong/bank

cd ..

cd front-end

grunt package

cd ..

scp -r frontend/ clouway@dev.telcong.com:/opt/telcong/bank

ssh clouway@dev.telcong.com sudo stop startbank

ssh clouway@dev.telcong.com sudo start startbank
