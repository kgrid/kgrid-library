#!/bin/bash

 mkdir application/shelf
 repos=(mopen-opioid-collection cpic-objects example-kos cancer-risk icon-array script-numerate postpci labwise )

 for i in "${repos[@]}"
 do
  url=(https://api.github.com/repos/kgrid-objects/$i/releases/latest)
  .circlecidownload_assets.sh "$url"
 done

 unzip -o *.zip -d application/shelf



