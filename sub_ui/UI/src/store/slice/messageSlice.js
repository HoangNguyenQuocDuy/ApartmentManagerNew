/* eslint-disable no-unused-vars */
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import newRequest from "../../untils/request";
import { RootEnum } from '../../assets/enum';
import { data } from "autoprefixer";

const initialState = []

export const fetchMessageByRoomId = createAsyncThunk(
    'messages/fetchMessageByRoomId',
    async ({ roomIdActive, accessToken }) => {
        console.log(roomIdActive, accessToken)

        try {
            const response = await newRequest.get(`${RootEnum.API_CHAT + 'rooms/' + roomIdActive}/messages`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });
            console.log('Data get messages by roomId:', response.data.data);

            return response.data.data;
        } catch (error) {
            console.log('Error when get messages by roomId:', error);
            throw error;
        }
    }
)

export const messageSlice = createSlice({
    name: 'messages',
    initialState,
    reducers: {
        addMessage: (state, action) => {
            console.log('message from chat room ws: ', action.payload)
            return [...state, action.payload]
        }
    },
    // eslint-disable-next-line no-unused-vars
    extraReducers: (builder) => {
        builder.addCase(fetchMessageByRoomId.fulfilled, (state, action) => {
            return [...action.payload]
        })
    }
})


export const { addMessage, removeMessage } = messageSlice.actions

export default messageSlice.reducer