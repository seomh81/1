ps -ef|grep python3

kill -9

rm -rf 1
rm -rf nohup.out

git clone https://github.com/seomh81/1.git

nohup python3 ~/1/1.py &

tail -f nohup.out

#스케줄러 크론탭 -e수정/생성 -l리스트확인 -r삭제
crontab -e
# Monitoring Process - every 1 mins
*/1 * * * * ~/1/cron/1pid_monitoring.sh >> ~/1/logs/1pid_monitoring.log 2>&1
*/1 * * * * ~/1/cron/2pid_monitoring.sh >> ~/1/logs/2pid_monitoring.log 2>&1
crontab -l
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