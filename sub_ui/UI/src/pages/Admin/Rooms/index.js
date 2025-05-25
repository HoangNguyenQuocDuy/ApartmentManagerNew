import { Pagination } from "flowbite-react";
import { useEffect, useRef, useState } from "react";
import { useSelector } from "react-redux";
import newRequest from "../../../untils/request";
import { notify } from "../../../untils/notification";
import ReactLoading from 'react-loading';
import { RootEnum } from "../../../assets/enum";

function Rooms() {
    const uploadRef = useRef()
    const roomNameRed = useRef()
    const accesstoken = localStorage.getItem("accessToken")
    const [openAddRoomBox, setOpenAddRoomBox] = useState(false)
    const [selectedImage, setSelectedImage] = useState()
    const [roomTypes, setRoomTypes] = useState()
    const [selectedRoomType, setSelectedRoomType] = useState()
    const [selectedRoom, setSelectedRoom] = useState()
    const [roomName, setRoomName] = useState('')
    const [confirmOrder, setConfirmOrder] = useState()
    const [isLoading, setIsLoading] = useState(false)

    const [currentPage, setCurrentPage] = useState(1);
    const onPageChange = page => {
        console.log(page)
        setCurrentPage(page)
    }
    const { pageSize } = useSelector(state => state.app)
    const [dataRoomsPaging, setDataRoomsPaging] = useState()

    useEffect(() => {
        if (!openAddRoomBox) {
            setConfirmOrder()
            setSelectedImage()
            setSelectedRoom()
            setRoomName('')
        }

    }, [openAddRoomBox])

    useEffect(() => {
        handleGetListRoom()
        getListRoomType()
    }, [currentPage])

    const handleUpdateRoom = async (roomId) => {
        const formData = new FormData();
        let flag = false

        if (roomName !== selectedRoom.name) {
            formData.append('name', roomName)
            flag = true
        } else {
            formData.append('name', '')
        }
        console.log('selectedRoomType: ', selectedRoomType)
        if (selectedRoomType !== selectedRoom.roomType.id) {
            formData.append('roomTypeId', selectedRoomType)
            flag = true
        } else {
            formData.append('roomTypeId', selectedRoomType)
        }
        if (selectedImage !== selectedRoom.image) {
            formData.append('file', selectedImage)
            flag = true
        } else {
            formData.append('file', new Blob(), 'emptyFile.png')
        }

        for (let [key, value] of formData.entries()) {
            console.log(`${key}: ${value}`);
        }

        if (!flag) {
            alert("Nothing need update!")
        } else {
            setIsLoading(true)
            newRequest.put(`${RootEnum.API_ROOM + roomId}`, formData, {
                headers: {
                    "Authorization": `Bearer ${accesstoken}`
                }
            })
                .then(data => {
                    console.log(data.data.data)
                    handleGetListRoom()
                    setOpenAddRoomBox(false)
                    notify('Create room successful!', 'success')
                })
                .catch(err => {
                    notify('Error getting rooms: ' + err, 'error')
                })
                .finally(() => {
                    setIsLoading(false)
                })
        }

    }

    const handleCreateRoom = async () => {
        console.log('selectedRoomType: ', selectedRoomType)
        const formData = new FormData();
        formData.append('name', roomName)
        formData.append('file', selectedImage)
        formData.append('roomTypeId', selectedRoomType)

        if (roomName.trim() === '') {
            alert('Room name is required!')
            return
        }

        setIsLoading(true)
        newRequest.post(`${RootEnum.API_ROOM}`, formData, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log(data.data.data)
                notify('Create room successful!', 'success')
                setOpenAddRoomBox(false)
                handleGetListRoom()
            })
            .catch(err => {
                notify('Error creating rooms: ' + err, 'error')
            })
            .finally(() => {
                setIsLoading(false)
            })
    }

    const handleDeletRoom = async (roomId) => {
        if (window.confirm('Do you want to delete this room?')) {
            setIsLoading(true)
            await newRequest.delete(`${RootEnum.API_ROOM + roomId}`, {
                headers: {
                    "Authorization": `Bearer ${accesstoken}`
                }
            })
                .then(() => {
                    handleGetListRoom()
                })
                .catch(err => {
                    notify('Error getting rooms: ' + err, 'error')
                })
                .finally(() => {
                    setIsLoading(false)
                })
        }
    }

    const handleGetListRoom = async () => {
        newRequest.get(`${RootEnum.API_ROOM}?page=${currentPage - 1}&size=${pageSize}`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log('list room: ', data.data.data.data)

                setDataRoomsPaging(data.data.data.data)
            })
            .catch(err => {
                notify('Error getting rooms: ' + err, 'error')
            })
    }

    const getListRoomType = async () => {
        newRequest.get(`${RootEnum.API_ROOMTYPE}`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                setSelectedRoomType(data.data.data[0].id)
                setRoomTypes(data.data.data)
                console.log("data ready to set SelectedRoomType: ", data.data.data[0].id)
            })
            .catch(err => {
                notify('Error getting list room types: ' + err, 'error')
            })
    }

    return (
        <div className="m-10 h-screen transition">
            <div className="w-full relative text-standard">
                <button onClick={() => setOpenAddRoomBox(state => !state)}
                    className={`flex items-center justify-center ${openAddRoomBox ? 'bg-green-500 text-white' :
                        'hover:bg-green-500 text-green-500 hover:text-white'} 
                        py-2 px-4 border border-green-500 hover:border-transparent rounded-md absolute right-0 border-solid transition z-10`}>
                    <span>Add Room</span>
                    <span className="w-4 mx-3">
                        <svg fill="currentColor" width='14' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M256 80c0-17.7-14.3-32-32-32s-32 14.3-32 32l0 144L48 224c-17.7 0-32 14.3-32 32s14.3 32 32 32l144 0 0 144c0 17.7 14.3 32 32 32s32-14.3 32-32l0-144 144 0c17.7 0 32-14.3 32-32s-14.3-32-32-32l-144 0 0-144z" /></svg>
                    </span>
                </button>
                <form onSubmit={(e) => { e.preventDefault() }} className="flex absolute justify-center items-center w-full background-12">
                    <div className={
                        `bg-white px-6 pt-4 me-10 rounded-xl pb-6 w-2/4 mt-10 shadow-lg dark:border-strokedark dark:bg-boxdark dark:text-white duration-500 origin-top
                            ${openAddRoomBox ? 'scale-1 visible h-auto opacity-100' : 'h-0 scale-0 h-0 invisible opacity-0'}`
                    }>
                        <h2 className="text-standard font-semibold leading-7 text-gray-900 mb-4 mt-2 text-center">Add Room Box</h2>
                        <div className="">
                            <div className="w-full">
                                <div className="">
                                    <label htmlFor="name" className="text-standard block text-sm font-medium leading-6 text-gray-900">
                                        Room Name
                                    </label>
                                    <div className="mt-2 border-transparent rounded-lg px-8">
                                        <div className="flex w-full rounded-md">
                                            <input
                                                ref={roomNameRed}
                                                onChange={e => setRoomName(e.target.value)}
                                                value={roomName}
                                                id="name"
                                                name="name"
                                                type="text"
                                                placeholder="A00..."
                                                autoComplete="name"
                                                className="text-standard w-full shadow-md border-1 bg-transparent py-2 ps-4 shadow-md rounded-md"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="sm:col-span-3 text-standard w-full my-4">
                                <label htmlFor="roomType" className="block text-gray-900">Room Type</label>
                                <div className="mt-2 border-none rounded-lg  px-8">
                                    <select onChange={e => { console.log(e.target.value); setSelectedRoomType(e.target.value) }} id="roomType" name="roomType" className="text-standard shadow-md border-none bg-transparent py-2 ps-4 shadow-md rounded-md w-full">
                                        {
                                            roomTypes && roomTypes.map(rt => (
                                                <option selected={selectedRoom && selectedRoom.roomType.type === rt.type} key={rt.id} value={rt.id} className="text-standard">{rt.type}</option>
                                            ))
                                        }
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div className="col-span-full mt-4">
                            <label htmlFor="cover-photo" className="block text-standard font-medium leading-6 text-gray-900">
                                Room Image
                            </label>
                            <div className="box-center">
                                {
                                    !selectedRoom ? selectedImage && <img alt="room's pic" className="w-60 h-40 rounded-lg rounded-md object-contain" src={URL.createObjectURL(selectedImage)} /> :
                                        selectedRoom.image !== null ? <img alt="room's pic" className="w-60 h-40 rounded-lg rounded-md object-contain" src={selectedImage} /> :
                                            selectedImage && <img alt="room's pic" className="w-60 h-40 rounded-lg rounded-md object-contain" src={URL.createObjectURL(selectedImage)} />
                                }
                            </div>
                            <div onClick={() => { uploadRef.current.click() }} className="mt-3 flex justify-center rounded-lg border border-dashed border-gray-900/25 py-2">
                                <div className="text-center">
                                    <label
                                        htmlFor="file-upload"
                                        className="relative cursor-pointer rounded-md bg-white font-semibold focus-within:outline-none hover:text-grey-800"
                                    >
                                        <span className="dark:text-white dark:bg-boxdark text-gray-800">Upload a file</span>
                                        <input
                                            onChange={(e) => {
                                                console.log(e.target.files[0])
                                                setSelectedImage(e.target.files[0])
                                            }}
                                            ref={uploadRef} id="file-upload" name="file-upload" type="file" className="sr-only w-full" />
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div className="relative w-full flex justify-center items-center mt-4">
                            <button type="button" onClick={() => {
                                selectedRoom ? handleUpdateRoom(selectedRoom.id) : handleCreateRoom()
                            }} className={`px-4 py-3 bg-green-400 text-white rounded-md transition hover:bg-green-500 ${isLoading && 'cursor-not-allowed'}`}>{selectedRoom ? 'Update' : 'Create'}</button>
                            {isLoading && <ReactLoading className='w-40 h-40 absolute top-1' type='spin' color={'#999'} height={'20px'} width={'5%'} />}
                        </div>
                    </div>
                </form >
            </div>

            <div className={`pb-4 relative transition-all duration-500 
                ${openAddRoomBox && selectedImage ? 'possition-table-addRoomActive-image' : openAddRoomBox && !selectedImage ? 'possition-table-addRoomActive' : 'top-0'}`}>
                <div className={`rounded-lg flex-col mb-20 border mt-16 border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1 transition`}>
                    <div className="max-w-full">
                        <table className="w-full table-auto">
                            <thead>
                                <tr className="bg-gray-2 text-left dark:bg-meta-4 transition">
                                    <th className="text-standard py-2 px-4 font-medium text-black dark:text-white xl:pl-11">
                                        Room name
                                    </th>
                                    <th className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                        Room Type
                                    </th>
                                    <th className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                        Image
                                    </th>
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Status
                                    </th>
                                    <th className="text-standard py-2 px-4 font-medium text-black dark:text-white">
                                        Actions
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                {dataRoomsPaging && dataRoomsPaging.items.map((r, key) => (
                                    <tr key={key}>
                                        <td className="border-b border-[#eee] py-2 px-4 pl-9 dark:border-strokedark xl:pl-11">
                                            <h5 className="text-black dark:text-white">
                                                {r.name}
                                            </h5>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {r.roomType.type}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            {
                                                r.image ? <img className="w-80 h-60 object-contain" alt="room's pic" src={r.image} /> :
                                                    <p className="text-black dark:text-white">-----</p>
                                            }
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p
                                                className={`inline-flex rounded-full bg-opacity-10 py-1 px-3 text-sm font-medium ${r.status === 'OCCUPIED'
                                                    ? 'bg-success text-success' : r.status === 'DISABLED' ? 'bg-danger text-danger' : 'bg-warning text-warning'
                                                    }`}
                                            >
                                                {r.status}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <div className="flex items-center space-x-3.5">
                                                <button onClick={() => {
                                                    if (confirmOrder === key) {
                                                        setOpenAddRoomBox(false)
                                                    } else {
                                                        setSelectedRoom(r)
                                                        setSelectedRoomType(r.roomType.id)
                                                        setRoomName(r.name)
                                                        setOpenAddRoomBox(true)
                                                        setConfirmOrder(key)
                                                        setSelectedImage(r.image)
                                                        roomNameRed.current.focus()
                                                    }

                                                }} className={`hover:text-info transition-colors ${confirmOrder === key && 'text-info'}`}>
                                                    <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M441 58.9L453.1 71c9.4 9.4 9.4 24.6 0 33.9L424 134.1 377.9 88 407 58.9c9.4-9.4 24.6-9.4 33.9 0zM209.8 256.2L344 121.9 390.1 168 255.8 302.2c-2.9 2.9-6.5 5-10.4 6.1l-58.5 16.7 16.7-58.5c1.1-3.9 3.2-7.5 6.1-10.4zM373.1 25L175.8 222.2c-8.7 8.7-15 19.4-18.3 31.1l-28.6 100c-2.4 8.4-.1 17.4 6.1 23.6s15.2 8.5 23.6 6.1l100-28.6c11.8-3.4 22.5-9.7 31.1-18.3L487 138.9c28.1-28.1 28.1-73.7 0-101.8L474.9 25C446.8-3.1 401.2-3.1 373.1 25zM88 64C39.4 64 0 103.4 0 152L0 424c0 48.6 39.4 88 88 88l272 0c48.6 0 88-39.4 88-88l0-112c0-13.3-10.7-24-24-24s-24 10.7-24 24l0 112c0 22.1-17.9 40-40 40L88 464c-22.1 0-40-17.9-40-40l0-272c0-22.1 17.9-40 40-40l112 0c13.3 0 24-10.7 24-24s-10.7-24-24-24L88 64z" /></svg>
                                                </button>
                                                <button onClick={() => { handleDeletRoom(r.id) }} className="hover:text-danger transition-colors">
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
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
                <div className="flex sm:justify-center mb-20 absolute w-full">
                    <Pagination currentPage={currentPage} totalPages={dataRoomsPaging ? dataRoomsPaging.totalPages : 0} onPageChange={onPageChange} showIcons />
                </div>
            </div>
        </div >
    );
}

export default Rooms;