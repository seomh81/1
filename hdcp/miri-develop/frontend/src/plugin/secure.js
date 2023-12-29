import SecureLS from "secure-ls";
export const ls = new SecureLS({
  encryptionSecret: `${process.env.VUE_APP_SECURE_KEY}`,
  encodingType: "rc4",
  isCompression: true,
});
