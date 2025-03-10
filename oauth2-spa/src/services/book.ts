import http from "@/api";
import { useOidcUser } from '@/stores/oidc'

import type { BookType } from "@/types/books";
import type { AxiosRequestConfig } from "axios";
import { esbuildVersion } from "vite";
import { ref } from "vue";
 

function retrieveAllBooks () {

    const oidcUser = useOidcUser()
    const books = ref<BookType[]>()

    

  
 
 
    return    http.get('', {
            headers :{
                'Authorization' : 'Bearer ' + oidcUser.user?.access_token 
            }
        })
        
}

export {
    retrieveAllBooks
}