<script setup>
import { ColumnChart } from "@opd/g2plot-vue";
import html2pdf from "html2pdf.js";
import dayjs from "dayjs";
import { ref } from "vue";
const props = defineProps(["chart", "text", "data"]);


const reportEl = ref();
const save = () => {
  let opt = {
    margin: 0,
    filename: `주요부품진단리포트-[${
      props.data.type == "MAIN ROPE"
        ? "권상로프"
        : props.data.type == "MAIN SHEAVE"
        ? "메인 쉬브"
        : props.data.type == "MAIN INV"
        ? "인버터"
        : props.data.type == "DOOR INV"
        ? "도어 컨트롤러"
        : "문 닫힘 안전 장치"
    }]_${dayjs().format("YYYYMMDD")}`,
    image: { type: "png" },
    useCORS: true,
    html2canvas: {
      // html2canvas 옵션
      useCORS: true, // 영역 안에 로컬 이미지를 삽입 할 때 옵션 필요
      scrollY: 0, // 스크롤 이슈 때문에 필수
      scale: 2, // browsers device pixel ratio
      letterRendering: true,
      allowTaint: false, //useCORS를 true로 설정 시 반드시 allowTaint를 false처리 해주어야함
    },
    jsPDF: {
      orientation: "portrait",
      unit: "mm",
      format: "a4",

      compressPDF: true,
    },
  };
  html2pdf(reportEl.value.$el, opt);
};
const defaultConfig = ref({
  appendPadding: 10,
  willReadFrequently: true,
  yField: "value",
  xField: "type",
  isStack: true,
  radius: 0.75,
  seriesField: "name",
  label: false,
  tooltip: false,

  color: ["#EF5350", "#BDBDBD", "#0D99FF"],
  legend: false,
  minColumnWidth: 25,
  maxColumnWidth: 25,
  xAxis: [
    {
      label: {
        position: "top",
        offset: 20,
      },
    },
  ],
});
const getYearDiff = (d1, d2, cycle, type) => {
  const date1 = dayjs(d1);
  const date2 = dayjs(d2);
  const date3 = date1.add(cycle, "year");

  if (date3.isBefore(date2)) {
    if (type == "over") {
      return date2.diff(date3, "year");
    } else {
      return date3.diff(date1, "year");
    }
  }
};
</script>
<template>
  <v-card width="40vw" class="customCard" id="element-to-print" theme="light">
    <v-toolbar density="compact">
      <v-toolbar-title>주요 부품 진단 리포트</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn @click="save">저장</v-btn>
    </v-toolbar>
    <v-sheet ref="reportEl">
      <v-container fluid>
        <v-row align="center" justify="center">
          <v-col cols="7">
            <v-card-title>{{
              data.type == "MAIN ROPE"
                ? "권상로프 (Main Rope)"
                : data.type == "MAIN SHEAVE"
                ? "메인 쉬브 (Main Sheave)"
                : data.type == "MAIN INV"
                ? "인버터 (Main Inverter)"
                : data.type == "DOOR INV"
                ? "도어 컨트롤러 (Door Controller)"
                : "문 닫힘 안전 장치 (Multibeam)"
            }}</v-card-title>
            <v-card-subtitle>- 상태 : 교체 권장</v-card-subtitle>
          </v-col>
          <v-col cols="5">
            <v-img
              height="200px"
              cover
              :src="
                require(`@/assets/img/report/${
                  data.type == 'MAIN ROPE'
                    ? 'rope'
                    : data.type == 'MAIN SHEAVE'
                    ? 'mainSheave'
                    : data.type == 'MAIN INV'
                    ? 'inverter'
                    : data.type == 'DOOR INV'
                    ? 'doorInv'
                    : 'multiBeam'
                }.png`)
              "
            />
          </v-col>
        </v-row>
        <v-row
          align="center"
          class="mx-auto my-4"
          justify="center"
          :style="{
            width: '75%',
          }"
        >
          <v-col cols="6">
            <span class="largeBoldText mb-2">{{ data.count1 }}</span>
            <column-chart
              class="myChart"
              v-bind="{
                ...defaultConfig,
                yAxis: {
                  label: {
                    formatter: (value) =>
                      `${value == 0 ? '0' : value.toString().slice(0, -4)}만`,
                  },
                },
                annotations: [
                  {
                    type: 'line',
                    /** 起始位置 */
                    start: ['내 호기', data.item.standard_durability?.limit],
                    end: [
                      '동일 호기 평균 값',
                      data.item.standard_durability?.limit,
                    ],
                    text: {
                      // 旅游萧条 标注
                      content: '표준',
                      rotate: 0,
                      autoRotate: false,
                      position: ['right'],

                      style: {
                        fontWeight: '700',
                        textAlign: 'center',
                        fill: '#0D99FF',
                      },
                    },
                    style: {
                      stroke: '#0D99FF',
                      lineWidth: 0.5,
                    },
                  },
                ],
              }"
              :data="[
                {
                  name: '초과 운행거리',
                  value:
                    data.item.standard_durability.limit -
                      data.item.accumulated_move <
                    0
                      ? Math.abs(
                          data.item.standard_durability.limit -
                            data.item.accumulated_move
                        )
                      : 0,
                  type: '내 호기',
                },
                {
                  name: '한계',
                  value: data.item.standard_durability?.average_limit,
                  type: '동일 호기 평균 값',
                },

                {
                  name: '현재 운행거리',
                  value:
                    data.item.accumulated_move >
                    data.item.standard_durability.limit
                      ? data.item.standard_durability.limit
                      : data.item.accumulated_move,
                  type: '내 호기',
                },
              ]"
            />
          </v-col>
          <v-col cols="6">
            <span class="largeBoldText mb-2">{{ data.count2 }}</span>
            <column-chart
              class="myChart"
              v-bind="{
                ...defaultConfig,
                yAxis: {
                  label: {
                    formatter: (value) => `${value}y`,
                  },
                },
                annotations: [
                  {
                    type: 'line',
                    start: [
                      '내 호기',
                      data.item.standard_durability?.replacement_cycle,
                    ],
                    end: [
                      '동일 호기 평균 값',
                      data.item.standard_durability?.replacement_cycle,
                      ,
                    ],
                    text: {
                      // 旅游萧条 标注
                      content: '표준',
                      rotate: 0,
                      autoRotate: false,
                      position: ['right'],

                      style: {
                        textAlign: 'center',
                        fill: '#0D99FF',
                      },
                    },
                    style: {
                      stroke: '#0D99FF',
                      lineWidth: 0.5,
                    },
                  },
                ],
              }"
              :data="[
                {
                  name: '경과 년수',
                  value: getYearDiff(
                    data.item.replacement_date,
                    new Date(),
                    data.item.standard_durability?.replacement_cycle,
                    'over'
                  ),

                  type: '내 호기',
                },
                {
                  name: '교체 주기',
                  value: data.item.standard_durability?.replacement_cycle,

                  type: '동일 호기 평균 값',
                },

                {
                  name: '사용 년수',
                  value: getYearDiff(
                    data.item.replacement_date,
                    new Date(),
                    data.item.standard_durability?.replacement_cycle
                  ),
                  type: '내 호기',
                },
              ]"
            />
          </v-col>
          <v-col cols="12"
            ><p
              :style="{
                fontSize: '14px',
                opacity: '0.7',
                wordBreak: 'keep-all',
              }"
            >
              {{ data.comment }}
            </p>
          </v-col>
        </v-row>
        <v-row
          class="mx-auto"
          :style="{
            width: '75%',
          }"
        >
          <v-list class="mx-auto">
            <v-list-item class="mb-2">
              <v-list-item-title
                :style="{ color: 'rgb(61,159,255)', fontWeight: 'bold' }"
                >기능 설명</v-list-item-title
              >
              <v-divider class="mt-1 mb-2"></v-divider>
              <p
                :style="{
                  fontSize: '14px',
                  opacity: '0.7',
                  wordBreak: 'keep-all',
                }"
              >
                {{
                  data.type == "MAIN ROPE"
                    ? "카와 균형추를 매달아 권상도르래의 회전을 상/하 운동으로 바꾸어주는 와이어 로프"
                    : data.type == "MAIN SHEAVE"
                    ? "권상기의 동력으로 회전하는 시브(도르래)로서 로프와 맞물려 카를 상승 및 하강 시킴"
                    : data.type == "MAIN INV"
                    ? "제어반 내에 설치되어 승강기의 속도와 토크를 제어하는 핵심 부품"
                    : data.type == "DOOR INV"
                    ? "카 도어의 안전 스위치와 멀티빔 등 각종 신호 및 열림 닫힘을 전기적으로 제어하고 제어반의 메인보드와 실시간 통신을 주고 받는 장치"
                    : "승강기 출입문에 설치되며, 적외선 투시를 통해 미세한 움직임까지 감지할 수 있어 출입문 끼임 및 훙격으로 부터 승객을 보호해줄 수 있는 안전장치"
                }}
              </p>
            </v-list-item>
            <v-list-item class="mb-2">
              <v-list-item-title
                :style="{ color: 'rgb(61,159,255)', fontWeight: 'bold' }"
                >점검 사항</v-list-item-title
              >
              <v-divider class="mt-1 mb-2"></v-divider>
              <div v-if="data.type == 'MAIN ROPE'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  소선단선여부, 로프의 마모, 장력, 윤활, 녹발생 여부, 변형 및
                  인장 정도
                </p>
                <v-img src="@/assets/img/report/rope2.png" cover></v-img>
              </div>
              <div v-if="data.type == 'MAIN SHEAVE'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  시브 홈 (언더컷) 잔여량 확인(특정 홈의 로프 침하가 많은지
                  확인)
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  시브 홈의 청결 확인, 베어링 이상 소음 확인
                </p>
              </div>
              <div v-if="data.type == 'MAIN INV'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  인버터 에러 확인 및 인버터 팬 동작 상태 확인
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  각종 PCB 및 소자들의 노후 상태 확인
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  각종 PCB 및 소자들의 노후 상태 확인
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  커넥터 결선 상태 및 청결 상태 확인
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  제어판 내부 이물질 제거
                </p>
              </div>
              <div v-if="data.type == 'DOOR INV'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  도어 컨트롤러 / 인버터 작동 상태 및 에러 확인
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  커넥터 결선 상태 및 청결 상태 확인
                </p>
              </div>
              <div v-if="data.type == 'SAFETY EDGE'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  적외선 발광, 수광 센서 동작 상태 점검, 세이프티 엣지 스위치
                  동작 여부 점검
                </p>
              </div>
            </v-list-item>
            <v-list-item class="mb-2">
              <v-list-item-title
                :style="{ color: 'rgb(61,159,255)', fontWeight: 'bold' }"
                >교체 기준</v-list-item-title
              >
              <v-divider class="mt-1 mb-2"></v-divider>
              <div v-if="data.type == 'MAIN ROPE'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  로프의 소손 및 윤활제(그리스) 부족으로 발청(녹가루)가 발생하는
                  경우
                </p>

                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  로프의 파단, 마모 및 편 마모가 발생된 경우
                </p>

                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  로프의 본 직경의 95% 이하인 경우
                </p>

                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  승강기 안전검사 기준에(로프의 마모 및 파손상태에 대한 기준)
                  부합하지 않는 경우
                </p>
              </div>
              <div v-if="data.type == 'MAIN SHEAVE'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  시브 홈의 마모량이 기준치를 초과한 경우 (언더컷 잔여량이
                  2mm이하, 로프 간 높이 편차 2mm이상)
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  로프와 시브 간 미끄러짐이 발생하는 경우
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  홈에 로프 형상이 있는 경우 경우
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  시브에 균열이 발생한 경우 경우
                </p>
              </div>
              <div v-if="data.type == 'MAIN INV'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  인버터 하드웨어 고장 발생 시, 인버터 노후화로 주행 중 카 진동
                  발생 시 (현장 환경 및 운행량 등에 따라 교체 주기가 달라질 수
                  있음)
                </p>
              </div>
              <div v-if="data.type == 'DOOR INV'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  도어 구동 상태 불량, 오 동작 등 도어 인버터 에러 발생 시
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  내구 연한이 지나 반복 고장이 발생한 경우 (현장 환경 및 운행량
                  등에 따라 교체 주기가 달라질 수 있음)
                </p>
              </div>
              <div v-if="data.type == 'SAFETY EDGE'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  멀티 빔 하드웨어 고장 발생 시, 멀티빔 노후화로 오작동 및 반복
                  고장 발생 시 (현장환경 및 운행량 등에 따라 교체 주기가 달라질
                  수 있음)
                </p>
              </div>
            </v-list-item>
            <v-list-item class="mb-2">
              <v-list-item-title
                :style="{ color: 'rgb(61,159,255)', fontWeight: 'bold' }"
              >
                발생 위험</v-list-item-title
              >
              <v-divider class="mt-1 mb-2"></v-divider>
              <div v-if="data.type == 'MAIN ROPE'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  교체주기 초과 시 로프 내 윤활제(그리스)가 부족하여 로프 및
                  시브 마모의 원인이 되며 이는 로프 슬립, 파단 발생으로
                  <strong>주행 중 급정지 및 갇힘 사고의</strong> 원인이 될 수
                  있음
                </p>
              </div>
              <div v-if="data.type == 'MAIN SHEAVE'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  교체주기 초과 시 시브 홈(언더) 마모로 로프가 시브에서 미끄러져
                  <strong>갇힘 사고가 발생할 수 있음</strong>
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  시브의 직경이 일정하지 않으면 굴곡이 발생하여 로프의 소선이
                  파단될 수 있음
                </p>
              </div>
              <div v-if="data.type == 'MAIN INV'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  교체주기 초과 시 승강기 제어 입/출력 신호의 오류가 생겨 인버터
                  고장에 의한
                  <strong>주행 중 급정지 및 갇힘 사고의</strong> 원인이 될 수
                  있음
                </p>
              </div>
              <div v-if="data.type == 'DOOR INV'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  교체 주기 초과 시 도어 인버터 오동작으로 인해 열림 닫힘 시
                  승객을 다치게 할 수 있음
                </p>
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  열림 동작이 불가하여 <strong>승객 갇힘 사고의</strong> 원인이
                  될 수 있음
                </p>
              </div>
              <div v-if="data.type == 'SAFETY EDGE'">
                <p
                  :style="{
                    fontSize: '14px',
                    opacity: '0.7',
                    wordBreak: 'keep-all',
                  }"
                >
                  교체주기 초과 시 멀티빔 센서가 동작이 안돼 승객이 탑승 중에
                  도어에 충돌하여 다칠 수 있음
                </p>
              </div>
            </v-list-item>
          </v-list>
        </v-row>
      </v-container>
      <v-footer class="pb-4">
        <span>Mobility To Possibility</span>
        <v-divider
          color="black"
          class="mx-2"
          thickness="2"
          vertical
        ></v-divider>
        <span>모빌리티에서 새로운 가능성으로</span>
        <v-spacer></v-spacer>
        <span>현대엘리베이터</span>
      </v-footer>
    </v-sheet>
  </v-card>
</template>
