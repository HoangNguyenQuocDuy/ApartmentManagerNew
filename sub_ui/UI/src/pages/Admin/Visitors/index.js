import { useEffect, useState } from "react";
import newRequest from "../../../untils/request";
import { useSelector } from "react-redux";
import { Pagination } from "flowbite-react";
import { RootEnum } from "../../../assets/enum";
import { notify } from "../../../untils/notification";
import { ToastContainer } from "react-toastify";
import { IoIosArrowDown } from "react-icons/io";
import styles from './visitors.module.scss'
import classnames from "classnames/bind";
import SearchBtn from "../../../components/SearchBtn";

const cx = classnames.bind(styles)

function Visitors() {
    const accesstoken = localStorage.getItem("accessToken")
    const [openUpdateLockerBox, setOpenUpdateLockerBox] = useState(false)
    const [listVisitRequestVisitor, setListVisitRequestVisitor] = useState()
    const status = ['No select', 'Pending', 'Confirmed', 'Rejected']
    const [selectedStatus, setSelectedStatus] = useState(status[0])
    const [statusActive, setStatusActive] = useState(false)
    const { theme } = useSelector(state => state.app)
    const [isOpenDropDown, setIsOpenDropDown] = useState(false)
    const [rooms, setRooms] = useState()
    const [roomIdActive, setRoomIdActive] = useState(false)
    const [selectedRoomId, setSelectedRoomId] = useState()

    const [currentPage, setCurrentPage] = useState(1);
    const onPageChange = page => {
        console.log(page)
        setCurrentPage(page)
    }
    const [visitLogs, setVisitLogs] = useState()
    const { pageSize } = useSelector(state => state.app)
    const user = useSelector(state => state.user)

    const handleGetListRoomAvailable = async () => {
        newRequest.get(`${RootEnum.API_ROOM}?all=true&filter=status==OCCUPIED`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log(data.data.data)

                setRooms(data.data.data.data.items)
                // setSelectedRoomId(data.data.data.data.items[0].id)
            })
            .catch(err => {
                notify('Error getting rooms: ' + err, 'error')
            })
    }

    const handleUpdateVisitor = ({ visitRequestId, status }) => {
        newRequest.patch(`${RootEnum.API_VISIT}visitRequests/${visitRequestId}`, { status }, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log('Update visit request successful: ', data.data.data)
                handleGetListVisitRequest()
            })
            .catch(err => {
                alert('Error when upadting visit request: ' + err)
            })
    }

    const handleGetListVisitRequest = async (q) => {
        await newRequest.get(`${RootEnum.API_VISIT}visitRequestVisitor/?page=${currentPage - 1}&size=${pageSize}${q ? q : ''}`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        }).then(data => {
            console.log('visit requests: ', data.data.data)
            setListVisitRequestVisitor(data.data.data.data)
        })
            .catch(error => {
                console.log('Error when get rooms by userId:', error);
            })
    }

    const handleCheckin = async ({ idCardNumber, visitRequestId }, status) => {
        if (status.toLowerCase() !== 'confirmed') {
            notify('Please confirmed this request before!', 'warn')
        } else if (status.toLowerCase() !== 'rejected') {
            notify('This request is rejected!', 'warn')
        } else {
            newRequest.post(`${RootEnum.API_VISIT}visit_securities/checkin`, {
                idCardNumber,
                staffId: user.id,
                visitRequestId
            }, {
                headers: {
                    "Authorization": `Bearer ${accesstoken}`
                }
            })
                .then(data => {
                    console.log('Security log created: ', data.data)
                    handleGetListVisitRequest()
                    notify('Checkin successful!', 'success')
                })
                .catch(err => {
                    console.log('Error upadting visit log: ' + err)
                    notify('Error upadting visit log: ' + err, 'error')
                })
        }
    }

    const handleCheckout = async (securityLogId) => {
        newRequest.patch(`${RootEnum.API_VISIT}visit_securities/checkout/${securityLogId}`, {
            staffId: user.id
        }, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log('Security log created: ', data.data)
                handleGetListVisitRequest()
                notify('Checkout successful!', 'success')
            })
            .catch(err => {
                console.log('Error upadting visit log: ' + err)
                notify('Error upadting visit log: ' + err, 'error')
            })
    }

    useEffect(() => {
        handleGetListVisitRequest()
        handleGetListRoomAvailable()
    }, [currentPage])
    useEffect(() => {
        handleGetListRoomAvailable()
    }, [])

    const handleCreateVisitorRequest = async (e) => {
        // formData.roomId = selectedRoomId
        // console.log('visitor request data: ', formData)
        // setAddingVisitRequest(true)
        // await newRequest.post(`${RootEnum.API_VISIT}listVisitRequestVisitor/`, formData, {
        //     headers: {
        //         "Authorization": `Bearer ${accessToken}`
        //     }
        // })
        //     .then(data => {
        //         console.log('data from create visit request: ', data.data)
        //         handleGetListVisitRequest()

        //         setFormData({
        //             residentId: user.id,
        //             visitDate: "",
        //             expectedCheckinTime: "",
        //             roomId: "",
        //             visitors: [{ fullname: "", phone: "", idCardNumber: "" }],
        //         })
        //     })
        //     .catch(err => {
        //         console.log('error from create visit request: ', err)
        //     })
        //     .finally(() => {
        //         setAddingVisitRequest(false)
        //     })
    };

    const formatJavaDateArray = (dateArray) => {
        const [year, month, day, hour, minute, second] = dateArray;

        const pad = (n) => String(n).padStart(2, '0');

        return `${pad(hour)}h${pad(minute)} ${day}/${pad(month)}/${pad(year)}`;
    }

    //
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const formatDateTime = (dateStr) => {
        if (!dateStr) return '';
        // Từ "2025-05-09T23:30" thành "2025-05-09 23:30:00"
        return encodeURIComponent(dateStr.replace('T', ' ') + ':00');
    };
    const handleSearch = () => {
        let q = ''
        if (selectedStatus !== 'No select') {
            q += `&status=${selectedStatus}`
        }
        if (selectedRoomId) {
            q += `&roomId=${selectedRoomId}`
        }
        if (startDate) {
            console.log(startDate)
            const fromDateFormatted = formatDateTime(startDate);
            q += `&fromDate=${fromDateFormatted}`;

            if (endDate) {
                const toDateFormatted = formatDateTime(endDate);
                q += `&toDate=${toDateFormatted}`;
            }
        }
        handleGetListVisitRequest(q)
    }

    return (
        <div className="m-10">
            <ToastContainer />
            <div className="w-full flex justify-end text-standard">
                <div className="p-4">
                    <div className="mb-4 flex items-end">
                        <div className="ml-4 dark:text-white">
                            <label className="block text-sm font-medium">From date</label>
                            <input
                                type="datetime-local"
                                value={startDate}
                                onChange={(e) => setStartDate(e.target.value)}
                                className={cx('title', 'input-block')}
                            />
                        </div>
                        <div className="ml-4 dark:text-white">
                            <label className="block text-sm font-medium">To date</label>
                            <input
                                type="datetime-local"
                                value={endDate}
                                onChange={(e) => setEndDate(e.target.value)}
                                className={cx('title', 'input-block')}
                            />
                        </div>
                        <div className="ml-4 dark:text-white">
                            <label className="block text-sm font-medium">Room ID</label>
                            <div className={cx('statusList')}>
                                <div onClick={() => { setRoomIdActive(!roomIdActive) }} className={cx('title')}>
                                    <div>{!selectedRoomId ? 'Room ID' : selectedRoomId}</div>
                                    <IoIosArrowDown size={20} />
                                </div>
                                <ul className={cx('list', { 'active': roomIdActive })}>
                                    {rooms && rooms.map((t, i) => <li className={cx({ active: t.id === selectedRoomId, dark: theme === 'dark' })}
                                        onClick={() => {
                                            setSelectedRoomId(t.id)
                                            setRoomIdActive(false)
                                        }} key={i} >{t.id}</li>)}
                                </ul>
                            </div>
                        </div>


                        {/* <div>
                            <label className="block text-sm font-medium">Status</label>
                            <select
                                value={status}
                                onChange={(e) => setStatus(e.target.value)}
                                className="p-2  pl-4 mt-1 block w-full rounded-md border-gray-300 shadow-sm"
                            >
                                <option className="pr-4" value="">Tất cả</option>
                                <option value="PENDING">Pending</option>
                                <option value="APPROVED">Approved</option>
                                <option value="REJECTED">Rejected</option>
                                <option value="CHECKED_IN">Checked-in</option>
                                <option value="CHECKED_OUT">Checked-out</option>
                            </select>
                        </div> */}
                        <div className="ml-4 dark:text-white">
                            <label className="block text-sm font-medium">Status</label>
                            <div className={cx('statusList')}>
                                <div onClick={() => { setStatusActive(!statusActive) }} className={cx('title')}>
                                    <div>{selectedStatus === '' ? 'Status' : selectedStatus}</div>
                                    <IoIosArrowDown size={20} />
                                </div>
                                <ul className={cx('list', { 'active': statusActive })}>
                                    {status.map((t, i) => <li className={cx({ active: t === selectedStatus, dark: theme === 'dark' })}
                                        onClick={() => {
                                            setSelectedStatus(t)
                                            setStatusActive(false)
                                        }} key={i} >{t}</li>)}
                                </ul>
                            </div>
                        </div>
                        <div className="ml-4">
                            <SearchBtn handleSearch={handleSearch} />
                        </div>
                    </div>
                </div>
            </div>
            <div className={`pb-4 relative transition-all duration-500 
                    ${openUpdateLockerBox ? 'top-60' : 'top-0'}
                `}>
                <div className={`rounded-lg flex-col mb-20 border mt-4 border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1 transition`}>
                    <div className="max-w-full ">
                        <table className="w-full table-auto">
                            <thead>
                                <tr className="bg-gray-2 text-left dark:bg-meta-4 transition">
                                    <th className="text-standard min-w-[150px] py-4 px-4 font-medium text-black dark:text-white">
                                        Visitor Name
                                    </th>
                                    <th className="text-standard min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
                                        Phone number
                                    </th>
                                    <th className="text-standard min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
                                        Id card number
                                    </th>
                                    <th className="text-standard min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
                                        Room Id
                                    </th>
                                    <th className="text-standard min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
                                        Expected checkin
                                    </th>
                                    <th className="text-standard min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
                                        Status
                                    </th>
                                    <th className="text-standard min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
                                        Entry time
                                    </th>
                                    <th className="text-standard min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
                                        Exit time
                                    </th>
                                    <th className="text-standard py-4 px-4 font-medium text-black dark:text-white">
                                        Actions
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                {listVisitRequestVisitor && listVisitRequestVisitor.items.map((v, key) => (
                                    <tr key={key}>
                                        <td className="border-b border-[#eee] py-2 px-2 pl-9 dark:border-strokedark xl:pl-11">
                                            <h5 className="font-medium text-black dark:text-white">
                                                {v.visitor.fullName}
                                            </h5>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {v.visitor.phone}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {v.visitor.idCardNumber}
                                            </p>
                                        </td>

                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {v.visitRequest.roomId}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {formatJavaDateArray(v.visitRequest.expectedCheckinTime)}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p
                                                className={`inline-flex rounded-full bg-opacity-10 py-1 px-3 text-sm font-medium ${v.visitRequest.status === 'CONFIRMED'
                                                    ? 'bg-success text-success' : v.visitRequest.status === 'PENDING' ? 'bg-warning text-warning' : 'bg-danger text-danger'}`}
                                            >
                                                {v.visitRequest.status.substring(0, 1) + v.visitRequest.status.toLowerCase().substring(1)}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {
                                                    v.visitRequest.status.toLowerCase() === 'rejected' ? '----------' : v.checkinTime ? formatJavaDateArray(v.checkinTime) :
                                                        <button onClick={() => { handleCheckin({ idCardNumber: v.visitor.idCardNumber, visitRequestId: v.visitRequest.id }, v.visitRequest.status) }}
                                                            className={`flex transition-opacity duration-150 p-2 opacity-80 rounded-md bg-green-400 text-white transition-colors hover:opacity-100`}
                                                        >
                                                            <span className="mr-2">Check-in</span>
                                                            <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M438.6 105.4c12.5 12.5 12.5 32.8 0 45.3l-256 256c-12.5 12.5-32.8 12.5-45.3 0l-128-128c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0L160 338.7 393.4 105.4c12.5-12.5 32.8-12.5 45.3 0z" /></svg>
                                                        </button>
                                                }
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {/* {moment(v.exitTime).format("HH:mm:ss DD/MM/YYYY")} */}
                                                {
                                                    v.checkinTime === null ? '----------' : v.checkoutTime ? formatJavaDateArray(v.checkoutTime) :
                                                        <button onClick={() => { handleCheckout(v.visitRequest.securityLogs[0].id) }}
                                                            className={`flex transition-opacity duration-150 p-2 opacity-80 rounded-md bg-red-400 text-white transition-colors hover:opacity-100`}
                                                        >
                                                            <span className="mr-2">Check-out</span>
                                                            <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512"><path d="M342.6 150.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L192 210.7 86.6 105.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L146.7 256 41.4 361.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0L192 301.3 297.4 406.6c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L237.3 256 342.6 150.6z" /></svg>

                                                            {/* <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M438.6 105.4c12.5 12.5 12.5 32.8 0 45.3l-256 256c-12.5 12.5-32.8 12.5-45.3 0l-128-128c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0L160 338.7 393.4 105.4c12.5-12.5 32.8-12.5 45.3 0z" /></svg> */}
                                                        </button>
                                                }

                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            {
                                                v.visitRequest.status.toLowerCase() === 'pending' ?
                                                    <div className="flex items-center space-x-3.5">
                                                        <button onClick={() => { handleUpdateVisitor({ visitRequestId: v.visitRequest.id, status: 'Confirmed' }) }} className={`hover:text-success transition-colors`}>
                                                            <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M438.6 105.4c12.5 12.5 12.5 32.8 0 45.3l-256 256c-12.5 12.5-32.8 12.5-45.3 0l-128-128c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0L160 338.7 393.4 105.4c12.5-12.5 32.8-12.5 45.3 0z" /></svg>
                                                        </button>
                                                        <button onClick={() => { handleUpdateVisitor({ visitRequestId: v.visitRequest.id, status: 'Rejected' }) }} className="hover:text-danger transition-colors">
                                                            <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512"><path d="M342.6 150.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L192 210.7 86.6 105.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L146.7 256 41.4 361.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0L192 301.3 297.4 406.6c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L237.3 256 342.6 150.6z" /></svg>
                                                        </button>
                                                    </div>
                                                    : '-----'
                                            }
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
                <div className="flex sm:justify-center mb-20 absolute w-full">
                    <Pagination currentPage={currentPage} totalPages={listVisitRequestVisitor ? listVisitRequestVisitor.totalPages : 0} onPageChange={onPageChange} showIcons />
                </div>
            </div>
        </div >
    );
}

export default Visitors;