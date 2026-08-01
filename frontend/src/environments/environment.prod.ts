export const environment = {
  production: true,
  useMockData: false,
  apiBaseUrl: '',
  devUserSub: '',
  // Same pool as dev for now — Phase 10 (Terraform) will give prod its own pool.
  cognito: {
    region: 'us-east-1',
    userPoolId: 'us-east-1_mOAavo9Gm',
    clientId: '4ehp88rga37ivonrq4qsonavdl',
  },
};
