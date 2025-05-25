// import { createContext, useState, useContext, useMemo } from 'react';
// import firebase from 'firebase';

// import { AuthContext } from './AuthProvider';
// import useFirestore from '~/hooks/useFirestore';
// import { addDocument } from '~/firebase/services';

// const AppContext = createContext();

// function AppProvider({ children }) {
//     const [userSettingVisible, setUserSettingVisible] = useState(false);
//     const [userAddRoomVisible, setUserAddRoomVisible] = useState(false);

//     const { displayName, uid, photoURL } = useContext(AuthContext);

//     //Theme: Dark - Light
//     const appConfig = JSON.parse(localStorage.getItem('localhost')) ?? { appTheme: 'dark' };

//     const setAppConfig = (key, value) => {
//         appConfig[key] = value;
//         localStorage.setItem('localhost', JSON.stringify(appConfig));
//     };

//     const [theme, setTheme] = useState(appConfig.appTheme);

//     const roomsCondition = useMemo(() => {
//         return {
//             fieldName: 'members',
//             operator: 'array-contains',
//             compareValue: uid,
//         };
//     }, [uid]);
//     const rooms = useFirestore('rooms', roomsCondition);

//     const handleClose = () => {
//         setUserSettingVisible(false);
//         setUserAddRoomVisible(false);
//     };

//     const [selectedRoom, setSelectedRoom] = useState({});

//     const messageCondition = useMemo(() => {
//         return {
//             fieldName: 'roomId',
//             operator: '==',
//             compareValue: selectedRoom.id,
//         };
//     }, [selectedRoom.id]);

//     const messages = useFirestore('messages', messageCondition);

//     const formatTime = (createAt) => {
//       if (createAt) {
//           const timestamp = createAt.toDate();
  
//           const time = {
//               minutes: 0,
//               hours: 0,
//               day: 0,
//               date: 0,
//               month: 0,
//               year: 0,
//           };
//           time.minutes = timestamp.getMinutes();
//           time.hours = timestamp.getHours();
//           time.day = timestamp.getDay();
//           time.date = timestamp.getDate();
//           time.month = timestamp.getMonth() + 1;
//           time.year = timestamp.getFullYear();
  
//           return time;
//       }
//     };

//     const sendMessage = (type = '', text = '', imageURL = '', roomId) => {
//         addDocument('messages', {
//             type,
//             uid,
//             displayName,
//             photoURL,
//             text,
//             imageURL,
//             roomId,
//             createdAt: firebase.firestore.FieldValue.serverTimestamp(),
//         });
//     };
//     //
//     // const appConfig = JSON.parse(
//     //   localStorage.getItem("xuanphuc_space_config")
//     // ) ?? { appTheme: "light" };

//     // const setAppConfig = (key, value) => {
//     //   appConfig[key] = value;
//     //   localStorage.setItem("xuanphuc_space_config", JSON.stringify(appConfig));
//     // };

//     // const [theme, setTheme] = useState(appConfig.appTheme);

//     //Close Personal Setting

//     return (
//         <AppContext.Provider
//             value={{
//                 userSettingVisible,
//                 setUserSettingVisible,
//                 theme,
//                 setTheme,
//                 setAppConfig,
//                 handleClose,
//                 rooms,
//                 userAddRoomVisible,
//                 setUserAddRoomVisible,
//                 selectedRoom,
//                 setSelectedRoom,
//                 messages,
//                 formatTime,
//                 sendMessage,
//             }}
//         >
//             {children}
//         </AppContext.Provider>
//     );
// }

// export { AppProvider, AppContext };
