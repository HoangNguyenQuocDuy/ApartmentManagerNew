import classnames from 'classnames/bind'
import ReactLoading from 'react-loading';

import styles from './addRelative.module.scss'
import { useEffect, useState } from 'react'
import { IoIosArrowDown } from 'react-icons/io'
import newRequest from '../../untils/request'
import { useDispatch, useSelector } from 'react-redux'
import { fetchRelatives } from '../../store/slice/relativesSlice'
import { notify } from '../../untils/notification'
import { setIsLoadRelative } from '../../store/slice/applicationSlice';
import { RootEnum } from '../../assets/enum';

const cx = classnames.bind(styles)

function AddRelative({ relative }) {
    const types = ['FATHER', 'MOTHER', 'HUSBAND', 'WIFE', 'SON', 'DAUGHTER', 'BROTHER', 
        'SISTER', 'GRANDFATHER', 'GRANDMOTHER', 'UNCLE', 'AUNT', 'FRIEND', 'OTHER']
    const user = useSelector(state => state.user)
    const dispatch = useDispatch()
    const contracts = useSelector(state => state.contracts)
    const roomIds = useSelector(state => state.rooms)

    const [fullname, setFullname] = useState(relative ? relative.fullName : '')
    const [idCard, setIdCard] = useState(relative ? relative.idCard : '')
    const [rType, setRType] = useState(relative ? relative.relationship : types[0])
    const [selectedRoomId, setSelectedRoomId] = useState(contracts.length > 0 && contracts[0].roomId)
    const [typeActive, setTypeActive] = useState(false)
    const [roomActive, setRoomActive] = useState(false)
    const [rSelectedAvatar, setRSelectedAvatar] = useState()
    const [isChangeAvatar, setIsChangeAvatar] = useState(false)

    const accessToken = localStorage.getItem('accessToken')

    useEffect(() => {
        if (relative) {
            setFullname(relative.fullName)
            setRType(relative.relationship)
            setIdCard(relative.idCard)
            setRSelectedAvatar(relative.avatar)
            setTypeActive(false)
        }
        console.log('relative: ', relative)
    }, [relative])

    const handleRAvatarSelected = (event) => {
        const file = event.target.files[0];
        if (file) {
            setIsChangeAvatar(true)
            const reader = new FileReader();
            reader.onloadend = () => {
                setRSelectedAvatar(reader.result);
            };
            reader.readAsDataURL(file);
        }
    };

    const handleFilterListRoomActive = () => {

    }

    const handleRelative = async () => {
        if (fullname.trim() === '') {
            notify('Fullname is required!', 'warn')
        } else if (!idCard) {
            notify('Id card is required!', 'warn')
        } else if (idCard.length < 9 || idCard.length >  11) {
            notify('Id card is invalid!', 'warn')
        }  
        else if (!rSelectedAvatar) {
            notify('Avatar is required!', 'warn')
        }
        else if (relative) {
            if (fullname.trim() !== relative.fullName || rType !== relative.relationship || isChangeAvatar || idCard.trim() !== relative.idCard) {
                const formData = new FormData();
                formData.append('fullName', fullname.trim());
                formData.append('idCard', idCard.trim());
                formData.append('relationship', rType);
                formData.append('roomId', selectedRoomId);
                formData.append('idCard', idCard);

                if (isChangeAvatar) {
                    const avatarFile = document.getElementById('rfile').files[0];
                    formData.append('file', avatarFile);
                }

                dispatch(setIsLoadRelative(true))
                await newRequest.patch(`${RootEnum.API_RELATIVES}${relative.id}`, formData, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                })
                    .then(data => {
                        console.log(data.data);
                        dispatch(fetchRelatives({ userId: user.id, accessToken }))
                        setFullname('')
                        setRSelectedAvatar()
                        setRType(types[0])
                        notify('Relative has been updated!', 'success')
                    }).catch(error => {
                        notify('Error updating relative: ' + error.response.data.message, 'error')
                    })
                    .finally(() => {
                        dispatch(setIsLoadRelative(false))
                    })
            }
        } else {
            const formData = new FormData();
            formData.append('fullName', fullname.trim());
            formData.append('userId', user.id);
            formData.append('roomId', selectedRoomId);
            formData.append('relationship', rType);
            formData.append('idCard', idCard);

            if (isChangeAvatar) {
                const avatarFile = document.getElementById('rfile').files[0];
                formData.append('file', avatarFile);
            }

            dispatch(setIsLoadRelative(true))
            await newRequest.post(`${RootEnum.API_RELATIVES}`, formData, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
                .then(data => {
                    console.log(data.data);
                    dispatch(fetchRelatives({ userId: user.id, accessToken }))
                    setFullname('')
                    setRSelectedAvatar()
                    setRType(types[0])
                    notify('Relative has been created!', 'success')
                })
                .catch(error => {
                    console.log('err when update relative: ', error)
                    notify('Error creating relative: ' + error.response.data.message, 'error')
                })
                .finally(() => {
                    dispatch(setIsLoadRelative(false))
                })
        }
    }

    return (
        <div className={cx('wrapper')}>
            <div className={cx('addRelativeBox')}>
                <div className={cx('sw')}>
                    <div className={cx('fieldBox')}>
                        <label htmlFor='rFirstname'>Fullname</label>
                        <input id='rFirstname' value={fullname} onChange={(e) => { setFullname(e.target.value) }}
                            placeholder='Fullname...' />
                    </div>
                    <div className={cx('fieldBox')}>
                        <label htmlFor='idCard'>ID Card</label>
                        <input type='number' id='idCard' value={idCard} onChange={(e) => { setIdCard(e.target.value) }}
                            placeholder='ID Card...' />
                    </div>
                    <div className={cx('fieldBox')}>
                        <label htmlFor='rType'>Type</label>
                        <div onClick={() => { setTypeActive(!typeActive) }} className={cx('type', 'selectedType')}>
                            <span>{rType.charAt(0) + rType.slice(1).toLowerCase()}</span>
                            <IoIosArrowDown size={20} className={cx('arrowIcon')} />
                        </div>
                        <ul className={cx('list', { 'active': typeActive })}>
                            {types.map((t, i) => <li className={cx('type', { active: t === rType })}
                                onClick={() => {
                                    setRType(t)
                                    setTypeActive(false)
                                }} key={i} >{t.charAt(0) + t.slice(1).toLowerCase()}</li>)}
                        </ul>
                    </div>

                    <div className={cx('fieldBox')}>
                        <label htmlFor='rType'>Room ID</label>
                        <div onClick={() => { setRoomActive(!roomActive) }} className={cx('type', 'selectedType')}>
                            <span>{selectedRoomId}</span>
                            <IoIosArrowDown size={20} className={cx('arrowIcon')} />
                        </div>
                        <ul className={cx('list', 'contract', { 'active': roomActive })}>
                            {roomIds && roomIds.map((r, i) => <li className={cx('type', { active: r === selectedRoomId })}
                                onClick={() => {
                                    setSelectedRoomId(r)
                                    setRoomActive(false)
                                }} key={i} >{r}</li>)}
                        </ul>
                    </div>
                </div>
                <div>
                    <div className={cx('avatarBox')}>
                        <div className={cx('_avatar')}>
                            <label htmlFor='rfile'>Avatar</label>
                            <input onChange={handleRAvatarSelected} id='rfile' type='file' accept="image/*" />
                        </div>
                        <div>
                            <img src={rSelectedAvatar && rSelectedAvatar} alt='avatar' />
                        </div>
                    </div>
                </div>
            </div>
            <div className={cx('btnBox')}>
                <button onClick={handleRelative} className={cx('btn')}>{relative ? 'Update' : 'Add'}</button>
            </div>
        </div>
    );
}

export default AddRelative;