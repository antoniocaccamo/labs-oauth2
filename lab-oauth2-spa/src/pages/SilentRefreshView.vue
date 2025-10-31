

<script setup lang="ts" >

import Auth from 'src/services/oidc.service';
import { onMounted} from 'vue';

onMounted(() =>{
  Auth.startSilentRenew()
})


</script>
