#!/bin/bash

 mkdir application/shelf
 .circleci/download_assets.sh https://api.github.com/repos/kgrid-objects/cpic-objects/releases/latest
 .circleci/download_assets.sh https://api.github.com/repos/kgrid-objects/mopen-opioid-collection/releases/latest
 .circleci/download_assets.sh https://api.github.com/repos/kgrid-objects/example-kos/releases/latest
 .circleci/download_assets.sh https://api.github.com/repos/kgrid-objects/cancer-risk/releases/latest
 .circleci/download_assets.sh https://api.github.com/repos/kgrid-objects/icon-array/releases/latest
 .circleci/download_assets.sh https://api.github.com/repos/kgrid-objects/script-numerate/releases/latest
 .circleci/download_assets.sh https://api.github.com/repos/kgrid-objects/postpci/releases/latest
 .circleci/download_assets.sh https://api.github.com/repos/kgrid-objects/labwise/releases/latest
 unzip -o opioid-all.zip -d application/shelf
 unzip -o cpic-all.zip -d application/shelf
 unzip -o hello-world.zip -d application/shelf
 unzip -o cancer-risk.zip -d application/shelf
 unzip -o icon-array.zip -d application/shelf
 unzip -o scriptnumerate-all.zip -d application/shelf
 unzip -o postpci-all.zip -d application/shelf
 unzip -o labwise-all.zip -d application/shelf