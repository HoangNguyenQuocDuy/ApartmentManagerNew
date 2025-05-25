import { useEffect, useState } from 'react';
import noAvatar from '../../assets/images/noAvatar.jpg'
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { useDispatch, useSelector } from 'react-redux';
import { addMessage } from '../../store/slice/messageSlice';
import { setIsAddMessage } from '../../store/slice/applicationSlice';
function Chat({ username, avatar }) {
    const dispatch = useDispatch()
    const [stompClient, setStompClient] = useState(null);
    const [message, setMessage] = useState('')
    const { chatRoomSelected, isAddMessage } = useSelector(state => state.app)
    const messages = useSelector(state => state.messages)
    const user = useSelector(state => state.user)
    const accessToken = localStorage.getItem("accessToken")

    useEffect(() => {
        if (user && chatRoomSelected) {
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
        }
    }, [user, chatRoomSelected]);

    const sendMessage = (chatMessage) => {
        if (stompClient && stompClient.connected) {
            stompClient.publish({
                destination: '/app/sendMessage',
                body: JSON.stringify(chatMessage),
            });
            setMessage('')
            dispatch(setIsAddMessage(true))
        } else {
            console.log('Not connected to WebSocket');
        }
    }

    useEffect(() => {
        if (stompClient) {
            console.log('stompClient: ', stompClient)
            const subscriptionRoomChat = stompClient.subscribe(`/notification/rooms/${chatRoomSelected}`, (message) => {
                const parsedMessage = JSON.parse(message.body);
                console.log('Received message from chatroom:', parsedMessage);
                dispatch(addMessage(parsedMessage));
            })
            return () => {
                subscriptionRoomChat.unsubscribe()
            };
        }
        console.log('------------------messages----------', message)
    }, [stompClient])

    return (
        <div class="absolute min-w-[440px] min-h-[520px] z-[100] center-y bottom-0 right-40 shadow-xl text-gray-700 bg-white px-6 py-4 rounded-md flex flex-col justify-between	">
            <div className=''>
                <div className='flex items-center mb-4'>
                    <img alt="pic" class="w-8 h-8 rounded-full" src={avatar || noAvatar} />
                    <p className='font-bold ms-2'>{username}</p>
                </div>
                <div className='overflow-y-scroll max-h-[380px]'>
                    {
                        messages && messages.map(m => {
                            if (m.userId !== user.id) {
                                return (
                                    <div key={m.id} class="flex flex-col w-full max-w-[360px] py-2 px-3 shadow-md rounded-e-3xl mb-4">
                                        <div class="flex items-center space-x-2 rtl:space-x-reverse">
                                            <p class="text-sm font-standard py-2.5 text-gray-900 dark:text-white">{m.content}</p>
                                        </div>
                                    </div>
                                )
                            }
                            return (
                                <div class="bg-blue-500 mb-1 flex flex-col w-full max-w-[360px] py-2 px-3 shadow-md rounded-s-3xl text-white">
                                    <div class="flex items-center space-x-2 rtl:space-x-reverse">
                                        <p class="text-sm font-standard py-2.5 text-gray-900 dark:text-white">{m.content}</p>
                                    </div>
                                </div>
                            )
                        })
                    }
                </div>
            </div>
            <div class="mb-6">
                <input
                    onKeyDown={(e) => {
                        if (e.key === 'Enter') {
                            console.log({ content: message, userId: user.id, chatRoomId: chatRoomSelected, displayName: user.firstname + user.lastname });
                        }
                    }}
                    onChange={(e) => { setMessage(e.target.value) }} value={message} placeholder='Type your message...' type="text" id="default-input" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block w-full p-2.5" />
            </div>
        </div>
    );
}

export default Chat;