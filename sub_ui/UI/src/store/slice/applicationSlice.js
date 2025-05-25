import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    isActiveNavBar: false,
    isLoadRelative: false,
    pageSize: 6,
    isReloadInvoice: false,
    chatRoomSelected: '',
    isAddMessage: false,
    userSettingVisible: false,
    setUserSettingVisible: false,
    theme: 'dark',
    rooms: null,
    userAddRoomVisible: false,
    selectedRoom: {},
    message: null,
}

export const applicationSlice = createSlice({
    name: 'app',
    initialState,
    reducers: {
        setIsActiveNavbar: (state, action) => {
            return {
                ...state,
                isActiveNavBar: action.payload
            }
        },
        setIsLoadRelative: (state, action) => {
            return {
                ...state,
                isLoadRelative: action.payload
            }
        },
        setPageSize: (state, action) => {
            return {
                ...state,
                pageSize: action.payload
            }
        },
        setIsReloadInvoice: (state, action) => {
            return {
                ...state,
                isReloadInvoice: action.payload
            }
        },
        setChatRoomSelected: (state, action) => {
            return {
                ...state,
                chatRoomSelected: action.payload
            }
        },
        setIsAddMessage: (state, action) => {
            return {
                ...state,
                isAddMessage: action.payload
            }
        },
        setSelectedRoom: (state, action) => {
            return {
                ...state,
                selectedRoom: action.payload
            }
        },
        setTheme: (state, action) => {
            return {
                ...state,
                theme: action.payload
            }
        }
    },
    extraReducers: (builder) => { }
})

export const { setIsActiveNavbar, setIsSubmitServey, setIsLoadRelative, setPageSize, setIsReloadInvoice, 
    setChatRoomSelected, setIsAddMessage, setSelectedRoom, setTheme } = applicationSlice.actions

export default applicationSlice.reducer