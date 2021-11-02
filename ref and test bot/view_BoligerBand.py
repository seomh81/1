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
        # 볼린저밴드 조회
        bb_data = upbit.get_bb('KRW-BTC', '30', '200', 10)

        for bb_data_for in bb_data:
            logging.info(bb_data_for)

    except KeyboardInterrupt:
        logging.error("KeyboardInterrupt Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)

    except Exception:
        logging.error("Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)