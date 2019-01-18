#!/bin/bash

artifacts=($(curl -s $1 | jq -r ".assets[].browser_download_url"))
echo ${#artifacts[@]} assests downloading

for url in "${artifacts[@]}"
do
	curl -L -O $url
done