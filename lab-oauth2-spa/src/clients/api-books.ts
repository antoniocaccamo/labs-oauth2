import axios from 'axios';
//import { api } from 'src/boot/axios';
import type { Book } from 'src/models/books';
import { useOidcStore } from 'src/stores/oidc-store';
import { appConfig } from 'src/config';

const apiPath = '/api/books';
const oidcUser = useOidcStore();

console.log("### baseURL: " + appConfig.apiBaseUrl);

export default class BooksApiClient {

  client = axios.create({
      baseURL: appConfig.apiBaseUrl,
  });

  public retrieveBooks(): Promise<Array<Book>> {
    return new Promise<Array<Book>>((resolve, reject) => {
      this.client
        .get<Book[]>(`${apiPath}`,
          {
          headers: {
              'Authorization': 'Bearer ' + oidcUser.user?.access_token
          }
        })
        .then((res) => resolve(res.data))
        .catch((reason) =>
          // eslint-disable-next-line @typescript-eslint/prefer-promise-reject-errors
          reject(reason)
        );
    });
  }

  // public retrieveAllBooks () : Promise<Book[]> {

  //     return new Promise<Book[]> ( (resolve, reject) => {

  //     const oidcUser = useOidcStore();

  //     const url =  "http://localhost:8080/api/books";
  //     console.log("### retrieving books from url: " + url);


  //     axios.get<Book[]>(url, {
  //         headers: {
  //             'Authorization': 'Bearer ' + oidcUser.user?.access_token
  //         }
  //     })
  //     .then((response: { data: Book[] | PromiseLike<Book[]>; }) => {
  //         resolve(response.data);
  //     })
  //     .catch((error: string) => {
  //         console.error("### error retrieving books: " + error);
  //         // eslint-disable-next-line @typescript-eslint/prefer-promise-reject-errors
  //         reject(error);
  //     });
  //     })
  // }


}
