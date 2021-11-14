from datetime import datetime, timedelta
'''
now = datetime.now()
print("현재 :" , now)	# 현재 : 2021-01-09 19:41:03.645702

before_one_microsecond = now - timedelta(microseconds=1)
print("1마이크로초 전 :", before_one_microsecond) # 1마이크로초 전 : 2021-01-09 19:41:03.645701
after_one_microsecond = now + timedelta(microseconds=1)
print("1마이크로초 후 :", after_one_microsecond)	# 1마이크로초 후 : 2021-01-09 19:41:03.645703

before_one_millisecond = now - timedelta(milliseconds=1)
print("1밀리초 전 :", before_one_millisecond)	# 1밀리초 전 : 2021-01-09 19:41:03.644702
after_one_millisecond = now + timedelta(milliseconds=1)
print("1밀리초 후 :", after_one_millisecond)	# 1밀리초 후 : 2021-01-09 19:41:03.646702

before_one_second = now - timedelta(seconds=1)
print("1초 전 :", before_one_second)	# 1초 전 : 2021-01-09 19:41:02.645702
after_one_second = now + timedelta(seconds=1)
print("1초 후 :", after_one_second)	# 1초 후 : 2021-01-09 19:41:04.645702

before_one_minute = now - timedelta(minutes=1)
print("1분 전 :", before_one_minute)	# 1분 전 : 2021-01-09 19:40:03.645702
after_one_minute = now + timedelta(minutes=1)
print("1분 후 :", after_one_minute)	# 1분 후 : 2021-01-09 19:42:03.645702

before_one_hour = now - timedelta(hours=1)
print("1시간 전 :", before_one_hour)	# 1시간 전 : 2021-01-09 18:41:03.645702
after_one_hour = now + timedelta(hours=1)
print("1시간 후 :", after_one_hour)	# 1시간 후 : 2021-01-09 20:41:03.645702

before_one_day = now - timedelta(days=1)
print("1일 전 :", before_one_day)	# 1일 전 : 2021-01-08 19:41:03.645702
after_one_day = now + timedelta(days=1)
print("1일 후 :", after_one_day)	# 1일 후 : 2021-01-10 19:41:03.645702

before_one_week = now - timedelta(weeks=1)
print("1주 후 :", before_one_week)	# 1주 후 : 2021-01-02 19:41:03.645702
after_one_week = now + timedelta(weeks=1)
print("1주 후 :", after_one_week)	# 1주 후 : 2021-01-16 19:41:03.645702

now = datetime.now()
due_time = now + timedelta(seconds=5)
now = now.strftime('%S')
due_time = due_time.strftime('%S')
print(now)
print(due_time)
i=0
'''


'''
i=0
due_time = (datetime.now() + timedelta(seconds=5)).strftime('%M%S')

while True:
    i += 1
    now = datetime.now().strftime('%M%S')
    print(now,i)
    if due_time == now:
        now = datetime.now()
        due_time = (datetime.now() + timedelta(seconds=5)).strftime('%M%S')
        i = 0
'''
#2021-11-14T17:12:00

now = str(datetime.now())
know = now[0:10] + 'T' + now[11:19]
print(know)