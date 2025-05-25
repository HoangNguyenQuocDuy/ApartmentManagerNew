import { useState, useContext } from "react";
import {
  faX,
  faMoon,
  faRightFromBracket,
  faUserPen,
} from "@fortawesome/free-solid-svg-icons";
import classNames from "classnames/bind";

import styles from "./setting.module.scss";
import AvatarInfo from "~/components/AvatarInfo";
import Feature from "~/components/Feature";
import { AppContext } from "~/Context/AppProvider";
import Icon from "~/components/Icon";
import { auth } from "~/firebase/config";
import { AuthContext } from "~/Context/AuthProvider"

const cx = classNames.bind(styles);

function Setting() {
  const appContext = useContext(AppContext);
  const { displayName, photoURL } = useContext(AuthContext)

  // const [name, setName] = 

  const changeTheme = () => {
    if (appContext.theme === "dark") {
      appContext.setTheme("light")
      appContext.setAppConfig("appTheme", 'light');
    } else {
      appContext.setTheme("dark")
      appContext.setAppConfig("appTheme", 'dark');
    }
  };
  

  const listSetting = [
    {
      icon: faMoon,
      title: `Chế độ tối: ${
        appContext.theme === "dark" ? "Đang bật" : "Đang tắt"
      } `,
      onClick: changeTheme,
    },
  ];

  return (
    <div className={cx("wrapper", { light: appContext.theme === "light" })}>
      <p className={cx("title")}>
        Tùy chọn
        <Icon
          onClick={appContext.handleClose}
          className={cx("remove")}
          icon={faX}
        />
      </p>
      <p className={cx("")}>Tài khoản</p>
      <div className={cx("avt")}>
        <AvatarInfo name={displayName} des='Sửa tên và ảnh đại diện' rightIcon={faUserPen} photoUrl={photoURL} />
      </div>
      <ul className={cx("setting")}>
        {listSetting.map((item, idx) => {
          return (
            <li key={idx}>
              <Feature
                icon={item.icon}
                title={item.title}
                onClick={item.onClick}
              />
            </li>
          );
        })}
      </ul>
      <div className={cx("log-out")}>
        <Feature onClick={() => { auth.signOut() }} icon={faRightFromBracket} title="Đăng xuất" />
      </div>
    </div>
  );
}

export default Setting;
