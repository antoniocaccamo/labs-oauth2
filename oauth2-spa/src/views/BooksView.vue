<script setup lang="ts">


import { onMounted, ref } from "vue";

import { retrieveAllBooks } from "../services/book"


const books = ref();

onMounted( () =>{

    retrieveAllBooks().then(resp => books.value= resp.data)

})

</script>


<template>
    <div>

        <li v-for="it in books" >
            {{it.id}} {{it.author}} {{it.title}} 
        </li>

    </div>
</template>