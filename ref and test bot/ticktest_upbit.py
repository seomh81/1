import pandas as pd
import pyupbit

# 참고 표준 출력 : https://wikidocs.net/20403

# 아래와 같이 BTC로도 가격 조회가 가능함
price_BTC = pyupbit.get_current_price("BTC-ETH")
print("ETH : {} BTC\n".format(price_BTC))

# 특정 종목의 날짜별 ohlcv 조회(20.7월~현재)
df = pyupbit.get_ohlcv("KRW-BTC")
print(df)

# ticks() 
import requests

url = "https://api.upbit.com/v1/trades/ticks?market=KRW-BTC&count=1"
resp = requests.get(url)
ticks = resp.json()

for tick in ticks:
    print(tick)

# https://gist.github.com/mr-yoo/a3d1f8a4152f94cf61e4bc566659cd20