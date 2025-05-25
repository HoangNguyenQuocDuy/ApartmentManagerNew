import { useRef, useEffect } from 'react';
import classNames from 'classnames/bind';

import styles from './chatRoom.module.scss';
// import { AppContext } from '../../Context/AppProvider';

import Sidebar from './Sidebar';
import ChatWindow from './ChatWindow';
// import Setting from '../../components/Modal/Setting';
import { useDispatch, useSelector } from 'react-redux';
import { fetchRoomByUserId } from '../../store/slice/chatRoomSlice';
import { setSelectedRoom } from '../../store/slice/applicationSlice';
import { fetchMessageByRoomId } from '../../store/slice/messageSlice';
// import AddRoom from './components/Modal/AddRoom';

const cx = classNames.bind(styles);

function ChatRoom() {
    const dispatch = useDispatch()
    const personalSettingRef = useRef();
    const addRoomRef = useRef();
    const { userSettingVisible, userAddRoomVisible, theme, handleClose } = useSelector(state => state.app)
    console.log('chatroom re-render');
    const { id } = useSelector(state => state.user)
    // const user = useSelector(state => state.user)
    const accessToken = localStorage.getItem('accessToken')

    // console.log(user)

    useEffect(() => {
        // setUserSettingVisible(false)
        handleFetchRoomsChat()
    }, [])

    const handleFetchRoomsChat = async () => {
        console.log(id)
        dispatch(fetchRoomByUserId({accessToken, id}))
            .then(data => {
                console.log("data chat rooms: ", data)
                dispatch(setSelectedRoom(data.payload ? data.payload[0] : {}))
                dispatch(fetchMessageByRoomId({ roomIdActive: data.payload[0].id, accessToken }))
            })
            .catch(err => {
                console.log('err when get chat rooms: ', err)
            }
        )
    }

    useEffect(() => {
        window.addEventListener('click', (e) => {
            if (e.target === personalSettingRef.current || e.target === addRoomRef.current) {
                handleClose();
            }
        });
    }, [userSettingVisible, userSettingVisible]);

    return (
        <div className={cx('wrapper', { overlay: userSettingVisible, light: theme === 'light' })}>
            <div className={cx('sidebar')}>
                <Sidebar />
            </div>
            <div className={cx('chatWindow')}>
                <ChatWindow />
            </div>
            <div
                ref={personalSettingRef}
                className={cx('personal-setting', {
                    overlay: userSettingVisible,
                })}
            >
                {/* <Setting ref={personalSettingRef} /> */}
            </div>
            <div
                ref={addRoomRef}
                className={cx('add-room', {
                    overlay: userAddRoomVisible,
                })}
            >
                {/* <AddRoom ref={addRoomRef} /> */}
            </div>
        </div>
    );
}

export default ChatRoom;
