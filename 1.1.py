# 볼린저밴트 5분봉 하단 점을 찍고 올라가면 매수 모니터링은 1분봉기준
# 저가 매수 로직만 세팅

import sys
import os
import time
import pandas as pd
import numpy
from decimal import Decimal
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
def start_buytrade(buy_amt, except_items, sell_pcnt, dcnt_pcnt):
    try:
        data_cnt = 0

        # 55분 마다 리스트 초기화 (변수 변경 시 아래도 변경해야 함)
        due_time = (datetime.now() + timedelta(hours=2)).strftime('%H')

        # 매수 될 때까지 반복 수행
        while True:

            # 전체 종목 추출
            # 1. KRW마켓  ``
            # 2. 제외할 ticker
            item_list = upbit.get_items('KRW', except_items)

            # ------------------------------------------------------------------
            # 보유 종목조회
            # ------------------------------------------------------------------
            target_items = upbit.get_accounts('Y', 'KRW')
            # ------------------------------------------------------------------
            # 보유 종목 현재가 조회
            # ------------------------------------------------------------------
            target_items_comma = upbit.chg_account_to_comma(target_items)
            tickers = upbit.get_ticker(target_items_comma)

            for target_item in target_items:
                for ticker in tickers:
                    #time.sleep(0.01)
                    if target_item['market'] == ticker['market']:

                        # -----------------------------------------------------
                        # 수익률 계산
                        # ((현재가 - 평균매수가) / 평균매수가) * 100
                        # -----------------------------------------------------
                        rev_pcnt = round(((Decimal(str(ticker['trade_price'])) - Decimal(
                            str(target_item['avg_buy_price']))) / Decimal(str(target_item['avg_buy_price']))) * 100, 2)

                        print('- 종목:' + str(target_item['market']))
                        print('- 평균매수가:' + str(target_item['avg_buy_price']))
                        print('- 현재가:' + str(ticker['trade_price']))
                        print('- 수익률:' + str(rev_pcnt))

                        # -----------------------------------------------------
                        # 현재 수익률이 매도 수익률 이상인 경우에만 진행
                        # -----------------------------------------------------
                        if Decimal(str(rev_pcnt)) < Decimal(str(sell_pcnt)):
                            print('- 현재 수익률이 매도 수익률 보다 낮아 진행하지 않음!!!')
                            continue

                        # -------------------------------------------------
                        # 고점을 계산하기 위해 최근 매수일자 조회
                        # 1. 해당 종목에 대한 거래 조회(done, cancel)
                        # 2. 거래일시를 최근순으로 정렬
                        # 3. 매수 거래만 필터링
                        # 4. 가장 최근 거래일자부터 현재까지 고점을 조회
                        # -------------------------------------------------
                        order_done = upbit.get_order_status(target_item['market'], 'done') + upbit.get_order_status(
                            target_item['market'], 'cancel')
                        order_done_sorted = upbit.orderby_dict(order_done, 'created_at', True)
                        order_done_filtered = upbit.filter_dict(order_done_sorted, 'side', 'bid')

                        # ------------------------------------------------------------------
                        # 캔들 조회
                        # ------------------------------------------------------------------
                        candles = upbit.get_candle(target_item['market'], '15', 20)

                        # ------------------------------------------------------------------
                        # 최근 매수일자 다음날부터 현재까지의 최고가를 계산
                        # ------------------------------------------------------------------
                        df = pd.DataFrame(candles)
                        mask = df['candle_date_time_kst'] > order_done_filtered[0]['created_at']
                        filtered_df = df.loc[mask]

                        higest_high_price = numpy.max(filtered_df['high_price'])

                        # -----------------------------------------------------
                        # 고점대비 하락률
                        # ((현재가 - 최고가) / 최고가) * 100
                        # -----------------------------------------------------
                        cur_dcnt_pcnt = round(((Decimal(str(ticker['trade_price'])) - Decimal(
                            str(higest_high_price))) / Decimal(str(higest_high_price))) * 100, 2)

                        print('- 매수 후 최고가:' + str(higest_high_price))
                        print('- 고점대비 하락률:' + str(cur_dcnt_pcnt))

                        if Decimal(str(cur_dcnt_pcnt)) < Decimal(str(dcnt_pcnt)):

                            # ------------------------------------------------------------------
                            # 시장가 매도
                            # 실제 매도 로직은 안전을 위해 주석처리 하였습니다.
                            # 실제 매매를 원하시면 테스트를 충분히 거친 후 주석을 해제하시면 됩니다.
                            # ------------------------------------------------------------------
                            print('시장가 매도 시작! [' + str(target_item['market']) + ']')
                            rtn_sellcoin_mp = upbit.sellcoin_mp(target_item['market'], 'Y')
                            print('시장가 매도 종료! [' + str(target_item['market']) + ']')
                            print(rtn_sellcoin_mp)

                        else:
                            print('- 고점 대비 하락률 조건에 맞지 않아 매도하지 않음!!!')
                            print('------------------------------------------------------')
                        # 전체 종목 반복
                    for item_list_for in item_list:


                        # 1분봉 (최대 200개 요청가능) - 6개 요청(5분전부터)
                        df_candle = upbit.get_candle(item_list_for['market'], '1', 6)

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
                        can_lowNow = df_candle[0]['low_price']
                        can_lowBefore1 = df_candle[1]['low_price']
                        can_lowBefore2 = df_candle[2]['low_price']
                        can_lowBefore3 = df_candle[3]['low_price']
                        can_lowBefore4 = df_candle[4]['low_price']
                        can_lowBefore5 = df_candle[5]['low_price']

                        #can_gapNow = can_highNow - can_lowNow
                        can_gapBefore1 = can_highBefore1 - can_lowBefore1
                        can_gapBefore2 = can_highBefore2 - can_lowBefore2
                        can_gapBefore3 = can_highBefore3 - can_lowBefore3
                        can_gapBefore4 = can_highBefore4 - can_lowBefore4
                        can_gapBefore5 = can_highBefore5 - can_lowBefore5

                        #can_eval = can_gapNow - (can_gapBefore1 + can_gapBefore2)# + can_gapBefore3 + can_gapBefore4 + can_gapBefore5) * 1
                        #vol_eval = vol_tradeNow - (vol_before1 + vol_before2 + vol_before3 + vol_before4 + vol_before5) * 0.5

                        # 볼린저밴드 15분봉
                        df_bb = upbit.get_bb(item_list_for['market'], '15', '200', 11) #15분봉으로 테스트

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

                        print("BBL", format((bb_now - can_lowNow) / bb_now * 100, '.2f'),"%",item_list_for['market'], "  BB추세", format(bb_eval1, '.4f') , "%---양수TRY / 제외종목:", except_items)

                        # 볼린저밴드 15분봉 하단을 찍을 때 매수
                        if bb_now > can_lowNow and bb_eval1 > 0 and bb_eval2 > 0 and bb_eval3 > 0 and bb_eval4 > 0 and bb_eval5 > 0 and bb_eval6 > 0 and bb_eval7 > 0 and bb_eval8 > 0 and can_lowNow < can_lowBefore1 and can_lowNow < can_lowBefore2 and can_lowNow < can_lowBefore3 and can_gapBefore1 != 0 and can_gapBefore2 != 0 and can_gapBefore3 != 0 and can_gapBefore4 != 0 and can_gapBefore5 != 0 and can_highBefore1 != can_highBefore2 and can_highBefore1 != can_highBefore3 and can_highBefore1 != can_highBefore4 and can_highBefore1 != can_highBefore5:

                            # 기준 충족 종목 종가
                            print(item_list_for['market'],'하한가' + str(can_lowNow))

                            # 지정가 매수
                            print('지정가 매수 시작!')
                            upbit.buycoin_tg(item_list_for['market'],buy_amt, can_lowNow)

                            # ------------------------------------------------------------------
                            # 매수 완료 종목은 매수 대상에서 제외
                            # ------------------------------------------------------------------
                            except_items = except_items + ',' + item_list_for['market'].split('-')[1]


                        time.sleep(0.01)

                        #if bb_now >= can_lowNow and vol_eval >= 0 and bb_eval1 >= 0:
                            #print("TRIED !!")

                        if data_cnt == 0 or data_cnt % 100 == 0:
                            print("매수 진행 중...[" + str(data_cnt) + "]")



                        now = datetime.now().strftime('%H')

                        if due_time == now:
                            except_items = ''
                            due_time = (datetime.now() + timedelta(hours=2)).strftime('%H')

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


    buy_amt = 10000
    print("매수금액:" + str(buy_amt))

    log_level = 'I'  # input("로그레벨(D:DEBUG, E:ERROR, 그 외:INFO) : ").upper()
    sell_pcnt = 2  # input("매도 수익률(ex:2%=2) : ")
    dcnt_pcnt = -1  # input("고점대비 하락률(ex:-1%=-1) : ")

    # 매수로직 시작
    try:
        except_items = ''
        while True:
            start_buytrade(buy_amt, except_items, sell_pcnt, dcnt_pcnt)


    except Exception:

        raise