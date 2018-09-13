#!/bin/bash

 mkdir application/shelf
 repos=(mopen-opioid-collection cpic-objects example-kos cancer-risk icon-array script-numerate postpci labwise )

 for i in "${repos[@]}"
 do
  url=(https://api.github.com/repos/kgrid-objects/$i/releases/latest)
  .circleci/download_assets.sh "$url"
 done

 unzip -o opioid-all.zip -d application/shelf
 unzip -o cpic-all.zip -d application/shelf
 unzip -o hello-world.zip -d application/shelf
 unzip -o cancer-risk.zip -d application/shelf
 unzip -o icon-array.zip -d application/shelf
 unzip -o scriptnumerate-all.zip -d application/shelf
 unzip -o postpci-all.zip -d application/shelf
 unzip -o labwise-all.zip -d application/shelf


