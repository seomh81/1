<script setup>
import SampleMenuView from "./SampleMenuView.vue";
import { onMounted, ref, computed } from "vue";
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
});
const error = ref("red");
const initMap = () => {
  const container = document.getElementById("map");
  const options = {
    center: new kakao.maps.LatLng(37.4995645, 127.0314101),
    level: 8,
  };

  //지도 객체를 등록합니다.
  //지도 객체는 반응형 관리 대상이 아니므로 initMap에서 선언합니다.
  const map = new kakao.maps.Map(container, options);

  const a = [14, 24];
  const data = [
    {
      title: "포스코빌딩",
      lat: 37.4934585,
      lang: 127.0252199,
      date: "2022-05-02",
      adress: "서울 특별시 테헤란로 423",
      part: {
        part1: "normal",
        part2: "normal",
        part3: "normal",
        part4: "normal",
        part5: "normal",
        part6: "error",
      },
    },
    {
      title: "원빌딩",

      lat: 37.4995645,
      lang: 127.0314101,
      date: "2022-11-02",
      adress: "서울 특별시 테헤란로 123",
      part: {
        part1: "normal",
        part2: "normal",
        part3: "normal",
        part4: "normal",
        part5: "normal",
        part6: "normal",
        part7: "normal",
        part8: "normal",
        part9: "normal",
        part10: "normal",
        part11: "normal",
        part12: "normal",
        part13: "normal",
        part14: "normal",
        part15: "normal",
        part16: "normal",
        part17: "normal",
        part18: "normal",
        part19: "normal",
      },
    },
    {
      title: "여삼빌딩",

      lat: 37.4965345,
      lang: 127.0614301,
      date: "2022-05-23",
      adress: "서울 특별시 테헤란로 2223",
      part: {
        part1: "error",
        part2: "error",
        part3: "error",
        part4: "normal",
        part5: "normal",
        part6: "normal",
        part7: "normal",
        part8: "normal",
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
  for (let i = 0; i < data.length; i++) {
    let position = new kakao.maps.LatLng(data[i].lat, data[i].lang);
    let normal = 0;
    let error = 0;

    Object.values(data[i].part).forEach((x) => {
      if (x === "normal") {
        normal++;
      } else {
        error++;
      }
    });
    let content = `
    ${error == 0 ? `<div class="live"> ${normal} </div>` : ""}

      ${error !== 0 ? `<div class="live"> ${normal} </div>` : ""}
      ${error !== 0 ? `<div class="live"> ${error} </div>` : ""}`;

    let content2 = `<div class="wrap">
      <div class="info">
           <div class="title">
           ${data[i].title}
             <div class="close" @click="closeOverlay" title="닫기"></div>
      </div>
            <div class="body">
                <div class="desc">
                  <div class="ellipsis"> ${data[i].adress}<div>
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
  }

  var iwContent =
      '<div style="padding:5px;">Hello World! <br><a href="https://map.kakao.com/link/map/Hello World!,33.450701,126.570667" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/Hello World!,33.450701,126.570667" style="color:blue" target="_blank">길찾기</a></div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
    iwPosition = new kakao.maps.LatLng(37.4965345, 127.0614301); //인포윈도우 표시 위치입니다

  // 인포윈도우를 생성합니다
  var infowindow = new kakao.maps.InfoWindow({
    position: iwPosition,
    content: iwContent,
  });
  infowindow.open(map, );

  let clusterer = new kakao.maps.MarkerClusterer({
    map: map,
    markers: customMakers,
    averageCenter: true,
    minLevel: 8,
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
};
</script>
<template>
  <v-container class="sample-test-container">
    <SampleMenuView />
    <v-card class="mapParent">
      <div id="map"></div>
    </v-card>
    <span class="live"> </span>
    asdas
  </v-container>
</template>
<style>
.live {
  height: 24px;
  width: 24px;
  border-radius: 50%;
  background-color: blue;
  animation: pulse 1500ms infinite;
}

@keyframes pulse {
  0% {
    box-shadow: #ff69b4 0 0 0 0;
  }
  75% {
    box-shadow: #ff69b400 0 0 0 16px;
  }
}
.mapParent {
  width: 100%;
  height: 50vh;
}
#map {
  width: 100%;
  height: 100%;
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
}
.wrap * {
  padding: 0;
  margin: 0;
}
.wrap .info {
  width: 286px;
  height: 120px;
  border-radius: 5px;
  border-bottom: 2px solid #ccc;
  border-right: 1px solid #ccc;
  overflow: hidden;
  background: #fff;
}
.wrap .info:nth-child(1) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}
.info .title {
  padding: 5px 0 0 10px;
  height: 30px;
  background: rgb(240, 240, 240);
  border-bottom: 1px solid #ddd;
  font-size: 16px;
  font-weight: bold;
}

.info .body {
  position: relative;
  overflow: hidden;
}
.info .desc {
  position: relative;
  margin: 13px 0 0 10px;
  height: 75px;
}
.desc .ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.desc .jibun {
  font-size: 11px;
  color: #888;
  margin-top: -2px;
}

.info .link {
  color: #5085bb;
}
</style>
