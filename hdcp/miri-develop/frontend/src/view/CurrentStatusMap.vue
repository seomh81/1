<script setup>
import { onMounted, ref, computed, watch, toRefs } from "vue";
import Info from "@/component/MapPage/Info";
import FacilityStatus from "@/component/MapPage/FacilityStatus.vue";
import FailHandleStatus from "@/component/MapPage/FailHandleStatus.vue";
import ConnettingService from "@/component/MapPage/ConnettingService.vue";
import AiDiagnosis from "@/component/MapPage/AiDiagnosis.vue";
import Inquiry from "@/component/MapPage/Inquiry.vue";
import ServiceRequestModal from "@/component/ServiceRequestModal.vue";
import { useToast } from "vue-toastification";
import { useRouter } from "vue-router";
import { connectionStore } from "@/store/ConnectionStore";
import { userStore } from "@/store/UserStore";
import { dataStore } from "@/store/DataStore";
import { useI18n } from "vue-i18n";
import { useDisplay } from "vuetify";
const { mdAndDown } = useDisplay();
const { t } = useI18n();
const router = useRouter();
const useConnectionStore = connectionStore();
const useDataStore = dataStore();
const useUserStore = userStore();
const toast = useToast();
const mapData = ref([]);
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
let clusterer;
const customWindow = ref();
const customWindowData = ref([]);
const customWindowIdx = ref(2);
const customWindowView = ref(false);
const customWindowSelected = ref([]);
const elNoList = ref([]);
const dialog = ref(false);
let map;

const initMap = async () => {
  if (clusterer) {
    clusterer.clear();
  }
  if (mapData.value.length > 0) {
    clusterer = new kakao.maps.MarkerClusterer({
      map: map,
      averageCenter: true,
      minLevel: 9,
      styles: [{ width: "0", height: "0" }],
    });
    kakao.maps.event.addListener(clusterer, "clustered", (clusters) => {
      clusters.forEach((cluster) => {
        let markers = cluster.getMarkers();
        const nCount = markers.filter((item) => {
          if (item.elData.elStatus !== "Y") {
            return item;
          }
        });
        cluster
          .getClusterMarker()
          .getContent().innerHTML = `<div class="customCluster ${
          nCount.length > 0 ? "error" : "success"
        }">
                                      <div class="iconBox">
                                        <span class="material-icons">elevator</span>
                                      </div>
                                      <p class="customMarkerText">${
                                        nCount.length
                                      } / ${markers.length}
                                        </p>
                                      </div>`;
      });
    });
    var geocoder = new kakao.maps.services.Geocoder();
    let idx = 0;
    let once = false;
    mapData.value.forEach(async (item) => {
      if (item.wgslat && item.wgslon) {
        let position = new kakao.maps.LatLng(item.wgslat, item.wgslon);
        createMapItems(map, item, position);
      } else if ((item.wgslat || item.wgslon) && item.address1) {
        // 주소 검색
        await geocoder.addressSearch(item.address1, function (result, status) {
          // 정상적으로 검색이 완료됐으면
          if (status === kakao.maps.services.Status.OK) {
            let position = new kakao.maps.LatLng(result[0].y, result[0].x);

            createMapItems(map, item, position);
          }
        });
      } else if (item.address1) {
        await geocoder.addressSearch(item.address1, function (result, status) {
          // 정상적으로 검색이 완료됐으면
          if (status === kakao.maps.services.Status.OK) {
            let position = new kakao.maps.LatLng(result[0].y, result[0].x);

            createMapItems(map, item, position);
          }
        });
      } else {
        idx++;
        once = true;
      }
    });
    if (once) {
      toast.warning(t("ERROR_LAT_LNG_ADDRESS", [idx]), {
        timeout: false,
      });
      once = false;
    }
    if (mapData.value[0]?.wgslat && mapData.value[0]?.wgslon) {
      let moveLatLon = new kakao.maps.LatLng(
        mapData.value[0]?.wgslat,
        mapData.value[0]?.wgslon
      );
      map.panTo(moveLatLon);
    } else if (mapData.value[0].address1) {
      await geocoder.addressSearch(
        mapData.value[0].address1,
        function (result, status) {
          // 정상적으로 검색이 완료됐으면
          if (status === kakao.maps.services.Status.OK) {
            let moveLatLon = new kakao.maps.LatLng(result[0].y, result[0].x);
            map.panTo(moveLatLon);
          }
        }
      );
    }
  }
  kakao.maps.event.addListener(map, "zoom_changed", () => {
    // 지도의 현재 레벨을 얻어옵니다
    customWindowData.value = [];
  });
};
const changeArrow = ref(false);
const createMapItems = (map, data, position) => {
  let content = `
    <div class="customMarker  ${
      data.elStatus === "Y" ? "successMarker" : "errorMarker"
    }">
          <div class="iconBox
          ${data.elStatus === "Y" ? "successIcon" : "errorIcon"}"
            >
            <span class="material-icons">elevator</span>

            </div>
            </div>`;

  const marker = new kakao.maps.CustomOverlay({
    content,
    position: position,
  });
  marker.elData = data;
  clusterer.addMarker(marker);

  let iwContent = customWindow?.value?.$el;
  let infowindow = new kakao.maps.CustomOverlay({
    position: position,
    content: iwContent,
  });
  marker.a.addEventListener("click", (e) => {
    customWindowData.value = [];
    customWindowSelected.value = [];
    customWindowData.value = mapData.value
      .filter((item) => {
        return (
          item.wgslat === marker.elData.wgslat &&
          item.wgslon === marker.elData.wgslon
        );
      })
      .sort((a, b) => {
        const aN = Number(a.hoNo.slice(-2));
        const bN = Number(b.hoNo.slice(-2));
        if (
          a.elStatus.toUpperCase() !== "N" &&
          b.elStatus.toUpperCase() === "N"
        )
          return 1;
        if (
          a.elStatus.toUpperCase() === "N" &&
          b.elStatus.toUpperCase() !== "N"
        )
          return -1;
        return a.elStatus.toUpperCase() === "N" &&
          b.elStatus.toUpperCase() === "N"
          ? aN - bN
          : aN > bN
          ? 1
          : -1;
      });

    if (customWindowData.value.length > 1) {
      customWindowIdx.value = 1;
    } else {
      customWindowIdx.value = 2;
    }
    infowindow.setContent(iwContent);

    infowindow.setMap(map);

    // infowindow.setContent(iwContent);
  });
  iwContent?.addEventListener("wheel", () => {
    kakao.maps.event.preventMap();
  });
};

const getMapView = async () => {
  const { code, data, because, result, message } =
    await useConnectionStore.request({
      url: "/v2/hccc/current/map-view",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio.value?.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    elNoList.value = [];
    if (data.length > 0) {
      mapData.value = data;
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU_MAP_PAGE")]));
    }
    mapData.value = mapData.value.sort((a, b) => {
      return a.elStatus === "N" ? 1 : -1;
    });
    await initMap();
  } else {
    toast.error(t("ERROR_API"));
  }
};
const renderingMap = async () => {
  if (window.kakao && window.kakao.maps) {
    const container = document.getElementById("map");
    const options = {
      center: new kakao.maps.LatLng(
        mapData.value[0]?.wgslat ?? 37.5665,
        mapData.value[0]?.wgslon ?? 126.978
      ),
      level: 9,
    };
    map = new kakao.maps.Map(container, options);
  } else {
    const script = document.createElement("script");
    /* global kakao */
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${process.env.VUE_APP_KAKAO_MAP_KEY}&libraries=services,clusterer,drawing`;
    document.head.appendChild(script);
    script.onload = () => {
      kakao.maps.load(() => {
        const container = document.getElementById("map");
        const options = {
          center: new kakao.maps.LatLng(
            mapData.value[0]?.wgslat ?? 37.5665,
            mapData.value[0]?.wgslon ?? 126.978
          ),
          level: 9,
        };
        map = new kakao.maps.Map(container, options);
      });
    };
  }
};
onMounted(async () => {
  await renderingMap();
  if (computedPortfolio.value?.userPortfolioMappingId) {
    await getMapView();
  }
});

watch(computedPortfolio, async (newVal, oldVal) => {
  mapData.value = [];
  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    await getMapView();
  }
});
watch(customWindowSelected, () => {
  if (customWindowSelected.value.length == 0) {
    customWindowView.value = false;
  }
});
</script>
<template>
  <div class="lv3MapPage pa-0">
    <div id="map"></div>
    <v-card
      class="customWindow customCard overflow-y-auto"
      ref="customWindow"
      rounded="lg"
      min-width="20vw"
      max-height="40vh"
      v-show="customWindowData.length > 0"
    >
      <div class="test">
        <v-toolbar
          compact="density"
          height="48"
          :color="
            customWindowData.length == 1 &&
            customWindowIdx === 2 &&
            customWindowData[0].elStatus?.toUpperCase() !== 'Y'
              ? '#e42f2f'
              : customWindowData.length > 1 &&
                customWindowIdx === 1 &&
                customWindowData.some(
                  (item) => item.elStatus?.toUpperCase() !== 'Y'
                )
              ? '#e42f2f'
              : customWindowData.length > 1 &&
                customWindowIdx === 2 &&
                customWindowSelected.elStatus?.toUpperCase() !== 'Y'
              ? '#e42f2f'
              : 'primary'
          "
        >
          <v-btn
            v-show="customWindowData.length > 1 && customWindowIdx == 2"
            icon
            color="white"
            @click="(customWindowIdx = 1), (customWindowView = false)"
          >
            <v-icon>mdi-arrow-left-bold</v-icon>
          </v-btn>
          <v-toolbar-title class="largeBoldText">
            <div v-if="customWindowIdx == 1">
              <span class="text-white">{{ customWindowData[0]?.buldNm }} </span>
            </div>
            <div v-if="customWindowIdx == 2">
              <span class="text-white"
                >{{
                  customWindowView
                    ? `${customWindowSelected.buldNm} - ${customWindowSelected.installationPlace}`
                    : `${customWindowData[0]?.buldNm} - ${customWindowData[0]?.installationPlace}`
                }}
              </span>
            </div>
          </v-toolbar-title>

          <v-btn
            icon
            dark
            color="white"
            @click="[(customWindowData = []), (customWindowView = false)]"
          >
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
        <v-divider></v-divider>
        <v-window v-model="customWindowIdx">
          <v-window-item :value="1">
            <v-sheet class="py-2 px-4">
              <v-card
                v-for="item in customWindowData"
                :key="item"
                :value="item"
                class="py-2 px-4 my-4 d-flex customCard"
                @click="
                  [
                    (customWindowSelected = item),
                    (customWindowIdx = 2),
                    (customWindowView = true),
                  ]
                "
              >
                <v-card-subtitle class="pl-0"
                  >{{ item.buldNm }} - {{ item.installationPlace }}
                </v-card-subtitle>
                <v-spacer></v-spacer>
                <v-icon
                  :style="{
                    color: item.elStatus === 'Y' ? '#00C44F' : 'red',
                  }"
                >
                  {{
                    item.elStatus === "Y"
                      ? "mdi-check-circle-outline"
                      : "mdi-alert-circle-check-outline"
                  }}
                </v-icon>
              </v-card>
            </v-sheet>
          </v-window-item>
          <v-window-item :value="2">
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="6" class="py-1" :style="{paddingRight:'5px'}">
                    <v-text-field
                      readonly
                      :label="t('PRJ_NO')"
                      :model-value="
                        customWindowView
                          ? customWindowSelected.prjNo +
                            customWindowSelected.hoNo
                          : customWindowData[0]?.prjNo +
                            customWindowData[0]?.hoNo
                      "
                      hide-details
                      variant="solo"
                      density="compact"
                    >
                    </v-text-field>
                  </v-col>
                  <v-col cols="6" class="py-1" :style="{paddingLeft:'5px'}">
                    <v-text-field
                      readonly
                      :label="t('BULD_NM')"
                      hide-details
                      variant="solo"
                      :model-value="
                        customWindowView
                          ? customWindowSelected.buldNm
                          : customWindowData[0]?.buldNm
                      "
                      density="compact"
                    >
                    </v-text-field>
                  </v-col>
                  <v-col 
                    :cols="(customWindowView ? customWindowSelected?.elStatus.toUpperCase() : customWindowData[0]?.elStatus.toUpperCase()) !== 'Y' ? '4': '6'"
                    :style="{paddingTop:'0', paddingBottom:'0', paddingRight:(customWindowView ? customWindowSelected?.acptDate : customWindowData[0]?.acptDate) ? '0' : '5px'}"
                  >
                    <v-card
                      class="py-2 px-4 my-1 d-flex"
                      :style="{justifyContent:'center', borderTopWidth:'1px'}"
                      @click="
                        [
                          useDataStore.targetMachineData.prjNo = customWindowView ? customWindowSelected.prjNo : customWindowData[0]?.prjNo,
                          useDataStore.targetMachineData.hoNo = customWindowView ? customWindowSelected.hoNo : customWindowData[0]?.hoNo,
                          router.push('/machine/detail'),
                        ]
                      "
                    >
                    <v-card-subtitle 
                        class="pl-0" 
                        :style ="{
                          color: (customWindowView ? customWindowSelected?.elStatus.toUpperCase() : customWindowData[0]?.elStatus.toUpperCase()) !== 'Y'
                              ? 'red'
                              : '#00C44F', 
                            fontWeight:'700',
                            paddingRight:'0px'}"
                        >{{t("EL_INFO")}}
                      </v-card-subtitle>
                    </v-card>
                  </v-col>
                  <v-col 
                    :cols="(customWindowView ? customWindowSelected?.elStatus.toUpperCase() : customWindowData[0]?.elStatus.toUpperCase()) !== 'Y' ? '4': '6'"
                    :style="{paddingTop:'0', paddingBottom:'0', paddingLeft:'5px', paddingRight:'5px'}"
                    v-if="customWindowView ? customWindowSelected?.acptDate : customWindowData[0]?.acptDate"
                  >
                    <v-card
                      class="py-2 px-4 my-1 d-flex"
                      :style="{justifyContent:'center', borderTopWidth:'1px'}"
                      @click="
                        [
                          useDataStore.targetAcptDate = customWindowView ? customWindowSelected?.acptDate : customWindowData[0]?.acptDate,
                          router.push('/service/calendars'),
                        ]
                      "
                    >
                    <v-card-subtitle 
                        class="pl-0" 
                        :style ="{
                          color: (customWindowView ? customWindowSelected?.elStatus.toUpperCase() : customWindowData[0]?.elStatus.toUpperCase()) !== 'Y'
                              ? 'red'
                              : '#00C44F', 
                            fontWeight:'700',
                            paddingRight:'0px'}"
                        >{{t("FAULT_LIST")}}
                      </v-card-subtitle>
                    </v-card>
                  </v-col>
                  <v-col 
                    :cols="(customWindowView ? customWindowSelected?.elStatus.toUpperCase() : customWindowData[0]?.elStatus.toUpperCase()) !== 'Y' ? '4': '6'"
                    :style="{paddingTop:'0', paddingBottom:'0', paddingLeft:(customWindowView ? customWindowSelected?.acptDate : customWindowData[0]?.acptDate) ? '0' : '5px'}"
                  >
                    <v-card
                      class="py-2 px-4 my-1 d-flex"
                      :style="{justifyContent:'center', borderTopWidth:'1px'}"
                      @click="
                        [
                          useDataStore.targetServiceElInfo = {
                            prjNo: customWindowView ? customWindowSelected?.prjNo : customWindowData[0]?.prjNo,
                            hoNo: customWindowView ? customWindowSelected?.hoNo : customWindowData[0]?.hoNo,
                          },
                          dialog = true,
                        ]
                      "
                    >
                      <v-card-subtitle 
                        class="pl-0" 
                        :style ="{
                          color: (customWindowView ? customWindowSelected?.elStatus.toUpperCase() : customWindowData[0]?.elStatus.toUpperCase()) !== 'Y'
                              ? 'red'
                              : '#00C44F', 
                            fontWeight:'700',
                            paddingRight:'0px'}"
                        >{{t("SERVICE_REQUEST")}}
                      </v-card-subtitle>
                    </v-card>
                  </v-col>
                  <v-col cols="12" class="py-1">
                    <v-text-field
                      readonly
                      hide-details
                      :label="t('ADDRESS')"
                      :model-value="
                        customWindowView
                          ? customWindowSelected.address1
                          : customWindowData[0]?.address1
                      "
                      variant="solo"
                      density="compact"
                    >
                    </v-text-field>
                  </v-col>
                  <v-col cols="12" class="py-1">
                    <v-text-field
                      readonly
                      hide-details
                      :label="t('CONTRACT_TOTAL_NUMBER')"
                      :model-value="`EL : ${
                        customWindowView
                          ? customWindowSelected.elLEa
                          : customWindowData[0]?.elLEa
                      }대, ES/MW : ${
                        customWindowView
                          ? customWindowSelected.elTEa -
                            customWindowSelected.elLEa
                          : customWindowData[0]?.elTEa -
                            customWindowData[0]?.elLEa
                      }대 `"
                      variant="solo"
                      density="compact"
                    >
                    </v-text-field>
                  </v-col>
                  <v-col cols="12" class="py-1">
                    <v-text-field
                      readonly
                      hide-details
                      :label="t('CONTRACT_DATE')"
                      :model-value="`${
                        customWindowView
                          ? customWindowSelected.contractStartDt
                            ? customWindowSelected.contractStartDt.replace(
                                /(\d{4})(\d{2})(\d{2})/g,
                                '$1-$2-$3'
                              )
                            : ''
                          : customWindowData[0]?.contractStartDt
                          ? customWindowData[0].contractStartDt.replace(
                              /(\d{4})(\d{2})(\d{2})/g,
                              '$1-$2-$3'
                            )
                          : ''
                      } - ${
                        customWindowView
                          ? customWindowSelected.contractEndDt
                            ? customWindowSelected.contractEndDt.replace(
                                /(\d{4})(\d{2})(\d{2})/g,
                                '$1-$2-$3'
                              )
                            : ''
                          : customWindowData[0]?.contractEndDt
                          ? customWindowData[0].contractEndDt.replace(
                              /(\d{4})(\d{2})(\d{2})/g,
                              '$1-$2-$3'
                            )
                          : ''
                      }`"
                      variant="solo"
                      density="compact"
                    >
                    </v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>
          </v-window-item>
        </v-window>
      </div>
    </v-card>
  </div>

  <teleport to="#mapDashBoard" :disabled="false"> </teleport>
  <teleport to="#extra-modal" :disabled="false">
    <div class="mapDashBoard" :style="{ width: mdAndDown ? '376px' : 'auto' }">
      <v-container class="px-3" fluid>
        <v-row>
          <v-col md="12" lg="6" class="py-0 px-1">
            <Info />
            <FacilityStatus />
            <FailHandleStatus />
          </v-col>
          <v-col
            md="12"
            lg="6"
            :class="mdAndDown ? 'pt-3' : ''"
            class="py-0 pl-1 pr-0"
          >
            <ConnettingService :elNoList="elNoList" />
            <AiDiagnosis />
            <Inquiry />
          </v-col>
        </v-row>
      </v-container>
    </div>
    <!-- <div class="modal">abvcvcdf</div> -->
  </teleport>
  <ServiceRequestModal v-model="dialog" v-if="dialog"></ServiceRequestModal>
</template>
<style lang="scss">
.close-fullscreen {
  display: none;
}
.mapDashBoard {
  position: fixed;

  width: auto;
  height: calc(100vh - 82px);
  top: 72px;
  overflow-y: auto;
  right: 0;
  z-index: 1000;
}
.lv3MapPage {
  position: relative;
  display: flex;
}

#map {
  width: 100%;
  height: calc(100vh - 64px);
  .customCluster {
    height: 40px;
    width: 124px;
    display: flex;
    justify-content: center;
    align-items: center;
    // justify-content: space-around;
    border-radius: 20px;

    color: white;
    .iconBox {
      color: #0d99ff;
      justify-content: center;
      width: 40px;
      height: 40px;
      left: 0;
      top: 0;
      //   position: absolute;
      display: flex;
      align-items: center;
      background-color: white;
      /* flex-basis: 30%; */
      border-radius: 50%;
      justify-content: center;
    }
    .customMarkerText {
      flex-grow: 1;
      margin-left: -10px;
      text-align: center;
      font-size: 16px;
      font-weight: 700;
    }
    &.success {
      background-color: #6ae24f;
      // animation: success 1500ms infinite;
    }

    &.error {
      background-color: #e42f2f;
      animation: error 1500ms infinite;
    }
  }
  .customMarker {
    width: 40px;
    height: 50px;
    position: absolute;
    bottom: 10px;
    &::after {
      content: "";
      position: absolute;
      bottom: 0;
      left: 50%;
      width: 0;
      height: 0;
      border: 15px solid transparent;
      border-bottom: 0;
      transform: translateX(-50%);
    }
    &.successMarker::after {
      border-top-color: #6ae24f;
    }
    &.errorMarker::after {
      border-top-color: red;
    }
    .iconBox {
      color: #0d99ff;
      justify-content: center;
      width: 40px;
      height: 40px;
      left: 0;

      top: 0;
      //   position: absolute;
      display: flex;
      align-items: center;
      background-color: white;
      /* flex-basis: 30%; */
      border-radius: 50%;
      justify-content: center;
      &.successIcon {
        background-color: #6ae24f;
        box-shadow: 4px 10px 5px 0px rgb(0 0 0 / 2%);

        color: white;
      }
      &.errorIcon {
        color: white;
        background-color: red;
        box-shadow: 4px 10px 5px 0px rgba(72, 68, 68, 0.301);
      }
    }
  }
  .customWindow {
    position: absolute;
    bottom: 40px;
    left: -20vw;
    z-index: 1001;
    overflow: hidden;
    .prjNoInput {
      .v-field__field {
        border: 1px solid #00c44f;
        border-radius: 4px;
        input {
          cursor: pointer;
        }
      }
    }
  }
  @keyframes warning {
    0% {
      box-shadow: #eeb148 0 0 0 0;
    }
    75% {
      box-shadow: #ff69b400 0 0 0 16px;
    }
  }
  @keyframes error {
    0% {
      box-shadow: #e42f2f 0 0 0 0;
    }
    75% {
      box-shadow: #ff69b400 0 0 0 16px;
    }
  }
  @keyframes success {
    0% {
      box-shadow: #6ae24f 0 0 0 0;
    }
    75% {
      box-shadow: #ff69b400 0 0 0 16px;
    }
  }
}

.MapPagedBox {
  overflow-x: hidden;
  overflow-y: auto;
  color: #fff;
  top: 4px;
  height: calc(100vh - 64px);
  right: 6px;

  z-index: 1;
  &::-webkit-scrollbar {
    width: 12px;
  }
  &::-webkit-scrollbar * {
    background: transparent;
  }
  &::-webkit-scrollbar-thumb {
    background: rgba(65, 65, 65, 1);
    border-radius: 10px;
  }
  .contractScroll {
    color: black;
    position: fixed;
    border-radius: 50%;
    width: 24px;
    height: 24px;

    bottom: 5%;
    right: 19%;
  }
  .scrollDownAnime {
    animation: down 1.5s infinite;
  }
  .scrollUpAnime {
    animation: up 1.5s infinite;
  }
}
@media screen and (max-width: 1264px) and (min-width: 320px) {
  .MapPagedBox {
    padding: 4px;
    position: initial;
    width: 100%;
    flex-shrink: 0;
    flex-basis: 390px;
    top: 0;
  }
  #map {
    flex-grow: 1;
  }
}
@media screen and (min-width: 1265px) {
  .MapPagedBox {
    position: absolute;
  }
}

@keyframes down {
  0% {
    transform: translateY(0);
    box-shadow: #8d98d9 0 0 0 0;
  }
  20% {
    transform: translateY(15px);
    box-shadow: #ff69b400 0 0 0 24px;
  }
  40% {
    transform: translateY(0);
  }
}
@keyframes up {
  0% {
    transform: translateY(0px);
    box-shadow: #8d98d9 0 0 0 0;
  }
  20% {
    transform: translateY(-15px);
    box-shadow: #ff69b400 0 0 0 24px;
  }
  40% {
    transform: translateY(0px);
  }
}
</style>
