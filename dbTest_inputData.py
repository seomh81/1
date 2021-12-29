import pymysql

# 전역변수 선언부
conn = None
cur = None

data1 = ""
data2 = ""
data3 = ""
data4 = ""

sql=""

#메인코드 접속정보, 커서생성
conn = pymysql.connect(host='smh.myds.me', user='tkek', password='***', db='smh', charset='utf8mb4')
cur = conn.cursor()

while(True): #break 만날 때까지 반복
    data1 = input("사용자 ID 입력(엔터는 종료):")
    if data1 == "": #data1 값이 없으면
        break #while문 나가기
    data2 = input("이름입력: ")
    data3 = input("이메일입력: ")
    data4 = input("생년입력 4자리(ex.1981): ")
    sql = "INSERT INTO userTable VALUES('" + data1 + "','"+ data2 + "','"+ data3 + "','"+ data4 + "')"
    cur.execute(sql)
    conn.commit()  # 커밋되기 전까지는 DB에 업로드 안됨

# conn.commit() #커밋되기 전까지는 DB에 업로드 안됨 (위로 올림. 바로 적용되도록)
conn.close()