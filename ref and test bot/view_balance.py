import sys
import logging
import traceback
import time

from lib import upbit
from decimal import Decimal

# -----------------------------------------------------------------------------
# - Name : main
# - Desc : 메인
# -----------------------------------------------------------------------------
if __name__ == '__main__':

    # noinspection PyBroadException
    try:
        # 로그레벨 설정(DEBUG)
        upbit.set_loglevel('I')

        # 주문가능 잔고 조회
        balance = upbit.get_balance('KRW-ARK') ####원하는 대상 넣을 것

        # 잔고 출력
        logging.info(balance)

    except KeyboardInterrupt:
        logging.error("KeyboardInterrupt Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)

    except Exception:
        logging.error("Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)


