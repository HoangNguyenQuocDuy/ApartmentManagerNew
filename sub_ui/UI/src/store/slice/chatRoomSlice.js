/* eslint-disable no-unused-vars */
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import newRequest from "../../untils/request";
import { RootEnum } from "../../assets/enum";

const initialState = []

export const fetchRoomByUserId = createAsyncThunk(
    'chatRooms/fetchChatRoomByUserId',
    async ({accessToken, id}) => {
        try {
            const response = await newRequest.get(`${RootEnum.API_CHAT + "rooms/" + id}`, {
                headers: {
                    "Authorization": `Bearer ${accessToken}`
                }
            });
            console.log('chatRoom data: ', response.data)
            return response.data;
        } catch (error) {
            console.log('Error when get chat rooms by userId:', error);
            throw error;
        }
    }
)


export const roomSlice = createSlice({
    name: 'rooms',
    initialState,
    reducers: {
    },
    // eslint-disable-next-line no-unused-vars
    extraReducers: (builder) => {
        builder.addCase(fetchRoomByUserId.fulfilled, (state, action) => {
            console.log('action.payload chat rooms: ', action.payload)
            return action.payload
        })
    }
})


export const { addRoom, removeRoom } = roomSlice.actions

export default roomSlice.reducer