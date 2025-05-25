import classnames from 'classnames/bind'

import styles from './header.module.scss'
import Navbar from "../Navbar";
import Chat from '../Chat';
import { useSelector } from 'react-redux';
import { FaRegAddressCard } from 'react-icons/fa';
import routes from '../../config/routes';
import { PiLockers } from 'react-icons/pi';
import { LiaFileInvoiceDollarSolid } from 'react-icons/lia';
import { MdOutlineDocumentScanner, MdOutlinePayment } from 'react-icons/md';
import { NavLink } from 'react-router-dom';

const cx = classnames.bind(styles)
const features = [
    {
        title: 'Register Card',
        icon: <FaRegAddressCard size={28} />,
        path: routes.services
    },
    {
        title: 'Locker',
        icon: <PiLockers size={28} />,
        path: routes.locker
    },
    {
        title: 'Invoice',
        icon: <LiaFileInvoiceDollarSolid size={28} />,
        path: routes.invoice
    },
    {
        title: 'Payment',
        icon: <MdOutlinePayment size={28} />,
        path: routes.payment
    },
    // {
    //     title: 'Survey',
    //     icon: <RiSurveyLine size={28} />,
    //     path: routes.surveys
    // },
    {
        title: 'Feedback',
        icon: <MdOutlineDocumentScanner size={28} />,
        path: routes.feedback
    },
]
function Header() {
    const user = useSelector(state => state.user)
    const accessToken = localStorage.getItem('accessToken')

    return (
        <div className={cx('wrapper')}>
            <Navbar />
            <div className='relative h-screen w-screen'>
                {
                    accessToken && user && <Chat username={'Admin'} />
                }
                <div className={cx('gridBox')}>
                    {features.map((feature, index) => (
                        <NavLink to={feature.path} key={index} className={cx('featureCard')}>
                            <div className={cx('icon')}>{feature.icon}</div>
                            <div className={cx('label')}>{feature.title}</div>
                        </NavLink>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Header;