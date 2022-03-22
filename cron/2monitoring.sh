#!/bin/sh
export PATH=$PATH:/usr/local/bin

# Change Directory
cd /home/opc

# Run Program
python3 ~/1/2.py I >> ~/1/logs/2monitoring.log 2>&1