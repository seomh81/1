import time
import os
import sys
import logging
import traceback

from decimal import Decimal

# 공통 모듈 Import
sys.path.append(os.path.dirname(os.path.dirname(__file__)))
from module import upbit4linux


# -----------------------------------------------------------------------------
# - Name : sell_coin_mp
# - Desc : 시장가 매도 진행
# - Input
# 1) target_item : 대상종목
# 2) min_buy_amt : 최소 매수금액
# 3) min_sell_amt : 최소 매도금액
# -----------------------------------------------------------------------------
def sell_coin_mp(target_item, min_buy_amt, min_sell_amt):
    try:

        logging.info('')
        logging.info("---------------------------------------------------------")
        logging.info(" 시장가 매도 진행!")
        logging.info("---------------------------------------------------------")

        # -----------------------------------------------------------------
        # 시장가 매도 진행
        # -----------------------------------------------------------------
        upbit4linux.sellcoin_mp(target_item, 'N')

        # -----------------------------------------------------------------
        # 시장가 매수 진행
        # -----------------------------------------------------------------
        buy_coin_mp(target_item, min_buy_amt, min_sell_amt)

    # ---------------------------------------
    # 모든 함수의 공통 부분(Exception 처리)
    # ----------------------------------------
    except Exception:
        raise


# -----------------------------------------------------------------------------
# - Name : buy_coin_mp
# - Desc : 시장가 매수 진행
# - Input
# 1) target_item : 대상종목
# 2) min_buy_amt : 최소 매수금액
# 3) min_sell_amt : 최소 매도금액
# -----------------------------------------------------------------------------
def buy_coin_mp(target_item, min_buy_amt, min_sell_amt):
    try:

        logging.info('')
        logging.info("---------------------------------------------------------")
        logging.info(" 시장가 매수 진행!")
        logging.info("---------------------------------------------------------")

        # -----------------------------------------------------------------
        # 시장가 매수 진행
        # -----------------------------------------------------------------
        upbit4linux.buycoin_mp(target_item, min_buy_amt)

        # -----------------------------------------------------------------
        # 시장가 매도 진행
        # -----------------------------------------------------------------
        sell_coin_mp(target_item, min_buy_amt, min_sell_amt)

    # ---------------------------------------
    # 모든 함수의 공통 부분(Exception 처리)
    # ----------------------------------------
    except Exception:
        raise


# -----------------------------------------------------------------------------
# - Name : start_trade
# - Desc : 호가창 흔들기
# - Input
# 1) target_item : 대상종목
# -----------------------------------------------------------------------------
def start_trade(target_item):
    try:

        logging.info("*********************************************************")
        logging.info("1. 매수종목 : " + str(target_item))
        logging.info("*********************************************************")

        # -----------------------------------------------------------------
        # 최소 매수/매도 금액 추출
        # -----------------------------------------------------------------
        order_chance = upbit4linux.get_order_chance(target_item)

        # 최소 매수금액
        min_buy_amt = order_chance['market']['bid']['min_total']

        # 최소 매도금액
        min_sell_amt = order_chance['market']['ask']['min_total']

        logging.info('')
        logging.info("---------------------------------------------------------")
        logging.info("1. 최소 매수금액 : " + str(min_buy_amt))
        logging.info("2. 최소 매도금액 : " + str(min_sell_amt))
        logging.info("---------------------------------------------------------")

        # -----------------------------------------------------------------
        # 시장가 매수 진행
        # -----------------------------------------------------------------
        buy_coin_mp(target_item, min_buy_amt, min_sell_amt)


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

        # ---------------------------------------------------------------------
        # 입력 받을 변수
        #
        # 1. 로그레벨
        #   1) 레벨 값 : D:DEBUG, E:ERROR, 그 외:INFO
        #
        # 2. 대상종목
        #   1) 종목코드 : 마켓코드-종목코드(ex. KRW-DOGE)
        #
        # ---------------------------------------------------------------------

        # 1. 로그레벨
        log_level = input("로그레벨(D:DEBUG, E:ERROR, 그 외:INFO) : ").upper()
        target_item = input("종목코드(ex:KRW-DOGE) : ").upper()

        upbit4linux.set_loglevel(log_level)

        logging.info("*********************************************************")
        logging.info("1. 로그레벨 : " + str(log_level))
        logging.info("2. 종목코드 : " + str(target_item))
        logging.info("*********************************************************")

        # 매수 로직 시작
        start_trade(target_item)


    except KeyboardInterrupt:
        logging.error("KeyboardInterrupt Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(-100)

    except Exception:
        logging.error("Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(-200)
