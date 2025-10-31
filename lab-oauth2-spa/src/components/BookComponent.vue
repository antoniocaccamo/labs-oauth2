<template>
  <div>
    <p>{{ title }}</p>
    <ul>
      <li v-for="book in books" :key="book.id" @click="increment">
        {{ book.id }} - {{ book.title }}
      </li>
    </ul>
    <p>Count: {{ bookCount }} </p>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import type { Book} from 'src/models/books';

interface Props {
  title: string;
  books?: Book[];
  active: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  books: () => [],
});

const clickCount = ref(0);
function increment() {
  clickCount.value += 1;
  return clickCount.value;
}

const bookCount = computed(() => props.books.length);
</script>
