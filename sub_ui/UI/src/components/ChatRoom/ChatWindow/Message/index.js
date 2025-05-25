import Tippy from '@tippyjs/react/headless';
import classNames from 'classnames/bind';

import styles from './message.module.scss';
import Image from '../../../Image';
import { useSelector } from 'react-redux';
import images from '../../../../assets/images';
import moment from 'moment/moment';
import { useEffect, useState } from 'react';

const cx = classNames.bind(styles);

function Message({ createdAt, key, text, displayName, photoURL, start, end, self, showAvatar, type, imgURL }) {
    const { theme } = useSelector(state => state.app);
    // const time = moment(createdAt).format("HH:mm:ss DD/MM/YYYY");
    // const [time, setTime] = useState()
    const [messageTime, setMessageTime] = useState('')
    const formatTime = () => {
        if (createdAt) {
            let timestamp = moment(createdAt).toDate();
            let today = new Date();

            const time = {
                minutes: 0,
                hours: 0,
                day: 0,
                date: 0,
                month: 0,
                year: 0,
            };
            time.minutes = timestamp.getMinutes();
            time.hours = timestamp.getHours();
            time.day = timestamp.getDay();
            time.date = timestamp.getDate();
            time.month = timestamp.getMonth() + 1;
            time.year = timestamp.getFullYear();

            if (time) {
                console.log('today: ', today.getDate())
                console.log('time: ', time.day)

                setMessageTime(today.getDate() !== time.date && today.getMonth() !== time.month && today.getFullYear !== time.year
                    ? `${time.hours}:${time.minutes} ${time.date} Tháng ${time.month}, ${time.year}`
                    : `${time.hours}:${time.minutes}`)
                console.log(messageTime)
            }
        }
    };


    useEffect(() => {
        formatTime()
    }, [])
    // console.log('createdAt: ', createdAt)
    console.log(messageTime)


    return (
        <div key={key} className={cx('wrapper', { self: self })}>
            <div>
                <p className={cx('user-name', { show: start, self: self })}>{displayName}</p>
                <div className={cx('messageItem', { self: self })}>
                    <Image className={cx('imgUser-chat', { show: showAvatar, self: self })} src={photoURL || images.defaultUser} />
                    {type === '@text' && (
                        <Tippy
                            delay={[500, 200]}
                            placement="left-start"
                            render={(attrs) => (
                                <div
                                    className={cx('tippy-message', {
                                        light: theme === 'light',
                                    })}
                                    tabIndex="-1"
                                    {...attrs}
                                >
                                    <span>{messageTime}</span>
                                </div>
                            )}
                        >
                            <p
                                className={cx('message', {
                                    light: theme === 'light',
                                    start: start,
                                    end: end,
                                    self: self,
                                    noImage: showAvatar,
                                })}
                            >
                                <span>{text}</span>
                            </p>
                        </Tippy>
                    )}

                    {type === '@image' && (
                        <Tippy
                            delay={[500, 200]}
                            placement="left-start"
                            render={(attrs) => (
                                <div
                                    className={cx('tippy-message', {
                                        light: theme === 'light',
                                    })}
                                    tabIndex="-1"
                                    {...attrs}
                                >
                                    <span>{messageTime}</span>
                                </div>
                            )}
                        >
                            <img
                                src={imgURL}
                                className={cx('image', { start: start, end: end, self: self, noImage: showAvatar })}
                            />
                        </Tippy>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Message;
