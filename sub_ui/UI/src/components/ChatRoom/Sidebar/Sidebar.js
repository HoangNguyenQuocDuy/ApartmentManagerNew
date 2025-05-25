import { useContext, useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import { faPenToSquare, faMagnifyingGlass, faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavLink } from 'react-router-dom';

import styles from './sidebar.module.scss';
import Image from '../../Image';
import Icon from '../../Icon';
import AvatarInfo from '../../AvatarInfo';
import { useDispatch, useSelector } from 'react-redux';
import { setSelectedRoom } from '../../../store/slice/applicationSlice';
import images from '../../../assets/images';

const cx = classNames.bind(styles);

function Sidebar() {
    const { rooms, uid, etUserAddRoomVisible, setUserSettingVisible, theme, selectedRoom, messages } =
        useSelector(state => state.app);
    const [isSearch, setIsSearch] = useState(false);
    const [active, setActive] = useState();
    const roomsChat = useSelector(state => state.roomsChat)
    const user = useSelector(state => state.roomsChat)
    const dispatch = useDispatch()

    useEffect(() => {
        if (messages) {
            messages.forEach((message) => {
                roomsChat.forEach((room) => {
                    let des = ''
                    if (message.roomId === room.id) {
                        let name;
                        if (message.uid === uid) {
                            name = 'Bạn';
                        } else {
                            name = message.displayName;
                        }
                        if (message.type === '@text') {
                            des = `${name}: ${message.text}`;
                        } else if (message.type === '@image') {
                            des = `${name}: đã gửi 1 ảnh`;
                        }
                        room = { ...room, lastMessage: des };
                    }
                });
            });
        }
    }, [messages]);

    const handleOpenUserSetting = () => {
        setUserSettingVisible(true);
    };
    const handleOpenAddRoom = () => {
        // setUserAddRoomVisible(true);
    };

    const handleSearchUser = () => {
        setIsSearch(!isSearch);
    };

    const handleChoseRoom = (room, idx) => {
        dispatch(setSelectedRoom(room));
        setActive(idx);
        console.log(active, idx);
    };
    // console.log('selectedRoom', selectedRoom.id);
    // console.log('active', active);

    const addClass = (idx) => {
        return active.idx === idx ? 'active' : '';
    };

    const descriptionRoom = (room) => {
        let des = '';
        messages.forEach((message) => {
            if (message.roomId === room.id) {
            }
        });
        return room.lastMessage;
    };

    return (
        <div className={cx('wrapper', { light: theme === 'light' })}>
            <div className={cx('wrapper-header')}>
                <div className={cx('header')}>
                    <Image onClick={handleOpenUserSetting} className={cx('img')} src={user.avatar || images.defaultUser} />
                    <span className='text-2xl'>
                        <NavLink to={'/'}>
                            QD Apartment
                        </NavLink>
                    </span>
                    <Icon onClick={handleOpenAddRoom} icon={faPenToSquare} />
                </div>
                <div className={cx('find-box')}>
                    <span
                        onClick={handleSearchUser}
                        className={cx('icon', {
                            light: theme === 'light',
                            searchUser: isSearch,
                        })}
                    >
                        {isSearch ? <Icon icon={faArrowLeft} /> : <FontAwesomeIcon icon={faMagnifyingGlass} />}
                    </span>

                    <input
                        className={cx({ light: theme === 'light', searchUser: isSearch })}
                        type="text"
                        placeholder="Tìm kiếm..."
                    />
                </div>
            </div>
            <div className={cx('room-list')}>
                {roomsChat &&
                    roomsChat.map((room, idx) => {
                        console.log(room)
                        return (
                            <AvatarInfo
                                onClick={() => {
                                    handleChoseRoom(room, idx);
                                }}
                                key={idx}
                                name={room.roomName}
                                des={room.lastMessage}
                                photoUrl={''}
                                className={cx('room', { light: theme === 'light', active: active === idx })}
                                positive
                                room
                            />
                        );
                    })}
                {/* <RoomList list={rooms}/> */}
            </div>
        </div>
    );
}

export default Sidebar;
