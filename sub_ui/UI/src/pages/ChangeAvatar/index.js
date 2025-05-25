import classnames from 'classnames/bind'
import { HiArrowLeft, HiOutlineArrowRight } from "react-icons/hi2";
import styles from './changeAvatar.module.scss'
import { useState } from 'react'
import { useDispatch } from 'react-redux';
import { fetchLogin } from '../../store/slice/accountSlice';
import { loadUser } from '../../store/slice/userSlice';
import { useNavigate } from 'react-router-dom';
import routes from '../../config/routes';
import { fetchRelatives } from '../../store/slice/relativesSlice';

const cx = classnames.bind(styles)

function ChangeAvatar() {
    const [isActive, setIsActive] = useState(false)
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [confirmedPassword, setConfirmedPassword] = useState('')
    const [email, setEmail] = useState('')
    const [err, setErr] = useState('')

    const dispatch = useDispatch()
    const navigate = useNavigate()

    const handleLogin = (e) => {
        e.preventDefault()

        if (username.trim() === '') {
            alert('Username is required!')
        }
        else if (password.trim() === '') {
            alert('Password is required!')
        } else {
            dispatch(fetchLogin({ username, password }))
                .then(action => {
                    if (action.error) {
                        setErr(action.payload.message)
                    } else {
                        console.log('action.payload after login: ', action.payload);
                        dispatch(loadUser(action.payload))
                        dispatch(fetchRelatives({ userId: action.payload.userResponse.id, accessToken: action.payload.accessToken }))
                        localStorage.setItem('accessToken', action.payload.accessToken)
                        navigate(routes.home)
                    }
                })
        }
    }

    return (
        <div className={cx('container')}>
            <div className={cx('wrapper')}>
                <div className={cx('formBox')}>
                    <form className={cx('form-forgot', { 'active': !isActive })}>
                        <h3>Forgot password</h3>
                        <div className={cx('form-item')}>
                            <input
                                type="email"
                                className="form-control"
                                placeholder="Enter your email"
                                value={email}
                                onChange={(e) => {
                                    if (err !== '') {
                                        setErr('')
                                    }
                                    setEmail(e.target.value)
                                }}
                            />
                        </div>

                        <div className={cx('buttonBox')}>
                            <button type="submit" className="btn btn-primary">
                                Submit
                            </button>
                        </div>
                        <div className={cx('over')}></div>
                    </form>

                    <form onSubmit={handleLogin} className={cx('form-login', { 'active': !isActive })}>
                        <h3>Sign In</h3>
                        <div className={cx('form-item')}>
                            <input
                                type="text"
                                className="form-control"
                                placeholder="Enter your username"
                                value={username}
                                onChange={(e) => {
                                    if (err !== '') {
                                        setErr('')
                                    }
                                    setUsername(e.target.value)
                                }}
                            />
                        </div>

                        <div className={cx('form-item')}>
                            <input
                                type="password"
                                className="form-control"
                                placeholder="Enter your password"
                                value={password}
                                onChange={(e) => {
                                    if (err !== '') {
                                        setErr('')
                                    }
                                    setPassword(e.target.value)
                                }}
                            />
                        </div>
                        {err && <p className={cx('errMessage')}>{err}</p>}
                        <div className={cx('buttonBox')}>
                            <button type="submit" className="btn btn-primary">
                                Submit
                            </button>
                        </div>
                    </form>
                </div>
                <div className={cx('introBox')}>
                    <div className={cx('introLogin', { 'active': !isActive })}>
                        <h3>Welcome to DVApartment</h3>
                        <p>Let's join with us to explore more!</p>
                        <div className={cx('buttonBox')}>
                            <button onClick={() => setIsActive(!isActive)}>
                                <HiArrowLeft size={24} />
                                <span>Forgot password</span>
                            </button>
                        </div>
                    </div>
                    <div className={cx('introForgot', { 'active': isActive })}>
                        <h3>Don't worry to much about that</h3>
                        <p>Let us know your email to get token to reset your password!</p>
                        <div className={cx('buttonBox')}>
                            <button onClick={() => setIsActive(!isActive)}>
                                <span>Login</span>
                                <HiOutlineArrowRight size={24} />
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ChangeAvatar