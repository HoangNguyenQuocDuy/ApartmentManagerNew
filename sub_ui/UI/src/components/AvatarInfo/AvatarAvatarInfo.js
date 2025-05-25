import { useContext, useEffect, memo } from 'react'
import classNames from "classnames/bind";
import { faCircle } from '@fortawesome/free-solid-svg-icons';

import styles from "./avatarInfo.module.scss";
import Image from "../Image";
import Icon from '../Icon'
import { useSelector } from 'react-redux';
// import { db } from '~/firebase/config'
// import { AuthContext } from '~/Context/AuthProvider';

const cx = classNames.bind(styles);

function AvatarInfo({ name, des, photoUrl, active, className, rightIcon, medium, onClick, room, maxName }) {
  const { theme } = useSelector(state => state.app)

  // useEffect(() => {
  //   const unsubscribe = onSnapshot(
  //     collection(db, 'users'), 
  //     (snapshot) => {
  //       const {data} = snapshot.docs.map((doc) => ({
  //         ...doc.data(),
  //         id: doc.id,
  //       }))
  //       console.log(data)
  //     },
  //     (error) => {
  //       console.log(error)
  //     }
  //   )

  //   return () => {
  //     unsubscribe()
  //   }
  // }, [])
  // console.log('avatarUser re-render')

  return (

    <div onClick={onClick} className={cx("wrapper", className, { light: theme==='light', medium: medium })}>
      <div className={cx("sub-wrapper")}>
        <div className={cx('box-img')}>
          <Image active={active} className={cx("img")} src={photoUrl} />
        </div>
        <div className={cx("info", { room: room })}>
          <div className={cx("name", { maxName: maxName })}>{ name }</div>
          <p className={cx("des")}>{ des }</p>
        </div>
      </div>
      {rightIcon && <Icon className={cx('icon-primary', 'iconTheme')} icon={rightIcon} />}
    </div>
  );
}

export default memo(AvatarInfo);
