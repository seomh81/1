# 사용방법
#
import schedule
import time

def job_second():
    print("I'm working...every second")

def job_minute():
    print("I'm working...every minute")

def job_hour():
    print("I'm working...every hour")

def job_day():
    print("I'm working...every day")

def job_monday():
    print("I'm working...every monday")

def job_wednesday():
    print("I'm working...every wednesday")

# # 10초에 한번씩 실행
schedule.every(10).seconds.do(job_second)
# # 10분에 한번씩 실행
schedule.every(10).minutes.do(job_minute)
# # 매 시간 실행
schedule.every().hours.do(job_hour)
# # 매일 10:30 에 실행
schedule.every().days.at("10:30").do(job_day)
# # 매주 월요일 실행
schedule.every().monday.do(job_monday)
# 매주 수요일 13:15 에 실행
schedule.every().wednesday.at("13:15").do(job_wednesday)

while True:
    schedule.run_pending()
    time.sleep(1)