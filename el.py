import requests
import time
from bs4 import BeautifulSoup
from module import upbit_new as upbit

t4 = ''
t44 = ''
t444 = ''

while True:

    search_keyword = '엘리베이터'
    url = f'https://search.naver.com/search.naver?where=news&sm=tab_jum&query={search_keyword}'

    r = requests.get(url)
    soup = BeautifulSoup(r.text, 'html.parser')
    news_titles = soup.select('.sp_nnews .news_wrap .news_tit')

    print('총', len(news_titles), '개의 뉴스 제목이 있습니다')
    print()
    t1 = ''
    for title in news_titles:
        #print(title['title'])
        t2 = title['title']
        t1 = t1 + '\n' + t2
        #라인에 보내기 내용보내기
        #upbit.send_line_message(title['title'])
    print(t1)
    t3 = t1

    if t3 != t4:
        print('내용이 변경되어 라인으로 전송합니다!!')
        upbit.send_line_message(t3)

    time.sleep(3)

    t4 = t3

    search_keyword = '승강기'
    url = f'https://search.naver.com/search.naver?where=news&sm=tab_jum&query={search_keyword}'

    r = requests.get(url)
    soup = BeautifulSoup(r.text, 'html.parser')
    news_titles = soup.select('.sp_nnews .news_wrap .news_tit')

    print('총', len(news_titles), '개의 뉴스 제목이 있습니다')
    print()
    t11 = ''
    for title in news_titles:
        # print(title['title'])
        t22 = title['title']
        t11 = t11 + '\n' + t22
        # 라인에 보내기 내용보내기
        # upbit.send_line_message(title['title'])
    print(t11)
    t33 = t11

    if t33 != t44:
        print('내용이 변경되어 라인으로 전송합니다!!')
        upbit.send_line_message(t33)

    time.sleep(3)

    t44 = t33


    search_keyword = '속보'
    url = f'https://search.naver.com/search.naver?where=news&sm=tab_jum&query={search_keyword}'

    r = requests.get(url)
    soup = BeautifulSoup(r.text, 'html.parser')
    news_titles = soup.select('.sp_nnews .news_wrap .news_tit')

    print('총', len(news_titles), '개의 뉴스 제목이 있습니다')
    print()
    t111 = ''
    for title in news_titles:
        # print(title['title'])
        t222 = title['title']
        t111 = t111 + '\n' + t222
        # 라인에 보내기 내용보내기
        # upbit.send_line_message(title['title'])
    print(t111)
    t333 = t111

    if t333 != t444:
        print('내용이 변경되어 라인으로 전송합니다!!')
        upbit.send_line_message(t333)

    time.sleep(3)

    t444 = t333

    time.sleep(14400)