import os
import sys
import time
import logging
import traceback
import cx_Oracle

# 공통 모듈 Import
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
        conn = cx_Oracle.connect(upbit.get_env_keyvalue('p_username'), upbit.get_env_keyvalue('p_password'),
                                 upbit.get_env_keyvalue('p_service'))

        # 커서 획득
        c = conn.cursor()

        """         
         WHERE A.PCNT_BY_ST > 10.0                      -- 시작가 현재 상승률 > 10.0%
           AND A.TRADE_CNT > 500                        -- 거래건수 > 500
         ORDER BY A.PCNT_BY_ST DESC                       -- 시작가 대비 현재 상승률 상위순으로 정렬

        """

        sql = "SELECT A.CODE \
              ,A.DT \
              ,A.ST_PRICE \
              ,A.EN_PRICE \
              ,A.MIN_PRICE \
              ,A.MAX_PRICE \
              ,A.TRADE_VOLUME \
              ,A.TRADE_CNT \
              ,A.PCNT_BY_ST \
              ,A.PCNT_BY_MAX \
              ,A.PCNT_BY_ST_MAX \
          FROM FIND_SHOOT_1MIN A \
         WHERE A.PCNT_BY_ST > 10.0 \
           AND A.TRADE_CNT > 500 \
         ORDER BY A.PCNT_BY_ST DESC"

        # ----------------------------------------------------------------------
        # 반복 수행
        # ----------------------------------------------------------------------
        while True:

            # SQL 수행
            c.execute(sql)

            """
            0  ,A.CODE \
            1  ,A.DT \
            2  ,A.ST_PRICE \
            3  ,A.EN_PRICE \
            4  ,A.MIN_PRICE \
            5  ,A.MAX_PRICE \
            6  ,A.TRADE_VOLUME \
            7  ,A.TRADE_CNT \
            8  ,A.PCNT_BY_ST \
            9  ,A.PCNT_BY_MAX \
            10 ,A.PCNT_BY_ST_MAX \    
            """

            rows = c.fetchall()

            # 대상조건 발견 시
            if len(rows) > 0:

                # 발견된 로우만큼 처리
                # 급등주 발견 텔레그렘 메세지 발송
                for row in rows:
                    logging.info(row)

                    message = '[급등주 발견]'
                    message = message + '\n\n종목코드:' + str(row[0])
                    message = message + '\n상승률:' + str(row[8])
                    message = message + '\n거래건수:' + str(row[7])

                    upbit.send_telegram_message(message)

                    logging.info('메세지 발송 완료!')
                    logging.info(message)

                # 중복 메세지 발송하지 않기 위해 60초간 Sleep
                time.sleep(60)

            # 조회 건수 체크
            data_cnt = data_cnt + 1

            if data_cnt % 1000 == 0:
                logging.info("Checking...[" + str(data_cnt) + "][" + str(len(rows)) + "]")

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
        log_level = sys.argv[1].upper()
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