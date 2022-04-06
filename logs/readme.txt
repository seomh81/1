######20220406

########1bot.txt

#확인 - 출력값
tail -f ~/1/logs/1monitoring.log
tail -f ~/1/logs/2monitoring.log
#확인 - 프로세스
tail -f ~/1/logs/1pid_monitoring.log
tail -f ~/1/logs/2pid_monitoring.log

#용량확인
df
#코드 수정 후 적용
ps -ef|grep python3
rm -rf ~/1
git clone https://github.com/seomh81/1.git

#전체 프로세스 킬
pkill python3
#용량확인
df -h

#파일 사용권한 부여
sudo chmod +x ~/1/cron/1monitoring.sh
sudo chmod +x ~/1/cron/2monitoring.sh
sudo chmod +x ~/1/cron/1pid_monitoring.sh
sudo chmod +x ~/1/cron/2pid_monitoring.sh
ls -all

crontab -e
#프로세스 먹통되는 현상 - cron에 주기적(1시간)으로 프로세스 종료
0 */1 * * * pkill python3
# Monitoring Process - every 1 mins
* * * * * ~/1/cron/1pid_monitoring.sh >> ~/1/logs/1pid_monitoring.log 2>&1
* * * * * ~/1/cron/2pid_monitoring.sh >> ~/1/logs/2pid_monitoring.log 2>&1

#크론탭 시작
sudo service crond start
#크론탭 중지
sudo service crond stop
#크론탭 재시작
sudo service crond restart

#백그라운드 - old : 재실행 안됨
nohup python3 ~/1/1.py &
nohup python3 ~/1/2.py &
tail -f ~/nohup.out
rm -rf ~/nohup.out

############## 크론탭 설정.txt

#한국시간으로 설정 + 확인
sudo timedatectl set-timezone Asia/Seoul
timedatectl

#스케줄러 크론탭 -e수정/생성 -l리스트확인 -r삭제
sudo chmod +x ~/1/cron/1monitoring.sh
sudo chmod +x ~/1/cron/2monitoring.sh
sudo chmod +x ~/1/cron/1pid_monitoring.sh
sudo chmod +x ~/1/cron/2pid_monitoring.sh
ls -all

#크론탭 쉘스크립트 모아 둘 폴더, 로그 폴더 생성
  /home/python/cron : 크론탭 쉘 스크립트 보관용 폴더
  /home/python/logs : 로그 보관용 폴더
mkdir cron
mkdir logs

#크론탭 조회
crontab -l
#크론탭 수정
crontab -e
# Monitoring Process - every 1 mins
* * * * * ~/1/cron/1pid_monitoring.sh >> ~/1/logs/1pid_monitoring.log 2>&1
#크론탭 삭제
crontab -r
#크론탭 시작
sudo service crond start
#크론탭 중지
sudo service crond stop
#크론탭 재시작
sudo service crond restart

#하루 82MB정도 쌓임. 로그 확인방법
cat ~/1/logs/1monitoring.log
cat ~/1/logs/2monitoring.log
cat ~/1/logs/1pid_monitoring.log
cat ~/1/logs/2pid_monitoring.log

#로그 관리 - 확인
cat /etc/logrotate.conf
cd /etc/logrotate.d
ls -rlt
cat syslog
#로그관리 초기 1번만 설정
sudo vi /etc/logrotate.d/1
~/1/logs/*.log {
    daily
    rotate 2
    dateext
    dateyesterday
    missingok
    copytruncate
    notifempty
}
sudo /usr/sbin/logrotate -f /etc/logrotate.conf


###1monitoring.sh
#!/bin/sh
export PATH=$PATH:/usr/local/bin

# Change Directory
#cd ~/1/
cd

# Run Program
python3 ~/1/1.py I >> ~/1/logs/1monitoring.log 2>&1

###1pid_monitoring.sh
#!/bin/bash

timestamp=`date +%Y/%m/%d_%H:%M`

# BUY PROCESS CHECK
PID=`ps -ef |grep "python3" |grep -v 'grep'|awk '{print $2}'`

if [ -z "$PID" ];
then
        echo "$timestamp"
        echo 'MONITORING PROCESS DEAD'
        ~/1/cron/1monitoring.sh
else
        echo "$timestamp"
        echo 'MONITORING PROCESS IS RUNNING'
fi


######### vm생성 후 초기세팅.txt

#VM생성 후 초기세팅
#git설치
sudo yum -y install git

#파이썬설치(업데이트)
sudo yum install python3.9
sudo update-alternatives --config python3
wget https://bootstrap.pypa.io/get-pip.py
python3 get-pip.py
pip install pandas
pip install requests
pip install PyJWT

#python을 python3으로 인식하게
alias python=python3

#한국시간으로 변경
sudo timedatectl set-timezone Asia/Seoul
timedatectl

#로그관리 초기 1번만 설정
sudo vi /etc/logrotate.d/1
~/1/logs/*.log {
    daily
    rotate 2
    dateext
    dateyesterday
    missingok
    copytruncate
    notifempty
}
sudo /usr/sbin/logrotate -f /etc/logrotate.conf


#Ubuntu(GCP)의 경우
sudo apt update && sudo apt upgrade -y
sudo apt install software-properties-common -y
sudo add-apt-repository ppa:deadsnakes/ppa
sudo apt install python3.10 python3.10-dev





