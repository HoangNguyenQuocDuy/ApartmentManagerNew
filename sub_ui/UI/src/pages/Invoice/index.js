import classnames from 'classnames/bind'

import styles from './invoice.module.scss'
import { useDispatch, useSelector } from 'react-redux'
import { useEffect, useState } from 'react'
import newRequest from '../../untils/request'
import { IoIosArrowDown } from 'react-icons/io'
import { Pagination } from 'flowbite-react'
import InvoiceCard from '../../components/InvoiceCard'
import { setIsActiveNavbar } from '../../store/slice/applicationSlice'
import { RootEnum } from '../../assets/enum'
import { notify } from '../../untils/notification'

const cx = classnames.bind(styles)

function Invoice() {
    const dispatch = useDispatch()
    const { pageSize, isReloadInvoice } = useSelector(state => state.app)
    const user = useSelector(state => state.user)
    const [invoices, setInvoices] = useState()
    const accessToken = localStorage.getItem('accessToken')
    const [statusActive, setStatusActive] = useState(false)
    const [typeActive, setTypeActive] = useState(false)
    const [selectedStatus, setSelectedStatus] = useState('')
    const [selectedType, setSelectedType] = useState('')
    const [currentPage, setCurrentPage] = useState(1);
    const [selectedTypeId, setSelectedTypeId] = useState()
    const onPageChange = page => {
        console.log(page)
        setCurrentPage(page)
    };

    const status = ['No select', 'Paid', 'Unpaid']
    const types = ['No select', 'Electric', 'Water', 'Room', 'Packing', 'Other']

    const handleGetInvoices = () => {
        newRequest.get(`${RootEnum.API_PAYMENT}?userId=${user.id}&page=${currentPage - 1}&size=${pageSize}`, {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        })
            .then(data => {
                console.log('invoices: ', data.data.data)
                setInvoices(data.data.data)
            })
            .catch(err => {
                alert('Error when get invoices: ', err)
            })
    }

    const handleSearchInvoices = () => {
        let q = `&page=${currentPage - 1}&size=${pageSize}`
        if (selectedStatus !== '' && selectedStatus !== 'No select') {
            q += `&status=${selectedStatus}`
        }
        if (selectedType !== '' && selectedType !== 'No select') {
            q += `&invoiceTypeId=${selectedTypeId}`
        }
        newRequest.get(`${RootEnum.API_PAYMENT}?userId=${user.id + q}`, {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        })
            .then(data => {
                console.log('invoices: ', data.data.data)
                setInvoices(data.data.data)
            })
            .catch(err => {
                console.log('error from getting list invoice: ', err)

                notify(err, 'warn')
            })
    }

    useEffect(() => {
        dispatch(setIsActiveNavbar(true))
        handleGetInvoices()
    }, [currentPage, isReloadInvoice])

    useEffect(() => {
        if (selectedType !== 'No select' || selectedType !== 'No select') {
            handleSearchInvoices()
        }
    }, [selectedStatus, selectedType])

    return (
        <div className={cx('wrapper')}>
            <div className={cx('searchBox')}>
                <div className={cx('statusList')}>
                    <div onClick={() => { setStatusActive(!statusActive) }} className={cx('title')}>
                        <div>{selectedStatus === '' ? 'Status' : selectedStatus}</div>
                        <IoIosArrowDown size={20} />
                    </div>
                    <ul className={cx('list', { 'active': statusActive })}>
                        {status.map((t, i) => <li className={cx({ active: t === selectedStatus })}
                            onClick={() => {
                                setSelectedStatus(t)
                                setStatusActive(false)
                            }} key={i} >{t}</li>)}
                    </ul>
                </div>
                <div className={cx('typeList')}>
                    <div onClick={() => { setTypeActive(!typeActive) }} className={cx('title')}>
                        <div>{selectedType === '' ? 'Type' : selectedType}</div>
                        <IoIosArrowDown size={20} />
                    </div>
                    <ul className={cx('list', { 'active': typeActive })}>
                        {types.map((t, i) => <li className={cx({ active: t === selectedType })}
                            onClick={() => {
                                setSelectedType(t)
                                setSelectedTypeId(i)
                                setTypeActive(false)
                            }} key={i} >{t}</li>)}
                    </ul>
                </div>
            </div>

            <div className={cx('invoices')}>
                {
                    invoices && invoices.content.map(item => (
                        <>
                            <InvoiceCard item={item} />
                        </>
                    ))
                }
                <div className="flex sm:justify-center mb-20 relative w-full top-10">
                    <Pagination currentPage={currentPage} totalPages={invoices ? invoices.totalPages : 0} onPageChange={onPageChange} showIcons />
                </div>
            </div>
        </div>
    );
}

export default Invoice;