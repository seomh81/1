
----- buy.py -----

logging.info('BB2 ---> ' + str(bb2[0]['BBH']) + ' / ' + str(bb2[0]['BBM']) + ' / ' + str(bb2[0]['BBL']) + ' / ' + str(
    candle[0]['trade_price']))

# --------------------------------------------------------------
# 볼린저 밴드 추가
# --------------------------------------------------------------
if (bb2[0]['BBL'] > candle[0]['low_price'] and bb[0]['BBL'] > bb2[0]['BBL'] and candle[0]['high_price'] != candle[1][
    'high_price'] and candle[0]['low_price'] != candle[1]['low_price'] and (candle[1]['high_price'] - candle[2][
    'high_price']) * (candle[1]['low_price'] - candle[2]['low_price']) != 2) or (
        bb2[0]['BBH'] <= candle[0]['high_price'] and bb[0]['BBM'] >= candle[0]['low_price'] and bb[0]['BBH'] > bb2[0][
    'BBH'] and candle[0]['high_price'] != candle[1][
    'high_price'] and candle[0]['low_price'] != candle[1]['low_price'] and (candle[1]['high_price'] - candle[2][
    'high_price']) * (candle[1]['low_price'] - candle[2]['low_price']) != 2):  # and (candle[0]['low_price'] - candle[1]['low_price']) / candle[1]['low_price'] < -1:

    logging.info('?????????????? ' + str(candle[0]['trade_price']) + ' > ' + str(bb2[0]['BBH']) + ' || ' + str(
        bb2[0]['BBM']) + ' > ' + str(candle[0]['low_price']))
    logging.info('!!!!!!!!!!!!!! ' + str(bb2[0]['BBL']) + ' > ' + str(candle[0]['low_price']))



----- upbit.py -----


# -----------------------------------------------------------------------------
# - Name : get_bb2
# - Desc : 볼린저밴드 조회
# - Input
#   1) candle_datas : 캔들 정보
# - Output
#   1) 볼린저 밴드 값
# -----------------------------------------------------------------------------
def get_bb2(candle_datas):
    try:

        # 볼린저밴드 데이터 리턴용
        bb_list = []

        # 캔들 데이터만큼 수행
        for candle_data_for in candle_datas:
            df = pd.DataFrame(candle_data_for)
            dfDt = df['candle_date_time_kst'].iloc[::-1]
            df = df['trade_price'].iloc[::-1]

            # 표준편차(곱)
            unit = 2

            band1 = unit * numpy.std(df[len(df) - 120:len(df)]) #와.,.. 볼밴이 두개였다니... 그래서 안고쳐지고 오작동한거임???
            bb_center = numpy.mean(df[len(df) - 120:len(df)])
            band_high = bb_center + band1
            band_low = bb_center - band1

            bb_list.append({"type": "BB", "DT": dfDt[0], "BBH": round(band_high, 4), "BBM": round(bb_center, 4),
                            "BBL": round(band_low, 4)})

        return bb_list


    # ----------------------------------------
    # 모든 함수의 공통 부분(Exception 처리)
    # ----------------------------------------
    except Exception:
        raise


