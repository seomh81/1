#!/bin/bash

timestamp=`date +%Y/%m/%d_%H:%M`

# BUY PROCESS CHECK
PID=`ps -ef |grep "adw.py" |grep -v 'grep'|awk '{print $2}'`

if [ -z "$PID" ];
then
        echo "$timestamp"
        echo 'MONITORING PROCESS DEAD'
        ~/1/cron/adw_mon.sh
else
        echo "$timestamp"
        echo 'MONITORING PROCESS IS RUNNING'
fi