export const environment = {
  production: false,
  useMockData: false,
  apiBaseUrl: 'http://localhost:8081',
  devUserSub: 'seed-user-alice',
  // Not secrets — a Cognito user pool/app client ID is meant to be embedded in
  // frontend code. This is a public client (no client secret was generated).
  cognito: {
    region: 'us-east-1',
    userPoolId: 'us-east-1_mOAavo9Gm',
    clientId: '4ehp88rga37ivonrq4qsonavdl',
  },
};
