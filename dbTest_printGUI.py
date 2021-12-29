import pymysql
from tkinter import *
from tkinter import messagebox


## 함수 선언부

# btnInsert 클릭 시 호출되는 함수
def insertData():
    con, cur = None, None
    data1, data2, data3, data4 = "", "", "", ""
    sql = ""

    conn = pymysql.connect(host='smh.myds.me', user='tkek', password='***', db='smh', charset='utf8mb4')
    cur = conn.cursor()

    data1 = edt1.get()
    data2 = edt2.get()
    data3 = edt3.get()
    data4 = edt4.get()
    # entry(한줄텍스트박스)로 입력받은 값을 data 변수들에 입력

    try:  # 예외처리 시작
        sql = "INSERT INTO userTable VALUES('" + data1 + "','" + data2 + "','" + data3 + "'," + data4 + ")"
        cur.execute(sql)
    except:  # 에러발생 시 작동
        messagebox.showerror('오류', '데이터 입력 오류가 발생함')
    else:  # 에러 없을 시 작동
        messagebox.showinfo('성공', '데이터 입력 성공')
    conn.commit()
    conn.close()


# btnSelect 클릭 시 호출되는 함수
def selectData():
    strData1, strData2, strData3, strData4 = [], [], [], []

    conn = pymysql.connect(host='smh.myds.me', user='tkek', password='***', db='smh', charset='utf8mb4')
    cur = conn.cursor()
    cur.execute("SELECT * FROM userTable")

    strData1.append("사용자ID")
    strData2.append("사용자이름")
    strData3.append("이메일")
    strData4.append("출생연도")
    strData1.append("-----------")
    strData2.append("-----------")
    strData3.append("-----------")
    strData4.append("-----------")
    # strData에 위에 값들을 먼저 추가

    while (True):
        row = cur.fetchone()  # 위에서 커서 실행으로 셀렉트한 테이블값을 한줄씩 row에 입력 후 다음줄로
        if row == None:
            break
        strData1.append(row[0])  # 리스트 strData1에 테이블 셀렉트한 첫번째 값 row[0] 입력
        strData2.append(row[1])
        strData3.append(row[2])
        strData4.append(row[3])

    listData1.delete(0, listData1.size() - 1)  # 리스트박스에 있는 값들을 모두 지워버림
    listData2.delete(0, listData2.size() - 1)
    listData3.delete(0, listData3.size() - 1)
    listData4.delete(0, listData4.size() - 1)

    for item1, item2, item3, item4 in zip(strData1, strData2, strData3, strData4):  # item에 strData들을 한줄씩 입력
        listData1.insert(END, item1)  # 리스트박스 마지막줄에 item 값들을 넣어줘서 보여
        listData2.insert(END, item2)
        listData3.insert(END, item3)
        listData4.insert(END, item4)
    conn.close()


## 메인 코드부
window = Tk()
window.geometry("600x300")
window.title("GUI 데이터 입력")

edtFrame = Frame(window)
edtFrame.pack()
listFrame = Frame(window)
listFrame.pack(side=BOTTOM, fill=BOTH, expand=1)

edt1 = Entry(edtFrame, width=10)
edt1.pack(side=LEFT, padx=10, pady=10)

edt2 = Entry(edtFrame, width=10)
edt2.pack(side=LEFT, padx=10, pady=10)

edt3 = Entry(edtFrame, width=10)
edt3.pack(side=LEFT, padx=10, pady=10)

edt4 = Entry(edtFrame, width=10)
edt4.pack(side=LEFT, padx=10, pady=10)

btnInsert = Button(edtFrame, text="입력", command=insertData)
btnInsert.pack(side=LEFT, padx=10, pady=10)

btnSelect = Button(edtFrame, text="조회", command=selectData)
btnSelect.pack(side=LEFT, padx=10, pady=10)

listData1 = Listbox(listFrame, bg='white')
listData1.pack(side=LEFT, fill=BOTH, expand=1)

listData2 = Listbox(listFrame, bg='white')
listData2.pack(side=LEFT, fill=BOTH, expand=1)

listData3 = Listbox(listFrame, bg='white')
listData3.pack(side=LEFT, fill=BOTH, expand=1)

listData4 = Listbox(listFrame, bg='white')
listData4.pack(side=LEFT, fill=BOTH, expand=1)

window.mainloop()