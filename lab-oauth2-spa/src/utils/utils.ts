import { PagesEnum } from "src/enums/pages-enum";
import { useRouter } from "vue-router";


export default class Utils {

  public static  checkNavigationError( error: { status: number; }) : void {
    console.error("###   error: " + JSON.stringify(error));
    if ( error.status === 401) {
      const router = useRouter();
      router.push({path : PagesEnum.Forbidden})
      .catch( error =>{
        console.error("###   error: " + JSON.stringify(error));
      })
    }
  }
}
