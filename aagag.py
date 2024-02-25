import requests
from bs4 import BeautifulSoup

# 크롤링할 사이트 URL
url = "https://aagag.com/issue/"

# 사이트로 요청을 보내고 응답을 받습니다.
response = requests.get(url)

# 응답의 상태 코드를 확인하여 정상적으로 응답을 받았는지 확인합니다.
if response.status_code == 200:
    # BeautifulSoup을 사용하여 HTML을 파싱합니다.
    soup = BeautifulSoup(response.text, "html.parser")

    # 게시글 제목을 찾습니다. 웹 페이지의 HTML 구조에 따라 적절한 요소를 선택해야 합니다.
    # 예를 들어, 게시글 제목이 <h2> 태그 안에 있다면, 아래와 같이 선택할 수 있습니다.
    titles = soup.find_all("title", class_="title")

    # 찾은 제목을 출력합니다.
    for title in titles:
        print(title.text.strip())
else:
    print("요청에 실패했습니다. 상태 코드:", response.status_code)