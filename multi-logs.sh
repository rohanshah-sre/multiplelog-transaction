#!/bin/bash

# TO DO?: create 2 seperate functions one for setup of dirs & cron 
# and a second to invoke only the log generation
# no need to check for the dirs everytime

##### function 1
### Creates log directories
# set the the directory path for the logs
directory_path="/var/log/multilog"

# setting the user who will own the created directory strucutre and files
# we'll add this script to cron later, this prevents needing to run as root
user=$(whoami)

# check if the directory exists, if not create it with ownership of the user running it
if [ ! -d "$directory_path" ]; then
   sudo mkdir -p "$directory_path" > /dev/null 2>&1 && sudo chown -R $user "$directory_path"
fi
# --------- 

#### function 2
# dttransid generate a random trasnaction ID in DATE-DYNATRANSACTION-12345 format
dttransid=$(date +%Y%m%d)DYNATRANSACTION$RANDOM

### the elusive purple monkey dishwasher id!!!
pmdwid=$(mktemp -u XXXXXXXXXXXXXXX)

# inittime sets the start time of the transaction (initial time)
# this inital cration time can then be referernece through each hop

### DEVICE #1
inittime=$(date +"%Y-%m-%d %H:%M:%S.%3N")
sed "s/INITTIME/$inittime/g; s/DYNATRANSID/$dttransid/g" /home/$(whoami)/multilog-transaction/new-templates/01-DEVICE01.xml >> $directory_path/processor-01.log && echo >> $directory_path/processor-01.log
sleep .3
### 

### DEVICE #2 
## dynatime sets & updates varibles for rolling timestamps 
# as each step of the script runs to mimic latency
# we'll then reference the initial timestamp for the first hop
dynatime=$(date +"%Y-%m-%d %H:%M:%S.%3N")
sed "s/DYNATIME/$dynatime/g; s/INITTIME/$inittime/g; s/DYNATRANSID/$dttransid/g; s/PMDWID/$pmdwid/g" /home/$(whoami)/multilog-transaction/new-templates/02-DEVICE02.json >> $directory_path/processor-02.log && echo >> $directory_path/processor-02.log
sleep .5

### DEVICE #3
## dynatime sets & updates varibles for rolling timestamps 
# as each step of the script runs to mimic latency
# we'll then reference the initial timestamp for the first hop
dynatime=$(date +"%Y-%m-%d %H:%M:%S.%3N")
sed "s/DYNATIME/$dynatime/g; s/INITTIME/$inittime/g; s/DYNATRANSID/$dttransid/g" /home/$(whoami)/multilog-transaction/new-templates/03-DEVICE03.txt >> $directory_path/processor-03.log && echo >> $directory_path/processor-03.log
sleep .5

### DEVICE #4
## dynatime sets & updates varibles for rolling timestamps 
# as each step of the script runs to mimic latency
# we'll then reference the initial timestamp for the first hop
dynatime=$(date +"%Y-%m-%d %H:%M:%S.%3N")
sed "s/DYNATIME/$dynatime/g; s/INITTIME/$inittime/g; s/DYNATRANSID/$dttransid/g" /home/$(whoami)/multilog-transaction/new-templates/04-DEVICE04.txt >> $directory_path/processor-04.log && echo >> $directory_path/processor-04.log
sleep .2


### DEVICE #5
## dynatime sets & updates varibles for rolling timestamps 
# as each step of the script runs to mimic latency
# we'll then reference the initial timestamp for the first hop
dynatime=$(date +"%Y-%m-%d %H:%M:%S.%3N")
sed "s/DYNATIME/$dynatime/g; s/INITTIME/$inittime/g; s/DYNATRANSID/$dttransid/g" /home/$(whoami)/multilog-transaction/new-templates/05-DEVICE05.xml >> $directory_path/processor-05.log && echo >> $directory_path/processor-05.log
sleep .7

### DEVICE #6
## dynatime sets & updates varibles for rolling timestamps 
# as each step of the script runs to mimic latency
# we'll then reference the initial timestamp for the first hop
dynatime=$(date +"%Y-%m-%d %H:%M:%S.%3N")
sed "s/DYNATIME/$dynatime/g; s/DYNATRANSID/$dttransid/g; s/PMDWID/$pmdwid/g" /home/$(whoami)/multilog-transaction/new-templates/06-DEVICE06.json >> $directory_path/processor-06.log && echo >> $directory_path/processor-06.log
sleep .5