# 신규 old_1.7.py -> old_4.py -> 4.py
import logging
import sys
import os
import time
from datetime import datetime, timedelta
import traceback
import pandas as pd
import numpy
import dateutil.parser

from decimal import Decimal

# 공통 모듈 Import
sys.path.append(os.path.dirname(os.path.dirname(__file__)))
from lib import upbit as upbit  # noqa


# -----------------------------------------------------------------------------
# - Name : start_selltrade
# - Desc : 매도 로직
# - Input
# 1) sell_pcnt : 매도 수익률
# 2) dcnt_pcnt : 고점대비 하락률
# -----------------------------------------------------------------------------
def start_selltrade(sell_pcnt, dcnt_pcnt):
    try:

        # ----------------------------------------------------------------------
        # 반복 수행
        # ----------------------------------------------------------------------
        while True:

            # ------------------------------------------------------------------
            # 보유 종목조회
            # ------------------------------------------------------------------
            target_items = upbit.get_accounts('Y', 'KRW')

            # ------------------------------------------------------------------
            # 보유 종목 현재가 조회
            # ------------------------------------------------------------------
            target_items_comma = upbit.chg_account_to_comma(target_items)
            tickers = upbit.get_ticker(target_items_comma)

            # -----------------------------------------------------------------
            # 보유 종목별 진행
            # -----------------------------------------------------------------
            for target_item in target_items:
                for ticker in tickers:
                    time.sleep(0.05)
                    if target_item['market'] == ticker['market']:

                        # -------------------------------------------------
                        # 고점을 계산하기 위해 최근 매수일시 조회
                        # 1. 해당 종목에 대한 거래 조회(done, cancel)
                        # 2. 거래일시를 최근순으로 정렬
                        # 3. 매수 거래만 필터링
                        # 4. 가장 최근 거래일자부터 현재까지 고점을 조회
                        # -------------------------------------------------
                        order_done = upbit.get_order_status(target_item['market'], 'done') + upbit.get_order_status(
                            target_item['market'], 'cancel')
                        order_done_sorted = upbit.orderby_dict(order_done, 'created_at', True)
                        order_done_filtered = upbit.filter_dict(order_done_sorted, 'side', 'bid')

                        # -------------------------------------------------
                        # 매수 직후 나타나는 오류 체크용 마지막 매수 시간 차이 계산
                        # -------------------------------------------------
                        # 마지막 매수 시간
                        last_buy_dt = datetime.strptime(
                            dateutil.parser.parse(order_done_filtered[0]['created_at']).strftime('%Y-%m-%d %H:%M:%S'),
                            '%Y-%m-%d %H:%M:%S')

                        # 현재 시간 추출
                        current_dt = datetime.strptime(datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
                                                       '%Y-%m-%d %H:%M:%S')

                        # 시간 차이 추출
                        diff = current_dt - last_buy_dt

                        # 매수 후 1분간은 진행하지 않음(업비트 오류 방지 용)
                        if diff.seconds < 60:
                            logging.info('- 매수 직후 발생하는 오류를 방지하기 위해 진행하지 않음!!!')
                            logging.info('------------------------------------------------------')
                            continue

                        # -----------------------------------------------------
                        # 수익률 계산
                        # ((현재가 - 평균매수가) / 평균매수가) * 100
                        # -----------------------------------------------------
                        rev_pcnt = round(((Decimal(str(ticker['trade_price'])) - Decimal(
                            str(target_item['avg_buy_price']))) / Decimal(str(target_item['avg_buy_price']))) * 100, 2)

                        logging.info('')
                        logging.info('------------------------------------------------------')
                        logging.info('- 종목:' + str(target_item['market']))
                        logging.info('- 평균매수가:' + str(target_item['avg_buy_price']))
                        logging.info('- 현재가:' + str(ticker['trade_price']))
                        logging.info('- 수익률:' + str(rev_pcnt))

                        # -----------------------------------------------------
                        # 현재 수익률이 매도 수익률 이상인 경우에만 진행
                        # -----------------------------------------------------
                        if Decimal(str(rev_pcnt)) < Decimal(str(sell_pcnt)):
                            logging.info('- 현재 수익률이 매도 수익률 보다 낮아 진행하지 않음!!!')
                            logging.info('------------------------------------------------------')
                            continue

                        # ------------------------------------------------------------------
                        # 캔들 조회
                        # ------------------------------------------------------------------
                        candles = upbit.get_candle(target_item['market'], '60', 200)

                        # ------------------------------------------------------------------
                        # 최근 매수일자 다음날부터 현재까지의 최고가를 계산
                        # ------------------------------------------------------------------
                        df = pd.DataFrame(candles)
                        mask = df['candle_date_time_kst'] > order_done_filtered[0]['created_at']
                        filtered_df = df.loc[mask]
                        highest_high_price = numpy.max(filtered_df['high_price'])

                        # -----------------------------------------------------
                        # 고점대비 하락률
                        # ((현재가 - 최고가) / 최고가) * 100
                        # -----------------------------------------------------
                        cur_dcnt_pcnt = round(((Decimal(str(ticker['trade_price'])) - Decimal(
                            str(highest_high_price))) / Decimal(str(highest_high_price))) * 100, 2)

                        logging.info('- 매수 후 최고가:' + str(highest_high_price))
                        logging.info('- 고점대비 하락률:' + str(cur_dcnt_pcnt))
                        logging.info('- 최종 매수시간:' + str(last_buy_dt))

                        if Decimal(str(cur_dcnt_pcnt)) < Decimal(str(dcnt_pcnt)):

                            # ------------------------------------------------------------------
                            # 시장가 매도
                            # 실제 매도 로직은 안전을 위해 주석처리 하였습니다.
                            # 실제 매매를 원하시면 테스트를 충분히 거친 후 주석을 해제하시면 됩니다.
                            # ------------------------------------------------------------------
                            logging.info('시장가 매도 시작! [' + str(target_item['market']) + ']')
                            rtn_sellcoin_mp = upbit.sellcoin_mp(target_item['market'], 'Y')
                            logging.info('시장가 매도 종료! [' + str(target_item['market']) + ']')
                            logging.info(rtn_sellcoin_mp)
                            logging.info('------------------------------------------------------')

                        else:
                            logging.info('- 고점 대비 하락률 조건에 맞지 않아 매도하지 않음!!!')
                            logging.info('------------------------------------------------------')


    # ---------------------------------------
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
                # df_candle = upbit.get_candle(item_list_for['market'], '3', 6)
                # print(df_candle)
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

                # can_tradeNow = df_candle[0]['trade_price']
                # can_highNow = df_candle[0]['high_price']
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

                # can_gapNow = can_highNow - can_lowNow
                can_gapBefore1 = can_highBefore1 - can_lowBefore1
                can_gapBefore2 = can_highBefore2 - can_lowBefore2
                can_gapBefore3 = can_highBefore3 - can_lowBefore3
                can_gapBefore4 = can_highBefore4 - can_lowBefore4
                can_gapBefore5 = can_highBefore5 - can_lowBefore5

                # can_eval = can_gapNow - (can_gapBefore1 + can_gapBefore2)# + can_gapBefore3 + can_gapBefore4 + can_gapBefore5) * 1
                # vol_eval = vol_tradeNow - (vol_before1 + vol_before2 + vol_before3 + vol_before4 + vol_before5) * 0.5

                # 볼린저밴드 15분봉
                # df_bb = upbit.get_bb(item_list_for['market'], '10', '200', 11) #15분봉으로 테스트

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

                print("BBL", format((bb_now - can_lowNow) / bb_now * 100, '.2f'), "%", item_list_for['market'], "| RSI",
                      format(rsi_now, '.4f'), "%  +TRY | except:", except_items)

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
                    print('It was close')
                    continue

                if bb_now > can_lowNow and rsi_before1 <= 30 and rsi_now > rsi_before1:  # and bb_gapBefore0 > 0 and bb_eval1 > 0  and bb_eval2 > 0 and bb_eval3 > 0and can_highBefore1 != can_highBefore2 and can_highBefore1 != can_highBefore3 and can_gapBefore1 != 0 and can_gapBefore2 != 0:# and bb_eval2 > 0 and bb_eval3 > 0 and bb_eval4 > 0 and bb_eval5 > 0  and bb_eval6 > 0 and bb_eval7 > 0 and bb_eval8 > 0 and can_lowNow < can_lowBefore1 and can_lowNow < can_lowBefore2 and can_lowNow < can_lowBefore3 and can_gapBefore1 != 0 and can_gapBefore2 != 0 and can_gapBefore3 != 0 and can_gapBefore4 != 0 and can_gapBefore5 != 0 and can_highBefore1 != can_highBefore2 and can_highBefore1 != can_highBefore3 and can_highBefore1 != can_highBefore4 and can_highBefore1 != can_highBefore5:

                    # 기준 충족 종목 종가
                    # print(item_list_for['market'],'하한가' + str(can_lowNow))

                    # 지정가 매수
                    print('target start!')
                    upbit.buycoin_tg(item_list_for['market'], buy_amt, can_lowNow)

                    time.sleep(30)

                    # 시장가 매수
                    # print('시장가 매수 시작!')
                    # upbit.buycoin_mp(item_list_for['market'],buy_amt)
                    # 매도표현식 참조
                    # rtn_sellcoin_mp = upbit.sellcoin_mp(target_item['market'], 'Y')

                    # ------------------------------------------------------------------
                    # 매수 완료 종목은 매수 대상에서 제외
                    # ------------------------------------------------------------------
                    except_items = except_items + ',' + item_list_for['market'].split('-')[1]

                    try:
                        # ---------------------------------------------------------------------
                        # Logic Start!
                        # ---------------------------------------------------------------------
                        # 미체결 주문 조회
                        locked_trade = upbit.get_order(item_list_for['market'])
                        secWaiting = 30
                        logging.info("취소전 대기 중... " + str(secWaiting) + "초")
                        logging.info(locked_trade)
                        time.sleep(secWaiting)

                        upbit.cancel_order(except_items_for, 'BUY')
                        logging.info("주문취소 진행!!")
                        continue

                    except:
                        continue

                # if bb_now >= can_lowNow and vol_eval >= 0 and bb_eval1 >= 0:
                # print("TRIED !!")

                if data_cnt == 0 or data_cnt % 100 == 0:
                    # print("gathering...[" + str(data_cnt) + "]")
                    continue

                now = datetime.now().strftime('%H%M')

                if due_time == now:
                    except_items = ''
                    time.sleep(1)
                    due_time = (datetime.now() + timedelta(hours=3)).strftime('%H%M')
                    continue

                # 조회건수증가
                data_cnt = data_cnt + 1
                # 타임슬립
                time.sleep(0.02)

                continue

            print('change to sell')
            start_selltrade(sell_pcnt, dcnt_pcnt)
            continue

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
    print("***** USAGE ******")
    print("[1] 로그레벨(D:DEBUG, E:ERROR, 그외:INFO)")

    # 로그레벨(D:DEBUG, E:ERROR, 그외:INFO)
    upbit.set_loglevel('I')

    buy_amt = 6000
    print("buy:" + str(buy_amt))

    sell_pcnt = -1  # input("매도 수익률(ex:2%=2) : ")
    dcnt_pcnt = -1  # input("고점대비 하락률(ex:-1%=-1) : ")
    print("target: " + str(sell_pcnt) + " | gap: " + str(dcnt_pcnt))

    except_items = ''
    start_buytrade(buy_amt, except_items)

    '''
    p1 = Process(target=start_buytrade, args=(buy_amt, except_items))
    p2 = Process(target=start_selltrade, args=(sell_pcnt, dcnt_pcnt))
    p1.start()
    p2.start()
    '''

    '''
    pool = Pool(processes = 4)
    pool.map(start_buytrade, buy_amt, except_items)
    pool.close()
    pool.join()
    '''

    # 매수로직 시작
    '''
    try:
        except_items = ''
        while True:
            start_buytrade(buy_amt, except_items)



    except Exception:

        raise
    '''