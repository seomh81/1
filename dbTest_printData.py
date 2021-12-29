import pymysql

# 전역변수 선언부
conn = None
cur = None

data1 = ""
data2 = ""
data3 = ""
data4 = ""

row=None #테이블 행을 받아줌

#메인코드 접속정보, 커서생성
conn = pymysql.connect(host='smh.myds.me', user='tkek', password='***', db='smh', charset='utf8mb4')
cur = conn.cursor()

cur.execute("SELECT * FROM userTable") #변수없이 직접 커서 실행 가능

print("사용자ID    사용자이름    이메일    출생연도")
print("---------------------------------------")

while(True): #break 만날 때까지 반복
    row = cur.fetchone() #row에 커서(테이블 셀렉트)를 한줄 입력하고 다음 줄로 넘어감
    if row == None: #커서(테이블 셀렉트) 값이 없으면, ""일 경우 에러뜸. None으로 해야 null값 인식함.
        break #while문 나가기
    data1 = row[0]
    data2 = row[1]
    data3 = row[2]
    data4 = row[3]
    print("%5s     %15s     %15s     %d" % (data1,data2,data3,data4))

# conn.commit() #커밋되기 전까지는 DB에 업로드 안됨 (위로 올림. 바로 적용되도록)
conn.close()