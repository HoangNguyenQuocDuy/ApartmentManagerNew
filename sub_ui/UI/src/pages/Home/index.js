import classnames from 'classnames/bind'

import styles from './home.module.scss'
import logo from '../../assets/images/logo.png'
import Header from "../../components/Header";
import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useRef, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { notify } from '../../untils/notification';
import { ToastContainer } from 'react-toastify';
import { setChatRoomSelected, setIsActiveNavbar } from '../../store/slice/applicationSlice';
import { fetchMessageByRoomId } from '../../store/slice/messageSlice';
import { NavLink, useLocation, useNavigate } from 'react-router-dom';
import routes from '../../config/routes';
import Image from '../../components/Image';
import { logout } from '../../store/slice/accountSlice';
import { clearUser } from '../../store/slice/userSlice';

const cx = classnames.bind(styles)

function Home() {
    const dispatch = useDispatch()
    const navigate = useNavigate()
    const accessToken = localStorage.getItem('accessToken')
    const [stompClient, setStompClient] = useState(null);
    const roomsChat = useSelector(state => state.roomsChat)
    const user = useSelector(state => state.user)
    const { chatRoomSelected } = useSelector(state => state.app)


    const location = useLocation();
    const { pathname } = location;

    const trigger = useRef(null);
    const sidebar = useRef(null);

    const storedSidebarExpanded = localStorage.getItem('sidebar-expanded');
    const [sidebarExpanded, setSidebarExpanded] = useState(
        storedSidebarExpanded === null ? false : storedSidebarExpanded === 'true'
    );

    const handleLogout = () => {
        dispatch(logout())
        dispatch(clearUser())
        navigate('/')
    }

    useEffect(() => {
        if (user.roleName === 'ROLE_ADMIN') {
            navigate(routes.adminDashboard)
        }

        dispatch(setIsActiveNavbar(false))
        dispatch(fetchMessageByRoomId({ roomIdActive: chatRoomSelected, accessToken }))
        if (user.roleName !== 'ROLE_ADMIN') {
            // dispatch(setChatRoomSelected(roomsChat[0].id))
        }
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
                setStompClient(client);

                client.subscribe(`/notification/lockers/${user.locker.id}`, (message) => {
                    console.log('Received message:', message.body);
                    notify(message.body, 'success');
                });
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
        if (stompClient) {
            const subscriptionCommonNotifyUser = stompClient.subscribe(`/commonNotify/${user.id}`, (message) => {
                console.log('message from commonNotify', message.body)
                notify(message.body, 'success')
            })
            const subscriptionCommonNotify = stompClient.subscribe(`/commonNotify`, (message) => {
                console.log('message from FIRE ALERT ', message.body)
                console.log('message from FIRE ALERT BODY ', message.body)

                notify(message.body, 'warn')
            })
            const subscriptionChatNotify = stompClient.subscribe(`/chat-rooms`, (message) => {
                console.log('message from chat room ', message.body)
                console.log('message from chat room body ', message.body)
            })

            return () => {
                subscriptionCommonNotifyUser.unsubscribe()
                subscriptionCommonNotify.unsubscribe()
                subscriptionChatNotify.unsubscribe()
            };
        }
    }, [stompClient])

    return (<div className={cx('container')}>
        <ToastContainer />
        <div className={cx('header')}>
            {/* <Header /> */}

            <aside
                ref={sidebar}
                className={cx('aside', 'w-70', 'text-standard', 'absolute', 'left-0', 'top-0', 'z-9999', 'flex', 'h-screen', 'flex-col', 'overflow-y-hidden', 'duration-300', 'ease-linear', 'lg:static', 'lg:translate-x-0', 'translate-x-full')}
            >
                {/* <!-- SIDEBAR HEADER --> */}
                <div className={cx('logo-box', 'gap-2', 'flex', 'items-center', 'justify-center', 'px-0', 'py-4')}>
                    <NavLink to="/">
                        <img src={logo} alt="Logo" />
                    </NavLink>

                    <button
                        ref={trigger}
                        aria-controls="sidebar"
                        className="block lg:hidden"
                    >
                        <svg
                            className="fill-current"
                            width="20"
                            height="18"
                            viewBox="0 0 20 18"
                            fill="currentColor"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                d="M19 8.175H2.98748L9.36248 1.6875C9.69998 1.35 9.69998 0.825 9.36248 0.4875C9.02498 0.15 8.49998 0.15 8.16248 0.4875L0.399976 8.3625C0.0624756 8.7 0.0624756 9.225 0.399976 9.5625L8.16248 17.4375C8.31248 17.5875 8.53748 17.7 8.76248 17.7C8.98748 17.7 9.17498 17.625 9.36248 17.475C9.69998 17.1375 9.69998 16.6125 9.36248 16.275L3.02498 9.8625H19C19.45 9.8625 19.825 9.4875 19.825 9.0375C19.825 8.55 19.45 8.175 19 8.175Z"
                                fill="currentColor"
                            />
                        </svg>
                    </button>
                    <div className={cx('title', 'font-bold', 'text-2xl', 'ms-2')}>QDApartment</div>
                </div>
                {/* <!-- SIDEBAR HEADER --> */}

                <div className="no-scrollbar flex flex-col overflow-y-auto duration-300 ease-linear">
                    {/* <!-- Sidebar Menu --> */}
                    <nav className="mt-5 lg:mt-9 lg:px-6">
                        {/* <!-- Menu Group --> */}
                        <div>

                            <NavLink to={routes.dashboard} className={cx('link', 'group', 'relative', 'flex', 'items-center', 'gap-2.5', 'rounded-sm', 'py-2', 'px-4', 'font-medium', 'text-bodydark1', 'duration-300', 'ease-in-out', 'hover:bg-graydark', 'dark:hover:bg-meta-4', {'active': pathname.includes(routes.dashboard)})}
                            >
                                <svg className="fill-current me-4" width='24' height="24" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M6.10322 0.956299H2.53135C1.5751 0.956299 0.787598 1.7438 0.787598 2.70005V6.27192C0.787598 7.22817 1.5751 8.01567 2.53135 8.01567H6.10322C7.05947 8.01567 7.84697 7.22817 7.84697 6.27192V2.72817C7.8751 1.7438 7.0876 0.956299 6.10322 0.956299ZM6.60947 6.30005C6.60947 6.5813 6.38447 6.8063 6.10322 6.8063H2.53135C2.2501 6.8063 2.0251 6.5813 2.0251 6.30005V2.72817C2.0251 2.44692 2.2501 2.22192 2.53135 2.22192H6.10322C6.38447 2.22192 6.60947 2.44692 6.60947 2.72817V6.30005Z" fill="" />
                                    <path d="M15.4689 0.956299H11.8971C10.9408 0.956299 10.1533 1.7438 10.1533 2.70005V6.27192C10.1533 7.22817 10.9408 8.01567 11.8971 8.01567H15.4689C16.4252 8.01567 17.2127 7.22817 17.2127 6.27192V2.72817C17.2127 1.7438 16.4252 0.956299 15.4689 0.956299ZM15.9752 6.30005C15.9752 6.5813 15.7502 6.8063 15.4689 6.8063H11.8971C11.6158 6.8063 11.3908 6.5813 11.3908 6.30005V2.72817C11.3908 2.44692 11.6158 2.22192 11.8971 2.22192H15.4689C15.7502 2.22192 15.9752 2.44692 15.9752 2.72817V6.30005Z" />
                                    <path d="M6.10322 9.92822H2.53135C1.5751 9.92822 0.787598 10.7157 0.787598 11.672V15.2438C0.787598 16.2001 1.5751 16.9876 2.53135 16.9876H6.10322C7.05947 16.9876 7.84697 16.2001 7.84697 15.2438V11.7001C7.8751 10.7157 7.0876 9.92822 6.10322 9.92822ZM6.60947 15.272C6.60947 15.5532 6.38447 15.7782 6.10322 15.7782H2.53135C2.2501 15.7782 2.0251 15.5532 2.0251 15.272V11.7001C2.0251 11.4188 2.2501 11.1938 2.53135 11.1938H6.10322C6.38447 11.1938 6.60947 11.4188 6.60947 11.7001V15.272Z" />
                                    <path d="M15.4689 9.92822H11.8971C10.9408 9.92822 10.1533 10.7157 10.1533 11.672V15.2438C10.1533 16.2001 10.9408 16.9876 11.8971 16.9876H15.4689C16.4252 16.9876 17.2127 16.2001 17.2127 15.2438V11.7001C17.2127 10.7157 16.4252 9.92822 15.4689 9.92822ZM15.9752 15.272C15.9752 15.5532 15.7502 15.7782 15.4689 15.7782H11.8971C11.6158 15.7782 11.3908 15.5532 11.3908 15.272V11.7001C11.3908 11.4188 11.6158 11.1938 11.8971 11.1938H15.4689C15.7502 11.1938 15.9752 11.4188 15.9752 11.7001V15.272Z" />
                                </svg>
                                Dashboard
                            </NavLink>
                            <NavLink to={routes.personal} className={cx('link', 'group', 'relative', 'flex', 'items-center', 'gap-2.5', 'rounded-sm', 'py-2', 'px-4', 'font-medium', 'text-bodydark1', 'duration-300', 'ease-in-out', 'hover:bg-graydark', 'dark:hover:bg-meta-4', {'active': pathname.includes(routes.personal)})}
                            >
                                {/* <svg className='me-4' stroke="currentColor" fill="currentColor" strokeWidth="0" viewBox="0 0 512 512" width='24' height='24' xmlns="http://www.w3.org/2000/svg"><path fill="none" strokeLinecap="round" strokeLinejoin="round" strokeWidth="32" d="M80 212v236a16 16 0 0 0 16 16h96V328a24 24 0 0 1 24-24h80a24 24 0 0 1 24 24v136h96a16 16 0 0 0 16-16V212"></path><path fill="none" strokeLinecap="round" strokeLinejoin="round" strokeWidth="32" d="M480 256 266.89 52c-5-5.28-16.69-5.34-21.78 0L32 256m368-77V64h-48v69"></path></svg> */}
                                <svg fill='currentColor' className='me-4' width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M304 128a80 80 0 1 0 -160 0 80 80 0 1 0 160 0zM96 128a128 128 0 1 1 256 0A128 128 0 1 1 96 128zM49.3 464l349.5 0c-8.9-63.3-63.3-112-129-112l-91.4 0c-65.7 0-120.1 48.7-129 112zM0 482.3C0 383.8 79.8 304 178.3 304l91.4 0C368.2 304 448 383.8 448 482.3c0 16.4-13.3 29.7-29.7 29.7L29.7 512C13.3 512 0 498.7 0 482.3z" /></svg>
                                Personal
                            </NavLink>
                            <NavLink to={routes.contracts} className={cx('link', 'group', 'relative', 'flex', 'items-center', 'gap-2.5', 'rounded-sm', 'py-2', 'px-4', 'font-medium', 'text-bodydark1', 'duration-300', 'ease-in-out', 'hover:bg-graydark', 'dark:hover:bg-meta-4', {'active': pathname.includes(routes.contracts)})}
                            >
                                <svg className='me-4' xmlns="http://www.w3.org/2000/svg" fill="currentColor" width={24} height={24} viewBox="0 0 384 512"><path d="M64 0C28.7 0 0 28.7 0 64L0 448c0 35.3 28.7 64 64 64l256 0c35.3 0 64-28.7 64-64l0-288-128 0c-17.7 0-32-14.3-32-32L224 0 64 0zM256 0l0 128 128 0L256 0zM80 64l64 0c8.8 0 16 7.2 16 16s-7.2 16-16 16L80 96c-8.8 0-16-7.2-16-16s7.2-16 16-16zm0 64l64 0c8.8 0 16 7.2 16 16s-7.2 16-16 16l-64 0c-8.8 0-16-7.2-16-16s7.2-16 16-16zm54.2 253.8c-6.1 20.3-24.8 34.2-46 34.2L80 416c-8.8 0-16-7.2-16-16s7.2-16 16-16l8.2 0c7.1 0 13.3-4.6 15.3-11.4l14.9-49.5c3.4-11.3 13.8-19.1 25.6-19.1s22.2 7.7 25.6 19.1l11.6 38.6c7.4-6.2 16.8-9.7 26.8-9.7c15.9 0 30.4 9 37.5 23.2l4.4 8.8 54.1 0c8.8 0 16 7.2 16 16s-7.2 16-16 16l-64 0c-6.1 0-11.6-3.4-14.3-8.8l-8.8-17.7c-1.7-3.4-5.1-5.5-8.8-5.5s-7.2 2.1-8.8 5.5l-8.8 17.7c-2.9 5.9-9.2 9.4-15.7 8.8s-12.1-5.1-13.9-11.3L144 349l-9.8 32.8z" /></svg>
                                {/* <IoDocumentTextOutline width={'24px'} height={24} /> */}
                                {/* <svg stroke="currentColor" fill="currentColor" className='me-4' height="24" width="24" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M50.7 58.5L0 160l208 0 0-128L93.7 32C75.5 32 58.9 42.3 50.7 58.5zM240 160l208 0L397.3 58.5C389.1 42.3 372.5 32 354.3 32L240 32l0 128zm208 32L0 192 0 416c0 35.3 28.7 64 64 64l320 0c35.3 0 64-28.7 64-64l0-224z" /></svg> */}
                                {/* <svg className='me-4' height="24" width="24" stroke="currentColor" fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M96 64c0-35.3 28.7-64 64-64L416 0c35.3 0 64 28.7 64 64l0 384 64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32l-112 0-288 0L32 512c-17.7 0-32-14.3-32-32s14.3-32 32-32l64 0L96 64zM384 288a32 32 0 1 0 0-64 32 32 0 1 0 0 64z" /></svg> */}
                                Contract
                            </NavLink>
                            <NavLink to={routes.visitOperator} className={cx('link', 'group', 'relative', 'flex', 'items-center', 'gap-2.5', 'rounded-sm', 'py-2', 'px-4', 'font-medium', 'text-bodydark1', 'duration-300', 'ease-in-out', 'hover:bg-graydark', 'dark:hover:bg-meta-4', {'active': pathname.includes(routes.visitOperator)})}
                            >
                                <svg fill='currentColor' className='me-4' width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 512"><path d="M144 0a80 80 0 1 1 0 160A80 80 0 1 1 144 0zM512 0a80 80 0 1 1 0 160A80 80 0 1 1 512 0zM0 298.7C0 239.8 47.8 192 106.7 192l42.7 0c15.9 0 31 3.5 44.6 9.7c-1.3 7.2-1.9 14.7-1.9 22.3c0 38.2 16.8 72.5 43.3 96c-.2 0-.4 0-.7 0L21.3 320C9.6 320 0 310.4 0 298.7zM405.3 320c-.2 0-.4 0-.7 0c26.6-23.5 43.3-57.8 43.3-96c0-7.6-.7-15-1.9-22.3c13.6-6.3 28.7-9.7 44.6-9.7l42.7 0C592.2 192 640 239.8 640 298.7c0 11.8-9.6 21.3-21.3 21.3l-213.3 0zM224 224a96 96 0 1 1 192 0 96 96 0 1 1 -192 0zM128 485.3C128 411.7 187.7 352 261.3 352l117.3 0C452.3 352 512 411.7 512 485.3c0 14.7-11.9 26.7-26.7 26.7l-330.7 0c-14.7 0-26.7-11.9-26.7-26.7z" /></svg>
                                Visitor
                            </NavLink>
                            <NavLink to={routes.invoice} className={cx('link', 'group', 'relative', 'flex', 'items-center', 'gap-2.5', 'rounded-sm', 'py-2', 'px-4', 'font-medium', 'text-bodydark1', 'duration-300', 'ease-in-out', 'hover:bg-graydark', 'dark:hover:bg-meta-4', {'active': pathname.includes(routes.invoice)})}
                            >
                                <svg className='me-4' stroke="currentColor" fill="currentColor" strokeWidth="0" viewBox="0 0 24 24" height="24" width="24" xmlns="http://www.w3.org/2000/svg"><path d="M17 2V4H20.0066C20.5552 4 21 4.44495 21 4.9934V21.0066C21 21.5552 20.5551 22 20.0066 22H3.9934C3.44476 22 3 21.5551 3 21.0066V4.9934C3 4.44476 3.44495 4 3.9934 4H7V2H17ZM7 6H5V20H19V6H17V8H7V6ZM9 16V18H7V16H9ZM9 13V15H7V13H9ZM9 10V12H7V10H9ZM15 4H9V6H15V4Z"></path></svg>
                                Invoices
                            </NavLink>
                            <NavLink to={routes.payment} className={cx('link', 'group', 'relative', 'flex', 'items-center', 'gap-2.5', 'rounded-sm', 'py-2', 'px-4', 'font-medium', 'text-bodydark1', 'duration-300', 'ease-in-out', 'hover:bg-graydark', 'dark:hover:bg-meta-4', {'active': pathname.includes(routes.payment)})}
                            >
                                <svg height="24" width="24" className='me-4' stroke="currentColor" fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M512 80c8.8 0 16 7.2 16 16l0 32L48 128l0-32c0-8.8 7.2-16 16-16l448 0zm16 144l0 192c0 8.8-7.2 16-16 16L64 432c-8.8 0-16-7.2-16-16l0-192 480 0zM64 32C28.7 32 0 60.7 0 96L0 416c0 35.3 28.7 64 64 64l448 0c35.3 0 64-28.7 64-64l0-320c0-35.3-28.7-64-64-64L64 32zm56 304c-13.3 0-24 10.7-24 24s10.7 24 24 24l48 0c13.3 0 24-10.7 24-24s-10.7-24-24-24l-48 0zm128 0c-13.3 0-24 10.7-24 24s10.7 24 24 24l112 0c13.3 0 24-10.7 24-24s-10.7-24-24-24l-112 0z" /></svg>
                                Payments
                            </NavLink>
                            {/* <NavLink to={routes.adminFeedbacks} className={cx('link', 'group', 'relative', 'flex', 'items-center', 'gap-2.5', 'rounded-sm', 'py-2', 'px-4', 'font-medium', 'text-bodydark1', 'duration-300', 'ease-in-out', 'hover:bg-graydark', 'dark:hover:bg-meta-4')}
            >
              <svg fill="currentColor" className='me-4' width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M205 34.8c11.5 5.1 19 16.6 19 29.2l0 64 112 0c97.2 0 176 78.8 176 176c0 113.3-81.5 163.9-100.2 174.1c-2.5 1.4-5.3 1.9-8.1 1.9c-10.9 0-19.7-8.9-19.7-19.7c0-7.5 4.3-14.4 9.8-19.5c9.4-8.8 22.2-26.4 22.2-56.7c0-53-43-96-96-96l-96 0 0 64c0 12.6-7.4 24.1-19 29.2s-25 3-34.4-5.4l-160-144C3.9 225.7 0 217.1 0 208s3.9-17.7 10.6-23.8l160-144c9.4-8.5 22.9-10.6 34.4-5.4z" /></svg>
              Feedbacks
            </NavLink> */}
                            {/* <NavLink to={routes.adminParkingRights} className={cx('link', 'group', 'relative', 'flex', 'items-center', 'gap-2.5', 'rounded-sm', 'py-2', 'px-4', 'font-medium', 'text-bodydark1', 'duration-300', 'ease-in-out', 'hover:bg-graydark', 'dark:hover:bg-meta-4')}
                            >
                                <svg fill="currentColor" className='me-4' xmlns="http://www.w3.org/2000/svg" width='24' height='24' viewBox="0 0 576 512"><path d="M528 160l0 256c0 8.8-7.2 16-16 16l-192 0c0-44.2-35.8-80-80-80l-64 0c-44.2 0-80 35.8-80 80l-32 0c-8.8 0-16-7.2-16-16l0-256 480 0zM64 32C28.7 32 0 60.7 0 96L0 416c0 35.3 28.7 64 64 64l448 0c35.3 0 64-28.7 64-64l0-320c0-35.3-28.7-64-64-64L64 32zM272 256a64 64 0 1 0 -128 0 64 64 0 1 0 128 0zm104-48c-13.3 0-24 10.7-24 24s10.7 24 24 24l80 0c13.3 0 24-10.7 24-24s-10.7-24-24-24l-80 0zm0 96c-13.3 0-24 10.7-24 24s10.7 24 24 24l80 0c13.3 0 24-10.7 24-24s-10.7-24-24-24l-80 0z" /></svg>
                                Parking Rights
                            </NavLink>
                            <NavLink to={routes.services} className={cx('link', 'group', 'relative', 'flex', 'items-center', 'gap-2.5', 'rounded-sm', 'py-2', 'px-4', 'font-medium', 'text-bodydark1', 'duration-300', 'ease-in-out', 'hover:bg-graydark', 'dark:hover:bg-meta-4')}
                            >
                                <svg fill='currentColor' width='24' height='24' className='me-4' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M142.9 142.9c-17.5 17.5-30.1 38-37.8 59.8c-5.9 16.7-24.2 25.4-40.8 19.5s-25.4-24.2-19.5-40.8C55.6 150.7 73.2 122 97.6 97.6c87.2-87.2 228.3-87.5 315.8-1L455 55c6.9-6.9 17.2-8.9 26.2-5.2s14.8 12.5 14.8 22.2l0 128c0 13.3-10.7 24-24 24l-8.4 0c0 0 0 0 0 0L344 224c-9.7 0-18.5-5.8-22.2-14.8s-1.7-19.3 5.2-26.2l41.1-41.1c-62.6-61.5-163.1-61.2-225.3 1zM16 312c0-13.3 10.7-24 24-24l7.6 0 .7 0L168 288c9.7 0 18.5 5.8 22.2 14.8s1.7 19.3-5.2 26.2l-41.1 41.1c62.6 61.5 163.1 61.2 225.3-1c17.5-17.5 30.1-38 37.8-59.8c5.9-16.7 24.2-25.4 40.8-19.5s25.4 24.2 19.5 40.8c-10.8 30.6-28.4 59.3-52.9 83.8c-87.2 87.2-228.3 87.5-315.8 1L57 457c-6.9 6.9-17.2 8.9-26.2 5.2S16 449.7 16 440l0-119.6 0-.7 0-7.6z" /></svg>
                                Entry Rights
                            </NavLink> */}
                            <NavLink to={routes.services} className={cx('link', 'group', 'relative', 'flex', 'items-center', 'gap-2.5', 'rounded-sm', 'py-2', 'px-4', 'font-medium', 'text-bodydark1', 'duration-300', 'ease-in-out', 'hover:bg-graydark', 'dark:hover:bg-meta-4', {'active': pathname.includes(routes.services)})}
                            >
                                <svg fill='currentColor' className='me-4' width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 512"><path d="M144 0a80 80 0 1 1 0 160A80 80 0 1 1 144 0zM512 0a80 80 0 1 1 0 160A80 80 0 1 1 512 0zM0 298.7C0 239.8 47.8 192 106.7 192l42.7 0c15.9 0 31 3.5 44.6 9.7c-1.3 7.2-1.9 14.7-1.9 22.3c0 38.2 16.8 72.5 43.3 96c-.2 0-.4 0-.7 0L21.3 320C9.6 320 0 310.4 0 298.7zM405.3 320c-.2 0-.4 0-.7 0c26.6-23.5 43.3-57.8 43.3-96c0-7.6-.7-15-1.9-22.3c13.6-6.3 28.7-9.7 44.6-9.7l42.7 0C592.2 192 640 239.8 640 298.7c0 11.8-9.6 21.3-21.3 21.3l-213.3 0zM224 224a96 96 0 1 1 192 0 96 96 0 1 1 -192 0zM128 485.3C128 411.7 187.7 352 261.3 352l117.3 0C452.3 352 512 411.7 512 485.3c0 14.7-11.9 26.7-26.7 26.7l-330.7 0c-14.7 0-26.7-11.9-26.7-26.7z" /></svg>
                                Service Register
                            </NavLink>

                            <div onClick={handleLogout} to={routes.login} className={cx('link', 'group', 'relative', 'flex', 'items-center', 'gap-2.5', 'rounded-sm', 'py-2', 'px-4', 'font-medium', 'text-bodydark1', 'duration-300', 'ease-in-out', 'hover:bg-graydark', 'dark:hover:bg-meta-4')}
                            >
                                <svg fill='currentColor' className='me-4' width={24} height={24} xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M502.6 278.6c12.5-12.5 12.5-32.8 0-45.3l-128-128c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L402.7 224 192 224c-17.7 0-32 14.3-32 32s14.3 32 32 32l210.7 0-73.4 73.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0l128-128zM160 96c17.7 0 32-14.3 32-32s-14.3-32-32-32L96 32C43 32 0 75 0 128L0 384c0 53 43 96 96 96l64 0c17.7 0 32-14.3 32-32s-14.3-32-32-32l-64 0c-17.7 0-32-14.3-32-32l0-256c0-17.7 14.3-32 32-32l64 0z" /></svg>
                                Logout
                            </div>
                        </div>
                    </nav>
                    {/* <!-- Sidebar Menu --> */}
                </div>
            </aside>
        </div>
    </div>);
}

export default Home;