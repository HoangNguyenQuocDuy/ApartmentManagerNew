import { useContext, useEffect, useState, useRef } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import classNames from 'classnames/bind';
import { faX, faMagnifyingGlass, faSpinner } from '@fortawesome/free-solid-svg-icons';

import styles from './addRoom.module.scss';
import Icon from '~/components/Icon';
import { AppContext } from '~/Context/AppProvider';
import Image from '~/components/Image';
import { db } from '~/firebase/config';
import { useDebounce } from '~/hooks/useDebounce';
import { addDocument } from '~/firebase/services';
import { AuthContext } from '~/Context/AuthProvider';

const cx = classNames.bind(styles);

function AddRoom() {
    const { uid, displayName } = useContext(AuthContext)
    const { theme, setUserAddRoomVisible, userAddRoomVisible } = useContext(AppContext);
    const [nameValue, setNameValue] = useState('');
    const [membersValue, setMembersValue] = useState('');
    const [selectedUsers, setSelectedUsers] = useState([]);
    const [listUsers, setListUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    const debounced = useDebounce(membersValue, 500);
    const inputUsersRef = useRef()

    const handleDeleteSelectedUser = (user) => {
        setSelectedUsers((prev) => {
            return prev.filter((value) => value.uid !== user.uid);
        });
    };

    useEffect(() => {
        if (!userAddRoomVisible) {
            setNameValue('');
            setMembersValue('');
            setSelectedUsers([]);
        }
    }, [userAddRoomVisible]);

    const handleSelectUser = (user) => {
        if (
            selectedUsers.find((value) => {
                return value.uid === user.uid;
            })
        ) {
            handleDeleteSelectedUser(user);
        } else {
            setSelectedUsers((prev) => {
                return [...prev, user];
            });
        }
    };

    useEffect(() => {
        if (!debounced) {
            setListUsers([]);
            return;
        }
        setLoading(true);
        db.collection('users')
            .where('keywords', 'array-contains', membersValue?.toLocaleLowerCase())
            .orderBy('displayName')
            .limit(20)
            .get()
            .then((snapShot) => {
                setListUsers(
                    snapShot.docs.map((doc) => {
                        const data = doc.data();
                        return {
                            displayName: data.displayName,
                            photoUrl: data.photoURL,
                            uid: data.uid,
                        };
                    }).filter(member => member.uid !== uid),
                );
                setLoading(false);
            });
    }, [debounced]);

    function handleChangeSearchUsers(e) {
        if (!membersValue.startsWith(' ')) {
            setMembersValue(e.target.value);
        }
    }

    const handleAddRoom = (e) => {
        e.preventDefault();

        if(selectedUsers.length > 0) { 
            let roomName = nameValue || selectedUsers.reduce((name, user) => {
                if (name == '') return `${user.displayName}`;
                return `${name}, ${user.displayName}`;
            }, displayName);

            addDocument('rooms', {
                displayName: roomName,
                adminId: uid,
                members: [...selectedUsers.map(user => {
                    return user.uid
                }), uid],
                lastMessage: {
                    type: '',
                    uid: '',
                    displayName: '',
                    text: ''
                }
            })
            console.log('1')
            setUserAddRoomVisible(false)
        } else {
            alert('Cần thêm ít nhất 1 thành viên để tạo nhóm.')
        }
    };

    return (
        <div>
            <form className={cx('wrapper', { light: theme === 'light' })}>
                <p className={cx('title')}>
                    Tạo nhóm chat
                    <Icon
                        onClick={() => {
                            setUserAddRoomVisible(false);
                        }}
                        className={cx('remove')}
                        icon={faX}
                    />
                </p>
                <div className={cx('box-name')}>
                    <label
                        for="inputName"
                        className={cx('label', { active: nameValue !== '', light: theme === 'light' })}
                    >
                        Nhập tên nhóm
                    </label>
                    <div className={cx('input-name')}>
                        <input
                            onChange={(e) => {
                                setNameValue(e.target.value);
                            }}
                            value={nameValue}
                            id={'inputName'}
                            placeholder="Tên nhóm..."
                            type="text"
                            className={cx({ light: theme === 'light' })}
                        />
                    </div>
                </div>
                <div className={cx('box-add')}>
                    <label
                        className={cx('label', {
                            active: membersValue !== '' || selectedUsers.length > 0,
                            light: theme === 'light',
                        })}
                        for="inputUser"
                    >
                        Thêm thành viên
                    </label>

                    <div className={cx('wrapper-members', { active: selectedUsers.length > 0 })}>
                        <div className={cx('members-add')}>
                            {selectedUsers.map((member) => {
                                return (
                                    <div className={cx('member')} key={member.uid}>
                                        <Image className={cx('member-img')} src={member.photoUrl} />
                                        <span
                                            onClick={() => {
                                                handleDeleteSelectedUser(member);
                                            }}
                                            className={cx('member-close')}
                                        >
                                            <FontAwesomeIcon icon={faX} />
                                        </span>
                                        <div className={cx('member-name')}>{member.displayName}</div>
                                    </div>
                                );
                            })}
                        </div>
                    </div>
                    <div className={cx('find-box')}>
                        <div className={cx('find-box-wrapper')}>
                            <label for="inputUser" className={cx('find-icon')}>
                                <FontAwesomeIcon icon={faMagnifyingGlass} />
                            </label>
                            <input
                                ref={inputUsersRef}
                                onChange={handleChangeSearchUsers}
                                value={membersValue}
                                id="inputUser"
                                className={cx({ light: theme === 'light' })}
                                type="text"
                                placeholder="Tìm kiếm..."
                            />
                            {membersValue !== '' && (
                                <span
                                    onClick={() => {
                                        setMembersValue('');
                                        inputUsersRef.current.focus()
                                    }}
                                    className={cx('close-icon', { light: theme === 'light' })}
                                >
                                    {!loading && <FontAwesomeIcon icon={faX} />}
                                </span>
                            )}
                            {loading && (
                                <span className={cx('loading-icon', { light: theme === 'light' })}>
                                    <span>
                                        <FontAwesomeIcon icon={faSpinner} />
                                    </span>
                                </span>
                            )}
                        </div>
                        <ul className={cx('list-users', { show: membersValue !== '' && listUsers.length > 0 })}>
                            {listUsers.map((user) => {
                                return (
                                    <li
                                        key={user.uid}
                                        onClick={() => {
                                            handleSelectUser(user);
                                        }}
                                        className={cx('user', {
                                            light: theme === 'light',
                                            active: selectedUsers.find((selectedUser) => selectedUser.uid === user.uid)
                                                ? true
                                                : false,
                                        })}
                                    >
                                        <div className={cx('user-info')}>
                                            <Image className={cx('img')} src={user.photoUrl} />
                                            <span className={cx('user-name')}>{user.displayName}</span>
                                        </div>
                                        <label className={cx('checkbox', { light: theme === 'light' })}>
                                            <input
                                                checked={
                                                    selectedUsers.find((selectedUser) => selectedUser.uid === user.uid)
                                                        ? true
                                                        : false
                                                }
                                                readOnly
                                                className={cx('input-check')}
                                                type="checkbox"
                                            />
                                            <span className={cx('geekmark')}></span>
                                        </label>
                                    </li>
                                );
                            })}
                        </ul>
                    </div>
                </div>
                <div className={cx('add-group', { active: selectedUsers.length > 0 })}>
                    <button onClick={handleAddRoom} className={cx({ active: selectedUsers.length > 0 })}>
                        Thêm nhóm
                    </button>
                </div>
            </form>
        </div>
    );
}

export default AddRoom;
