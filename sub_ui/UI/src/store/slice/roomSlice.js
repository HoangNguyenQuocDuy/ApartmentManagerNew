/* eslint-disable no-unused-vars */
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import newRequest from "../../untils/request";

const initialState = []

export const fetchRoomByUserId = createAsyncThunk(
    'rooms/fetchRoomByUserId',
    async(token) => {
        try {
            const response = await newRequest.get(`/rooms/`, { 
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });
            return response.data.data;
        } catch (error) {
            console.log('Error when get rooms by userId:', error);
            throw error;
        }
    }
)


export const roomSlice = createSlice({
    name: 'rooms',
    initialState,
    reducers: {
        addRoom: (state, action) => {
            return [action.payload, ...state]
        },
        removeRoom: (state, action) => {
            // return state.filter(comment => comment.commentId !== action.payload)
        },
        loadListRoomIdFromListContract: (state, action) => {
            console.log("contract for roomSlice: ", action.payload)

            return action.payload
                .filter(c => c.status.toLowerCase() === 'active')
                .map(c => c.roomId)
        }
    },
    // eslint-disable-next-line no-unused-vars
    extraReducers: (builder) => {
        builder.addCase(fetchRoomByUserId.fulfilled, (state, action) => {
            console.log(action.payload)
            return action.payload
        })
    }
})


export const {  addRoom, removeRoom , loadListRoomIdFromListContract} = roomSlice.actions

export default roomSlice.reducer