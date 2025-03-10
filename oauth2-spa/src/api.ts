import axios from "axios";

export default axios.create({
  baseURL: import.meta.env.VITE_BOOK_API_BASE_URL,
  headers: {
    "Content-type": "application/json"
  }
});
