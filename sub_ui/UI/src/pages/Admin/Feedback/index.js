import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import newRequest from "../../../untils/request";
import { notify } from "../../../untils/notification";
import { Pagination } from "flowbite-react";
import ReactLoading from 'react-loading';
import moment from "moment";

function FeedbacksAdmin() {
    const accesstoken = localStorage.getItem("accessToken")
    const [feedbacks, setFeedbacks] = useState()
    const [isLoading, setIsLoading] = useState(false)

    const [currentPage, setCurrentPage] = useState(1);
    const onPageChange = page => {
        console.log(page)
        setCurrentPage(page)
    }
    const { pageSize } = useSelector(state => state.app)

    useEffect(() => {
        handleGetListFeedback()
    }, [currentPage])

    const handleGetListFeedback = async () => {
        newRequest.get(`/feedbacks/?page=${currentPage - 1}&size=${pageSize}`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log('f rights: ', data.data.data)
                setFeedbacks(data.data.data)
            })
            .catch(err => {
                alert('Error getting list f rights: ' + err)
                notify('Error getting list f rights: ' + err, 'error')
            })
    }

    // const handleGetList

    return (
        <div className="m-10">
            <div className={`pb-4 relative transition-all duration-500`}>
                <div className={`rounded-lg flex-col mb-20 border mt-4 border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1 transition`}>
                    <div className="max-w-full ">
                        <table className="w-full table-auto">
                            <thead>
                                <tr className="bg-gray-2 text-left dark:bg-meta-4 transition">
                                    <th className="text-standard min-w-[150px] py-4 px-4 font-medium text-black dark:text-white">
                                        Room Name
                                    </th>
                                    <th className="text-standard min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
                                        Title
                                    </th>
                                    <th className="text-standard min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
                                        Content
                                    </th>
                                    <th className="text-standard py-4 px-4 font-medium text-black dark:text-white">
                                        Created At
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                {feedbacks && feedbacks.content.map((f, key) => (
                                    <tr key={key}>
                                        <td className="border-b border-[#eee] py-2 px-2 pl-9 dark:border-strokedark xl:pl-11">
                                            <h5 className="font-medium text-black dark:text-white">
                                                {f.user.room.name}
                                            </h5>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {f.title}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className={`text-black dark:text-white`}>
                                                {f.content}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className={`text-black dark:text-white`}>
                                                {moment(f.createdAt).format("HH:mm:ss DD/MM/YYYY")}
                                            </p>
                                        </td>
                                        {/* <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <div className="flex items-center space-x-3.5">
                                                <button className={`hover:text-success transition`}>
                                                    <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M438.6 105.4c12.5 12.5 12.5 32.8 0 45.3l-256 256c-12.5 12.5-32.8 12.5-45.3 0l-128-128c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0L160 338.7 393.4 105.4c12.5-12.5 32.8-12.5 45.3 0z" /></svg>
                                                </button>
                                                <button className="hover:text-danger transition">
                                                    <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512"><path d="M342.6 150.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L192 210.7 86.6 105.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L146.7 256 41.4 361.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0L192 301.3 297.4 406.6c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L237.3 256 342.6 150.6z" /></svg>
                                                </button>
                                            </div>
                                        </td> */}
                                    </tr>
                                ))}
                            </tbody>
                            <div className="absolute w-full flex justify-center items-center mt-4">
                                {isLoading && <ReactLoading className='w-40 h-40 absolute top-1' type='spin' color={'#999'} height={'20px'} width={'5%'} />}
                            </div>
                        </table>
                    </div>
                </div>
                <div className="flex sm:justify-center mb-20 absolute w-full">
                    <Pagination currentPage={currentPage} totalPages={feedbacks ? feedbacks.totalPages : 0} onPageChange={onPageChange} showIcons />
                </div>
            </div>
        </div >
    );
}

export default FeedbacksAdmin;