<script setup>
import { onMounted, ref, computed } from "vue";
import Info from "@/component/Dashboard/Info";

import FacilityStatus from "@/component/Dashboard/FacilityStatus.vue";
import Notice from "@/component/Dashboard/Notice.vue";
import ConnettingService from "@/component/Dashboard/ConnettingService.vue";
import AiDiagnosis from "@/component/Dashboard/AiDiagnosis.vue";
import Inquiry from "@/component/Dashboard/Inquiry.vue";
const error = ref("red");
const initMap = async () => {
  const container = document.getElementById("map");
  const options = {
    center: new kakao.maps.LatLng(37.4995645, 127.0314101),
    level: 9,
  };

  //지도 객체를 등록합니다.
  //지도 객체는 반응형 관리 대상이 아니므로 initMap에서 선언합니다.
  const map = new kakao.maps.Map(container, options);

  const data = [
    {
      title: "포스코빌딩",
      lat: 37.4934585,
      lang: 127.0252199,
      date: "2022-05-02",
      address: "서울 특별시 테헤란로 423",
      type: "",
      part: {
        part1: "normal",
        part2: "normal",
        part3: "normal",
        part4: "normal",
        part5: "normal",
      },
    },
    {
      title: "원빌딩",

      lat: 37.4995645,
      lang: 127.0314101,
      date: "2022-11-02",
      address: "서울 특별시 테헤란로 123",
      type: "",

      part: {
        part1: "normal",
        part2: "normal",
        part3: "error",
        part4: "error",
        part5: "error",
      },
    },
    {
      title: "NAVER D2 Startup Factory",

      lat: 37.4965345,
      lang: 127.0614301,
      date: "2022-05-23",
      address: "서울 서초구 서초대로74길 14 더에셋빌딩 18층",
      type: "",

      part: {
        part1: "error",
        part2: "error",
        part3: "error",
        part4: "error",
        part5: "normal",
      },
    },
  ];
  // 데이터에서 좌표 값을 가지고 마커를 표시합니다
  // 마커 클러스터러로 관리할 마커 객체는 생성할 때 지도 객체를 설정하지 않습니다
  //   var markers = areas.map((x) => {
  //     return new kakao.maps.Marker({
  //       position: new kakao.maps.LatLng(x.lat, x.lang),
  //       title: x.type,
  //     });
  //   });
  // 클러스터러에 마커들을 추가합니다
  const b = data.filter((x) => {
    Object.values(x).filter((x) => {});
  });
  const closeOverlay = (e) => {
    overlay.setMap(null);
    console.log(e);
  };

  let overlay = [];
  let customMakers = [];
  var geocoder = new kakao.maps.services.Geocoder();
  let clusterer = new kakao.maps.MarkerClusterer({
    map: map,
    averageCenter: true,
    minLevel: 7,
    styles: [
      {
        // calculator 각 사이 값 마다 적용될 스타일을 지정한다
        width: "40px",
        height: "40px",
        background: "rgba(51, 204, 255, 0.9)",
        borderRadius: "50%",
        color: "#fff",
        textAlign: "center",
        fontWeight: "bold",
        lineHeight: "40px",
      },
    ],
  });
  for (let i = 0; i < data.length; i++) {
    await geocoder.addressSearch(data[i].address, function (result, status) {
      // 정상적으로 검색이 완료됐으면
      if (status === kakao.maps.services.Status.OK) {
        let position = new kakao.maps.LatLng(result[0].y, result[0].x);

        let checkMachine = {};
        Object.values(data[i].part).forEach((x) => {
          checkMachine[x] = (checkMachine[x] || 0) + 1;
        });
        let content = `
    ${
      !!checkMachine.error == false || checkMachine.error < 2
        ? `<div class="customMaker success"> 
            <div class="iconBox">
                <span class="material-icons">
elevator
</span>                </div>
                <p class="customMakerText">
                    ${checkMachine.normal}
                    </p>

             </div>`
        : checkMachine.error > 1 && checkMachine.error < 4
        ? `<div class="customMaker warning"> 
            <div class="iconBox">
                <span class="material-icons">
elevator
</span>                </div>
                <p class="customMakerText">
                    ${checkMachine.normal} / ${checkMachine.error}
                </p>
                </div>`
        : checkMachine.error > 3
        ? `<div class="customMaker error"> 
                    <div class="iconBox">
                        <span class="material-icons">
elevator
</span>                </div>
                <p class="customMakerText">
                ${checkMachine.normal} / ${checkMachine.error}
                </p>
                </div>`
        : ""
    }`;

        let content2 = `<div class="wrap">
      <div class="info">
           <div class="title">
           ${data[i].title}
             <div class="close" @click="closeOverlay" title="닫기"></div>
      </div>
            <div class="body">
                <div class="desc">
                  <div class="ellipsis"> ${data[i].address}<div>
                    <div class="jibun ellipsis">(우) 63309 (지번) 영평동 2181</div>
                    <div>${data[i].date}</div>
                </div>
            </div>
        </div>
    </div>`;
        // 커스텀 오버레이를 생성합니다
        customMakers.push(
          new kakao.maps.CustomOverlay({
            map: map,
            position: position,
            content: content,
            title: data[i].title,
          })
        );
        overlay.push(
          new kakao.maps.CustomOverlay({
            content: content2,
            position: position,
            title: data[i].title,
          })
        );
        clusterer.addMarkers(customMakers);
        customMakers.forEach((item) => {
          item.a.addEventListener("mouseover", () => {
            overlay.forEach((x) => {
              if (item.n.La == x.n.La) x.setMap(map);
            });
          });
          item.a.addEventListener("mouseout", () => {
            overlay.forEach((c) => {
              c.setMap(null);
            });
          });
        });
      }
    });
  }

  var iwContent =
      '<div style="padding:5px;">Hello World! <br><a href="https://map.kakao.com/link/map/Hello World!,33.450701,126.570667" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/Hello World!,33.450701,126.570667" style="color:blue" target="_blank">길찾기</a></div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
    iwPosition = new kakao.maps.LatLng(37.4965345, 127.0614301); //인포윈도우 표시 위치입니다

  // 인포윈도우를 생성합니다
  //   var infowindow = new kakao.maps.InfoWindow({
  //     position: iwPosition,
  //     content: iwContent,
  //   });
  //   infowindow.open(map);
};
const changeArrow = ref(false);
const contractListBox = ref(null);

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap();
  } else {
    const script = document.createElement("script");
    /* global kakao */
    script.onload = () => kakao.maps.load(initMap);
    script.src =
      "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=4af7528468f829af37efd365338b4b0f&libraries=services,clusterer,drawing";
    document.head.appendChild(script);
  }

  console.log(contractListBox);
  document.querySelector(".dashboarddBox").addEventListener("scroll", (e) => {
    if (e.target.scrollHeight - e.target.clientHeight == e.target.scrollTop) {
      changeArrow.value = true;
    } else {
      changeArrow.value = false;
    }
  });
});
var doc = document.documentElement;
// 전체화면 설정
const openFullScreenMode = () => {
  if (doc.requestFullscreen) doc.requestFullscreen();
  else if (doc.webkitRequestFullscreen)
    // Chrome, Safari (webkit)
    doc.webkitRequestFullscreen();
  else if (doc.mozRequestFullScreen)
    // Firefox
    doc.mozRequestFullScreen();
  else if (doc.msRequestFullscreen)
    // IE or Edge
    doc.msRequestFullscreen();
  //   document.querySelector(".fullscreen").hide();
  //   document.querySelector(".close-fullscreen").show();
};
// 전체화면 해제
const closeFullScreenMode = () => {
  if (document.exitFullscreen) document.exitFullscreen();
  else if (document.webkitExitFullscreen)
    // Chrome, Safari (webkit)
    document.webkitExitFullscreen();
  else if (document.mozCancelFullScreen)
    // Firefox
    document.mozCancelFullScreen();
  else if (document.msExitFullscreen)
    // IE or Edge
    document.msExitFullscreen();
  document.querySelector(".fullscreen").show();
  document.querySelector(".close-fullscreen").hide();
};

</script>
<template>
  <!-- <a class="fullscreen" href="#" title="전체화면" @click="openFullScreenMode">
    전체화면
  </a>
  <a
    class="close-fullscreen"
    href="#"
    title="창모드"
    @click="closeFullScreenMode"
  >
    창모드
  </a> -->
  <div class="lv3DashBoard pa-0">
    <div id="map"></div>
    <div class="py-0 pr-1 dashboarddBox">
      <v-container class="px-3">
        <v-row>
          <v-col md="12" lg="6" class="py-0 px-1">
            <Info />
            <FacilityStatus />
            <Notice />
          </v-col>
          <v-col md="12" lg="6" class="py-0 pl-1 pr-0">
            <ConnettingService />
            <AiDiagnosis />
            <Inquiry />
          </v-col>
        </v-row>
      </v-container>
    </div>

    <!-- <div
          class="contractScroll"
          :class="{
            scrollDownAnime: changeArrow == false,
            scrollUpAnime: changeArrow == true,
          }"
        >
          <i
            v-if="changeArrow == false"
            class="fa-solid fa-caret-down"
            :style="{ fontSize: '40px', opacity: '0.7' }"
          ></i>
          <i
            v-if="changeArrow == true"
            class="fa-solid fa-caret-up"
            :style="{ fontSize: '40px', opacity: '0.7' }"
          ></i>
        </div> -->
  </div>
  <!-- </v-card> -->
</template>
<style lang="scss">
.close-fullscreen {
  display: none;
}

.lv3DashBoard {
  position: relative;
  display: flex;
}
#map {
  width: 100%;
  height: calc(100vh - 64px);
  .customMaker {
    height: 40px;
    width: 124px;
    display: flex;
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
    .customMakerText {
      flex-grow: 1;
      margin-left: -10px;
      text-align: center;
      font-size: 16px;
      font-weight: 700;
    }
  }
  .success {
    background-color: #6ae24f;
    animation: success 1500ms infinite;
  }
  .warning {
    background-color: #eeb148;
    animation: warning 1500ms infinite;
  }
  .error {
    background-color: #e42f2f;
    animation: error 1500ms infinite;
  }
  @keyframes success {
    0% {
      box-shadow: #6ae24f 0 0 0 0;
    }
    75% {
      box-shadow: #ff69b400 0 0 0 16px;
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

  .mapParent {
    width: 100%;
    height: 50vh;
  }

  .markerCustome {
    display: flex;
    justify-content: center;
    height: 40px;
    color: white;
  }
  .normalPart {
    width: 40px;
    border-radius: 15%;
    border: 1px solid black;
    text-align: center;
    line-height: 40px;

    flex-grow: 1;
    background-color: #00c853;
  }
  .double {
    border-radius: 15% 0 0 15%;
  }
  .errorPart {
    width: 40px;
    border: 1px solid black;
    margin-left: 0.5px;
    border-radius: 0 15% 15% 0;
    flex-grow: 1;
    line-height: 40px;
    text-align: center;

    background-color: #e64a19;
  }
  .wrap {
    position: absolute;
    left: 0;
    bottom: 40px;
    width: 288px;
    height: 132px;
    margin-left: -144px;
    text-align: left;
    overflow: hidden;
    font-size: 12px;
    font-family: "Malgun Gothic", dotum, "돋움", sans-serif;
    line-height: 1.5;
    * {
      padding: 0;
      margin: 0;
    }
    .info {
      width: 286px;
      height: 120px;
      border-radius: 5px;
      border-bottom: 2px solid #ccc;
      border-right: 1px solid #ccc;
      overflow: hidden;
      background: #fff;
      &:nth-child(1) {
        border: 0;
        box-shadow: 0px 1px 2px #888;
      }
      .link {
        color: #5085bb;
      }
      .title {
        padding: 5px 0 0 10px;
        height: 30px;
        background: rgb(240, 240, 240);
        border-bottom: 1px solid #ddd;
        font-size: 16px;
        font-weight: bold;
      }
      body {
        position: relative;
        overflow: hidden;
      }
      .desc {
        position: relative;
        margin: 13px 0 0 10px;
        height: 75px;
        .ellipsis {
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        .jibun {
          font-size: 11px;
          color: #888;
          margin-top: -2px;
        }
      }
    }
  }
}
@media screen and (max-width: 1264px) and (min-width: 320px) {
  .dashboarddBox {
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
  .dashboarddBox {
    position: absolute;
  }
}

.dashboarddBox {
  overflow-x: hidden;
  overflow-y: auto;
  color: #fff;
  top: 4px;
  height: calc(100vh - 64px);
  right: 6px;

  z-index: 10;
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
