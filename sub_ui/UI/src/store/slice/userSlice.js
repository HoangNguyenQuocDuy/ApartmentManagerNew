import { createSlice } from "@reduxjs/toolkit";

const initialState = {}

export const userSlice = createSlice({
    name: 'user',
    initialState,
    reducers: {
        loadUser: (state, action) => {
            console.log('action.payload: ', action.payload)
            // if (Object.keys(state).length !== 0) {
            //     return { ...state, user: { ...action.payload.data.userResponse } }
            // }
            return action.payload
        },
        clearUser: () => {
            return initialState
        }
    },
    extraReducers: (builder) => { }
})

export const { loadUser, clearUser } = userSlice.actions

export default userSlice.reducer