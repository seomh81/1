import os
import sys
import time
import logging
import traceback

# 공통 모듈 Import
import psycopg2

sys.path.append(os.path.dirname(os.path.dirname(__file__)))
from module import upbit


# -----------------------------------------------------------------------------
# - Name : start_find_shoot
# - Desc : 급등주 찾기 로직
# -----------------------------------------------------------------------------
def start_find_shoot():
    try:

        data_cnt = 0

        # DB 연결
        conn = psycopg2.connect(host='132.226.171.188'
                                , dbname='upbit_db'
                                , user='upbit'
                                , password='seo22082208'
                                , port='5432')

        # 커서 획득
        c = conn.cursor()

        sql = "select * from FIND_SHOOT_1MIN where chg_pcnt > 0.49 and trade_cnt > 1499 order by chg_pcnt desc"

        # ----------------------------------------------------------------------
        # 반복 수행
        # ----------------------------------------------------------------------
        while True:
            #print('급등주 찾기!')

            # SQL 수행
            c.execute(sql)

            rows = c.fetchall()



            # 대상조건 발견 시
            if len(rows) > 0:

                # 발견된 로우만큼 처리
                # 급등주 발견 텔레그렘 메세지 발송
                for row in rows:
                    # print(row)

                    message = '[급등주 발견]'
                    message1 = '\n\n종목코드:' + str(row[0])
                    message2 = '\n상승률:' + str(row[9])
                    message3 = '\n거래건수:' + str(row[8])

                    # upbit.send_telegram_message(message)

                    # print('메세지 발송 완료!')
                    print(message)
                    print(message1)
                    print(message2)
                    print(message3)

                    # ------------------------------------------------------------------
                    # 기매수 여부 판단
                    # ------------------------------------------------------------------
                    accounts = upbit.get_accounts('Y', 'KRW')
                    account = list(filter(lambda x: x.get('market') == str(row[0]), accounts))

                    # 이미 매수한 종목이면 다시 매수하지 않음
                    # sell_bot.py에서 매도 처리되면 보유 종목에서 사라지고 다시 매수 가능
                    if len(account) > 0:
                        logging.info('기 매수 종목으로 매수하지 않음....[' + str(row[0]) + ']')
                        continue

                    # ------------------------------------------------------------------
                    # 캔들 1분봉 저가인지 판단
                    # ------------------------------------------------------------------
                    df_candle = upbit.get_candle(str(row[0]), '1', 6)
                    df_candle5 = upbit.get_candle(str(row[0], '5', 6))

                    can_tradeNow = df_candle[0]['trade_price']
                    can_highBefore1 = df_candle[1]['high_price']
                    can_highBefore2 = df_candle[2]['high_price']
                    can_highBefore3 = df_candle[3]['high_price']
                    can_highBefore4 = df_candle[4]['high_price']
                    can_highBefore5 = df_candle[5]['high_price']
                    can_lowBefore1 = df_candle[1]['low_price']
                    can_lowBefore2 = df_candle[2]['low_price']
                    can_lowBefore3 = df_candle[3]['low_price']
                    can_lowBefore4 = df_candle[4]['low_price']
                    can_lowBefore5 = df_candle[5]['low_price']


                    can5_highBefore1 = df_candle5[1]['high_price']
                    can5_highBefore2 = df_candle5[2]['high_price']
                    can5_highBefore3 = df_candle5[3]['high_price']
                    can5_highBefore4 = df_candle5[4]['high_price']
                    can5_highBefore5 = df_candle5[5]['high_price']
                    can5_lowBefore1 = df_candle5[1]['low_price']
                    can5_lowBefore2 = df_candle5[2]['low_price']
                    can5_lowBefore3 = df_candle5[3]['low_price']
                    can5_lowBefore4 = df_candle5[4]['low_price']
                    can5_lowBefore5 = df_candle5[5]['low_price']

                    if (can_tradeNow <= can_highBefore1) or (can_tradeNow <= can_highBefore2) or (can_tradeNow <= can_highBefore3) or (can_tradeNow <= can_highBefore4) or (can_tradeNow <= can_highBefore5) or (can_tradeNow <= can_lowBefore1) or (can_tradeNow <= can_lowBefore2) or (can_tradeNow <= can_lowBefore3) or (can_tradeNow <= can_lowBefore4) or (can_tradeNow <= can_lowBefore5) or (can_tradeNow <= can5_highBefore1) or (can_tradeNow <= can5_highBefore2) or (can_tradeNow <= can5_highBefore3) or (can_tradeNow <= can5_highBefore4) or (can_tradeNow <= can5_highBefore5) or (can_tradeNow <= can5_lowBefore1) or (can_tradeNow <= can5_lowBefore2) or (can_tradeNow <= can5_lowBefore3) or (can_tradeNow <= can5_lowBefore4) or (can_tradeNow <= can5_lowBefore5):
                        logging.info('1분봉 5개에 비해 현재가가 낮으므로 매수하지 않음....[' + str(row[0]) + ']')
                        continue

                    upbit.buycoin_mp(str(row[0]), 9995)
                    print('시장가 매수 완료! [' + str(row[0]) + ']')

                # 중복 메세지 발송하지 않기 위해 60초간 Sleep X
                time.sleep(1)

            # 조회 건수 체크
            data_cnt = data_cnt + 1
            #print('급등주 찾기!')

            if data_cnt % 1000 == 0:
                print("Checking...[" + str(data_cnt) + "][" + str(len(rows)) + "]")

    # ---------------------------------------
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
        log_level = 'I'
        upbit.set_loglevel(log_level)

        if log_level == '':
            logging.error("입력값 오류!")
            sys.exit(-1)

        logging.info("***** INPUT ******")
        logging.info("[1] 로그레벨(D:DEBUG, E:ERROR, 그외:INFO):" + str(log_level))

        # ---------------------------------------------------------------------
        # Logic Start!
        # ---------------------------------------------------------------------
        start_find_shoot()

    except KeyboardInterrupt:
        logging.error("KeyboardInterrupt Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(-100)

    except Exception:
        logging.error("Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(-200)
