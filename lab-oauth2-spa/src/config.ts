

interface AppConfig {
  apiBaseUrl: string;
  oauth2: {
    tenantId: string;
    clientId: string;
    resourceServerClientId: string;
  };
}

export const appConfig: AppConfig = {
  apiBaseUrl: process.env.LAB_API_BASE_URL || '',
  oauth2: {
    tenantId: process.env.LAB_TENANT_ID || '',
    clientId: process.env.LAB_OAUTH2_SPA_CLIENT_ID || '',
    resourceServerClientId:
      process.env.LAB_OAUTH2_RESOURCE_SERVER_CLIENT_ID || '',
  },
};

console.log("### appConfig.apiBaseUrl: " + appConfig.apiBaseUrl);
console.log("### appConfig.oauth2.tenantId: " + appConfig.oauth2.tenantId);
console.log("### appConfig.oauth2.clientId: " + appConfig.oauth2.clientId);
console.log("### appConfig.oauth2.resourceServerClientId: " + appConfig.oauth2.resourceServerClientId);
