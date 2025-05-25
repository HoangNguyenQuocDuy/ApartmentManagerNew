import classnames from "classnames/bind";

import styles from './locker.module.scss'
import { useDispatch, useSelector } from "react-redux";
import { useEffect, useState } from "react";
import Status from '../../components/Status'
import moment from "moment";
import newRequest from '../../untils/request'
import { Pagination } from "flowbite-react";
import { setIsActiveNavbar } from "../../store/slice/applicationSlice";

const cx = classnames.bind(styles)

function Locker() {
    const dispatch = useDispatch()
    const [orders, setOrders] = useState()
    const user = useSelector(state => state.user)
    const accessToken = localStorage.getItem("accessToken")
    const [currentPage, setCurrentPage] = useState(1);
    const { pageSize } = useSelector(state => state.app)
    const onPageChange = page => {
        console.log(page)
        setCurrentPage(page)
    };
    useEffect(() => {
        handleGetOrders()
    }, [currentPage])

    const handleGetOrders = async () => {
        await newRequest.get(`/orders/?lockerId=${user.locker.id}&page=${currentPage - 1}&size=${pageSize}`, {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        })
            .then(data => {
                setOrders(data.data.data)
            })
            .catch(err => {
                alert('Error when get orders: ', err)
            })
    }

    useEffect(() => {
        dispatch(setIsActiveNavbar(true))
        handleGetOrders()
    }, [])

    return (
        <>
            <div className={cx('wrapper')}>
                <div className={cx('list')}>
                    {
                        orders && orders.content.map(o => (
                            <div key={o.id} className={cx('item')}>
                                <div className={cx('imgBox')}>
                                    <img alt={`order_#${o.id}`} src={o.image} />
                                </div>
                                <div>
                                    <div className={cx('status')}><b>Status: </b><Status status={o.status} /></div>
                                    <div className={cx('Dtime')}><b>Delivery time: </b>{moment(o.createdAt).format("HH:mm:ss DD/MM/YYYY")}</div>
                                    <div className={cx('Rtime')}><b>Received time: </b>{o.updatedAt ? moment(o.updatedAt).format("HH:mm:ss DD/MM/YYYY") : '  -----'}</div>
                                </div>
                            </div>
                        ))
                    }
                </div>
            </div>
            <div className="flex sm:justify-center mb-20 mt-20 absolute w-full">
                <Pagination currentPage={currentPage} totalPages={orders ? orders.totalPages : 0} onPageChange={onPageChange} showIcons />
            </div>
        </>
    );
}

export default Locker;