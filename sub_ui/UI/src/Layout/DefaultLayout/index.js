import React, { useEffect, useState } from 'react';
import Navbar from '../../components/Navbar';
import { useSelector } from 'react-redux';
import { ToastContainer } from 'react-toastify';
import classnames from 'classnames/bind';
import styles from './defaultLayout.module.scss';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { notify } from '../../untils/notification';

const cx = classnames.bind(styles);

function DefaultLayout({ children }) {
    const { isActiveNavBar } = useSelector(state => state.app);

    const [stompClient, setStompClient] = useState(null);
    const user = useSelector(state => state.user)

    useEffect(() => {
        if (user) {

            const accessToken = localStorage.getItem("accessToken")
            const socket = new SockJS('http://localhost:8989/ws');
            const client = new Client({
                webSocketFactory: () => socket,
                connectHeaders: {
                    Authorization: `Bearer ${accessToken}`
                },
                debug: (str) => console.log(str),
            });

            client.onConnect = () => {
                console.log('Stomp client connected');
                setStompClient(client)
            };

            client.onStompError = (frame) => {
                console.error('Stomp client error:', frame);
            };

            client.activate();

            return () => {
                client.deactivate();
            };
        }
    }, []);

    useEffect(() => {
        if (stompClient && user.locker) {
            const subscriptionCommonNotifyUser = stompClient.subscribe(`/commonNotify/${user.id}`, (message) => {
                console.log('message from personal ws ', message.body)
                notify(message.body, 'success')
            })
            const subscriptionCommonNotify = stompClient.subscribe(`/commonNotify`, (message) => {
                console.log('message from FIRE ALERT ', message.body)
                console.log('message from FIRE ALERT BODY ', message.body)

                notify(message.body, 'warn')
            })
            // const subscriptionOrderNotify = stompClient.subscribe(`/notification/lockers/${user.locker.id}`, (message) => {
            //     notify(message.body, 'success')
            // })
            // const subscriptionCardNotify = stompClient.subscribe(`/notification/relatives/user/${user.id}`, (message) => {
            //     let status = ''
            //     if (message.body.toLowerCase().includes('confirmed')) {
            //         status = 'success'
            //     } else {
            //         status = 'warn'
            //     }
            //     notify(message.body, status)
            // })
            // const subscriptionSurveyNotify = stompClient.subscribe(`/notification/surveys`, (message) => {
            //     notify(message.body, 'success')
            // })

            return () => {
                subscriptionCommonNotifyUser.unsubscribe()
                subscriptionCommonNotify.unsubscribe()
                // subscriptionOrderNotify.unsubscribe()
                // subscriptionCardNotify.unsubscribe()
                // subscriptionSurveyNotify.unsubscribe()
            };
        }
    }, [stompClient])

    return (
        <>
            <Navbar />
            <ToastContainer />
            <div className={cx('wrapper', { 'nav': isActiveNavBar })}>
                {children}
            </div>
        </>
    );
}

export default DefaultLayout;
