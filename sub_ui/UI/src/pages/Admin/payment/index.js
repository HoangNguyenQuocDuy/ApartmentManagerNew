import { useEffect, useState } from "react";
import Datepicker from "react-tailwindcss-datepicker";
import newRequest from "../../../untils/request";
import { useSelector } from "react-redux";
import { notify } from "../../../untils/notification";
import { Pagination } from "flowbite-react";
import ReactLoading from 'react-loading';
import moment from "moment/moment";
import { RootEnum } from "../../../assets/enum";
import Status from "../../../components/Status";

function PaymentAdmin() {
    const accesstoken = localStorage.getItem("accessToken")
    const [isLoading, setIsLoading] = useState(false)

    const [currentPage, setCurrentPage] = useState(1);
    const onPageChange = page => {
        console.log(page)
        setCurrentPage(page)
    };
    const { pageSize } = useSelector(state => state.app)
    const [dataPaymentsPaging, setDataPaymentsPaging] = useState()
    const [openUpdateInvoiceBox, setOpenUpdateInvoiceBox] = useState(false)
    const [selectedImage, setSelectedImage] = useState('')
    const [selectedPayment, setSelectedPayment] = useState()

    useEffect(() => {
        handleGetPayments()
    }, [currentPage])

    const handleGetPayments = () => {
        newRequest.get(`${RootEnum.API_PAY}?page=${currentPage - 1}&size=${pageSize}`, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log('payments paging: ', data.data.data)

                setDataPaymentsPaging(data.data.data)
            })
            .catch(err => {
                notify('Error getting payments: ' + err, 'error')
            })
    }

    const handleUpdateInvoice = (status) => {
        newRequest.put(`/invoices/${selectedPayment.invoice.id}`, { status }, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                handleGetPayments()
                setSelectedPayment()
                setOpenUpdateInvoiceBox(false)
            })
            .catch(err => {
                notify('Error updating invoice: ' + err, 'error')
            })
    }

    function formatPaidAt(paidAtArray) {
        if (!paidAtArray || paidAtArray.length < 6) {
            return '------';
        }

        const date = new Date(
            paidAtArray[0],                  // year
            paidAtArray[1] - 1,              // month (0-based)
            paidAtArray[2],                  // day
            paidAtArray[3],                  // hour
            paidAtArray[4],                  // minute
            paidAtArray[5],                  // second
            Math.floor((paidAtArray[6] || 0) / 1_000_000) // nanoseconds -> milliseconds
        );

        return moment(date).format('HH:mm:ss DD/MM/YYYY');
    }

    const handleUpdatePayment = ({paymentId, status}) => {
        newRequest.patch(`${RootEnum.API_PAY + paymentId}`, status, {
            headers: {
                "Authorization": `Bearer ${accesstoken}`
            }
        })
            .then(data => {
                console.log('payments paging: ', data.data.data)

                setDataPaymentsPaging(data.data.data)
            })
            .catch(err => {
                notify('Error getting payments: ' + err, 'error')
            })
    }

    return (
        <div className="m-10">
            <div className="w-full relative text-standard">
                <form onSubmit={(e) => { e.preventDefault() }} className="flex absolute justify-center items-center w-full background-12">
                    <div className={
                        `bg-white px-6 pt-4 rounded-xl pb-6 w-2/4 mt-10 shadow-lg dark:border-strokedark dark:bg-boxdark dark:text-white duration-500 origin-top
                            ${openUpdateInvoiceBox ? 'scale-1 visible h-auto opacity-100 m-0' : 'h-0 scale-0 h-0 invisible opacity-0'}`
                    }>

                        <div className="col-span-full mt-4">
                            <label htmlFor="cover-photo" className="block text-standard font-medium leading-6 text-gray-900">
                                Payment
                            </label>
                            <div className="box-center">
                                {
                                    selectedPayment && selectedPayment.uploadImage && <img alt="payment 's pic" className="w-60 h-40 rounded-lg rounded-md object-contain" src={selectedPayment.uploadImage} />
                                }
                            </div>
                        </div>
                        <div className="flex mt-6">
                            <div className="relative w-full flex justify-center items-center mt-4">
                                <button type="button" onClick={() => {
                                    handleUpdateInvoice("Rejected")
                                }} className={`px-4 py-3 bg-red-400 text-white rounded-md transition hover:bg-red-500 ${isLoading && 'cursor-not-allowed'}`}>Reject</button>
                            </div>
                            <div className="relative w-full flex justify-center items-center mt-4">
                                <button type="button" onClick={() => {
                                    handleUpdateInvoice("Paid")
                                }} className={`px-4 py-3 bg-green-400 text-white rounded-md transition hover:bg-green-500 ${isLoading && 'cursor-not-allowed'}`}>Confirm</button>
                            </div>
                        </div>
                        {isLoading && <ReactLoading className='w-40 h-40 absolute top-1' type='spin' color={'#999'} height={'20px'} width={'5%'} />}
                    </div>
                </form >
            </div>
            <div className={`pb-2 relative transition-all duration-500 ${openUpdateInvoiceBox ? 'top-90' : 'top-0'}`}>
                <div className={`rounded-lg flex-col mb-20 border mt-16 border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1 transition`}>
                    <div className="max-w-full ">
                        <table className="w-full table-auto">
                            <thead>
                                <tr className="bg-gray-2 text-left dark:bg-meta-4 transition">
                                    <th className="text-standard py-2 px-4 font-medium text-black dark:text-white xl:pl-11">
                                        Transaction No
                                    </th>
                                    <th className="text-standard min-w-[150px] py-2 px-4 font-medium text-black dark:text-white">
                                        Bank Tran No
                                    </th>
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Bank Code
                                    </th>
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Invoice type
                                    </th>
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Amount
                                    </th>
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Due Date
                                    </th>
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Payment Date
                                    </th>
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Status
                                    </th>
                                    <th className="text-standard min-w-[120px] py-2 px-4 font-medium text-black dark:text-white">
                                        Image
                                    </th>
                                    <th className="text-standard py-2 px-4 font-medium text-black dark:text-white">
                                        Actions
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                {dataPaymentsPaging && dataPaymentsPaging.content.map((i, key) => (
                                    <tr key={key}>
                                        <td className="border-b border-[#eee] py-2 px-4 pl-9 dark:border-strokedark xl:pl-11">
                                            <h5 className="font-medium text-black dark:text-white">
                                                {i.transactionNo || 'Credit transfer'}
                                            </h5>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {i.extraData ? i.extraData.vnp_BankTranNo : '-----'}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">

                                            {i.extraData ? i.extraData.vnp_BankCode : '-----'}
                                            {
                                                // i.bankCode ? <p className="text-black dark:text-white">
                                                //     {i.bankCode}
                                                // </p> : <p
                                                //     className={`inline-flex rounded-full bg-opacity-10 py-1 px-3 text-sm font-medium ${i.invoice.status === 'Paid'
                                                //         ? 'bg-success text-success' : i.invoice.status === 'Pending' ? 'bg-warning text-warning' : i.invoice.status === 'Rejected' ? 'bg-danger text-danger' : ''
                                                //         }`}
                                                // >
                                                //     {i.invoice.status}
                                                // </p>
                                            }


                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {i.invoice.invoiceType}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {i.amount}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {moment(i.dueDate).format("HH:mm:ss-DD/MM/YYYY")}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {formatPaidAt(i.createdAt)}

                                                {/* {moment(i.createdAt).format("HH:mm:ss-DD/MM/YYYY")} */}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {i.status && <Status status={i.status} />}
                                            </p>
                                        </td>
                                        <td className="w-40 border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            <p className="text-black dark:text-white">
                                                {i.uploadImage && <img src={i.uploadImage} alt="" />}
                                            </p>
                                        </td>
                                        <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            {
                                                i.uploadImage && <div className="flex items-center space-x-3.5">
                                                    <button onClick={() => { handleUpdatePayment({ status: "Completed", paymentId: i.id }) }} className={`hover:text-success transition`}>
                                                        <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M438.6 105.4c12.5 12.5 12.5 32.8 0 45.3l-256 256c-12.5 12.5-32.8 12.5-45.3 0l-128-128c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0L160 338.7 393.4 105.4c12.5-12.5 32.8-12.5 45.3 0z" /></svg>
                                                    </button>
                                                    <button onClick={() => { handleUpdatePayment({ status: "Canceled", paymentId: i.id }) }} className="hover:text-danger transition">
                                                        <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512"><path d="M342.6 150.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L192 210.7 86.6 105.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L146.7 256 41.4 361.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0L192 301.3 297.4 406.6c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L237.3 256 342.6 150.6z" /></svg>
                                                    </button>
                                                </div>
                                            }
                                        </td>
                                        {/* <td className="border-b border-[#eee] py-2 px-4 dark:border-strokedark">
                                            {
                                                !i.transactionNo && <div className="flex items-center space-x-3.5">
                                                    <button onClick={() => {
                                                        setOpenUpdateInvoiceBox(!openUpdateInvoiceBox)
                                                        setSelectedPayment(i)
                                                    }} className={`hover:text-info transition-colors`}>
                                                        <svg fill="currentColor" width='24' height='24' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M441 58.9L453.1 71c9.4 9.4 9.4 24.6 0 33.9L424 134.1 377.9 88 407 58.9c9.4-9.4 24.6-9.4 33.9 0zM209.8 256.2L344 121.9 390.1 168 255.8 302.2c-2.9 2.9-6.5 5-10.4 6.1l-58.5 16.7 16.7-58.5c1.1-3.9 3.2-7.5 6.1-10.4zM373.1 25L175.8 222.2c-8.7 8.7-15 19.4-18.3 31.1l-28.6 100c-2.4 8.4-.1 17.4 6.1 23.6s15.2 8.5 23.6 6.1l100-28.6c11.8-3.4 22.5-9.7 31.1-18.3L487 138.9c28.1-28.1 28.1-73.7 0-101.8L474.9 25C446.8-3.1 401.2-3.1 373.1 25zM88 64C39.4 64 0 103.4 0 152L0 424c0 48.6 39.4 88 88 88l272 0c48.6 0 88-39.4 88-88l0-112c0-13.3-10.7-24-24-24s-24 10.7-24 24l0 112c0 22.1-17.9 40-40 40L88 464c-22.1 0-40-17.9-40-40l0-272c0-22.1 17.9-40 40-40l112 0c13.3 0 24-10.7 24-24s-10.7-24-24-24L88 64z" /></svg>
                                                    </button>
                                                </div>
                                            }
                                        </td> */}
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
                <div className="flex sm:justify-center mb-20 absolute w-full">
                    <Pagination currentPage={currentPage} totalPages={dataPaymentsPaging ? dataPaymentsPaging.totalPages : 0} onPageChange={onPageChange} showIcons />
                </div>
            </div>
        </div >
    );
}

export default PaymentAdmin;