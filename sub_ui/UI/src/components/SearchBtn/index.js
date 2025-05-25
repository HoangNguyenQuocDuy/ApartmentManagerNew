import { FaMagnifyingGlass } from "react-icons/fa6";

import styles from './searchBtn.module.scss'
import classnames from 'classnames/bind'
import { useSelector } from "react-redux";

const cx = classnames.bind(styles)

function SearchBtn({ handleSearch }) {
    const { theme } = useSelector(state => state.app) 

    return (
        <div onClick={handleSearch} className={cx('warpper', { dark: theme==='dark' })}>
            <button>
                Search
            </button>
            <FaMagnifyingGlass />
        </div>
    );
}

export default SearchBtn;