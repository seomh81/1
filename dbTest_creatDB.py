#테이블 만들기

import pymysql #pymysql 임포트

#전역변수 선언부
conn = None
cur = None

sql=""

#메인코드 접속정보, 커서생성
conn = pymysql.connect(host='smh.myds.me', user='tkek', password='***', db='smh', charset='utf8mb4')
cur = conn.cursor()

#실행할 SQL문
#sql = "SELECT * FROM test"
sql = "CREATE TABLE IF NOT EXISTS userTable (id char(4), userName char(10), email char(15), birthYear int)"

cur.execute(sql)

conn.commit()

conn.close()