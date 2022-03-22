# 캔들 1분봉 볼밴 1분봉 상승기울기 매수 5050
# 볼린저밴트 5분봉 하단 점을 찍고 올라가면 매수 모니터링은 1분봉기준
# 저가 매수 로직만 세팅

import sys
import os
import time
from datetime import datetime, timedelta

# 공통 모듈 Import
sys.path.append(os.path.dirname(os.path.dirname(__file__)))
from lib import oldupbit as upbit  # noqa




# -----------------------------------------------------------------------------
# - Name : start_buytrade
# - Desc : 매수 로직
# - Input
# 1) minute_interval : 몇 분봉을 볼지 정함
# 2) minute_check : 몇개의 봉을 기준삼을지 정함
# 3) buy_amt : 매수금액
# -----------------------------------------------------------------------------
def start_buytrade(buy_amt, except_items):
    try:
        data_cnt = 0

        # 55분 마다 리스트 초기화 (변수 변경 시 아래도 변경해야 함)
        due_time = (datetime.now() + timedelta(hours=3)).strftime('%H%M')

        # 매수 될 때까지 반복 수행
        while True:

            # 전체 종목 추출
            # 1. KRW마켓  ``
            # 2. 제외할 ticker
            item_list = upbit.get_items('KRW', except_items)

            # 전체 종목 반복
            for item_list_for in item_list:


                # 1분봉 (최대 200개 요청가능) - 6개 요청(5분전부터)
                df_candle = upbit.get_candle(item_list_for['market'], '1', 1)

                can_lowNow = df_candle[0]['low_price']


                # 볼린저밴드 1분봉
                df_bb = upbit.get_bb(item_list_for['market'], '1', '200', 3) #1분봉으로 테스트

                bb_now = df_bb[0]['BBL']
                bb_Before1 = df_bb[1]['BBL']
                bb_Before2 = df_bb[2]['BBL']

                bb_gapBefore0 = bb_now - bb_Before1
                bb_gapBefore1 = bb_Before1 - bb_Before2

                bb_nowH = df_bb[0]['BBH']
                bb_Before1H = df_bb[1]['BBH']
                bb_Before2H = df_bb[2]['BBH']

                bb_gapBefore0H = bb_nowH - bb_Before1H
                bb_gapBefore1H = bb_Before1H - bb_Before2H


                print(format((bb_now-can_lowNow)/bb_now*100, '.1f'),'%', int(bb_gapBefore0 - bb_gapBefore1),">>>",item_list_for['market'], "  양수TRY / 제외종목:", except_items)

                # 볼린저밴드 1분봉 하단을 반등 시 매수
                if bb_now > can_lowNow and bb_gapBefore0 > 0 and bb_gapBefore1 > 0 and bb_gapBefore0H > 0 and bb_gapBefore1H > 0:

                    # 기준 충족 종목 종가
                    print(item_list_for['market'],'하한가' + str(can_lowNow))

                    # 지정가 매수
                    print('지정가 매수 시작!')
                    upbit.buycoin_tg(item_list_for['market'],buy_amt, can_lowNow)

                    # ------------------------------------------------------------------
                    # 매수 완료 종목은 매수 대상에서 제외
                    # ------------------------------------------------------------------
                    except_items = except_items + ',' + item_list_for['market'].split('-')[1]


                time.sleep(0.03)



                if data_cnt == 0 or data_cnt % 100 == 0:
                    print("매수 진행 중...[" + str(data_cnt) + "]")



                now = datetime.now().strftime('%H%M')

                if due_time == now:
                    except_items = ''
                    due_time = (datetime.now() + timedelta(hours=1)).strftime('%H%M')

                # 조회건수증가
                data_cnt = data_cnt + 1

    # ----------------------------------------
    # 모든 함수의 공통 부분(Exception 처리)
    # ----------------------------------------
    except Exception:
        raise
# -----------------------------------------------------------------------------
# - Name : main
# - Desc : 메인
# -----------------------------------------------------------------------------
if __name__ == '__main__':


    buy_amt = 5050
    print("매수금액:" + str(buy_amt))

    # 매수로직 시작
    try:
        except_items = ''
        while True:
            start_buytrade(buy_amt, except_items)


    except Exception:

        raise