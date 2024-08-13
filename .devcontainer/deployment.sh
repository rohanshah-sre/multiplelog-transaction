#!/usr/bin/env bash

DT_DEPLOYMENT_URL=${DT_URL}/api/v1/deployment/installer/agent/unix/default/latest?arch=x86
wget  -O Dynatrace-OneAgent-Linux-1.295.66.20240805-161707.sh "$DT_DEPLOYMENT_URL" --header="Authorization: Api-Token $DT_PAAS_TOKEN"
./Dynatrace-OneAgent-Linux-1.295.66.20240805-161707.sh