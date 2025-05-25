import classnames from "classnames/bind";

import styles from './service.module.scss'
import { useEffect, useState } from "react";
import { useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { IoIosArrowDown } from "react-icons/io";
import newRequest from "../../untils/request";
import Card from "../../components/Card";
import ReactLoading from 'react-loading';
import { notify } from "../../untils/notification";
import { ToastContainer } from "react-toastify";
import { setIsActiveNavbar } from "../../store/slice/applicationSlice";
import { RootEnum } from "../../assets/enum";

const cx = classnames.bind(styles)

function Service() {
    const dispatch = useDispatch()
    const user = useSelector(state => state.user)
    const accessToken = localStorage.getItem('accessToken')
    const relatives = useSelector(state => state.relatives)

    const [typeOfVehicle, setTypeOfVehicle] = useState('')
    const [licensePlates, setLicensePlates] = useState('')
    const [isEntryRight, setIsEntryRight] = useState(false)
    const [isParkingRight, setIsParkingRight] = useState(false)
    const [active, setActive] = useState(false)
    const [selectedRelated, selectedRelative] = useState(relatives[0] ?? null)
    const [isTypeOfVehicleError, setIsTypeOfVehicleError] = useState(false)
    const [isLicensePlatesError, setIsLicensePlatesError] = useState(false)
    const [entriedCards, setEntriedCards] = useState()
    const [parkCards, setParkCards] = useState()
    const [isLoading, setIsLoading] = useState(false)

    const handleGetEntryCards = async () => {
        setIsLoading(true)
        try {
            const response = await newRequest.get(`${RootEnum.API_ENTRY_RIGHT}?userId=${user.id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });
            console.log('response---: ', response.data.data)
            setEntriedCards(response.data.data.data.items);
        } catch (err) {
            console.log('err: ', err)
            alert('Error when getting entried cards: ' + err);
        } finally {
            setIsLoading(false);
        }
    }

    const handleGetParkCards = async () => {
        setIsLoading(true)
        try {
            const response = await newRequest.get(`${RootEnum.API_PARKING_RIGHT}?userId=${user.id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });
            setParkCards(response.data.data.data.items);
        } catch (err) {
            console.log('err: ', err)
            alert('Error when getting entried cards: ' + err);
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        dispatch(setIsActiveNavbar(true))
        handleGetEntryCards()
        handleGetParkCards()
        console.log('entriedCards: ', entriedCards)
        console.log('parkCards: ', parkCards)

        console.log('relative: ', relatives)
    }, [])


    const typeRef = useRef()
    const licensePlatesRef = useRef()

    const handleRegisterEntry = () => {
        if (isEntryRight) {
            setIsLoading(true)
            newRequest.post(`${RootEnum.API_ENTRY_RIGHT}`, {
                relativeId: selectedRelated.id
            }, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
                .then(data => {
                    notify('Entry card has been created!', 'success')
                    console.log('2')
                    handleGetEntryCards()
                    setIsEntryRight(false)
                })
                .catch(err => {
                    console.log('err create entryright: ', err)
                    notify('Error when creating entry right: ' + err, 'error')
                })
                .finally(() => {
                    setIsLoading(false)
                })
        }
    }

    const handleRegisterParking = () => {
        if (isParkingRight) {
            if (typeOfVehicle.trim() === '') {
                setIsTypeOfVehicleError(true)
                typeRef.current.focus()
            } else if (licensePlates.trim() === '') {
                setIsLicensePlatesError(true)
                licensePlatesRef.current.focus()
            } else {
                setIsLoading(true)
                newRequest.post(`${RootEnum.API_PARKING_RIGHT}`, {
                    typeOfVehicle, licensePlates, relativeId: selectedRelated.id
                }, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                })
                    .then(() => {
                        notify('Parking card has been created!', 'success')
                        setTypeOfVehicle('')
                        setLicensePlates('')
                        setIsParkingRight(false)
                        handleGetParkCards()
                    })
                    .catch(err => {
                        notify('Error when creating parking right: ' + err, 'error')
                    })
                    .finally(() => {
                        setIsLoading(false);
                    })
            }
        }
    }

    const handleRegisterCard = async () => {
        if (!isParkingRight && !isEntryRight) {
            notify('Please check the type you wanna create!', 'warn')
        } else {
            handleRegisterEntry()
            handleRegisterParking()
        }

    }

    return (
        <div className={cx('container')}>
            <div className={cx('wrapper')}>
                <h2>Register Card</h2>
                {/* <ToastContainer />   */}
                <div className={cx('rBox')}>
                    <div className={cx('title')}>Relatives</div>

                    <div onClick={() => { setActive(!active) }} className={cx('type', 'selectedRelative')}>
                        <span>
                            {selectedRelated && selectedRelated.fullName}
                        </span>
                        <IoIosArrowDown size={20} className={cx('arrowIcon')} />
                        <ul className={cx('list', { 'active': active })}>
                            {relatives && relatives.map(t => <li className={cx('type', { active: selectedRelated && t.id === selectedRelated.id })}
                                onClick={() => {
                                    selectedRelative(t)
                                    setActive(false)
                                }} key={t.id} >{t.fullName}</li>)}
                        </ul>
                    </div>
                </div>
                <div className={cx('entryBox')}>
                    <div className={cx('fieldBox')}>
                        <label htmlFor='type'>Type of vehicle</label>
                        <div className={cx('field')}>
                            <input type='text' ref={typeRef} value={typeOfVehicle}
                                onChange={(e) => {
                                    setTypeOfVehicle(e.target.value)
                                    if (isTypeOfVehicleError) {
                                        setIsTypeOfVehicleError(false)
                                    }
                                }}
                                id='type' placeholder='Type of vehicle...' />
                            <span className={cx('noted', { active: isTypeOfVehicleError })}>Please fill this field</span>
                        </div>
                    </div>
                    <div className={cx('fieldBox')}>
                        <label htmlFor='plates'>License plates</label>
                        <div className={cx('field')}>
                            <input type='text' ref={licensePlatesRef} value={licensePlates}
                                onChange={(e) => {
                                    setLicensePlates(e.target.value)
                                    if (isLicensePlatesError) {
                                        setIsLicensePlatesError(false)
                                    }
                                }}
                                id='plates' placeholder='License Plates...' />
                            <span className={cx('noted', { active: isLicensePlatesError })}>Please fill this field</span>
                        </div>
                    </div>
                    <div className={cx('note')}>
                        <span>(Please fill in the 2 fields above if registation includes parking)</span>
                    </div>
                </div>
                <div className={cx('rightBox')}>
                    <label>
                        <input checked={isEntryRight} type="checkbox"
                            onChange={(e) => setIsEntryRight(e.target.checked)}
                        />
                        <span className={cx('title')}>Entry Right</span>
                    </label>
                    <label className={cx('parking')}>
                        <input checked={isParkingRight} type="checkbox"
                            onChange={(e) => setIsParkingRight(e.target.checked)}
                        />
                        <span className={cx('title')}>Parking Right</span>
                    </label>
                </div>

                <div className={cx('btnBox')}>
                    <button onClick={handleRegisterCard} className={cx('', { prevent: isLoading })}>Register</button>
                    {isLoading && <ReactLoading className={cx('loading')} type='spin' color={'#999'} />}
                </div>
            </div>
            <div className={cx('wrapper')}>
                <h2>Your Cards</h2>
                <div className={cx('cards')}>
                    {
                        entriedCards && entriedCards.map(ec => (
                            <Card key={ec.id} card={ec} />
                        ))
                    }
                    {
                        parkCards && parkCards.map(ec => (
                            <Card key={ec.id} card={ec} />
                        ))
                    }
                </div>
            </div>
        </div >
    );
}

export default Service;