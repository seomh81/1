from lib import upbit as upbit
import pandas as pd

#볼린저밴드 테스트
'''
bb1 = upbit.get_bb('KRW-BTC','30','200',1)

print(bb1[0]['BBL'])

#bb1_1 = bb1(['BBH'])

#print(bb1_1)

#df_bb1 = pd.DataFrame.from_records(bb1)
#df_bb1.to_excel('test_bb3.xlsx')

'''


#전체 보조지표 조회

indecs_all = upbit.get_indicators('KRW-BTC', '30', '200', 1)

print(indecs_all[3][0]['BBL'])
i=0


for indecs_all_for in indecs_all:
    print(indecs_all[i])
    i += 1

#accounts = upbit.get_accounts("N","KRW")
#print(accounts)

import os
import sys
import logging
import traceback

# 공통 모듈 Import
sys.path.append(os.path.dirname(os.path.dirname(__file__)))
from lib import upbit as upbit  # noqa

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
        # 잔고 조회(KRW, 소액 제외)
        account_data = upbit.get_accounts("Y", "KRW")

        for account_data_for in account_data:
            logging.info(account_data_for)

        logging.info('')

        # 잔고 조회(KRW, 소액 포함)
        account_data = upbit.get_accounts("N", "KRW")

        for account_data_for in account_data:
            logging.info(account_data_for)

    except KeyboardInterrupt:
        logging.error("KeyboardInterrupt Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)

    except Exception:
        logging.error("Exception 발생!")
        logging.error(traceback.format_exc())
        sys.exit(1)