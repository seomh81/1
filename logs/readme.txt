#백그라운드
ps -ef|grep python3
kill -9
rm -rf 1
rm -rf nohup.out
git clone https://github.com/seomh81/1.git
nohup python3 ~/1/1.py &
tail -f nohup.out

#스케줄러 크론탭 -e수정/생성 -l리스트확인 -r삭제
sudo chmod +x ~/1/cron/1monitoring.sh
sudo chmod +x ~/1/cron/2monitoring.sh
sudo chmod +x ~/1/cron/1pid_monitoring.sh
sudo chmod +x ~/1/cron/2pid_monitoring.sh
crontab -e
# Monitoring Process - every 1 mins
*/1 * * * * ~/1/cron/1pid_monitoring.sh >> ~/1/logs/1pid_monitoring.log 2>&1
*/1 * * * * ~/1/cron/2pid_monitoring.sh >> ~/1/logs/2pid_monitoring.log 2>&1
crontab -l
#하루 82MB정도 쌓임. 로그 확인방법
cat ~/1/logs/1monitoring.log
cat ~/1/logs/2monitoring.log
cat ~/1/logs/1pid_monitoring.log
cat ~/1/logs/2pid_monitoring.log

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

#한국시간으로 변경
sudo timedatectl set-timezone Asia/Seoul
timedatectl






