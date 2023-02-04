import os
import sys
import logging
import math
import schedule
import traceback
import time

# 공통 모듈 Import
sys.path.append(os.path.dirname(os.path.dirname(__file__)))
#from module import upbit
from module import upbit#_seomh as upbit  # noqa

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
        # 전 종목 리스트 조회
        item_list = upbit.get_items('KRW', 'BTC,ETH') ####제외하고 싶은 종목을 두번째 항에 넣을 것 '' -> 'BTC,ETH'
        #item_list = upbit.get_items('KRW', 'BTC,ETH,NEO,MTL,LTC,XRP,ETC,OMG,SNT,WAVES,XEM,QTUM,LSK,STEEM,XLM,ARDR,ARK,STORJ,GRS,REP,ADA,SBD,POWR,BTG,ICX,EOS,TRX,SC,ONT,ZIL,POLY,ZRX,LOOM,BCH,BAT,IOST,RFR,CVC,IQ,IOTA,MFT,ONG,GAS,UPP,ELF,KNC,BSV,THETA,QKC,BTT,MOC,ENJ,TFUEL,MANA,ANKR,AERGO,ATOM,TT,CRE,MBL,WAXP,HBAR,MED,MLK,STPT,ORBS,VET,CHZ,STMX,DKA,HIVE,KAVA,AHT,LINK,XTZ,BORA,JST,CRO,TON,SXP,HUNT,PLA,DOT,SRM,MVL,STRAX,AQT,GLM,SSX,META,FCT2,CBK,SAND,HUM,DOGE,STRK,PUNDIX,FLOW,DAWN,AXS,STX,XEC')
        # BTC,ETH,NEO,MTL,LTC,XRP,ETC,OMG,SNT,WAVES,XEM,QTUM,LSK,STEEM,XLM,ARDR,ARK,STORJ,GRS,REP,ADA,SBD,POWR,BTG,ICX,EOS,TRX,SC,ONT,ZIL,POLY,ZRX,LOOM,BCH,BAT,IOST,RFR,CVC,IQ,IOTA,MFT,ONG,GAS,UPP,ELF,KNC,BSV,THETA,QKC,BTT,MOC,ENJ,TFUEL,MANA,ANKR,AERGO,ATOM,TT,CRE,MBL,WAXP,HBAR,MED,MLK,STPT,ORBS,VET,CHZ,STMX,DKA,HIVE,KAVA,AHT,LINK,XTZ,BORA,JST,CRO,TON,SXP,HUNT,PLA,DOT,SRM,MVL,STRAX,AQT,GLM,SSX,META,FCT2,CBK,SAND,HUM,DOGE,STRK,PUNDIX,FLOW,DAWN,AXS,STX,XEC
        #item_list = upbit.get_items('KRW', 'NEO,MTL,LTC,XRP,ETC,OMG,SNT,WAVES,XEM,QTUM,LSK,STEEM,XLM,ARDR,ARK,STORJ,GRS,REP,ADA,SBD,POWR,BTG,ICX,EOS,TRX,SC,ONT,ZIL,POLY,ZRX,LOOM,BCH,BAT,IOST,RFR,CVC,IQ,IOTA,MFT,ONG,GAS,UPP,ELF,KNC,BSV,THETA,QKC,BTT,MOC,ENJ,TFUEL,MANA,ANKR,AERGO,ATOM,TT,CRE,MBL,WAXP,HBAR,MED,MLK,STPT,ORBS,VET,CHZ,STMX,DKA,HIVE,KAVA,AHT,LINK,XTZ,BORA,JST,CRO,TON,SXP,HUNT,PLA,DOT,SRM,MVL,STRAX,AQT,GLM,SSX,META,FCT2,CBK,SAND,HUM,DOGE,STRK,PUNDIX,FLOW,DAWN,AXS,STX,XEC')
        #item_list = [{'market': 'KRW-BTC'}]
        #item_list = [{'market': 'KRW-BTC'},{'market': 'KRW-ETH'},{'market': 'KRW-ICX'}]
        print(item_list)  # 지정리스트 만들기 위해

        logging.info(len(item_list))
        logging.info(item_list)

        # 원화 매수 가능 금액 조회
        #krw_bal = upbit.get_krwbal()

        #logging.info(krw_bal)

        ### option 1,2 중 택일 후 1개는 주석처리
        # 종목별 매수 금액 설정(일정 금액 직접 입력) - option1
        item_buy_amt = 5500 ####원하는 매수 금액, 원래 주석처리됨

        # 종목별 매수 금액 설정(KRW잔고 이용) - option2
        #item_buy_amt = math.floor(krw_bal['available_krw'] / len(item_list))

        logging.info(item_buy_amt)

        # 종목별 처리
        for item_list_for in item_list:
            logging.info('종목코드:' + item_list_for['market'])

            # ------------------------------------------------------------------
            # 기매수 여부 판단
            # ------------------------------------------------------------------
            accounts = upbit.get_accounts('Y', 'KRW')
            account = list(filter(lambda x: x.get('market') == str(item_list_for['market']), accounts))

            # 이미 매수한 종목이면 다시 매수하지 않음
            # sell_bot.py에서 매도 처리되면 보유 종목에서 사라지고 다시 매수 가능
            if len(account) > 0:
                logging.info('기 매수 종목으로 매수하지 않음....[' + str(item_list_for['market']) + ']')
                continue

            # 시장가 매수
            # ★ 실제 매수가 될 수 있어 아래 주석 처리함.
            # 정말로 매수를 원하면 아래 주석을 풀면 됩니다.
            upbit.buycoin_mp(item_list_for['market'], item_buy_amt) ####해당코드 주석처리로 설정

            time.sleep(0.01)

        logging.info('전 종목 매수 완료')

        time.sleep(3600)

    except KeyboardInterrupt:
        logging.error("KeyboardInterrupt Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)

    except Exception:
        logging.error("Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)