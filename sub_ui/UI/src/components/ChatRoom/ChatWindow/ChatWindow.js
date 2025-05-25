import { useContext, useEffect, useState, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import 'tippy.js/dist/tippy.css';
import classNames from 'classnames/bind';
import {
    faEllipsis,
    faPen,
    faBell,
    faMagnifyingGlass,
    faImage,
    faChevronRight,
    faCircleDot,
    faThumbsUp,
    faDragon,
    faSignature,
} from '@fortawesome/free-solid-svg-icons';

import styles from './chatWindow.module.scss';
import AvatarInfo from '../../AvatarInfo';
import Icon from '../../Icon';
import Image from '../../Image';
// import RoomOptions from '../../RoomOptions';
// import Feature from '../../Feature';
import MessageForm from '../../MessageForm';
// import { AuthContext } from '~/Context/AuthProvider';
import Message from './Message';
// import { db } from '~/firebase/config';
import { useDispatch, useSelector } from 'react-redux';
import { addMessage, fetchMessageByRoomId } from '../../../store/slice/messageSlice';

const cx = classNames.bind(styles);

const roomOptions = [
    {
        id: 0,
        leftIcon: faDragon,
        title: 'Tùy chỉnh đoạn chat',
        rightIcon: faChevronRight,
        subList: [
            {
                leftIcon: faPen,
                title: 'Đổi tên đoạn chat',
                onClick: (e) => {
                    e.stopPropagation();
                    console.log('duy');
                },
            },
            {
                leftIcon: faImage,
                title: 'Thay đổi ảnh',
                type: 'file',
            },
            {
                leftIcon: faSignature,
                title: 'Chỉnh sửa biệt danh',
            },
        ],
    },
    {
        id: 1,
        //selectedRoom.members.length
        title: `Thành viên đoạn chat(${0})`,
        rightIcon: faChevronRight,
        subList: [
            {
                leftIcon: faDragon,
                title: 'Chỉnh sửa biệt danh',
            },
            {
                leftIcon: faMagnifyingGlass,
                title: 'Tìm kiếm trong cuộc trò chuyện',
            },
        ],
    },
    {
        id: 2,
        title: 'File phương tiện, File và liên kết',
        rightIcon: faChevronRight,
        subList: [
            {
                leftIcon: faCircleDot,
                title: 'Đổi chủ đề',
            },
            {
                leftIcon: faMagnifyingGlass,
                title: 'Tìm kiếm trong cuộc trò chuyện',
            },
        ],
    },
    {
        id: 3,
        title: 'Quyền riêng tư và hỗ trợ',
        rightIcon: faChevronRight,
        subList: [
            {
                leftIcon: faCircleDot,
                title: 'Đổi chủ đề',
            },
            {
                leftIcon: faThumbsUp,
                title: 'Thay đổi biểu tượng cảm xúc',
            },
        ],
    },
];
function ChatWindow() {
    const dispatch = useDispatch()
    const { theme, selectedRoom, setSelectedRoom, rooms, formatTime } = useSelector(state => state.app)
    const [isOpenOptions, setIsOpenOptions] = useState(false);
    const [self, setSelf] = useState(false);
    const messagesBox = useRef();
    const [listUser, setListUser] = useState([]);
    const accessToken = localStorage.getItem("accessToken")
    const messages = useSelector(state => state.messages)
    const mesLength = messages ? messages.length : 0;

    const user = useSelector(state => state.user);
    console.log(messages);
    
    useEffect(() => {
        console.log('selectedRoom chat: ', selectedRoom)
        dispatch(fetchMessageByRoomId({ roomIdActive: selectedRoom.id, accessToken }))
    }, [selectedRoom])
    // db.collection('rooms').doc('AKbmjiSXk3jFuRlPw4VX').get().then((data) => {
    //         console.log(data.data())
    //     })
    //     console.log(uid)
    // if (selectedRoom && messages) {
    //     const lastTimeMessage = formatTime(messages[messages.length - 1].createdAt)
    //     const today = new Date()
    //     let status
    //     if (today.getMinutes() !== lastTimeMessage.minutes) {
    //         if (today.getDate() > lastTimeMessage.date) {
    //             status = `${today.getDate() - lastTimeMessage.date} ngày`
    //         } else {
    //             if (today.getHours() > lastTimeMessage.hours) {
    //                 status = `${today.getHours() - lastTimeMessage.hours} giờ`
    //             } else {
    //                 if (today.getMinutes() > lastTimeMessage.minutes) {
    //                     status = `${today.getMinutes() - lastTimeMessage.minutes} phút`
    //                 } else {
    //                     status = 'Đang hoạt động'
    //                 }
    //             }
    //         }
    //     }
    //     // console.log(status)
    // }

    // const sl = selectedRoom.members.length ? selectedRoom.members.length : 0
    // const usersRef = db.collection('users').get()

    // useEffect(() => {
    //     usersRef.then(snapShot => {
    //             let arr = []
    //             snapShot.docs.forEach(doc => {
    //                 arr.push(doc.data())
    //             })
    //             setListUser(arr)
    //         })
    // }, usersRef)

    const [stompClient, setStompClient] = useState(null);
    const [message, setMessage] = useState('')
    // const messages = useSelector(state => state.messages)

    useEffect(() => {
        // if (user && chatRoomSelected) {
            const socket = new SockJS('http://localhost:8989/ws');
            const client = new Client({
                webSocketFactory: () => socket,
                connectHeaders: {
                    Authorization: `Bearer ${accessToken}`,
                },
                debug: (str) => console.log(str),
            });

            client.onConnect = () => {
                console.log('Stomp client connected');
                setStompClient(client);

                // client.subscribe(`/notification/rooms/${chatRoomSelected}`, (message) => {
                //     const parsedMessage = JSON.parse(message.body);
                //     console.log('Received message from chatroom:', parsedMessage);
                //     dispatch(addMessage(parsedMessage));
                // });
            };

            client.onStompError = (frame) => {
                console.error('Stomp client error:', frame);
            };

            client.activate();

            return () => {
                client.deactivate();
            };
        // }
    }, [user, selectedRoom]);

    useEffect(() => {
        console.log('stompClient: ', stompClient)
        if (stompClient) {
            const subscriptionChatNotify = stompClient.subscribe(`/chat-rooms/${selectedRoom.id}`, (message) => {
                console.log('message from chat room ', message.body)
                console.log('message from chat room body ', message.body)
                dispatch(addMessage(JSON.parse(message.body)))
            })

            return () => {
                subscriptionChatNotify.unsubscribe()
            };
        }
    }, [stompClient])

    const sendMessage = ({ type, content, file, displayName }) => {
        console.log('chatMessage: ', { type, content, file, displayName })
        if (stompClient && stompClient.connected) {
            stompClient.publish({
                destination: '/app/chat.sendMessage',
                body: JSON.stringify({ type, content, file, userId: user.id, chatRoomId: selectedRoom.id, displayName }),
            });
            setMessage('')
            // dispatch(setIsAddMessage(true))
        } else {
            console.log('Not connected to WebSocket');
        }
    }

    // useEffect(() => {
    //     if (stompClient) {
    //         console.log('stompClient: ', stompClient)
    //         const subscriptionRoomChat = stompClient.subscribe(`/chat-room/${chatRoomSelected}`, (message) => {
    //             const parsedMessage = JSON.parse(message.body);
    //             console.log('Received message from chatroom:', parsedMessage);
    //             // dispatch(addMessage(parsedMessage));
    //         })
    //         return () => {
    //             subscriptionRoomChat.unsubscribe()
    //         };
    //     }
    //     console.log('------------------messages----------', message)
    // }, [stompClient])

    var tempToggle = [];
    useEffect(() => {
        roomOptions.forEach((item, idx) => {
            tempToggle[idx] = false;
        });
        console.log('tempToggle re-render');
    }, []);
    const [toggleSubMenu, setToggleSubMenu] = useState(tempToggle);

    // useEffect(() => {
    //     setSelectedRoom(rooms[rooms.length - 1]);
    // }, [rooms])

    useEffect(() => {
        messagesBox.current.scrollIntoView({ behavior: 'smooth', block: 'end', inline: 'nearest' });
    }, [messages]);

    return (
        <div className={cx('wrapper')}>
            {/* Header */}
            <div className={cx('main', { openOption: isOpenOptions })}>
                <div className={cx('header')}>
                    <AvatarInfo
                        name={selectedRoom ? selectedRoom.roomName : ''}
                        medium
                        className={cx('person-title')}
                        photoUrl={'photoURL'}
                        maxName
                    />
                    <Icon
                        noBackground
                        onClick={() => {
                            setIsOpenOptions(!isOpenOptions);
                        }}
                        className={cx('icon')}
                        icon={faEllipsis}
                    />
                </div>

                <div className={cx('body')}>
                    <div className={cx('messages', { self: self })}>
                        {messages && messages.map((message, i) => {
                            let self = message.userId === user.id;

                            if (message.type === '@text') {
                            }
                            // let mesFinally = messages[mesLength - 1];
                            let mesCurrent = message;
                            let mesNext = messages[i + 1];
                            let mesPrevious = messages[i - 1];

                            if (i === 0) {
                                if (!mesNext || (mesNext && mesNext.userId !== mesCurrent.userId)) {
                                    return (
                                        <Message
                                            createdAt={message.createdAt}
                                            self={self}
                                            start={true}
                                            end={false}
                                            imgURL={message.imageURL}
                                            showAvatar={true}
                                            type={'@text'}
                                            key={message.id}
                                            text={message.content}
                                            displayName={message.displayName}
                                            photoURL={message.photoURL}
                                        />
                                    );
                                } else {
                                    return (
                                        <Message
                                            createdAt={message.createdAt}
                                            self={self}
                                            start={true}
                                            end={false}
                                            imgURL={message.imageURL}
                                            showAvatar={false}
                                            type={'@text'}
                                            key={message.id}
                                            text={message.content}
                                            displayName={message.displayName}
                                            photoURL={message.photoURL}
                                        />
                                    );
                                }
                            }

                            if (i === mesLength - 1) {
                                if (mesPrevious && mesCurrent.userId !== mesPrevious.userId) {
                                    return (
                                        <Message
                                            createdAt={message.createdAt}
                                            self={self}
                                            start={true}
                                            end={false}
                                            imgURL={message.imageURL}
                                            showAvatar={true}
                                            type={'@text'}
                                            key={message.id}
                                            text={message.content}
                                            displayName={message.displayName}
                                            photoURL={message.photoURL}
                                        />
                                    );
                                }
                                if (mesPrevious && mesCurrent.userId === mesPrevious.userId) {
                                    return (
                                        <Message
                                            createdAt={message.createdAt}
                                            self={self}
                                            start={false}
                                            end={true}
                                            imgURL={message.imageURL}
                                            showAvatar={true}
                                            type={'@text'}
                                            key={message.id}
                                            text={message.content}
                                            displayName={message.displayName}
                                            photoURL={message.photoURL}
                                        />
                                    );
                                }
                            }

                            if (mesPrevious && mesCurrent.userId !== mesPrevious.userId) {
                                if (mesNext && mesCurrent.userId !== mesNext.userId) {
                                    return (
                                        <Message
                                            createdAt={message.createdAt}
                                            self={self}
                                            start={true}
                                            end={false}
                                            imgURL={message.imageURL}
                                            showAvatar={true}
                                            type={'@text'}
                                            key={message.id}
                                            text={message.content}
                                            displayName={message.displayName}
                                            photoURL={message.photoURL}
                                        />
                                    );
                                } else {
                                    return (
                                        <Message
                                            createdAt={message.createdAt}
                                            self={self}
                                            start={true}
                                            end={false}
                                            imgURL={message.imageURL}
                                            showAvatar={false}
                                            type={'@text'}
                                            key={message.id}
                                            text={message.content}
                                            displayName={message.displayName}
                                            photoURL={message.photoURL}
                                        />
                                    );
                                }
                            }

                            if (mesNext && mesCurrent.userId !== mesNext.userId) {
                                return (
                                    <Message
                                        createdAt={message.createdAt}
                                        self={self}
                                        start={false}
                                        end={true}
                                        imgURL={message.imageURL}
                                        showAvatar={true}
                                        type={'@text'}
                                        key={message.id}
                                        text={message.content}
                                        displayName={message.displayName}
                                        photoURL={message.photoURL}
                                    />
                                );
                            }

                            if (mesPrevious && mesCurrent.userId === mesPrevious.userId) {
                                if (mesNext && mesCurrent.userId === mesNext.userId) {
                                    return (
                                        <Message
                                            createdAt={message.createdAt}
                                            self={self}
                                            start={false}
                                            end={false}
                                            imgURL={message.imageURL}
                                            showAvatar={false}
                                            type={'@text'}
                                            key={message.id}
                                            text={message.content}
                                            displayName={message.displayName}
                                            photoURL={message.photoURL}
                                        />
                                    );
                                } else {
                                    return (
                                        <Message
                                            createdAt={message.createdAt}
                                            self={self}
                                            start={false}
                                            end={true}
                                            imgURL={message.imageURL}
                                            showAvatar={false}
                                            type={'@text'}
                                            key={message.id}
                                            text={message.content}
                                            displayName={message.displayName}
                                            photoURL={message.photoURL}
                                        />
                                    );
                                }
                            }
                        })}
                        <div ref={messagesBox}></div>
                    </div>
                    <div className={cx('footer')}>
                        {/* <img src='http://localhost:3000/17bc23c7-7782-48e4-b430-427b6deebdae'/> */}
                        <div className={cx('message-wrapper')}>
                            <MessageForm sendMessage={sendMessage} />
                        </div>
                    </div>
                </div>
            </div>
            {/* Body */}
            {isOpenOptions && (
                <div
                    className={cx('option', {
                        light: theme === 'light',
                        open: isOpenOptions,
                    })}
                >
                    <div>
                        <Image className={cx('img')} src="" />
                    </div>
                    <div className={cx('box-name')}>
                        <p className={cx('name')}>{selectedRoom.displayName}</p>
                        <p className={cx('des')}>Đang hoạt động</p>
                    </div>
                    <div className={cx('quick-item')}>
                        <div className={cx('user')}>
                            <Icon icon={faBell} />
                            <p>Tắt thông báo</p>
                        </div>
                        <div className={cx('user')}>
                            <Icon icon={faMagnifyingGlass} />
                            <p>Tìm kiếm</p>
                        </div>
                    </div>
                    <ul className={cx('roomOption')}>
                        {roomOptions.map((item, idx) => {
                            return (
                                <li
                                    key={idx}
                                    onClick={() => {
                                        let toggle = [...toggleSubMenu];
                                        toggle[idx] = !toggle[idx];
                                        setToggleSubMenu(toggle);
                                    }}
                                >
                                    {/* <RoomOptions
                                        leftIcon={item.leftIcon}
                                        title={item.title}
                                        rightIcon={item.rightIcon}
                                        rotateRightIcon={toggleSubMenu[idx]}
                                    />
                                    {item.subList ? (
                                        <ul className={cx('subMenu', { open: toggleSubMenu[idx] })}>
                                            {item.subList.map((subItem, idx) => {
                                                return (
                                                    <Feature
                                                        type={subItem.type}
                                                        key={idx}
                                                        onClick={subItem.onClick}
                                                        icon={subItem.leftIcon}
                                                        title={subItem.title}
                                                    />
                                                );
                                            })}
                                        </ul>
                                    ) : (
                                        <></>
                                    )} */}
                                </li>
                            );
                        })}
                    </ul>
                </div>
            )}
        </div>
    );
}

export default ChatWindow;
