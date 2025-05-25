import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import newRequest from "../../untils/request";

const initialState = []

export const fetchPayment = createAsyncThunk(
    'payments/fetchPayment',
    async ({ userId, accessToken }, { rejectWithValue }) => {
        try {
            const response = await newRequest.get(`/payments/list?userId=${userId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });
            return response.data;
        } catch (error) {
            return rejectWithValue(new Error(error.response.data));
        }
    }
)

export const paymentSlice = createSlice({
    name: 'payments',
    initialState,
    reducers: {
    },
    extraReducers: (builder) => {
        builder.addCase(fetchPayment.fulfilled, (state, action) => {
            return action.payload
        })
     }
})

// export const {  } = paymentSlice.actions

export default paymentSlice.reducer