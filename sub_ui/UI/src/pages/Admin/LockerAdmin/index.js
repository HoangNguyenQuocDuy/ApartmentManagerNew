import { useEffect, useState } from "react";
import newRequest from "../../../untils/request";
import { useSelector } from "react-redux";
import { Pagination } from "flowbite-react";
import { notify } from "../../../untils/notification";
import moment from "moment";

function LockerAdmin() {
    const accesstoken = localStorage.getItem("accessToken")
    const [openAddOrder, setOpenAddOrder] = useState(false)
    const [orders, setOrders] = useState()
    const { pageSize } = useSelector(state => state.app)
    const [currentPage, setCurrentPage] = useState(1);
    const [lockers, setLockers] = useState()
    const [selectedLocker, setSelectedLocker] = useState()
    const onPageChange = page => {
        setCurrentPage(page)
    };
    useEffect(() => {
        handleGetLockers()
    }, [currentPage])

    const deleteLocker = async (orderId) => {
        if (window.confirm('Do you want to delete this order?')) {
            await newRequest.delete(`/lockers/${orderId}`, {
                headers: {
                    "Authorization": `Bearer ${accesstoken}`
                }
            })
                .then(data => {
                    console.log(data.data.data)
                    notify('Delete locker successful!', 'success')
                    handleGetLockers()
                })
                .catch(err => {
                    notify('Error when deleting order: ' + err, 'error')
                })
        }
    }

    const handleGetLockers = () => {
        newRequest.get(`/lockers/?page=${currentPage - 1}&size=${pageSize}`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                setLockers(data.data.data)
                console.log('lockers: ', data.data.data)
            })
            .catch(err => {
                notify('Error get lockers: ' + err, 'error')
            })
    }

    const handleAddLocker = () => {
        if (window.confirm('Do you wanna add locker?')) {
            newRequest.post(`/lockers/`, {}, {
                headers: {
                    "Authorization": `Bearer ${accesstoken}`
                }
            })
                .then(data => {
                    handleGetLockers()
                })
                .catch(err => {
                    notify('Error get lockers: ' + err, 'error')
                })
        }
    }

    return (
        <div className="m-10">
            <div className="w-full relative text-standard">
                <button onClick={handleAddLocker}
                    className={`flex items-center justify-center hover:bg-green-500 text-green-500 hover:text-white
                        py-2 px-4 border border-green-500 hover:border-transparent rounded-md absolute right-0 border-solid transition z-10`}>
                    <span>Add Locker</span>
                    <span className="w-4 mx-3">
                        <svg fill="currentColor" width='14' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M256 80c0-17.7-14.3-32-32-32s-32 14.3-32 32l0 144L48 224c-17.7 0-32 14.3-32 32s14.3 32 32 32l144 0 0 144c0 17.7 14.3 32 32 32s32-14.3 32-32l0-144 144 0c17.7 0 32-14.3 32-32s-14.3-32-32-32l-144 0 0-144z" /></svg>
                    </span>
                </button>
            </div>
            <div className={`pb-4 relative transition-all duration-500`}>
                <div className={`rounded-lg flex-col mb-4 border mt-16 border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1 transition`}>
                    <div className="max-w-full ">
                        <table className="w-full table-auto">
                            <thead>
                                <tr className="bg-gray-2 text-left dark:bg-meta-4 transition">
                                    <th className="text-standard py-4 px-4 font-medium text-black dark:text-white xl:pl-11">
                                        Locker ID
                                    </th>
                                    <th className="text-standard min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
                                        Status
                                    </th>
                                    <th className="text-standard py-4 px-4 font-medium text-black dark:text-white">
                                        Actions
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                {lockers && lockers.content.map((order, key) => (
                                    <tr key={key}>
                                        <td className="border-b border-[#eee] py-2 px-2 pl-9 dark:border-strokedark xl:pl-11">
                                            <h5 className="font-medium text-black dark:text-white">
                                                {order.id}
                                            </h5>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p
                                                className={`inline-flex rounded-full bg-opacity-10 py-1 px-3 text-sm font-medium ${order.status === 'Using'
                                                    ? 'bg-success text-success' : 'bg-warning text-warning'
                                                    }`}
                                            >
                                                {order.status}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <div className="flex items-center space-x-3.5">
                                                <button onClick={() => { deleteLocker(order.id) }} className="hover:text-danger transition-colors">
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
            </div>
            <div className="flex justify-center mb-20 ">
                <Pagination currentPage={currentPage} totalPages={lockers ? lockers.totalPages : 0} onPageChange={onPageChange} showIcons />
            </div>
        </div >
    );
}

export default LockerAdmin;