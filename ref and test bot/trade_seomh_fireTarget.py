'''

매수 모니터링:
    업비트 모든 코인 추적
    5분봉 기준 9개 캔들로 추적
매수 기준:
    현 거래량이 이전 8개(40분)보다 0.01% 초과할 때
    현 가격이 이전 8개 고가를 초과할 때 + 이전 가격의 0.01% 초과할 때
    시장가 매수 6000원
매도 모니터링:
    매수와 동일
매도 기준:
    종가가 

'''

import logging
import os
import sys
import time
import traceback
import upbitpy
import pyupbit
import requests
from decimal import Decimal
import datetime

# 공통 모듈 Import
sys.path.append(os.path.dirname(os.path.dirname(__file__)))
from lib import upbit as upbit  # noqa


# -----------------------------------------------------------------------------
# - Name : start_selltrade
# - Desc : 매도 로직
# - Input
# 1) target_item : 대상 종목
# 2) rsi_sell_value : 매도 기준 RSI 값
# 3) rsi_buy_value : 매수 기준 RSI 값
# 4) buy_amt : 매수 금액
# -----------------------------------------------------------------------------
def start_selltrade(target_item, buy_amt):#(target_item, minute_interval, minute_check, buy_amt):
    try:
        # 매도 될때까지 반복
        while True:

            #logging.info('매도 로직 가동중...')

            # 해당 종목의 RSI 지표 산출
            # 1. 10분봉 기준 RSI 지표 산출
            #rsi_data = upbit.get_indicators(target_item, '10', 200, 5)

            df = pyupbit.get_ohlcv(target_item, "minute1", 10)
            # 9분 전 하한가
            #l_b10 = int(df.iloc[0]['low'])  # 9분 전 하한가
            
            l_b9 = float(df.iloc[-9]['low'])
            l_b8 = float(df.iloc[-8]['low'])
            l_b7 = float(df.iloc[-7]['low'])
            l_b6 = float(df.iloc[-6]['low'])
            l_b5 = float(df.iloc[-5]['low'])
            l_b4 = float(df.iloc[-4]['low'])
            l_b3 = float(df.iloc[-3]['low'])
            l_b2 = float(df.iloc[-2]['low'])
            l_b1 = float(df.iloc[-1]['low'])  # 현 하한가
            h_b4 = float(df.iloc[-4]['high'])
            h_b3 = float(df.iloc[-3]['high'])
            h_b2 = float(df.iloc[-2]['high'])
            h_b1 = float(df.iloc[-1]['high']) # 현 상한가
            c_now = float(df.iloc[-1]['close'])  # 현 종가

            
            #l_avg = ((l_b2 + l_b3 + l_b4 + l_b5 + l_b6 + l_b7 + l_b8 + l_b9) / 8) * 0.9999

            diff_now = c_now - l_b3 # 2분전 하한가 가격 쫒아가기
            #diff_now = c_now - l_avg
            print(diff_now, target_item, "Diff.")

            time.sleep(0.1) # 타임슬립을 넣어주지 않으면 조회수가 너무 많아 팅김. 0.1 이상일 것.


            # 트레일링 스탑 매도
            if diff_now < -1:
                # 기준 충족 종목 RSI 데이터
                logging.info(c_now)

                # 시장가 매도
                logging.info('시장가 매도 시작!')
                upbit.sellcoin_mp(target_item) # , 'Y') <- positional argument 1개여야 하는데 2개 주어짐. 원문에는 두개로 주어짐. upbit에 한개만 씀.

                # 매도 시간 처리 고려 5분봉 기준 더블매수 방지목적 3분 stop
                time.sleep(3)

                # 다시 매수 로직 시작
                start_buytrade(buy_amt)

            '''
            # RSI 값이 기준값을 초과하면 매도
            if Decimal(rsi_data[0][0]['RSI']) > Decimal(rsi_sell_value):
                # 기준 충족 종목 RSI 데이터
                logging.info(rsi_data)

                # 시장가 매도
                logging.info('시장가 매도 시작!')
                upbit.sellcoin_mp(target_item, 'Y')

                # 매도 시간 처리 고려
                time.sleep(3)

                # 다시 매수 로직 시작
                start_buytrade(rsi_buy_value, rsi_sell_value, buy_amt)
                
            '''

    # ----------------------------------------
    # 모든 함수의 공통 부분(Exception 처리)
    # ----------------------------------------
    except Exception:
        raise


# -----------------------------------------------------------------------------
# - Name : start_buytrade
# - Desc : 매수 로직
# - Input
# 1) minute_interval : 몇 분봉을 볼지 정함
# 2) minute_check : 몇개의 봉을 기준삼을지 정함
# 3) buy_amt : 매수금액
# -----------------------------------------------------------------------------
def start_buytrade(buy_amt): #(minute_interval, minute_check, buy_amt):
    try:

        data_cnt = 0

        # 매수 될 때까지 반복 수행
        while True:

            # 전체 종목 추출
            # 1. KRW마켓
            # 2. BTC, ETH 제외
            item_list = upbit.get_items('KRW', '') #'BTC,ETH')

            # 전체 종목 반복
            for item_list_for in item_list:

                # 해당 종목의 RSI 지표 산출
                # 1. 10분봉 기준 RSI 지표 산출
                #rsi_data = upbit.get_indicators(item_list_for['market'], '10', 200, 5)

                # 1분봉 (최대 200개 요청가능) - 10개 요청(9분전부터)
                df = pyupbit.get_ohlcv(item_list_for['market'], "minute1", 10)
                #df = pyupbit.get_ohlcv(item_list_for['market'], "minute%d"%(minute_interval), minute_check)
                #print(df)
                timenow = datetime.datetime.now()
                #print(timenow)
                #print(item_list_for['market'])
                '''
                # 아래 코드 축약
                m_check = 0
                i_check = minute_check
                while m_check < minute_check:
                    i_check = 10
                '''

                #v_b10 = int(df.iloc[0]['volume']) #9분 전 거래량
                
                v_b9 = float(df.iloc[-9]['volume'])
                v_b8 = float(df.iloc[-8]['volume'])
                v_b7 = float(df.iloc[-7]['volume'])
                v_b6 = float(df.iloc[-6]['volume'])
                
                v_b5 = float(df.iloc[-5]['volume'])
                v_b4 = float(df.iloc[-4]['volume'])
                v_b3 = float(df.iloc[-3]['volume'])
                v_b2 = float(df.iloc[-2]['volume'])
                v_now = float(df.iloc[-1]['volume']) #현 거래량

                #print(v_b9)

                
                l_b9 = float(df.iloc[-9]['low'])
                l_b8 = float(df.iloc[-8]['low'])
                l_b7 = float(df.iloc[-7]['low'])
                l_b6 = float(df.iloc[-6]['low'])
                
                l_b5 = float(df.iloc[-5]['low'])
                l_b4 = float(df.iloc[-4]['low'])
                l_b3 = float(df.iloc[-3]['low'])
                l_b2 = float(df.iloc[-2]['low'])
                l_b1 = float(df.iloc[-1]['low'])  # 현 하한가
                h_b4 = float(df.iloc[-4]['high'])
                h_b3 = float(df.iloc[-3]['high'])
                h_b2 = float(df.iloc[-2]['high'])
                h_b1 = float(df.iloc[-1]['high']) # 현 상한가
                c_now = float(df.iloc[-1]['close'])  # 현 종가

                hl_b4 = (h_b4 - l_b4)
                hl_b3 = (h_b3 - l_b3)
                hl_b2 = (h_b2 - l_b2)
                gap_ck = hl_b2 + hl_b3# + hl_b4
                diff_now = h_b1 - h_b2 - gap_ck

                #10분 동안 거래량 기준 계산 기준 보정
                v_sum = v_b2 + v_b3 + v_b4# + v_b5# + v_b6 + v_b7 + v_b8 + v_b9# + v_b10
                #v_sum *= 1.5
                #l_b2 *= 1.0001
                buy_price = l_b1

                if diff_now > 1 and v_now > v_sum:
                    ok_no = "OK"
                else:
                    ok_no = "NO"

                #mc_check = (c_now - l_b2)*100 / l_b2
                mv_check = (v_now - v_sum)*100 / v_sum
                print(ok_no,"  ★ 가격차 " + str(int(diff_now)) +" 원", item_list_for['market'], "종가 " + str(int(c_now)) + "원", "■ 거래량 " + str(int(mv_check)) + "%")
                #print("  ★   가격 "+str(int(mc_check))+"%",item_list_for['market'], "■  거래량 "+str(int(mv_check))+"%")


                # 100시간 전 거래량
                #firstVolume = int(df.iloc[0]['volume'])
                # 현재 종가
                #curClose = int(df.iloc[-1]['close'])
                # 현재 거래량
                #curVolume = int(df.iloc[-1]['volume'])
                time.sleep(0.1)

                # 10분 전체 거래량 합보다 현 거래량이 클 경우 매수
                if diff_now > 1 and c_now > hl_b2 +1 and c_now > hl_b3 +1 and c_now > hl_b4 +1 and hl_b2 != 0 and hl_b3 != 0 and hl_b4 != 0 and v_now > v_sum :
                #if v_now > v_sum and c_now > l_b2 and c_now > l_b3 and c_now > l_b4 and c_now > l_b5 and c_now > l_b6 and c_now > l_b7 and c_now > l_b8 and c_now > l_b9:
                    # 기준 충족 종목 로깅
                    logging.info(item_list_for)

                    # 기준 충족 종목 종가
                    logging.info('종가' + str(c_now))

                    # 시장가 매수
                    #logging.info('시장가 매수 시작!')
                    #upbit.buycoin_mp(item_list_for['market'], buy_amt)

                    # 지정가 매수
                    logging.info('지정가 매수 시작!')
                    upbit.buycoin_tg(item_list_for['market'], buy_amt, buy_price)

                    # 매수 시간 처리 고려(3초정도)
                    time.sleep(1)

                    # 매도 로직 호출
                    start_selltrade(item_list_for['market'], buy_amt) #(item_list_for['market'], minute_interval, minute_check, buy_amt)


                '''
                # RSI 값이 기준값 미만으로 떨어지면 매수
                if Decimal(rsi_data[0][0]['RSI']) < Decimal(rsi_buy_value):
                    # 기준 충족 종목 로깅
                    logging.info(item_list_for)

                    # 기준 충족 종목 RSI 데이터
                    logging.info(rsi_data)

                    # 시장가 매수
                    logging.info('시장가 매수 시작!')
                    upbit.buycoin_mp(item_list_for['market'], buy_amt)

                    # 매수 시간 처리 고려
                    time.sleep(3)

                    # 매도 로직 호출
                    start_selltrade(item_list_for['market'], rsi_sell_value, rsi_buy_value, buy_amt)
                '''



                if data_cnt == 0 or data_cnt % 100 == 0:
                    logging.info("매수 로직 가동중...[" + str(data_cnt) + "]")

                # 조회건수증가
                data_cnt = data_cnt + 1

    # ----------------------------------------
    # 모든 함수의 공통 부분(Exception 처리)
    # ----------------------------------------
    except Exception:
        raise






# -----------------------------------------------------------------------------
# - Name : main
# - Desc : 메인
# -----------------------------------------------------------------------------
if __name__ == '__main__':

    # noinspection PyBroadException
    try:

        print("***** USAGE ******")
        print("[1] 로그레벨(D:DEBUG, E:ERROR, 그외:INFO)")

        # 로그레벨(D:DEBUG, E:ERROR, 그외:INFO)
        upbit.set_loglevel('I')

        # ---------------------------------------------------------------------
        # Logic Start!
        # ---------------------------------------------------------------------
        #minute_interval = input("몇 분봉으로 볼까요?(ex. 1,3,5,10,15,30,60,240분봉) 기준 5:")
        #minute_check = input("몇개 봉을 기준 삼을까요?(ex. 최대 200) 기준 10:")
        #buy_amt = input("매수금액(ex:최소 5000 이상):")
        buy_amt = 6000

        #logging.info("매수 기준 RSI 값:" + str(minute_interval))
        #logging.info("매도 기준 RSI 값:" + str(minute_check))
        logging.info("매수금액:" + str(buy_amt))

        # 매수로직 시작
        #start_buytrade(minute_interval, minute_check, buy_amt)
        start_buytrade(buy_amt)

    except KeyboardInterrupt:
        logging.error("KeyboardInterrupt Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(-100)

    except Exception:
        logging.error("Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(-200)