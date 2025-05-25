import classnames from "classnames/bind";
import styles from './newUserLayout.module.scss'
import NewHeader from "../../components/NewHeader/NewHeader";
import Home from "../../pages/Home";

const cx = classnames.bind(styles)

function NewUserLayout({ children }) {
    return (
        <div className='flex w-full h-full'>
            <Home />
            <div className="relative flex flex-1 flex-col overflow-y-auto overflow-x-hidden dark:bg-boxdark-2 dark:text-bodydark h-screen transition">
                <NewHeader />
                <div className={cx('children')}>
                    {children}
                </div>
            </div>
        </div>
    );
}

export default NewUserLayout;