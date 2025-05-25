import { Pagination } from "flowbite-react";
import { useEffect, useRef, useState } from "react";
import { useSelector } from "react-redux";
import newRequest from "../../../untils/request";
import { RootEnum } from "../../../assets/enum";
import ReactLoading from 'react-loading';
import { notify } from "../../../untils/notification";

function Contract() {
    const uploadRef = useRef()
    const roomNameRed = useRef()
    const accesstoken = localStorage.getItem("accessToken")
    const [openAddContractBox, setOpenAddContractBox] = useState(false)
    const [selectedImage, setSelectedImage] = useState()
    const [roomTypes, setRoomTypes] = useState()
    const [listUser, setListUser] = useState()
    const [listRoom, setListRoom] = useState()
    const [customerId, setCustomerId] = useState()
    const [selectedRoom, setSelectedRoom] = useState()
    const [roomName, setRoomName] = useState('')
    const [confirmOrder, setConfirmOrder] = useState()
    const [isLoading, setIsLoading] = useState(false)
    const [totalAmount, setTotalAmount] = useState(0)
    const [termFrequency, setTermFrequency] = useState('')
    const [startDate, setStartDate] = useState()
    const [endDate, setEndDate] = useState()
    const [contractType, setContractType] = useState()
    const [isSetTotalAmount, setIsSetTotalAmount] = useState(false)
    const [listContract, setListContract] = useState()

    const [currentPage, setCurrentPage] = useState(1);
    const onPageChange = page => {
        console.log(page)
        setCurrentPage(page)
    }
    const { pageSize } = useSelector(state => state.app)
    const [dataContractsPaging, setDataContracysPaging] = useState()

    useEffect(() => {
        if (!openAddContractBox) {
            setConfirmOrder()
            setSelectedImage()
            setCustomerId()
            setRoomName('')
            setContractType('RENTAL')
        }
        handleGetListRoomAvailable()
    }, [openAddContractBox])

    useEffect(() => {
        handleGetListRoomAvailable()
        handleGetListUser()
        handleGetListContract()
    }, [currentPage])

    function formatDateTime(arr) {
        if (!Array.isArray(arr) || arr.length < 5) return "Invalid date";
        const [year, month, day, hour, minute] = arr;
        return `${day.toString().padStart(2, '0')}/` +
            `${month.toString().padStart(2, '0')}/` +
            `${year} ` +
            `${hour.toString().padStart(2, '0')}:` +
            `${minute.toString().padStart(2, '0')}`;
    }

    useEffect(() => {
        handleCheckContractType(contractType)
    }, [contractType, selectedRoom])

    const handleUpdateContract = async (contractId, contractStatus) => {
        setIsLoading(true)
        newRequest.patch(`${RootEnum.API_CONTRACT + contractId}`, JSON.stringify(contractStatus), {
            headers: {
                "Authorization": `Bearer ${accesstoken}`,
                "Content-Type": "application/json"
            }
        })
            .then(data => {
                console.log(data.data.data)
                handleGetListContract()
                setOpenAddContractBox(false)
                notify('Update contract successful!', 'success')
            })
            .catch(err => {
                notify('Error when updating contract: ' + err, 'error')
            })
            .finally(() => {
                setIsLoading(false)
            })

    }

    const handleCreateContract = async () => {
        console.log(selectedRoom)

        if (totalAmount === 0) {
            alert('Total amount is required!')
            return
        }
        if (termFrequency.trim() === '') {
            alert('Term frequency is required!')
            return
        }
        if (startDate === null) {
            alert('Start date is required!')
            return
        }
        if (endDate === null) {
            alert('End date is required!')
            return
        }

        setIsLoading(true)
        newRequest.post(`${RootEnum.API_CONTRACT}`, {
            totalAmount,
            termFrequency,
            customerId,
            roomId: selectedRoom.id,
            startDate,
            endDate,
            contractType
        }, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log(data.data.data)
                notify('Create contract successful!', 'success')
                setOpenAddContractBox(false)
                handleGetListRoomAvailable()
            })
            .catch(err => {
                notify('Error when creating contract: ' + err, 'error')
                console.log('Error when creating contract: ', err)
            })
            .finally(() => {
                setIsLoading(false)
            })
    }

    const handleDeletRoom = async (selectedRoom) => {
        if (window.confirm('Do you want to delete this room?')) {
            setIsLoading(true)
            await newRequest.delete(`${RootEnum.API_ROOM + selectedRoom}`, {
                headers: {
                    "Authorization": `Bearer ${accesstoken}`
                }
            })
                .then(() => {
                    handleGetListRoomAvailable()
                })
                .catch(err => {
                    notify('Error getting rooms: ' + err, 'error')
                })
                .finally(() => {
                    setIsLoading(false)
                })
        }
    }

    const handleGetListContract = async () => {
        newRequest.get(`${RootEnum.API_CONTRACT}?page=${currentPage - 1}&size=${pageSize}`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log("list contract: ", data.data.data.data)

                setListContract(data.data.data.data)
                setSelectedRoom(data.data.data[0])
            })
            .catch(err => {
                notify('Error getting contracts: ' + err, 'error')
            })
    }

    const handleGetListRoomAvailable = async () => {
        newRequest.get(`${RootEnum.API_ROOM}?all=true&filter=status==Available`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log(data.data.data)

                setListRoom(data.data.data.data.items)
                setSelectedRoom(data.data.data.data.items[0])
            })
            .catch(err => {
                notify('Error getting rooms: ' + err, 'error')
            })
    }

    const handleGetListUser = async () => {
        newRequest.get(`${RootEnum.API_USER}?all=true`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                setCustomerId(data.data.data.data.items[0].id)
                setListUser(data.data.data.data.items)
                console.log("data ready to set customerId: ", data.data.data.content[0].id)
            })
            .catch(err => {
                notify('Error getting list user: ' + err, 'error')
            })
    }

    const handleCheckContractType = (contractType) => {
        if (contractType === 'OTHER') {
            setIsSetTotalAmount(true)
        } else if (contractType === 'RENTAL') {
            setIsSetTotalAmount(false)
            console.log(selectedRoom)
            setTotalAmount(selectedRoom && selectedRoom.roomType.rentPrice)
        } else if (contractType === 'BUY') {
            setIsSetTotalAmount(false)
            console.log(selectedRoom)
            setTotalAmount(selectedRoom && selectedRoom.roomType.roomPrice)
        }
    }

    return (
        <div className="m-10 h-screen transition">
            <div className="w-full relative text-standard">
                <button
                    onClick={() => setOpenAddContractBox(state => !state)}
                    className={`flex items-center justify-center ${openAddContractBox ? 'bg-green-500 text-white' :
                        'hover:bg-green-500 text-green-500 hover:text-white'} 
                            py-2 px-4 border border-green-500 hover:border-transparent rounded-md absolute right-0 border-solid transition z-10`}>
                    <span>Add Contract</span>
                    <span className="w-4 mx-3">
                        <svg fill="currentColor" width='14' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512">
                            <path d="M256 80c0-17.7-14.3-32-32-32s-32 14.3-32 32v144H48c-17.7 0-32 14.3-32 32s14.3 32 32 32h144v144c0 17.7 14.3 32 32 32s32-14.3 32-32V288h144c17.7 0 32-14.3 32-32s-14.3-32-32-32H256V80z" />
                        </svg>
                    </span>
                </button>

                <form onSubmit={(e) => { e.preventDefault() }} className="flex absolute justify-center items-center w-full background-12">
                    <div className={`
                        bg-white px-6 pt-4 rounded-xl pb-6 w-2/4 mt-10 shadow-lg dark:border-strokedark dark:bg-boxdark dark:text-white duration-500 origin-top
                        ${openAddContractBox ? 'scale-1 visible h-auto opacity-100' : 'h-0 scale-0 invisible opacity-0'}
                        `}>
                        <h2 className="text-standard font-semibold leading-7 text-gray-900 mb-4 mt-2 text-center">Add Contract</h2>

                        {/* Contract Type */}
                        <div className="mb-4">
                            <label htmlFor="contractType" className="block text-sm font-medium text-gray-900">
                                Contract Type
                            </label>
                            <select
                                id="contractType"
                                name="contractType"
                                value={contractType}
                                onChange={e => {
                                    setContractType(e.target.value)
                                    handleCheckContractType(e.target.value)
                                }}
                                className="w-full mt-1 p-2 border border-gray-300 rounded-md"
                            >
                                <option value="RENTAL">Rental</option>
                                <option value="BUY">Buy</option>
                                <option value="OTHER">Other</option>
                            </select>
                        </div>

                        {/* Total Amount */}
                        <div className="mb-4">
                            <label htmlFor="totalAmount" className="block text-sm font-medium text-gray-900">
                                Total Amount
                            </label>
                            <input
                                disabled={!isSetTotalAmount}
                                type="number"
                                id="totalAmount"
                                name="totalAmount"
                                placeholder="e.g. 1000000"
                                value={totalAmount}
                                onChange={e => setTotalAmount(e.target.value)}
                                className="w-full mt-1 p-2 border border-gray-300 rounded-md"
                            />
                        </div>

                        {/* Term Frequency */}
                        <div className="mb-4">
                            <label htmlFor="termFrequency" className="block text-sm font-medium text-gray-900">
                                Term Frequency
                            </label>
                            <select
                                id="termFrequency"
                                name="termFrequency"
                                value={termFrequency}
                                onChange={e => setTermFrequency(e.target.value)}
                                className="w-full mt-1 p-2 border border-gray-300 rounded-md"
                            >
                                <option value="">-- Select Term --</option>
                                <option value="MONTHLY">Monthly</option>
                                <option value="QUARTERLY">Quarterly</option>
                                <option value="YEARLY">Yearly</option>
                            </select>
                        </div>

                        {/* Customer ID */}
                        <div className="sm:col-span-3 text-standard w-full my-4">
                            <label htmlFor="customerId" className="block text-gray-900">Customer</label>
                            <div className="mt-2 border-none rounded-lg  px-8">
                                <select onChange={e => { console.log(e.target.value); setCustomerId(e.target.value) }} id="customerId" name="customerId" className="text-standard shadow-md border-none bg-transparent py-2 ps-4 shadow-md rounded-md w-full">
                                    {
                                        listUser && listUser.map(u => (
                                            <option selected={customerId && customerId === u.id} key={u.id} value={u.id} className="text-standard">{u.id + " - " + u.firstname + " " + u.lastname + " - " + u.email}</option>
                                        ))
                                    }
                                </select>
                            </div>
                        </div>

                        {/* Room ID */}
                        <div className="sm:col-span-3 text-standard w-full my-4">
                            <label htmlFor="selectedRoom" className="block text-gray-900">Room</label>
                            <div className="mt-2 border-none rounded-lg  px-8">
                                <select onChange={e => {
                                    console.log(JSON.parse(e.target.value))
                                    setSelectedRoom(prev => {
                                        if (prev) {
                                            return JSON.parse(e.target.value)
                                        }
                                    })
                                }} id="selectedRoom" name="selectedRoom" className="text-standard shadow-md border-none bg-transparent py-2 ps-4 shadow-md rounded-md w-full">
                                    {
                                        listRoom && listRoom.map(r => (
                                            <option selected={selectedRoom && selectedRoom.id === r.id} key={r.id} value={JSON.stringify(r)} className="text-standard">{r.name + ' - ' + r.roomType.type}</option>
                                        ))
                                    }
                                </select>
                            </div>
                        </div>

                        {/* Start Date */}
                        <div className="mb-4">
                            <label htmlFor="startDate" className="block text-sm font-medium text-gray-900">
                                Start Date
                            </label>
                            <input
                                type="datetime-local"
                                id="startDate"
                                name="startDate"
                                value={startDate}
                                onChange={e => setStartDate(e.target.value)}
                                className="w-full mt-1 p-2 border border-gray-300 rounded-md"
                            />
                        </div>

                        {/* End Date */}
                        <div className="mb-4">
                            <label htmlFor="endDate" className="block text-sm font-medium text-gray-900">
                                End Date
                            </label>
                            <input
                                type="datetime-local"
                                id="endDate"
                                name="endDate"
                                value={endDate}
                                onChange={e => setEndDate(e.target.value)}
                                className="w-full mt-1 p-2 border border-gray-300 rounded-md"
                            />
                        </div>

                        {/* Submit */}
                        <div className="flex justify-center">
                            <button
                                type="submit"
                                onClick={() => handleCreateContract()}
                                className="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600"
                            >
                                Create Contract
                            </button>
                        </div>
                    </div>
                </form>
            </div>

            <div className={`pb-4 relative transition-all duration-500 
                ${openAddContractBox ? 'possition-table-addRoomActive-image mt-50' : 'top-0'}`}>
                <div className={`rounded-lg flex-col mb-20 border mt-16 border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1 transition`}>
                    <div className="max-w-full">
                        <table className="w-full table-auto">
                            <thead>
                                <tr className="bg-gray-2 text-left dark:bg-meta-4 transition">
                                    <th className="text-standard py-2 px-4 font-medium text-black dark:text-white xl:pl-11">
                                        Contract Number
                                    </th>
                                    <th className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                        Contract Type
                                    </th>
                                    <th className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                        Start Date
                                    </th>
                                    <th className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                        End Date
                                    </th>
                                    <th className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                        User ID
                                    </th>
                                    <th className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                        Room ID
                                    </th>
                                    <th className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                        Total Amount
                                    </th>
                                    <th className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                        Term Frequency
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
                                {listContract && listContract.items.map((contract, key) => (
                                    <tr key={key}>
                                        <td className="border-b border-[#eee] py-2 px-4 pl-9 dark:border-strokedark xl:pl-11">
                                            <h5 className="text-black dark:text-white">
                                                {contract.contractNumber}
                                            </h5>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {contract.contractType}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            {formatDateTime(contract.startDate)}
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            {formatDateTime(contract.endDate)}
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            {contract.userId}
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            {contract.roomId}
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            {contract.totalAmount}
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            {contract.termFrequency}
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p
                                                className={`inline-flex rounded-full bg-opacity-10 py-1 px-3 text-sm font-medium ${contract.status === 'ACTIVE'
                                                    ? 'bg-success text-success' : contract.status === 'TERMINATED' ? 'bg-danger text-danger' : 'bg-warning text-warning'
                                                    }`}
                                            >
                                                {contract.status}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <div className="flex items-center space-x-3.5">
                                                <button onClick={() => {
                                                    const isConfirmed = window.confirm("Confirm this contract?");
                                                    if (isConfirmed) {
                                                        handleUpdateContract(contract.id, "ACTIVE")
                                                    } else {
                                                        setOpenAddContractBox(false);
                                                    }

                                                }} className={`hover:text-success transition-colors ${confirmOrder === key && 'text-info'}`}>
                                                    <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M438.6 105.4c12.5 12.5 12.5 32.8 0 45.3l-256 256c-12.5 12.5-32.8 12.5-45.3 0l-128-128c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0L160 338.7 393.4 105.4c12.5-12.5 32.8-12.5 45.3 0z" /></svg>
                                                    {/* <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M441 58.9L453.1 71c9.4 9.4 9.4 24.6 0 33.9L424 134.1 377.9 88 407 58.9c9.4-9.4 24.6-9.4 33.9 0zM209.8 256.2L344 121.9 390.1 168 255.8 302.2c-2.9 2.9-6.5 5-10.4 6.1l-58.5 16.7 16.7-58.5c1.1-3.9 3.2-7.5 6.1-10.4zM373.1 25L175.8 222.2c-8.7 8.7-15 19.4-18.3 31.1l-28.6 100c-2.4 8.4-.1 17.4 6.1 23.6s15.2 8.5 23.6 6.1l100-28.6c11.8-3.4 22.5-9.7 31.1-18.3L487 138.9c28.1-28.1 28.1-73.7 0-101.8L474.9 25C446.8-3.1 401.2-3.1 373.1 25zM88 64C39.4 64 0 103.4 0 152L0 424c0 48.6 39.4 88 88 88l272 0c48.6 0 88-39.4 88-88l0-112c0-13.3-10.7-24-24-24s-24 10.7-24 24l0 112c0 22.1-17.9 40-40 40L88 464c-22.1 0-40-17.9-40-40l0-272c0-22.1 17.9-40 40-40l112 0c13.3 0 24-10.7 24-24s-10.7-24-24-24L88 64z" /></svg> */}
                                                </button>
                                                <button onClick={() => {
                                                    const isConfirmed = window.confirm("Cancel this contract?");
                                                    if (isConfirmed) {
                                                        handleUpdateContract(contract.id, "TERMINATED")
                                                    }
                                                }} className="hover:text-danger transition-colors">
                                                    <svg className="fill-current" width="24" height="24" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512"><path d="M342.6 150.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L192 210.7 86.6 105.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L146.7 256 41.4 361.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0L192 301.3 297.4 406.6c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L237.3 256 342.6 150.6z" /></svg>
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
                    <Pagination currentPage={currentPage} totalPages={listContract ? listContract.totalPages : 0} onPageChange={onPageChange} showIcons />
                </div>
            </div>
        </div >
    );
}

export default Contract;