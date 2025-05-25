import classnames from 'classnames/bind'

import styles from './status.module.scss'

const cx = classnames.bind(styles)

function Status({ status }) {
    console.log(status)
    return (
        <div className={cx('wrapper', status.toLowerCase())}>
            <span className={cx('text')}>{ status[0].toUpperCase() + status.toLowerCase().slice(1) }</span>
        </div>
    );
}

export default Status;