#!/bin/bash
export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
pid=`ps -ef | grep "elasticsearch" | grep -v 'grep' | awk '{print $2}'`
if [ -z $pid ]; then
    echo $(date)
    service elasticsearch start
    echo ""
fi