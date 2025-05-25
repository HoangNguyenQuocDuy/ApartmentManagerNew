import classnames from 'classnames/bind'
import '../../components/GlobalStyles/globalStyle.scss'

import { useDispatch, useSelector } from 'react-redux';
import { FaArrowLeftLong, FaCheck, FaPlus, FaRegPenToSquare } from 'react-icons/fa6';
import images from '../../assets/images';
import { NavLink } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import ReactLoading from 'react-loading';
import { PiTrashSimpleBold } from 'react-icons/pi';
import AddRelative from '../../components/AddRelative';
import newRequest from '../../untils/request';
import { setIsActiveNavbar, setIsLoadRelative } from '../../store/slice/applicationSlice';
import { deleteRelative } from '../../store/slice/relativesSlice';
import { loadUser } from '../../store/slice/userSlice';
import { ToastContainer, toast } from 'react-toastify';
import { notify } from '../../untils/notification'
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { RootEnum } from '../../assets/enum';

import styles from './visitRequest.module.scss'
import { IoIosArrowDown } from 'react-icons/io';
import moment from 'moment';
import { Pagination } from 'flowbite-react';
import Status from '../Status';

const cx = classnames.bind(styles)

const VisitRequest = () => {
    const dispatch = useDispatch()
    const user = useSelector(state => state.user)

    const accessToken = localStorage.getItem('accessToken')
    const roomIds = useSelector(state => state.rooms)
    const [selectedRoomId, setSelectedRoomId] = useState(roomIds && roomIds.length > 0 ? roomIds[0] : null)
    const [roomActive, setRoomActive] = useState(false)
    const [addingVisitRequest, setAddingVisitRequest] = useState(false)
    const [visitRequests, setVisitRequests] = useState()
    const [stompClient, setStompClient] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const onPageChange = page => {
        console.log(page)
        setCurrentPage(page)
    }
    const { pageSize } = useSelector(state => state.app)

    const [formData, setFormData] = useState({
        residentId: user.id,
        visitDate: "",
        expectedCheckinTime: "",
        roomId: "",
        visitors: [{ fullname: "", phone: "", idCardNumber: "" }],
    });

    const handleVisitorChange = (index, field, value) => {
        const updatedVisitors = [...formData.visitors];
        updatedVisitors[index][field] = value;
        setFormData({ ...formData, visitors: updatedVisitors });
    };

    const addVisitor = () => {
        setFormData({
            ...formData,
            visitors: [...formData.visitors, { fullname: "", phone: "", idCardNumber: "" }],
        });
    };

    const removeVisitor = (index) => {
        const updatedVisitors = formData.visitors.filter((_, i) => i !== index);
        setFormData({ ...formData, visitors: updatedVisitors });
    };

    const handleCreateVisitorRequest = async (e) => {
        e.preventDefault();
        formData.roomId = selectedRoomId
        console.log('visitor request data: ', formData)
        setAddingVisitRequest(true)
        await newRequest.post(`${RootEnum.API_VISIT}visitRequests/`, formData, {
            headers: {
                "Authorization": `Bearer ${accessToken}`
            }
        })
            .then(data => {
                console.log('data from create visit request: ', data.data)
                handleGetListVisitRequest()

                setFormData({
                    residentId: user.id,
                    visitDate: "",
                    expectedCheckinTime: "",
                    roomId: "",
                    visitors: [{ fullname: "", phone: "", idCardNumber: "" }],
                })
            })
            .catch(err => {
                console.log('error from create visit request: ', err)
            })
            .finally(() => {
                setAddingVisitRequest(false)
            })
    };

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    useEffect(() => {
        dispatch(setIsActiveNavbar(true))

    }, [])
    useEffect(() => {
        handleGetListVisitRequest()
    }, [currentPage])

    const handleGetListVisitRequest = async () => {
        try {
            const response = await newRequest.get(`${RootEnum.API_VISIT}visitRequests/?page=${currentPage - 1}&size=${pageSize}&userId=${user.id}`, {
                headers: {
                    "Authorization": `Bearer ${accessToken}`
                }
            });
            console.log('visit requests: ', response.data.data)
            setVisitRequests(response.data.data)
        } catch (error) {
            console.log('Error when get rooms by userId:', error);
        }
    }

    useEffect(() => {
        if (user) {

            const accessToken = localStorage.getItem("accessToken")
            const socket = new SockJS('http://localhost:8989/ws');
            const client = new Client({
                webSocketFactory: () => socket,
                connectHeaders: {
                    'X-User-Id': user.id // <== Gửi user ID bạn đang đọc trong backend
                },
                // connectHeaders: {
                //     Authorization: `Bearer ${accessToken}`
                // },
                debug: (str) => console.log(str),
            });

            client.onConnect = () => {
                console.log('Stomp client connected');
                setStompClient(client);
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
        <div className={cx('container')}>
            <ToastContainer />
            <div className={cx('wrapper')}>
                <div className={cx('infomations')}>
                    <div>
                        <span className={cx('sub-title')}>Create Visit Request</span>
                    </div>
                    <form className={cx("visit-request-form")} onSubmit={handleCreateVisitorRequest}>
                        {/* <label>Visit Date</label>
                        <input type="datetime-local" name="visitDate" value={formData.visitDate} onChange={handleChange} required /> */}

                        <div className={cx('fieldBox')}>
                            <label htmlFor='rType'>Check-in</label>
                            <input type="datetime-local" name="expectedCheckinTime" value={formData.expectedCheckinTime} onChange={handleChange} required />
                        </div>

                        {/* <label>Room ID</label>
                        <input type="number" name="roomId" value={formData.roomId} onChange={handleChange} required /> */}

                        <div className={cx('fieldBox')}>
                            <label htmlFor='rType'>Room ID</label>
                            <div onClick={() => { setRoomActive(!roomActive) }} className={cx('type', 'selectedType')}>
                                <span>{selectedRoomId}</span>
                                <IoIosArrowDown size={20} className={cx('arrowIcon')} />
                            </div>
                            <ul className={cx('list', 'contract', { 'active': roomActive })}>
                                {roomIds && roomIds.map((r, i) => <li className={cx('type', { active: r === selectedRoomId })}
                                    onClick={() => {
                                        setSelectedRoomId(r)
                                        setRoomActive(false)
                                    }} key={i} >{r}</li>)}
                            </ul>
                        </div>

                        <div className={cx('wrap-btn', 'd-flex')} onClick={addVisitor}>
                            <h3>Visitors</h3>
                            <div className={cx('btnBoxAdd')}>
                                <button className={cx('btn')}>
                                    Add Visitor
                                </button>
                                <FaPlus size={24} className={cx('plusIcon')} />
                            </div>
                        </div>
                        <div className={cx('visitors')}>
                            <div className={cx('scroll-box')}>
                                {formData.visitors.map((visitor, index) => (
                                    <div key={index} className={cx("visitor-group")}>
                                        <input
                                            type="text"
                                            placeholder="Full Name"
                                            value={visitor.fullname}
                                            onChange={(e) => handleVisitorChange(index, "fullname", e.target.value)}
                                            required
                                            className='mb-1'
                                        />
                                        <input
                                            type="number"
                                            placeholder="Phone"
                                            value={visitor.phone}
                                            onChange={(e) => handleVisitorChange(index, "phone", e.target.value)}
                                            required
                                            className='mb-1'
                                        />
                                        <input
                                            type="number"
                                            placeholder="ID Card Number"
                                            value={visitor.idCardNumber}
                                            onChange={(e) => handleVisitorChange(index, "idCardNumber", e.target.value)}
                                            required
                                        />
                                        {formData.visitors.length > 1 && (
                                            <button type="button" onClick={() => removeVisitor(index)} className={cx("remove-btn")}>Remove</button>
                                        )}
                                    </div>
                                ))}
                            </div>
                        </div>

                        <div className={cx('btnBox')}>
                            <button type='submit' className={cx('btn', { prevent: addingVisitRequest })}>
                                Add and wait for confirmation
                            </button>
                            {addingVisitRequest && <ReactLoading className={cx('loading')} type='spin' color={'#999'} height={'5%'} width={'5%'} />}
                        </div>
                    </form>


                </div>

                <div className={cx('visitRequests')}>
                    <table className={cx('table')} cellSpacing="0" cellPadding="0">
                        <thead>
                            <tr>
                                <th>Expected Checkin Time</th>
                                <th>Visit Date</th>
                                <th>Status</th>
                                <th>Room Id</th>
                                <th>Visitors</th>
                            </tr>
                        </thead>
                        <tbody>
                            {visitRequests && visitRequests.content.map((f, key) => (
                                <tr key={key}>
                                    <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                        <p className={`pl-2 text-black dark:text-white`}>
                                            {moment(f.expectedCheckinTime).format("HH:mm:ss DD/MM/YYYY")}
                                        </p>
                                    </td>
                                    <td className="border-b border-[#eee] py-2 px-2 dark:border-strokedark">
                                        <p className="pl-2 font-medium text-black dark:text-white">
                                            {f.visitDate ? f.visitDate : '------'}
                                        </p>
                                    </td>
                                    <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                        <p className="pl-2 text-black dark:text-white">
                                            <Status status={f.status} key={key} />
                                        </p>
                                    </td>
                                    <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                        <p className={`pl-2 text-black dark:text-white`}>
                                            {f.roomId}
                                        </p>
                                    </td>
                                    <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                        <p className={`pl-2 text-black dark:text-white`}>
                                            {f.roomId}
                                        </p>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <div className={cx('pageBox')}>
                        <Pagination currentPage={currentPage} totalPages={visitRequests ? visitRequests.totalPages : 0} onPageChange={onPageChange} showIcons />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default VisitRequest;
