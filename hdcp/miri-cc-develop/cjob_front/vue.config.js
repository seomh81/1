const { defineConfig } = require('@vue/cli-service')

const IS_LIVE = process.env.NODE_ENV === "production";
const MIN_EXT = IS_LIVE ? ".min" : "";

module.exports = defineConfig({
  transpileDependencies: true,
  pluginOptions: {
    vuetify: {},
    i18n: {
      locale: 'ko',
      fallbackLocale: 'en',
      localeDir: 'locales',
      enableLegacy: false,
      runtimeOnly: false,
      compositionOnly: false,
      fullInstall: true
    }
  },
  css: {
    extract: {
      filename: `css/[name]${MIN_EXT}.css`,
      chunkFilename: `css/chunk/[name]${MIN_EXT}.css`,
    },
  },
  chainWebpack: (config) => {
    config.output
      .filename(`js/[name]${MIN_EXT}.js`)
      .chunkFilename(`js/chunk/[name]${MIN_EXT}.js`);
    config.when(!IS_LIVE, (config) => {
      config.devtool("source-map");
    });
    config.optimization.merge({
      splitChunks: {
        cacheGroups: {
          defaultVendors: {
            name: "chunk/vendors",
          },
          common: {
            name: "chunk/common",
          },
        },
      },
    });
  }
})

