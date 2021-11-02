import os
import sys
import logging
import math
import traceback

# 공통 모듈 Import
sys.path.append(os.path.dirname(os.path.dirname(__file__)))
from lib import upbit
#from lib import upbit as upbit  # noqa

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
        # MACD 조회
        macd_data = upbit.get_macd('KRW-BTC', '30', '200', 10) ####30분봉 기준, 200개 캔들, 10개 MACD 호출

        for macd_data_for in macd_data:
            logging.info(macd_data_for)

    except KeyboardInterrupt:
        logging.error("KeyboardInterrupt Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)

    except Exception:
        logging.error("Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)