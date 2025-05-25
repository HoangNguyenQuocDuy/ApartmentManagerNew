import { useEffect, useRef, useState } from "react";
import newRequest from "../../../untils/request";
import { Pagination } from "flowbite-react";
import { useDispatch, useSelector } from "react-redux";
import { notify } from "../../../untils/notification";
import Chat from "../../../components/Chat";
import { setChatRoomSelected } from "../../../store/slice/applicationSlice";
import { fetchMessageByRoomId } from "../../../store/slice/messageSlice";
import { fetchRoomByUserId } from "../../../store/slice/chatRoomSlice";
import { RootEnum } from "../../../assets/enum";
import { toast, ToastContainer } from "react-toastify";

function Residents() {
    const accesstoken = localStorage.getItem("accessToken")
    const usernameInputRef = useRef()

    const { id } = useSelector(state => state.user)
    const roomsChat = useSelector(state => state.roomsChat)

    const [openAddUserBox, setOpenAddUserBox] = useState(false)
    const [selectedRoom, setSelectedRoom] = useState()
    const [confirmOrder, setConfirmOrder] = useState()

    const [username, setUsername] = useState('')
    const [firstname, setFirstname] = useState('')
    const [lastname, setLastname] = useState('')
    const [email, setEmail] = useState('')
    const [phone, setPhone] = useState('')

    const [listRoomUnoccupied, setListRoomUnoccupied] = useState()
    const [openChatBox, setOpenChatBox] = useState(false)
    const [selectedUser, setSelectedUser] = useState()

    const [currentPage, setCurrentPage] = useState(1);
    const onPageChange = page => {
        console.log(page)
        setCurrentPage(page)
    };
    const { pageSize } = useSelector(state => state.app)
    const [dataUserPaging, setDataUserPaging] = useState()
    const dispatch = useDispatch()

    useEffect(() => {
        if (!openAddUserBox) {
            setConfirmOrder()
            setOpenAddUserBox(false)
            setUsername('')
            setFirstname('')
            setLastname('')
            setEmail('')
            setPhone('')
        }
        handleGetListUser()
        // getListRoomUnoccupied()
        // getListLockerUnoccupied()

    }, [openAddUserBox, currentPage])

    const handleGetListUser = async () => {
        console.log(pageSize, accesstoken)
        newRequest.get(`${RootEnum.API_USER}?filter=roleName==ROLE_RESIDENT&page=${currentPage - 1}&size=${pageSize}`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log('list user: ', data.data.data)
                setDataUserPaging(data.data.data.data)
            })
            .catch(err => {
                notify('Error getting residents: ' + err, 'error')
            })
    }

    const handleUpdateResident = async (userId) => {
        if (window.confirm('Do you want to disable this resident?')) {
            newRequest.patch(`${RootEnum.API_USER + userId}?status=Disable`, {}, {
                headers: {
                    "Authorization": `Bearer ${accesstoken}`
                }
            })
                .then(data => {
                    console.log(data.data.data)
                    handleGetListUser()
                })
                .catch(err => {
                    notify('Error deleting residents: ' + err, 'error')
                })
        }
    }

    const handleAddResident = async () => {
        await newRequest.post(`${RootEnum.API_USER}`, {
            username, firstname, lastname, email, phone
        }, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(() => {
                // getListLockerUnoccupied()
                // getListRoomUnoccupied()
                handleGetListUser()
                setOpenAddUserBox(false)
                // dispatch(fetchRoomByUserId(accesstoken))

                notify('Add new resident successful!', 'success')
            })
            .catch((err) => {
                notify('Error when creating resident: ' + err.response.data.message, 'error')
                // alert(err.response.data.message)
                console.log(err)
            })
    }

    const getListRoomUnoccupied = async () => {
        await newRequest.get(`${RootEnum.API_ROOM}list?status=Available`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log('Room Unoccupied: ', data.data.data)
                setListRoomUnoccupied(data.data.data)
                setSelectedRoom(data.data.data[0].id)
            })
            .catch(err => {
                notify('Error when getting list room unoccupied: ' + err, 'error')
            })
    }

    // const getListLockerUnoccupied = async () => {
    //     await newRequest.get('/lockers/list?status=Blank', {
    //         headers: {
    //             "Authorization": `Bearer ${accesstoken}`
    //         }
    //     })
    //         .then(data => {
    //             console.log('Lockers Unoccupied: ', data.data.data)
    //             setListLockerUnoccupied(data.data.data)
    //             setSelectedLocker(data.data.data[0].id)
    //         })
    //         .catch(err => {
    //             notify('Error when getting list locker unoccupied: ' + err, 'error')
    //         })
    // }

    // useEffect(() => {
    //     dispatch(fetchRoomByUserId(accesstoken))
    // }, [])

    return (
        <div className="m-10 h-screen transition">
            <ToastContainer />
            <div className="w-full relative text-standard">
                {/* <ToastContainer /> */}
                <button onClick={() => setOpenAddUserBox(state => !state)}
                    className={`flex items-center justify-center ${openAddUserBox ? 'bg-green-500 text-white' :
                        'hover:bg-green-500 text-green-500 hover:text-white'} 
                        py-3 px-6 border border-green-500 hover:border-transparent rounded-md absolute right-0 border-solid transition z-10`}>
                    <span>Add Resident</span>
                    <span className="w-4 mx-3">
                        <svg fill="currentColor" width='14' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M256 80c0-17.7-14.3-32-32-32s-32 14.3-32 32l0 144L48 224c-17.7 0-32 14.3-32 32s14.3 32 32 32l144 0 0 144c0 17.7 14.3 32 32 32s32-14.3 32-32l0-144 144 0c17.7 0 32-14.3 32-32s-14.3-32-32-32l-144 0 0-144z" /></svg>
                    </span>
                </button>
                <form onSubmit={(e) => { e.preventDefault() }} className="flex absolute justify-center items-center w-full background-12">
                    <div className={
                        `bg-white px-6 pt-4 rounded-xl pb-6 w-2/4 mt-10 shadow-lg dark:border-strokedark dark:bg-boxdark dark:text-white duration-500 origin-top
                            ${openAddUserBox ? 'scale-1 visible h-auto opacity-100' : 'h-0 scale-0 h-0 invisible opacity-0'}`
                    }>
                        <h2 className="text-standard font-semibold leading-7 text-gray-900 mb-4 mt-2 text-center">Resident Box</h2>
                        <div className="">
                            <div className="w-full">
                                <div className="">
                                    <label htmlFor="username" className="text-standard block text-sm font-medium leading-6 text-gray-900">
                                        Username
                                    </label>
                                    <div className="mt-2 border-transparent rounded-lg ">
                                        <div className="flex w-full rounded-md">
                                            <input
                                                ref={usernameInputRef}
                                                onChange={e => setUsername(e.target.value)}
                                                value={username}
                                                id="username"
                                                name="username"
                                                type="text"
                                                placeholder="Username..."
                                                className="text-standard w-full shadow-md border-1 bg-transparent py-2 ps-4 shadow-md rounded-md"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="w-full mt-4 flex justify-between pe-8">
                                <div className="w-2/4 pe-8">
                                    <label htmlFor="firstname" className="text-standard block text-sm font-medium leading-6 text-gray-900">
                                        Firstname
                                    </label>
                                    <div className="mt-2 border-transparent rounded-lg">
                                        <div className="flex w-full rounded-md">
                                            <input
                                                onChange={e => setFirstname(e.target.value)}
                                                value={firstname}
                                                id="firstname"
                                                name="firstname"
                                                type="text"
                                                placeholder="Firstname..."
                                                className="text-standard w-full shadow-md border-1 bg-transparent py-2 ps-4 shadow-md rounded-md"
                                            />
                                        </div>
                                    </div>
                                </div>
                                <div className="w-2/4 ps-6">
                                    <label htmlFor="lastname" className="text-standard block text-sm font-medium leading-6 text-gray-900">
                                        Lastname
                                    </label>
                                    <div className="mt-2 border-transparent rounded-lg">
                                        <div className="flex w-full rounded-md">
                                            <input
                                                onChange={e => setLastname(e.target.value)}
                                                value={lastname}
                                                id="lastname"
                                                name="lastname"
                                                type="text"
                                                placeholder="Lastname..."
                                                className="text-standard w-full shadow-md border-1 bg-transparent py-2 ps-4 shadow-md rounded-md"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="w-full mt-4 flex justify-between">
                                <div className="w-2/4 pe-12">
                                    <label htmlFor="email" className="text-standard block text-sm font-medium leading-6 text-gray-900">
                                        Email
                                    </label>
                                    <div className="mt-2 border-transparent rounded-lg">
                                        <div className="flex w-full rounded-md">
                                            <input
                                                onChange={e => setEmail(e.target.value)}
                                                value={email}
                                                id="email"
                                                name="email"
                                                type="email"
                                                placeholder="abc@gmail.com"
                                                className="text-standard w-full shadow-md border-1 bg-transparent py-2 ps-4 shadow-md rounded-md"
                                            />
                                        </div>
                                    </div>
                                </div>

                                <div className="pe-8 w-2/4 ps-2">
                                    <label htmlFor="phone" className="text-standard block text-sm font-medium leading-6 text-gray-900">
                                        Phone
                                    </label>
                                    <div className="mt-2 border-transparent rounded-lg">
                                        <div className="flex w-full rounded-md">
                                            <input
                                                onChange={e => setPhone(e.target.value)}
                                                value={phone}
                                                id="phone"
                                                name="phone"
                                                type="text"
                                                placeholder="84+ | "
                                                className="text-standard w-full shadow-md border-1 bg-transparent py-2 ps-4 shadow-md rounded-md"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>

                            {/* <div className="w-full flex justify-between pe-8">
                                <div className="sm:col-span-3 text-standard w-full my-4">
                                    <label htmlFor="roomName" className="block text-gray-900">Room Name</label>
                                    <div className="mt-2 border-none rounded-lg pe-6">
                                        <select onChange={e => setSelectedRoom(e.target.value)} value={selectedRoom} id="roomName" name="roomName" className="text-standard shadow-md border-none bg-transparent py-2 ps-4 shadow-md rounded-md w-full">
                                            {
                                                listRoomUnoccupied && listRoomUnoccupied.map(r => (
                                                    <option key={r.id} value={r.id} className="text-standard">{r.name}</option>
                                                ))
                                            }
                                        </select>
                                    </div>
                                </div>

                                <div className="\text-standard w-full my-4 ps-8">
                                    <label htmlFor="lockerId" className="block text-gray-900">Locker ID</label>
                                    <div className="mt-2 border-none rounded-lg">
                                        <select onChange={e => setSelectedLocker(e.target.value)} value={selectedLocker} id="lockerId" name="lockerId" className="text-standard shadow-md border-none bg-transparent py-2 ps-4 shadow-md rounded-md w-full">
                                            {
                                                listLockerUnoccupied && listLockerUnoccupied.map(l => (
                                                    <option key={l.id} value={l.id} className="text-standard">{l.id}</option>
                                                ))
                                            }
                                        </select>
                                    </div>
                                </div>
                            </div> */}
                        </div>
                        <div className="w-full flex justify-center items-center mt-4">
                            <button onClick={handleAddResident} type="button" className="px-4 py-3 bg-green-400 text-white rounded-md transition hover:bg-green-500">Create</button>
                        </div>
                    </div>
                </form >
            </div>

            <div className={`pb-2 relative transition-all duration-500 
                    ${openAddUserBox ? 'possition-table-addInvoiceActive' : 'top-0 mt-16'}
                `}>
                <div className={`rounded-lg flex-col mb-6 border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1 transition`}>
                    <div className="max-w-full ">
                        <table className="w-full table-auto">
                            <thead>
                                <tr className="bg-gray-2 text-left dark:bg-meta-4 transition">
                                    <th className="text-standard py-2 px-4 font-medium text-black dark:text-white">
                                        User
                                    </th>
                                    <th className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                        Name
                                    </th>
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Email
                                    </th>
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Phone
                                    </th>
                                    {/* <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Locker ID
                                    </th>
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Room Name
                                    </th> */}
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Status
                                    </th>
                                    <th className="text-standard py-2 px-4 font-medium text-black dark:text-white">
                                        Actions
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    dataUserPaging && dataUserPaging.items.map((u, k) => (
                                        <tr key={u.id}>
                                            <td className="text-standard py-2 px-4 font-medium text-black dark:text-white">
                                                {u.username}
                                            </td>
                                            <td className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                                {u.firstname + ' ' + u.lastname}
                                            </td>
                                            <td className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                                {u.email}
                                            </td>
                                            <td className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                                {u.phone}
                                            </td>
                                            <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                                <p
                                                    className={`inline-flex rounded-full bg-opacity-10 py-1 px-3 text-sm font-medium ${u.status === 'Active'
                                                        ? 'bg-success text-success' : u.status==='Disable' ? 'bg-danger text-danger' : 'bg-warning text-warning'
                                                        }`}
                                                >
                                                    {u.status}
                                                </p>
                                            </td>
                                            <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                                <div className="flex items-center space-x-3.5">
                                                    <button onClick={() => {
                                                        if (u.status === 'Paid') {
                                                            alert('Invoice has been paid, cannot be modify!')
                                                            return
                                                        } else {
                                                            if (confirmOrder === k) {
                                                                // setOpenAddInvoiceBox(false)
                                                            } else {
                                                                // setAmount(i.amount)
                                                                // setDescription(i.description)
                                                                // setDueDate(i.dueDate)
                                                                // setSelectedInvoiceType(i.invoiceType.type)
                                                                // setOpenAddInvoiceBox(true)
                                                                // setConfirmOrder(key)
                                                            }
                                                        }
                                                    }} className={`hover:text-info transition-colors ${confirmOrder === k && 'text-info'}`}>
                                                        <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M441 58.9L453.1 71c9.4 9.4 9.4 24.6 0 33.9L424 134.1 377.9 88 407 58.9c9.4-9.4 24.6-9.4 33.9 0zM209.8 256.2L344 121.9 390.1 168 255.8 302.2c-2.9 2.9-6.5 5-10.4 6.1l-58.5 16.7 16.7-58.5c1.1-3.9 3.2-7.5 6.1-10.4zM373.1 25L175.8 222.2c-8.7 8.7-15 19.4-18.3 31.1l-28.6 100c-2.4 8.4-.1 17.4 6.1 23.6s15.2 8.5 23.6 6.1l100-28.6c11.8-3.4 22.5-9.7 31.1-18.3L487 138.9c28.1-28.1 28.1-73.7 0-101.8L474.9 25C446.8-3.1 401.2-3.1 373.1 25zM88 64C39.4 64 0 103.4 0 152L0 424c0 48.6 39.4 88 88 88l272 0c48.6 0 88-39.4 88-88l0-112c0-13.3-10.7-24-24-24s-24 10.7-24 24l0 112c0 22.1-17.9 40-40 40L88 464c-22.1 0-40-17.9-40-40l0-272c0-22.1 17.9-40 40-40l112 0c13.3 0 24-10.7 24-24s-10.7-24-24-24L88 64z" /></svg>
                                                    </button>
                                                    <button onClick={() => { handleUpdateResident(u.id) }} className="hover:text-danger transition-colors">
                                                        <svg
                                                            className="fill-current"
                                                            width="24"
                                                            height="24"
                                                            viewBox="0 0 18 18"
                                                            fill="none"
                                                            xmlns="http://www.w3.org/2000/svg"
                                                        >
                                                            <path
                                                                d="M13.7535 2.47502H11.5879V1.9969C11.5879 1.15315 10.9129 0.478149 10.0691 0.478149H7.90352C7.05977 0.478149 6.38477 1.15315 6.38477 1.9969V2.47502H4.21914C3.40352 2.47502 2.72852 3.15002 2.72852 3.96565V4.8094C2.72852 5.42815 3.09414 5.9344 3.62852 6.1594L4.07852 15.4688C4.13477 16.6219 5.09102 17.5219 6.24414 17.5219H11.7004C12.8535 17.5219 13.8098 16.6219 13.866 15.4688L14.3441 6.13127C14.8785 5.90627 15.2441 5.3719 15.2441 4.78127V3.93752C15.2441 3.15002 14.5691 2.47502 13.7535 2.47502ZM7.67852 1.9969C7.67852 1.85627 7.79102 1.74377 7.93164 1.74377H10.0973C10.2379 1.74377 10.3504 1.85627 10.3504 1.9969V2.47502H7.70664V1.9969H7.67852ZM4.02227 3.96565C4.02227 3.85315 4.10664 3.74065 4.24727 3.74065H13.7535C13.866 3.74065 13.9785 3.82502 13.9785 3.96565V4.8094C13.9785 4.9219 13.8941 5.0344 13.7535 5.0344H4.24727C4.13477 5.0344 4.02227 4.95002 4.02227 4.8094V3.96565ZM11.7285 16.2563H6.27227C5.79414 16.2563 5.40039 15.8906 5.37227 15.3844L4.95039 6.2719H13.0785L12.6566 15.3844C12.6004 15.8625 12.2066 16.2563 11.7285 16.2563Z"
                                                                fill=""
                                                            />
                                                            <path
                                                                d="M9.00039 9.11255C8.66289 9.11255 8.35352 9.3938 8.35352 9.75942V13.3313C8.35352 13.6688 8.63477 13.9782 9.00039 13.9782C9.33789 13.9782 9.64727 13.6969 9.64727 13.3313V9.75942C9.64727 9.3938 9.33789 9.11255 9.00039 9.11255Z"
                                                                fill=""
                                                            />
                                                            <path
                                                                d="M11.2502 9.67504C10.8846 9.64692 10.6033 9.90004 10.5752 10.2657L10.4064 12.7407C10.3783 13.0782 10.6314 13.3875 10.9971 13.4157C11.0252 13.4157 11.0252 13.4157 11.0533 13.4157C11.3908 13.4157 11.6721 13.1625 11.6721 12.825L11.8408 10.35C11.8408 9.98442 11.5877 9.70317 11.2502 9.67504Z"
                                                                fill=""
                                                            />
                                                            <path
                                                                d="M6.72245 9.67504C6.38495 9.70317 6.1037 10.0125 6.13182 10.35L6.3287 12.825C6.35683 13.1625 6.63808 13.4157 6.94745 13.4157C6.97558 13.4157 6.97558 13.4157 7.0037 13.4157C7.3412 13.3875 7.62245 13.0782 7.59433 12.7407L7.39745 10.2657C7.39745 9.90004 7.08808 9.64692 6.72245 9.67504Z"
                                                                fill=""
                                                            />
                                                        </svg>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
                <div className="flex overflow-x-auto sm:justify-center mb-20">
                    <Pagination currentPage={currentPage} totalPages={dataUserPaging ? dataUserPaging.totalPages : 0} onPageChange={onPageChange} showIcons />
                </div>

            </div>
            {openChatBox && <Chat username={selectedUser.username} avatar={selectedUser.avatar} />}

        </div >
    );
}

export default Residents;