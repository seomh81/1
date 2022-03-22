#!/bin/sh
export PATH=$PATH:/usr/local/bin

# Change Directory
cd /home/opc

# Run Program
python3 ~/1/1.py I >> ~/1/logs/1monitoring.log 2>&1