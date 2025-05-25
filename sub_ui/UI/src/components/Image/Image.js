import { useState } from 'react';
import { faCircle } from '@fortawesome/free-solid-svg-icons';
import classNames from 'classnames/bind';

import images from '../../assets/images/';
import Icon from '../Icon';
import styles from './image.module.scss';

const cx = classNames.bind(styles);

function Image({ src, alt, onClick, active, className, fallback: customFallback = images.defaultUser, ...props }) {
    const [fallback, setFallback] = useState('');

    const handleError = () => {
        setFallback(customFallback);
    };

    return (
        <span className={cx('wrapper')}>
            <img
                onClick={onClick}
                className={className}
                src={fallback || src}
                alt={alt}
                {...props}
                onError={handleError}
            />
            {active && <Icon className={cx('state')} noBackground icon={faCircle} />}
        </span>
    );
}

export default Image;
