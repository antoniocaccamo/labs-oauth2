<template>
  <q-page class="row items-center justify-evenly" >
    <book-component
      title="Example component"
      active
      :books="books"
      v-if="!isLoading && ! hasError"
    ></book-component>
    <q-spinner v-if="isLoading" size="200px"/>
    <div v-if="hasError">
      <p>Error loading books. Please try again later.</p>
    </div>
  </q-page>
</template>


<script setup lang="ts">
import { type Book } from 'src/models/books';
import { onMounted, ref } from 'vue';
import  BookComponent  from 'src/components/BookComponent.vue';
import  BooksApiClient  from 'src/clients/api-books';

const books = ref<Book[]>([]);

const isLoading = ref(true);
const hasError = ref(false);

onMounted(() => {


  const apiClient = new BooksApiClient();

  apiClient.retrieveBooks()
  .then(response=> {
    books.value = response;
  })
  .catch(error => {
    console.error('Error fetching books:', error);
    hasError.value = true;
  })
  .finally(() => {
    isLoading.value = false;
  });

});



</script>
