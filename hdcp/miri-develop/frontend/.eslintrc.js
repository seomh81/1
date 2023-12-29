module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: ["plugin:vue/vue3-essential", "eslint:recommended"],

  parser: "vue-eslint-parser",
  parserOptions: {
    parser: "espree",
    ecmaVersion: 2022,
    sourceType: "module",
  },
  rules: {
    "vue/multi-word-component-names": "off",
    "no-unused-vars": "off",
    "vue/no-reserved-component-names": [
      "error",
      {
        disallowVueBuiltInComponents: true,
        disallowVue3BuiltInComponents: true,
      },
    ],
    "vue/no-use-v-if-with-v-for": "off",

    "no-console": process.env.NODE_ENV === "production" ? "off" : "warn",
    "no-debugger": process.env.NODE_ENV === "production" ? "off" : "warn",
  },
};
