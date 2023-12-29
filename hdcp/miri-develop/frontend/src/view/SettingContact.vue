<script setup>
import { UserSettingMenu } from "@/component/Template.js";
import { userStore } from "@/store/UserStore";
import { ref, onMounted, reactive, watch, computed } from "vue";
import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";
import { connectionStore } from "@/store/ConnectionStore";
const useUserStore = userStore();
const toast = useToast();

const useConnectionStore = connectionStore();
const contactList = ref([]);
const model = ref();
const model2 = ref();
const { t } = useI18n();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);

const engineerList = ref([]);
const getlist = async () => {
  const { code, result, data, because, message } =
    await useConnectionStore.request({
      url: "/v2/contract/current/contact/list",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio.value.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    if (data.length > 0) {
      data.forEach((item) => {
        if (!!item == true && item.mob) {
          item.mob = item.mob
            .replace(/[^0-9]/g, "")
            .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
            // eslint-disable-next-line
            .replace(/(\-{1,2})$/g, "");
        }
      });
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MY_ACCOUNT")]));
    }
    contactList.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("MY_ACCOUNT")]));
  }
};
const getEngineer = async () => {
  const { code, result, data, because, message } =
    await useConnectionStore.request({
      url: "/v2/contract/current/contact/engineer",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio.value.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    if (data.length > 0) {
      data.forEach((item) => {
        if (item.mob) {
          item.mob = item.mob
            .replace(/[^0-9]/g, "")
            .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
            // eslint-disable-next-line
            .replace(/(\-{1,2})$/g, "");
        }
      });
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MY_ENGINEER")]));
    }
    engineerList.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("MY_ENGINEER")]));
  }
};

watch(computedPortfolio, async (newVal, oldVal) => {
  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    getlist();
    getEngineer();
  }
});
onMounted(() => {
  if (computedPortfolio.value?.userPortfolioMappingId) {
    getlist();
    getEngineer();
  }
});
</script>
<template>
  <v-container class="settingMenu" fluid>
    <v-row justify="center">
      <v-col sm="4" lg="3" class="pr-0">
        <v-card class="settingMenuCard menuCard rounded-s-lg">
          <UserSettingMenu></UserSettingMenu>
        </v-card>
      </v-col>
      <v-col sm="8" lg="7" class="pl-0">
        <v-card
          class="settingContentCard contentCard rounded-e-lg"
          border="none"
        >
          <v-toolbar class="contentToolbar" color="transparent">
            <v-toolbar-title>{{ t("MY_ACCOUNT") }}</v-toolbar-title>
          </v-toolbar>
          <v-container class="contentInner overflow-y-auto">
            <v-slide-group
              v-model="model"
              class="pa-4 custom-slide-group"
              center-active
              show-arrows="always"
            >
              <v-slide-group-item
                v-slot="{ toggle }"
                v-for="item in contactList"
                :key="item"
              >
                <v-sheet @click="toggle" class="ma-4" width="240" height="120">
                  <v-card-title> {{ item?.nm }} </v-card-title>

                  <v-card-subtitle>
                    {{ item?.dispStr }}
                    <v-tooltip activator="parent" location="top">
                      {{ item?.dispStr }}
                    </v-tooltip>
                  </v-card-subtitle>
                  <v-card-text>{{
                    item?.mob ? item.mob : "데이터 없음"
                  }}</v-card-text>
                </v-sheet>
              </v-slide-group-item>
            </v-slide-group>
          </v-container>
          <v-toolbar class="contentToolbar" color="transparent">
            <v-toolbar-title>{{ t("MY_ENGINEER") }}</v-toolbar-title>
          </v-toolbar>
          <v-container class="contentInner overflow-y-auto">
            <v-slide-group
              v-model="model2"
              class="pa-4 custom-slide-group"
              center-active
              show-arrows="always"
            >
              <v-slide-group-item
                v-slot="{ toggle }"
                v-for="item in engineerList"
                :key="item"
              >
                <v-sheet @click="toggle" class="ma-4" width="240" height="120">
                  <v-card-title> {{ item?.nm }} </v-card-title>
                  <v-card-subtitle>
                    {{ item?.dispStr }}
                    <v-tooltip activator="parent" location="top">
                      {{ item?.dispStr }}
                    </v-tooltip></v-card-subtitle
                  >
                </v-sheet>
              </v-slide-group-item>
            </v-slide-group>
          </v-container>

          <v-toolbar class="contentToolbar" color="transparent">
            <v-toolbar-title>{{ t("SERVICE_CENTER") }}</v-toolbar-title>
          </v-toolbar>
          <v-container class="contentInner ml-6">
            <v-sheet class="ma-4 ml-15" width="240" height="120">
              <v-card-title> 현대 고객케어센터 </v-card-title>
              <v-card-subtitle> 1577-0603 </v-card-subtitle>
            </v-sheet>
          </v-container>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
<style scoped lang="scss">
.custom-slide-group .v-slide-group__prev,
.custom-slide-group .v-slide-group__next {
  display: inherit !important;
  opacity: 1 !important;
}
</style>
