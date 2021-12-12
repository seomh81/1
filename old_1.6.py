# 캔들 & 볼밴 15분봉 매수 6000(지정가)

import sys
import os
import time
from datetime import datetime, timedelta

# 공통 모듈 Import
sys.path.append(os.path.dirname(os.path.dirname(__file__)))
from lib import upbit as upbit  # noqa




# -----------------------------------------------------------------------------
# - Name : start_buytrade
# - Desc : 매수 로직
# - Input
# 1) minute_interval : 몇 분봉을 볼지 정함
# 2) minute_check : 몇개의 봉을 기준삼을지 정함
# 3) buy_amt : 매수금액
# -----------------------------------------------------------------------------
def start_buytrade(buy_amt, except_items):
    try:
        data_cnt = 0

        # 55분 마다 리스트 초기화 (변수 변경 시 아래도 변경해야 함)
        due_time = (datetime.now() + timedelta(hours=3)).strftime('%H%M')

        # 매수 될 때까지 반복 수행
        while True:

            # 전체 종목 추출
            # 1. KRW마켓  ``
            # 2. 제외할 ticker
            item_list = upbit.get_items('KRW', except_items)

            # 전체 종목 반복
            for item_list_for in item_list:


                # 1분봉 (최대 200개 요청가능) - 6개 요청(5분전부터)
                #df_candle = upbit.get_candle(item_list_for['market'], '3', 6)
                #print(df_candle)
                # 호출 수 줄이기
                indicators = upbit.get_indicator_sel(item_list_for['market'], '15', 200, 11, ['RSI', 'BB', 'CANDLE'])

                df_bb = indicators['BB']
                df_candle = indicators['CANDLE']
                df_rsi = indicators['RSI']

                rsi_now = df_rsi[0]['RSI']
                rsi_before1 = df_rsi[1]['RSI']

                '''
                vol_tradeNow = df_candle[0]['candle_acc_trade_volume']
                vol_before1 = df_candle[1]['candle_acc_trade_volume']
                vol_before2 = df_candle[2]['candle_acc_trade_volume']
                vol_before3 = df_candle[3]['candle_acc_trade_volume']
                vol_before4 = df_candle[4]['candle_acc_trade_volume']
                vol_before5 = df_candle[5]['candle_acc_trade_volume']
                '''

                #can_tradeNow = df_candle[0]['trade_price']
                #can_highNow = df_candle[0]['high_price']
                can_highBefore1 = df_candle[1]['high_price']
                can_highBefore2 = df_candle[2]['high_price']
                can_highBefore3 = df_candle[3]['high_price']
                can_highBefore4 = df_candle[4]['high_price']
                can_highBefore5 = df_candle[5]['high_price']
                can_highNow = df_candle[0]['high_price']
                can_lowNow = df_candle[0]['low_price']
                can_lowBefore1 = df_candle[1]['low_price']
                can_lowBefore2 = df_candle[2]['low_price']
                can_lowBefore3 = df_candle[3]['low_price']
                can_lowBefore4 = df_candle[4]['low_price']
                can_lowBefore5 = df_candle[5]['low_price']

                can_openNow = df_candle[0]['opening_price']
                can_tradeNow = df_candle[0]['trade_price']
                can_openBefore1 = df_candle[1]['opening_price']
                can_tradeBefore1 = df_candle[1]['trade_price']

                #can_gapNow = can_highNow - can_lowNow
                can_gapBefore1 = can_highBefore1 - can_lowBefore1
                can_gapBefore2 = can_highBefore2 - can_lowBefore2
                can_gapBefore3 = can_highBefore3 - can_lowBefore3
                can_gapBefore4 = can_highBefore4 - can_lowBefore4
                can_gapBefore5 = can_highBefore5 - can_lowBefore5

                #can_eval = can_gapNow - (can_gapBefore1 + can_gapBefore2)# + can_gapBefore3 + can_gapBefore4 + can_gapBefore5) * 1
                #vol_eval = vol_tradeNow - (vol_before1 + vol_before2 + vol_before3 + vol_before4 + vol_before5) * 0.5

                # 볼린저밴드 15분봉
                #df_bb = upbit.get_bb(item_list_for['market'], '10', '200', 11) #15분봉으로 테스트

                bb_nowBBM = df_bb[0]['BBM']
                bb_now = df_bb[0]['BBL']
                bb_Before1 = df_bb[1]['BBL']
                bb_Before2 = df_bb[2]['BBL']
                bb_Before3 = df_bb[3]['BBL']
                bb_Before4 = df_bb[4]['BBL']
                bb_Before5 = df_bb[5]['BBL']
                bb_Before6 = df_bb[6]['BBL']
                bb_Before7 = df_bb[7]['BBL']
                bb_Before8 = df_bb[8]['BBL']
                bb_Before9 = df_bb[9]['BBL']
                bb_Before10 = df_bb[10]['BBL']
                bb_gapBefore0 = bb_now - bb_Before1
                bb_gapBefore1 = bb_Before1 - bb_Before2
                bb_gapBefore2 = (bb_Before1 - bb_Before3) / 2
                bb_gapBefore3 = (bb_Before1 - bb_Before4) / 3
                bb_gapBefore4 = (bb_Before1 - bb_Before5) / 4
                bb_gapBefore5 = (bb_Before1 - bb_Before6) / 5
                bb_gapBefore6 = (bb_Before1 - bb_Before7) / 6
                bb_gapBefore7 = (bb_Before1 - bb_Before8) / 7
                bb_gapBefore8 = (bb_Before1 - bb_Before9) / 8
                bb_gapBefore9 = (bb_Before1 - bb_Before10) / 9
                bb_eval1 = bb_gapBefore1 - bb_gapBefore2
                bb_eval2 = bb_gapBefore1 - bb_gapBefore3
                bb_eval3 = bb_gapBefore1 - bb_gapBefore4
                bb_eval4 = bb_gapBefore1 - bb_gapBefore5
                bb_eval5 = bb_gapBefore1 - bb_gapBefore6
                bb_eval6 = bb_gapBefore1 - bb_gapBefore7
                bb_eval7 = bb_gapBefore1 - bb_gapBefore8
                bb_eval8 = bb_gapBefore1 - bb_gapBefore9

                print("BBL", format((bb_now - can_lowNow) / bb_now * 100, '.2f'),"%",item_list_for['market'], "  BB trend", format(bb_eval1, '.4f') , "%+++TRY / except:", except_items)


                '''
                # 급등 시 매수 1.5%
                if (can_tradeNow - can_openNow) / can_openNow * 100 > 1.5 and can_lowNow < bb_nowBBM and can_highNow > can_highBefore1 and can_highNow > can_highBefore2 and can_highNow > can_highBefore3 and can_highNow > can_highBefore4 and can_highNow > can_highBefore5:
                    upbit.buycoin_mp(item_list_for['market'], buy_amt)
                    except_items = except_items + ',' + item_list_for['market'].split('-')[1]


                # 급락 시 매수 -3%
                if (can_tradeBefore1 - can_openBefore1) / can_openBefore1 * 100 < -3 and can_highNow > bb_nowBBM and can_highBefore1 < can_highNow:
                    upbit.buycoin_tg(item_list_for['market'], buy_amt, can_lowNow)
                    except_items = except_items + ',' + item_list_for['market'].split('-')[1]
                '''

                # 볼린저밴드 15분봉 하단을 찍을 때 매수
                if bb_now > can_lowNow and rsi_now <= 30:
                    print('It is closed')
                    continue
                if bb_now > can_lowNow and rsi_now > 30 and rsi_before1 <=30:# and bb_gapBefore0 > 0 and bb_eval1 > 0  and bb_eval2 > 0 and bb_eval3 > 0and can_highBefore1 != can_highBefore2 and can_highBefore1 != can_highBefore3 and can_gapBefore1 != 0 and can_gapBefore2 != 0:# and bb_eval2 > 0 and bb_eval3 > 0 and bb_eval4 > 0 and bb_eval5 > 0  and bb_eval6 > 0 and bb_eval7 > 0 and bb_eval8 > 0 and can_lowNow < can_lowBefore1 and can_lowNow < can_lowBefore2 and can_lowNow < can_lowBefore3 and can_gapBefore1 != 0 and can_gapBefore2 != 0 and can_gapBefore3 != 0 and can_gapBefore4 != 0 and can_gapBefore5 != 0 and can_highBefore1 != can_highBefore2 and can_highBefore1 != can_highBefore3 and can_highBefore1 != can_highBefore4 and can_highBefore1 != can_highBefore5:

                    # 기준 충족 종목 종가
                    #print(item_list_for['market'],'하한가' + str(can_lowNow))

                    # 지정가 매수
                    print('지정가 매수 시작!')
                    upbit.buycoin_tg(item_list_for['market'],buy_amt, can_lowNow)

                    # 시장가 매수
                    #print('시장가 매수 시작!')
                    #upbit.buycoin_mp(item_list_for['market'],buy_amt)
                    #매도표현식 참조
                    #rtn_sellcoin_mp = upbit.sellcoin_mp(target_item['market'], 'Y')

                    # ------------------------------------------------------------------
                    # 매수 완료 종목은 매수 대상에서 제외
                    # ------------------------------------------------------------------
                    except_items = except_items + ',' + item_list_for['market'].split('-')[1]

                    time.sleep(5)
                    continue




                #if bb_now >= can_lowNow and vol_eval >= 0 and bb_eval1 >= 0:
                    #print("TRIED !!")

                if data_cnt == 0 or data_cnt % 100 == 0:
                    print("gathering...[" + str(data_cnt) + "]")
                    continue



                now = datetime.now().strftime('%H%M')

                if due_time == now:
                    except_items = ''
                    due_time = (datetime.now() + timedelta(hours=3)).strftime('%H%M')
                    continue

                # 조회건수증가
                data_cnt = data_cnt + 1
                # 타임슬립
                time.sleep(0.06)

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


    buy_amt = 6000
    print("buy:" + str(buy_amt))

    # 매수로직 시작
    try:
        except_items = ''
        while True:
            start_buytrade(buy_amt, except_items)


    except Exception:

        raise