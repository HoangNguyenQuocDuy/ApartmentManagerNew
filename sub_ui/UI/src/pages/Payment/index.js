import classnames from 'classnames/bind'

import styles from './payment.module.scss'
import { useDispatch, useSelector } from 'react-redux';
import { fetchPayment } from '../../store/slice/paymentSlice';
import { useEffect, useState } from 'react';
import moment from 'moment';
import Status from '../../components/Status';
import { IoIosArrowDown } from 'react-icons/io';
import newRequest from '../../untils/request';
import { notify } from '../../untils/notification';
import { Pagination } from 'flowbite-react';
import { setIsActiveNavbar } from '../../store/slice/applicationSlice';
import { RootEnum } from '../../assets/enum';

const cx = classnames.bind(styles)

function Payment() {

    const dispatch = useDispatch()
    const accessToken = localStorage.getItem("accessToken")
    const { pageSize } = useSelector(state => state.app)
    const [payments, setPayments] = useState()
    const user = useSelector(state => state.user)
    const [selectedMonth, setSelectedMonth] = useState(0)
    const [selectedYear, setSelectedYear] = useState(0)
    const [years, setYears] = useState([])
    const [activeMonth, setActiveMonth] = useState(false)
    const [activeYear, setActiveYear] = useState(false)
    const [paymentsSearch, setPaymentSearch] = useState([])
    const [currentPage, setCurrentPage] = useState(1);
    const onPageChange = page => {
        console.log(page)
        setCurrentPage(page)
    };
    useEffect(() => {
        handleGetPayments()
    }, [currentPage])

    const handleGetPayments = () => {
        newRequest.get(`${RootEnum.API_PAY}?page=${currentPage - 1}&size=${pageSize}&userId=${user.id}`, {
            headers: {
                "Authorization": `Bearer ${accessToken}`
            }
        })
            .then(data => {
                console.log('payments paging: ', data.data.data)

                setPayments(data.data.data)
            })
            .catch(err => {
                notify('Error getting payments: ' + err, 'error')
            })
    }

    const months = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]

    const handleInitYears = () => {
        const currentYear = new Date().getFullYear()
        for (let i = currentYear; i >= 2020; i--) {
            setYears(state => [...state, i])
        }
    }
    console.log('paymentsSearch: ', paymentsSearch)

    useEffect(() => {
        dispatch(fetchPayment({ userId: user.id, accessToken }))
        dispatch(setIsActiveNavbar(true))
        if (years.length === 0) {
            handleInitYears()
        }
    }, [])

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
        <div className={cx('container')}>
            <div className={cx('searchBox')}>
                <div className={cx('monthList')}>
                    <div onClick={() => { setActiveMonth(!activeMonth) }} className={cx('title')}>
                        <div>{selectedMonth === 0 ? 'Month' : selectedMonth}</div>
                        <IoIosArrowDown size={20} />
                    </div>
                    <ul className={cx('list', { 'active': activeMonth })}>
                        {months.map((t, i) => <li className={cx({ active: t === selectedMonth })}
                            onClick={() => {
                                setSelectedMonth(t)
                                setActiveMonth(false)
                            }} key={i} >{t}</li>)}
                    </ul>
                </div>
                <div className={cx('yearList')}>
                    <div onClick={() => { setActiveYear(!activeYear) }} className={cx('title')}>
                        <div>{selectedYear === 0 ? 'Year' : selectedYear}</div>
                        <IoIosArrowDown size={20} />
                    </div>
                    <ul className={cx('list', { 'active': activeYear })}>
                        {years.sort().map((t, i) => <li className={cx({ active: t === selectedYear })}
                            onClick={() => {
                                setSelectedYear(t)
                                setActiveYear(false)
                            }} key={i} >{t}</li>)}
                    </ul>
                </div>

                <div onClick={() => { }} className={cx('btnBox')}>
                    <button>Search</button>
                </div>
            </div>

            <div className={cx('wrapper')}>
                <table className={cx('table')} cellSpacing="0" cellPadding="0">
                    <thead>
                        <tr>
                            <th>Transaction No</th>
                            <th>Bank Tran No</th>
                            <th>Bank Code</th>
                            <th>Invoice type</th>
                            <th>Amount</th>
                            <th>Due Date</th>
                            <th>Payment Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            payments && payments.content.length > 0 && payments.content.map(p => (
                                <tr>
                                    <td>{p.transactionId}</td>
                                    <td>{p.bankTranNo}</td>
                                    <td>{p.bankCode}</td>
                                    <td className={cx('ct')}><Status status={p.status} /></td>
                                    <td>{p.amount} $</td>
                                    <td>{moment(p.invoice.dueDate).format("HH:mm:ss-DD/MM/YYYY")}</td>
                                    <td>{formatPaidAt(p.createdAt)}</td>
                                </tr>
                            ))
                        }
                    </tbody>
                </table>
            </div>
            <div className="flex sm:justify-center mb-20 absolute w-full">
                <Pagination currentPage={currentPage} totalPages={payments ? payments.totalPages : 0} onPageChange={onPageChange} showIcons />
            </div>
        </div>
    );
}

export default Payment;