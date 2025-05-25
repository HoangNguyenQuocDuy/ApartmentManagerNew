import { forwardRef } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import classNames from "classnames/bind";

import styles from "./icon.module.scss";
import { useSelector } from "react-redux";

const cx = classNames.bind(styles);

const Icon = forwardRef(
  ({ icon, className, onClick, IconAccess, width, height, color, noBackground }, ref) => {
    const { theme } = useSelector(state => state.app);

    return (
      <button
        ref={ref}
        onClick={onClick}
        className={cx("wrapper", className, {
          noBackground: noBackground,
          light: theme === "light",
        })}
      >
        {IconAccess ? (
          <IconAccess width={width} height={height} color={color} />
        ) : (
          <FontAwesomeIcon icon={icon} />
        )}
      </button>
    );
  }
);

export default Icon;
