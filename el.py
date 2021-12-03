import requests
import time
from bs4 import BeautifulSoup
from lib import upbit_new as upbit

while True:

    time.sleep(3)

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

    if t1 != t1:
        upbit.send_line_message(title['title'])

    time.sleep(3)

    #time.sleep(14400)