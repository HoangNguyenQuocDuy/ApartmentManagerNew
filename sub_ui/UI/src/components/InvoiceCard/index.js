import classnames from "classnames/bind";
import styles from './invoiceCard.module.scss'
import moment from "moment";
import Status from "../Status";
import ReactLoading from 'react-loading';
import newRequest from "../../untils/request";
import { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { notify } from "../../untils/notification";
import { setIsReloadInvoice } from "../../store/slice/applicationSlice";
import { RootEnum } from "../../assets/enum";

const cx = classnames.bind(styles)

function InvoiceCard({ item }) {
    const [selectedFormOfPayment, setSelectedFormOfPayment] = useState('Via Bank')
    const user = useSelector(state => state.user)
    const accessToken = localStorage.getItem('accessToken')
    const [isLoading, setIsLoading] = useState(false)
    const [image, setImage] = useState()
    const uploadRef = useRef()
    const dispatch = useDispatch()

    useEffect(() => {
        console.log('item: ', item)
        dispatch(setIsReloadInvoice(false))
    }, [])

    const handlePayInvoice = (invoicerentId, amount) => {
        console.log(selectedFormOfPayment)
        if (selectedFormOfPayment === 'Via Bank') {
            setIsLoading(true)
            newRequest.post(`${RootEnum.API_PAY}pay?amount=${amount}`, {
                userId: user.id,
                invoiceId: invoicerentId
            }, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
                .then(data => {
                    window.open(data.data.data.url, '_blank')
                    console.log('pay: ', data.data)
                })
                .catch(err => {
                    console.log("when pay this invoice: ", err)
                    notify('ERR when pay this invoice: ' + err, 'error')
                })
                .finally(() => {
                    setIsLoading(false)
                })
        } else {
            if (image) {
                const formData = new FormData();
                formData.append('invoiceId', invoicerentId);
                formData.append('userId', user.id);
                formData.append('file', image);
                setIsLoading(true)

                newRequest.post(`${RootEnum.API_PAY}manual`, formData, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    }
                })
                    .then(data => {
                        console.log('payment: ', data.data)
                        dispatch(setIsReloadInvoice(true))
                        notify('Payment successful!', 'success')
                    })
                    .catch(err => {
                        console.log(err)
                        notify("ERR when pay this invoice: " + err, 'error')
                    })
                    .finally(() => {
                        setIsLoading(false)
                    })
            } else {
                alert("No payment's image selected!")
            }
        }

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

    return (
        <div key={item.id} className={cx('item')}>
            <div className={cx('title', item.invoiceType.toLowerCase())}>
                <p>{item.invoiceType}</p>
            </div>
            <div className={cx('info')}>
                <div>
                    {/* <div className={cx('amount')}><span className={cx('title')}>Room:</span>{item.room.name}</div> */}
                    <div className={cx('amount')}><span className={cx('title')}>Amount: </span>{item.amount} VND</div>
                    <div className={cx('status')}>
                        <span className={cx('title')}>Status:</span>
                        <Status status={item.invoiceStatus} />
                    </div>
                    <div className={cx('dueDate')}>
                        <span className={cx('title')}>Due date:</span>
                        {moment(item.dueDate).format("HH:mm:ss DD/MM/YYYY")}
                    </div>
                    <div className={cx('dueDate')}>
                        <span className={cx('title')}>Pay date:</span>
                        {item.paidAt ? formatPaidAt(item.paidAt) : '------'}
                    </div>
                </div>
            </div>
            {
                item.invoiceStatus === 'Unpaid' ?
                    <>
                        {
                            !item.paidAt && <div className={cx('select-form')}>
                                <select onChange={e => {
                                    console.log(e.target.value)
                                    setSelectedFormOfPayment(e.target.value)
                                }} value={selectedFormOfPayment} id="formOfPayment" name="formOfPayment" className="text-standard shadow-md border-none bg-transparent py-2 ps-4 shadow-md rounded-md w-full">
                                    <option>Via Bank</option>
                                    <option>Credit transfer</option>
                                </select>
                            </div>
                        }
                        {
                            selectedFormOfPayment === 'Credit transfer' && item.invoiceStatus === 'Unpaid' && <>
                                <div className="box-center">
                                    {
                                        image && <img alt="payment's pic" className="w-60 h-40 rounded-lg rounded-md object-contain" src={URL.createObjectURL(image)} />
                                    }
                                </div>

                                {
                                    !item.paidAt && <div onClick={() => { uploadRef.current.click() }} className="mt-3 mx-10 flex justify-center rounded-lg border border-dashed border-gray-900/25 py-2">
                                        <div className="text-center">
                                            <label
                                                htmlFor="file-upload"
                                                className="relative cursor-pointer rounded-md bg-white font-semibold focus-within:outline-none hover:text-grey-800"
                                            >
                                                <span className="dark:text-white dark:bg-boxdark text-gray-800">Upload a payment image</span>
                                                <input
                                                    onChange={(e) => {
                                                        console.log(e.target.files[0])
                                                        setImage(e.target.files[0])
                                                    }}
                                                    ref={uploadRef} id="file-upload" name="file-upload" type="file" className="sr-only w-full" />
                                            </label>
                                        </div>
                                    </div>
                                }
                            </>
                        }
                        {
                            !item.paidAt && <div className={cx('btnBox')}>
                                <button onClick={() => { handlePayInvoice(item.id, item.amount) }} className={cx('payBtn', { 'prevent': item.status === 'Paid' })}>
                                    Pay
                                </button>
                                <div className="absolute w-full flex justify-center items-center mt-4">
                                    {isLoading && <ReactLoading className='w-40 h-40 absolute top-(-20)' type='spin' color={'#999'} height={'20px'} width={'5%'} />}
                                </div>
                            </div>

                        }
                    </> : <></>
            }
        </div>
    );
}

export default InvoiceCard;