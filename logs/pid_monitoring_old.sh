#!/bin/bash

timestamp=`date +%Y/%m/%d/%H:%M`

# BUY PROCESS CHECK
PID=`ps -ef |grep -w '1/1.py' |grep -v grep|awk '{print $2}'`

if [ -z "$PID" ]
then
        echo "$timestamp"
        echo 'MONITORING PROCESS DEAD'
        /home/opc/1/cron/monitoring.sh
else
        echo "$timestamp"
        echo 'MONITORING PROCESS IS RUNNING'
fi