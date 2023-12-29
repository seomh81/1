<script setup>
import { connectionStore } from "@/store/ConnectionStore";
import { useToast, TYPE } from "vue-toastification";
import { userStore } from "@/store/UserStore";
import { uiStore } from "@/store/UiStore";
import { useI18n } from "vue-i18n";
import { ref, onMounted, computed, watch, reactive } from "vue";
const props = defineProps(["modelValue", "data"]);
const { t } = useI18n();
const emits = defineEmits(["update:modelValue"]);
const computedOpens = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emits("update:modelValue", value);
  },
});

const useUiStore = uiStore();
const toast = useToast();
const useUserStore = userStore();
const useConnectionStore = connectionStore();

const failDetailInfo = ref([]);

const getReceptionDetails = async () => {
  console.log(props.data.acptNo);

  const { data, result, because, message, code } =
    await useConnectionStore.request({
      url: "/v2/hccc/current/targetReception",
      method: "post",
      queryparam: {
        targetAcptNo:props.data.acptNo
      },
    });
  if (code == 200) {
    if(data.length > 0)
    {
      data.forEach((item) => {
        failDetailInfo.value = item;
      });
    }
    else
    {
      emits('update:modelValue', false);
    }
    
  }
};

getReceptionDetails();

</script>
<template>
  <v-dialog v-model="computedOpens" persistent width="30vw">
    <v-card rounded="lg" elevation="4" class="py-3">
      <div style="display:flex;justify-content: space-between;">
        <v-card-subtitle class="py-4">{{ t("FAULT_DETAIL") }}
        </v-card-subtitle>
        <v-btn 
          icon="mdi-close"
          :style="{margin:'0 10px 0 0'}"
          @click="[
            emits('update:modelValue', false),
          ]"
        ></v-btn>
      </div>
      <v-container>
        <v-row>
          <v-col cols="3" class="justify-center d-flex align-center">
            {{ t("MAP_FILED_NAME") }}
          </v-col>
          <v-col cols="9">
            {{failDetailInfo.prjNm}}
          </v-col>
          <v-col cols="3" class="justify-center d-flex align-center">{{
            t("PRJ_NO")
          }}</v-col>
          <v-col cols="9">
            {{failDetailInfo.carNo}}
          </v-col>
          <v-col cols="3" class="justify-center d-flex align-center">{{
            t("REGISTER_TYPE")
          }}</v-col>
          <v-col cols="9">
            {{failDetailInfo.conslMcdNm}}<br>
            {{failDetailInfo.conslScdNm}}
          </v-col>
          <v-col 
            cols="3" 
            class="justify-center d-flex align-center"
            v-if="props.data.status == '완료' ? true : false"
          >
            {{ t("FAIL_PART") }}
          </v-col>
          <v-col cols="9" v-if="props.data.status == '완료' ? true : false">
            {{failDetailInfo.bkdnLocLcdNm}}<br>
            {{failDetailInfo.bkdnLocMcdNm}}<br>
            {{failDetailInfo.bkdnLocScdNm}}
          </v-col>
          <v-col 
            cols="3" 
            class="justify-center d-flex align-center"
            v-if="props.data.status == '완료' ? true : false"
          >
            {{t("FAIL_REASON")}}
          </v-col>
          <v-col cols="9" v-if="props.data.status == '완료' ? true : false">
            {{failDetailInfo.bkdnCauCdNm}}
          </v-col>
          <v-col 
            cols="3" 
            class="justify-center d-flex align-center"
            v-if="props.data.status == '완료' ? true : false"
          >
            {{t("PROCESS_RESULT")}}
          </v-col>
          <v-col cols="9" v-if="props.data.status == '완료' ? true : false">
            {{failDetailInfo.procRsltCdNm}}
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-dialog>
</template>
