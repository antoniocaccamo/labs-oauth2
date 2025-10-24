<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated class="bg-primary text-white cust">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" v-if="isAuthenticated" />
        <q-avatar square color="white" >
          <img  src="logo.png" style="width:min-content">
        </q-avatar>
        <q-toolbar-title>{{ $t('home.title') }}
          <span class="text-subtitle2 q-ml-md" v-if="isAuthenticated" >{{ $t('home.welcome', { name: oidcStore.currentUsername }) }}</span>
        </q-toolbar-title>

        <!-- <q-btn dense square icon="history" class="q-ml-md" v-if="isAuthenticated " to="/thread-list">
           <q-badge color="deep-orange-14" floating>{{ threadStore.numberOfThreads }}</q-badge>
          <q-tooltip>{{ $t('threads.threads') }}</q-tooltip>
        </q-btn> -->

        <div class="q-pa-md q-gutter-sm">
          <q-btn-dropdown
            class="glossy"
            color="deep-orange-14"
            dropdown-icon="settings"
            v-if="isAuthenticated"
          >
            <div class="row no-wrap q-pa-md">

              <div class="column items-center">
                <q-avatar size="64px">
                  <img src="https://cdn.quasar.dev/img/boy-avatar.png" />
                </q-avatar>

                <div class="text-subtitle2 q-mt-md q-mb-xs">{{ oidcStore.currentUsername }}</div>
                <div class="text-subtitle2 q-mt-md q-mb-xs">{{ oidcStore.currentEmail }}</div>

                <q-btn
                  color="primary"
                  label="Logout"
                  push
                  size="sm"
                  v-close-popup
                  @click="logout"
                />
              </div>
            </div>
          </q-btn-dropdown>



          <q-btn
            dense
            flat
            round
            icon="login"
            v-if="!isAuthenticated"
            @click="login"
            label="Login"
          />
        </div>

        <!-- <q-btn dense flat round icon="menu" @click="toggleRightDrawer" /> -->
      </q-toolbar>
    </q-header>

    <!-- left drawer -->

    <q-drawer show-if-above v-model="leftDrawerOpen" side="left" bordered >

      <div class="text-h5 q-mb-md" style="text-align: center;margin-top: 20px;" v-if="isAuthenticated">
        {{ $t('threads.threads') }}
        <span >
          <q-btn dense square icon="add" class="q-ml-md" color="primary" style="margin-right: 20px;" padding="md" to="/thread-new">
            <q-tooltip >{{ $t('threads.new') }}</q-tooltip>
          </q-btn>
        </span>
      </div>
      <q-scroll-area style="height: calc(80% - 100px)"  v-if="isAuthenticated">

        <q-list  >


          <q-item v-for="(conversation,index) in links" :key="index" clickable v-ripple
            @click="goTo(conversation.path)">


            <q-item-section>
              <q-item-label lines="1">
                {{ conversation.summary }}
              </q-item-label>

              <q-item-label class="conversation__summary" caption>
                {{ conversation.id }}
              </q-item-label>
            </q-item-section>



          </q-item>

        </q-list>

      </q-scroll-area>


    </q-drawer>

    <!-- page container -->

    <q-page-container>
      <router-view />
    </q-page-container>


    <!-- footer -->

    <q-footer elevated >
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" v-if="isAuthenticated" />
        <q-toolbar-title></q-toolbar-title>
               <div class="q-pa-md q-gutter-sm"  v-if="isAuthenticated">
        <q-select class=" text-white"
                  v-model="locale"
                  :options="localeOptions"

                  dense
                  borderless
                  emit-value
                  map-options
                  options-dense
                  style="min-width: 150px;"

        />
        </div>
        <q-btn dense flat round icon="brightness_medium" @click="toggleLightDark" />
      </q-toolbar>
    </q-footer>
  </q-layout>
</template>

<script setup lang="ts">
import { computed,  onMounted, ref, watch } from 'vue';
import { useQuasar } from 'quasar';
import { useOidcStore } from 'stores/oidc-store';
import { useI18n } from 'vue-i18n';

import { useRouter } from 'vue-router';
import { PagesEnum } from 'src/enums/pages-enum';
import Utils from 'src/utils/utils';
import Auth from 'src/services/oidc.service';
const { locale } = useI18n({ useScope: 'global' })


const localeOptions = [
  { value: 'en', label: 'English' },
  { value: 'it', label: 'Italian' }
]

const $q = useQuasar();
const leftDrawerOpen = ref(false);

const links = ref([{ id: '0', summary: 'Home', path: '/' }, { id: '1', summary: 'Books', path: '/books' }]);

//const active = ref(true);
// const rightDrawerOpen = ref(false)

//const oidcService = inject<UserManager>('oidcService');
const router = useRouter();

const oidcStore = useOidcStore();
const isAuthenticated = computed( () => oidcStore.isAuthenticated )


watch(
  () => oidcStore.isDark,
  () => $q.dark.set(oidcStore.isDark),
);

onMounted( () => {
  $q.dark.set(oidcStore.isDark);
  //await oidcService?.initialize();
});

// Methods

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value;
}

// function toggleRightDrawer () {
//   rightDrawerOpen.value = !rightDrawerOpen.value;
// }

function toggleLightDark() {
  oidcStore.toggleDark();
}

// async function login() {
//   await oidcService?.signIn()
//     .catch(error =>{
//       console.error("###   error: " + JSON.stringify(error));
//     });
// }

function goTo(aPath: string) {
  router.push({ path: aPath })
    .catch(error => {
      console.error("###   error: " + JSON.stringify(error));
      Utils.checkNavigationError(error);
    });
}

function login() {
  console.log("###   login() called");
  Auth.signinPopup()
    .catch(error =>{
      console.error("###   error: " + JSON.stringify(error));
    });
}

function logout() {
  try {
    console.debug("###   logout() called");
    oidcStore.reset();
    Auth.signoutPopup()
      .catch(error =>{
        console.error("###   error: " + JSON.stringify(error));
      })
      .finally(() => {
         router?.push(PagesEnum.Index)
          .catch(error =>{
            console.error("###   error: " + JSON.stringify(error));
          });
      });
  } catch (e) {
    console.error("###   error: " + JSON.stringify(e));
  }
}

</script>

<style  >

.my-card {
  width: 80%;
}

</style>
