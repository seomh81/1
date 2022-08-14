import os
import sys
import time
import json
import datetime
import asyncio
import logging
import traceback
import websockets
import cx_Oracle

# 실행 환경에 따른 공통 모듈 Import
sys.path.append(os.path.dirname(os.path.dirname(__file__)))
from module import upbit

# 프로그램 정보
pgm_name = 'save_ticker'
pgm_name_kr = '업비트 TICKER 웹소켓 데이터 저장'

# 오라클 데이터베이스 접속정보
p_username = 'ADMIN'
p_password = 'Tkek1234seo2208'
p_service = 'seobot1_high'


# -----------------------------------------------------------------------------
# - Name : get_subscribe_items
# - Desc : 구독 대상 종목 조회
# -----------------------------------------------------------------------------
def get_subscribe_items():
    try:
        subscribe_items = []

        # KRW 마켓 전 종목 추출
        items = upbit.get_items('KRW', '')

        # 종목코드 배열로 변환
        for item in items:
            subscribe_items.append(item['market'])

        return subscribe_items

    # ---------------------------------------
    # Exception 처리
    # ----------------------------------------
    except Exception:
        raise


# -----------------------------------------------------------------------------
# - Name : upbit_ws_client
# - Desc : 업비트 웹소켓
# -----------------------------------------------------------------------------
async def upbit_ws_client():
    try:

        # 처리 Count 용
        data_cnt = 0

        # 중복 실행 방지용
        seconds = 0

        # 종목별 시퀀스
        item_seq = {}

        # 구독 데이터 조회
        subscribe_items = get_subscribe_items()

        logging.info('구독 종목 개수 : ' + str(len(subscribe_items)))
        logging.info('구독 종목 : ' + str(subscribe_items))

        # 구독 데이터 조립
        subscribe_fmt = [
            {"ticket": "test-websocket"},
            {
                "type": "ticker",
                "codes": subscribe_items,
                "isOnlyRealtime": True
            },
            {"format": "SIMPLE"}
        ]

        subscribe_data = json.dumps(subscribe_fmt)

        # 오라클 데이터 베이스 연결
        conn = cx_Oracle.connect(p_username, p_password, p_service)

        # 커서 획득
        cur = conn.cursor()

        # INSERT SQL 준비
        sql = "INSERT INTO ticker_data(datetime, code, opening_price, high_price, low_price, trade_price, " \
              "prev_closing_price, change, change_price, signed_change_price, change_rate, " \
              "signed_change_rate, trade_volume, acc_trade_volume, acc_trade_volume_24h, acc_trade_price, " \
              "acc_trade_price_24h, trade_date, trade_time, trade_timestamp, ask_bid, acc_ask_volume,  " \
              "acc_bid_volume, highest_52_week_price, highest_52_week_date, lowest_52_week_price, " \
              "lowest_52_week_date, market_state, is_trading_suspended, " \
              "delisting_date, market_warning, timestamp, stream_type, sys_datetime, seq, p_seq) VALUES " \
              "(TO_DATE(:1, 'YYYYMMDDHH24MISS') + (9/24),:2,:3,:4,:5,:6,:7,:8,:9,:10,:11,:12,:13,:14,:15,:16,:17,:18,:19,:20,:21,:22,:23,:24,:25,:26," \
              ":27,:28,:29,:30,:31,:32,:33,current_date,:34,:35) "

        async with websockets.connect(upbit.ws_url) as websocket:

            await websocket.send(subscribe_data)

            while True:
                period = datetime.datetime.now()

                data = await websocket.recv()
                data = json.loads(data)

                # 종목별 시퀀스 채번
                if data['cd'] in item_seq:
                    item_seq[data['cd']] = item_seq[data['cd']] + 1
                else:
                    item_seq[data['cd']] = 1

                # 저장용 데이터 조립
                args = (data['tdt'] + data['ttm'], data['cd'], data['op'], data['hp'], data['lp'], data['tp'],
                        data['pcp'], data['c'], data['cp'], data['scp'], data['cr'],
                        data['scr'], data['tv'], data['atv'], data['atv24h'], data['atp'],
                        data['atp24h'], data['tdt'], data['ttm'], data['ttms'], data['ab'],
                        data['aav'], data['abv'], data['h52wp'], data['h52wdt'], data['l52wp'],
                        data['l52wdt'], data['ms'], data['its'],
                        '', data['mw'], data['tms'], data['st'], item_seq[data['cd']], item_seq[data['cd']] - 1)

                # 데이터 저장
                cur.execute(sql, args)
                conn.commit()

                data_cnt = data_cnt + 1

                # 데이터 저장 로깅
                if data_cnt % 1000 == 0:
                    logging.info("[" + str(datetime.datetime.now()) + "] [TICKER_DATA] Inserting Data....[" + str(
                        data_cnt) + "]")

                # 5초마다 종목 정보 재 조회 후 추가된 종목이 있으면 웹소켓 다시 시작
                if (period.second % 5) == 0 and seconds != period.second:
                    # 중복 실행 방지
                    seconds = period.second

                    # 종목 재조회
                    re_subscribe_items = get_subscribe_items()

                    # 현재 종목과 다르면 웹소켓 다시 시작
                    if subscribe_items != re_subscribe_items:
                        logging.info('종목 달리짐! 웹소켓 다시 시작')
                        logging.info('\n\n')
                        logging.info('*************************************************')
                        logging.info('기존 종목[' + str(len(subscribe_items)) + '] : ' + str(subscribe_items))
                        logging.info('종목 재조회[' + str(len(re_subscribe_items)) + '] : ' + str(re_subscribe_items))
                        logging.info('*************************************************')
                        logging.info('\n\n')
                        await websocket.close()
                        time.sleep(1)
                        await upbit_ws_client()

    # ----------------------------------------
    # 모든 함수의 공통 부분(Exception 처리)
    # ----------------------------------------
    except Exception as e:
        logging.error('Exception Raised!')
        logging.error(e)
        logging.error('Connect Again!')

        # 웹소켓 다시 시작
        await upbit_ws_client()


# -----------------------------------------------------------------------------
# - Name : main
# - Desc : 메인
# -----------------------------------------------------------------------------
async def main():
    try:
        # 웹소켓 시작
        await upbit_ws_client()

    except Exception as e:
        logging.error('Exception Raised!')
        logging.error(e)


# -----------------------------------------------------------------------------
# - Name : main
# - Desc : 메인
# -----------------------------------------------------------------------------
if __name__ == "__main__":

    # noinspection PyBroadException
    try:
        print("***** USAGE ******")
        print("[1] 로그레벨(D:DEBUG, E:ERROR, 그외:INFO)")

        if sys.platform.startswith('win32'):
            # 로그레벨(D:DEBUG, E:ERROR, 그외:INFO)
            log_level = 'I'
            upbit.set_loglevel(log_level)
        else:
            # 로그레벨(D:DEBUG, E:ERROR, 그외:INFO)
            #log_level = sys.argv[1].upper()
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
        # 웹소켓 시작
        asyncio.run(main())

    except KeyboardInterrupt:
        logging.error("KeyboardInterrupt Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(-100)

    except Exception:
        logging.error("Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(-200)