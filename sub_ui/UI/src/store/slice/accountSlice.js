/* eslint-disable no-useless-catch */
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit"
import newRequest from "../../untils/request"
import { useNavigate } from "react-router-dom"
import { RootEnum } from "../../assets/enum"

const initialState = {
    username: '',
    accessToken: '',
    // refreshToken: ''
}

export const fetchLogin = createAsyncThunk(
    'account/fetchLogin',
    async ({ username, password }, { rejectWithValue }) => {
        try {
            console.log('login slice')
            const response = await newRequest.post(`${RootEnum.API_AUTH}login`, {
                username, password
            });
            console.log(response);
            return response.data.data;
        } catch (error) {
            console.log(error)
            return rejectWithValue(new Error(error.response.data.data));
        }
    }
)

export const accountSlice = createSlice({
    name: 'account',
    initialState,
    reducers: {
        login: (state, action) => {
            console.log(action.payload.data)
            return {
                ...action.payload.data
            }
        },
        logout: () => {
            localStorage.removeItem('accessToken')
            return {}
        }
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchLogin.fulfilled, (state, action) => {
                return {
                    username: action.payload.userResponse.username,
                    accessToken: action.payload.accessToken
                }
            })
    }
})

export default accountSlice.reducer

export const { login, logout } = accountSlice.actions
