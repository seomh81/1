import os
import sys
import logging
import math
import traceback
import time

# 공통 모듈 Import
sys.path.append(os.path.dirname(os.path.dirname(__file__)))
from lib import upbit as upbit
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
        # 보유 종목 리스트 조회
        item_list = upbit.get_accounts('Y', 'KRW') ####5000원 미반 소량 보유종목 제외하기 위해 Y, 마켓정보가 두번째

        logging.info(len(item_list))
        logging.info(item_list)

        # 종목별 처리
        for item_list_for in item_list:
            logging.info('종목코드:' + item_list_for['market'])

            # 시장가 매도
            # ★ 실제 매도가 될 수 있어 아래 주석 처리함.
            # 실제 매도를 원하는 경우 아래 주석을 해제하면 됩니다.
            upbit.sellcoin_mp(item_list_for['market'],'Y') ####해당 코드 주석처리로 설정
            
            time.sleep(0.1)

        logging.info('전 종목 매도 완료')

    except KeyboardInterrupt:
        logging.error("KeyboardInterrupt Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)

    except Exception:
        logging.error("Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)