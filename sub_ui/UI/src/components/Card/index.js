import classnames from 'classnames/bind'
import styles from './card.module.scss'
import images from '../../assets/images'
import moment from 'moment';

const cx = classnames.bind(styles)

function Card({ card }) {
    console.log(card)
    function formatPaidAt(paidAtArray) {
        if (!paidAtArray || paidAtArray.length < 6) {
            return '------';
        }

        const date = new Date(
            paidAtArray[0],                  // year
            paidAtArray[1] - 1,              // month (0-based)
            paidAtArray[2],                  // day
            paidAtArray[3],                  // hour
            paidAtArray[4],                  // minute
            paidAtArray[5],                  // second
            Math.floor((paidAtArray[6] || 0) / 1_000_000) // nanoseconds -> milliseconds
        );

        return moment(date).format('HH:mm:ss DD/MM/YYYY');
    }
    return (
        <div className={cx('container')}>
            <h3><strong>DV APARTMENT</strong></h3>
            <div className={cx('infomations')}>
                <div className={cx('avatar')}>
                    <img src={card.relative.avatar || images.noAvatar} alt='avatar' />
                </div>
                <div className={cx('fields')}>
                    <div className={cx('field')}>
                        <span className={cx('name', 'title')}>Name: </span>
                        <span>{`${card.relative.fullName}`}</span>
                    </div>
                    {/* <div className={cx('field')}>
                        <span className={cx('room', 'title')}>Room: </span>
                        <span>{card.relative.userId.room.name}</span>
                    </div> */}
                    <div className={cx('field')}>
                        <span className={cx('cardType', 'title')}>Card type: </span>
                        <span>{card.licensePlates ? 'Parking card' : 'Entry card'}</span>
                    </div>
                    <div className={cx('field')}>
                        <span className={cx('title')}>Status: </span>
                        <span className={cx('status', {
                            cancel: card.status === 'Canceled', pending: card.status === 'Pending',
                            confirmed: card.status === 'Active'
                        })}>{card.status}</span>
                    </div>
                    {
                        card.typeOfVehicle &&
                        <div className={cx('field')}>
                            <span className={cx('typeOfVehicle', 'title')}>Type of vehicle: </span>
                            <span>{card.typeOfVehicle}</span>
                        </div>
                    }
                    {
                        card.licensePlates &&
                        <div className={cx('field')}>
                            <span className={cx('licensePlates', 'title')}>License plates: </span>
                            <span>{card.licensePlates}</span>
                        </div>
                    }
                    <div className={cx('field')}>
                        <span className={cx('createdAt', 'title')}>License date: </span>
                        <span>{card.createdAt ? formatPaidAt(card.createdAt) : '-----'}</span>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Card;