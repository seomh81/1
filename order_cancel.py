import os
import sys
import logging
import traceback

# 공통 모듈 Import
sys.path.append(os.path.dirname(os.path.dirname(__file__)))
from lib import oldupbit as upbit  # noqa

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
        # 미체결 주문 조회
        locked_trade = upbit.get_order('KRW-DOGE')
        logging.info("취소전")
        logging.info(locked_trade)

        # 미체결 주문 취소
        upbit.cancel_order('KRW-DOGE', 'BUY')

        # 미체결 주문 재조회
        locked_trade = upbit.get_order('KRW-DOGE')
        logging.info("취소후")
        logging.info(locked_trade)

    except KeyboardInterrupt:
        logging.error("KeyboardInterrupt Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)

    except Exception:
        logging.error("Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)