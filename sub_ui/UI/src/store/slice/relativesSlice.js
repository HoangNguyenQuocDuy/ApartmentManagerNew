/* eslint-disable no-useless-catch */
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit"
import newRequest from "../../untils/request"
import { RootEnum } from "../../assets/enum"

const initialState = []

export const fetchRelatives = createAsyncThunk(
    'relative/fetchRelatives',
    async ({ userId, accessToken }, { rejectWithValue }) => {
        try {
            console.log(userId, " ", accessToken)
            const response = await newRequest.get(`${RootEnum.API_RELATIVES}?all=true&userId=${userId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });
            console.log('response from relativeSlice: ', response.data.data);
            return response.data.data.data.items;
        } catch (error) {
            return rejectWithValue(new Error(error.response.data));
        }
    }
)

export const relativeSlice = createSlice({
    name: 'relatives',
    initialState,
    reducers: {
        deleteRelative: (state, action) => {
            return state.filter(r => r.id !== action.payload)
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchRelatives.fulfilled, (state, action) => {
                return action.payload
            })
    }
})

export default relativeSlice.reducer

export const { deleteRelative } = relativeSlice.actions
