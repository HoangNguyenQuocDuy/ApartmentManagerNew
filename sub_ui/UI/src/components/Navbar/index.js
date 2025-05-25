import classnames from 'classnames/bind'
import { IoIosLogOut, IoMdLogIn } from "react-icons/io";
import { FaRegAddressCard, FaUsers } from "react-icons/fa";
import { IoHomeOutline } from "react-icons/io5";
import { NavLink, useNavigate } from "react-router-dom";
import { BsPerson } from "react-icons/bs";

import styles from './navbar.module.scss'
import image from '../../assets/images'
import routes from '../../config/routes';
import { useDispatch, useSelector } from 'react-redux';
import { PiLockers } from 'react-icons/pi';
import { MdOutlineDocumentScanner, MdOutlinePayment } from 'react-icons/md';
import { RiSurveyLine } from 'react-icons/ri';
import { clearUser } from '../../store/slice/userSlice';
import { logout } from '../../store/slice/accountSlice';
import { useEffect, useState } from 'react';
import { LiaFileInvoiceDollarSolid } from 'react-icons/lia';

const cx = classnames.bind(styles)

function Navbar() {
    const navigate = useNavigate()
    const dispatch = useDispatch()
    const user = useSelector(state => state.user)
    const [isActive, setIsActive] = useState(false)
    const { isActiveNavBar } = useSelector(state => state.app)

    const handleLogout = () => {
        dispatch(logout())
        dispatch(clearUser())
        navigate('/')
    }

    useEffect(() => {
        const handleScroll = () => {
            const scrollY = window.scrollY;
            if (scrollY >= 80) {
                setIsActive(true);
            } else {
                setIsActive(false);
            }
        };
    
        window.addEventListener('scroll', handleScroll);
    
        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);
    

    return (
        <div className={cx("controller", { 'active': isActive || isActiveNavBar })}>
            <div className={cx('list')}>
                <NavLink to={'/'} className={cx('logoBox')}>
                    <div className={cx('imgBox')}>
                        <img src={isActive || isActiveNavBar ? image.logo_active : image.logo} alt={'logo'} />
                    </div>
                    <div className={cx('title')}>DVApartment</div>
                </NavLink>
                <div className={cx('features')}>
                    <NavLink to={routes.home} className={(nav) => {
                        return cx('item', { active: nav.isActive });
                    }}>
                        <IoHomeOutline size={24} />
                        <span >Home</span>
                    </NavLink>
                    {
                        Object.keys(user).length !== 0 ? <>
                            {/* <NavLink to={routes.locker} className={(nav) => {
                                return cx('item', { active: nav.isActive });
                            }}>
                                <PiLockers size={24} />
                                <span>Locker</span>
                            </NavLink>
                            <NavLink to={routes.invoice} className={(nav) => {
                                return cx('item', { active: nav.isActive });
                            }}>
                                <LiaFileInvoiceDollarSolid size={24} />
                                <span>Invoice</span>
                            </NavLink>
                            <NavLink to={routes.payment} className={(nav) => {
                                return cx('item', { active: nav.isActive });
                            }}>
                                <MdOutlinePayment size={24} />
                                <span>Payment</span>
                            </NavLink>
                            <NavLink to={routes.surveys} className={(nav) => {
                                return cx('item', { active: nav.isActive });
                            }}>
                                <RiSurveyLine size={24} />
                                <span>Survey</span>
                            </NavLink>
                            <NavLink to={routes.feedback} className={(nav) => {
                                return cx('item', { active: nav.isActive });
                            }}>
                                <MdOutlineDocumentScanner size={24} />
                                <span>Feedbacks</span>
                            </NavLink>
                            <NavLink to={routes.services} className={(nav) => {
                                return cx('item', { active: nav.isActive });
                            }}>
                                <FaRegAddressCard size={24} />
                                <span>Register Card</span>
                            </NavLink> */}
                            <NavLink to={routes.personal} className={(nav) => {
                                return cx('item', { active: nav.isActive });
                            }}>
                                <BsPerson size={24} />
                                <span>{`${user.firstname} ${user.lastname}`}</span>
                            </NavLink>
                            {/* <NavLink to={routes.contracts} className={(nav) => {
                                return cx('item', { active: nav.isActive });
                            }}>
                                <BsPerson size={24} />
                                <span>Contracts</span>
                            </NavLink> */}
                            <button onClick={handleLogout} className={cx('item')}>
                                <IoIosLogOut size={24} /><span>Logout</span>
                            </button>
                        </> :
                            <>
                                <div className={cx('item')}>
                                    <FaUsers size={24} />
                                    <span>About US</span>
                                </div>
                                <NavLink to={routes.login} className={(nav) => {
                                    return cx('item', { active: nav.isActive });
                                }}>
                                    <IoMdLogIn size={24} />
                                    <span>Login</span>
                                </NavLink>
                            </>
                    }
                </div>
            </div>
        </div>
    );
}

export default Navbar;