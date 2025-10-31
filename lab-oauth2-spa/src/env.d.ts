declare namespace NodeJS {
  interface ProcessEnv {
    NODE_ENV: string;
    VUE_ROUTER_MODE: 'hash' | 'history' | 'abstract' | undefined;
    VUE_ROUTER_BASE: string | undefined;
    LAB_TENANT_ID:string;
    LAB_OAUTH2_SPA_CLIENT_ID: string;
    LAB_OAUTH2_RESOURCE_SERVER_CLIENT_ID: string;
    LAB_API_BASE_URL: string;
  }
}
