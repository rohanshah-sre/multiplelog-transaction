#!/bin/bash

##########################
# 2. Run test harness TODO
#export OTEL_SERVICE_NAME=iio-fluentbitv3
#export PYTEST_RUN_NAME=startup-automated-test
#export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317
#pytest --export-traces codespaces_test.py

echo "In post-create.sh" > /tmp/status.log

DT_DEPLOYMENT_URL=${DT_URL}/api/v1/deployment/installer/agent/unix/default/latest?arch=x86

echo $DT_DEPLOYMENT_URL >> /tmp/status.log
echo $DT_PAAS_TOKEN >> /tmp/status.log
echo "
docker run -d \
--restart=on-failure:5 \
--read-only=true \
--pid=host \
--net=host \
--cap-drop ALL \
--cap-add CHOWN \
--cap-add DAC_OVERRIDE \
--cap-add DAC_READ_SEARCH \
--cap-add FOWNER \
--cap-add FSETID \
--cap-add KILL \
--cap-add NET_ADMIN \
--cap-add NET_RAW \
--cap-add SETFCAP \
--cap-add SETGID \
--cap-add SETUID \
--cap-add SYS_ADMIN \
--cap-add SYS_CHROOT \
--cap-add SYS_PTRACE \
--cap-add SYS_RESOURCE \
--security-opt apparmor:unconfined \
-v /:/mnt/root \
-v /opt:/mnt/volume_storage_mount \
-e ONEAGENT_ENABLE_VOLUME_STORAGE=true \
-e ONEAGENT_INSTALLER_SCRIPT_URL=$DT_DEPLOYMENT_URL \
-e ONEAGENT_INSTALLER_DOWNLOAD_TOKEN=$DT_PAAS_TOKEN\ 
dynatrace/oneagent  " >> /tmp/status.log

docker run -d \
--restart=on-failure:5 \
--read-only=true \
--pid=host \
--net=host \
--cap-drop ALL \
--cap-add CHOWN \
--cap-add DAC_OVERRIDE \
--cap-add DAC_READ_SEARCH \
--cap-add FOWNER \
--cap-add FSETID \
--cap-add KILL \
--cap-add NET_ADMIN \
--cap-add NET_RAW \
--cap-add SETFCAP \
--cap-add SETGID \
--cap-add SETUID \
--cap-add SYS_ADMIN \
--cap-add SYS_CHROOT \
--cap-add SYS_PTRACE \
--cap-add SYS_RESOURCE \
--security-opt apparmor:unconfined \
-v /:/mnt/root \
-v /opt:/mnt/volume_storage_mount \
-e ONEAGENT_ENABLE_VOLUME_STORAGE=true \
-e ONEAGENT_INSTALLER_SCRIPT_URL=$DT_DEPLOYMENT_URL \
-e ONEAGENT_INSTALLER_DOWNLOAD_TOKEN=$DT_PAAS_TOKEN \
dynatrace/oneagent  