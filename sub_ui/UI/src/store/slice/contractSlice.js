import { createSlice } from "@reduxjs/toolkit";

const initialState = []

export const contractSlice = createSlice({
    name: 'contracts',
    initialState,
    reducers: {
        loadListContract: (state, action) => {
            console.log('action.payload LIST contract: ', action.payload)
            // if (Object.keys(state).length !== 0) {
            //     return { ...state, user: { ...action.payload.data.userResponse } }
            // }
            return action.payload
        },
        addContract: (state, action) => { // contract
            console.log('action.payload ADD contract: ', action.payload)
            
            return [...state, action.payload]
        },
        removeContract: (state, action) => { // contractId
            console.log('action.payload ROMOVE contract: ', action.payload)

            return state.filter(c => c.id !== action.payload)
        }
    },
    extraReducers: (builder) => { }
})

export const { loadListContract } = contractSlice.actions

export default contractSlice.reducer