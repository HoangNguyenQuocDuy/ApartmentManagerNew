import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { persistStore, persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage";
import paymentSlice from "./slice/paymentSlice";
import applicationSlice from "./slice/applicationSlice";
import relativeSlice from "./slice/relativesSlice";
import accountSlice from "./slice/accountSlice";
import userSlice from "./slice/userSlice";
import chatRoomSlice from './slice/chatRoomSlice'
import messageSlice from "./slice/messageSlice";
import contractSlice from "./slice/contractSlice";
import roomSlice from "./slice/roomSlice";


const persistConfig = {
    key: 'root',
    storage,
}

const rootReducer = combineReducers({
    user: userSlice,
    account: accountSlice,
    app: applicationSlice,
    payments: paymentSlice,
    relatives: relativeSlice,
    roomsChat: chatRoomSlice,
    messages: messageSlice,
    contracts: contractSlice,
    rooms: roomSlice
})

const persistedReducer = persistReducer(persistConfig, rootReducer)

export const store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false
    }),
})

export const persistor = persistStore(store)