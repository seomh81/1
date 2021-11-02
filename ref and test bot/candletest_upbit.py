#분봉
#1,3,5,10,15,30,60,240분봉에 대해서 최대 200개 조회 가능
#from pandas.core.indexes.base import Index
import pyupbit
import pandas as pd

#30분봉(최대 200개 요청가능)
df = pyupbit.get_ohlcv("KRW-BTC", "minute1", 5)
df.index.name='time'

df.to_excel("test.xlsx")

#100시간 전 종가
firstClose = int(df.iloc[0]['close'])
#100시간 전 거래량
firstVolume = int(df.iloc[0]['volume'])
#현재 종가
curClose = int(df.iloc[-1]['close'])
#현재 거래량
curVolume = int(df.iloc[-1]['volume'])

#test
#v_b9 = int(df.iloc[-9]['volume'])
#print(v_b9)

print('처음 종가/거래량 : ' + str(firstClose) + ' / ' + str(firstVolume))
print('현재 종가/거래량 : ' + str(curClose) + ' / ' + str(curVolume))

#간단한 알고리즘
if(firstClose < curClose and firstVolume*1.2 < curVolume):
    print('급증 가능성 ↑')
else:
    print('급등 가능성 ↓')


print('-1 close',int(df.iloc[-1]['close']))
print('-2 close',int(df.iloc[-2]['close']))
print('-3 close',int(df.iloc[-3]['close']))
print('0 close',int(df.iloc[0]['close']))

print('-1 high',int(df.iloc[-1]['high']))
print('-3 high',int(df.iloc[-3]['high']))
print('-1 low',int(df.iloc[-1]['low']))
print('-1 volume',int(df.iloc[0]['volume']))
'''
print('0 index',df.iloc[0]['time'])

print('1 index',df.iloc[1]['time'])
print('2 index',df.iloc[2]['time'])
print('-1 index',df.iloc[-1]['time'])
print('-2 index',df.iloc[-2]['time'])
'''