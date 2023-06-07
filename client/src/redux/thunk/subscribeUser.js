import { createAsyncThunk } from "@reduxjs/toolkit";
import { myAxios } from "src/utils/axiosSetup";
import { getFollowings } from "./getFollowings.js";

export const subscribeUser = createAsyncThunk(
  "user/subscribe",
  async (id, { rejectWithValue, dispatch }) => {
    try {
      // request to server if we have token in localStorage it will inject src/utils/axiosSetup into request
      const { data } = await myAxios.post(`/user/subscribe/${id}`);
      dispatch(getFollowings("profile"));
      return data;
    } catch (error) {
      // set message error from server
      const errorMessage = error.response.data || error.message;
      return rejectWithValue(errorMessage);
    }
  }
);
