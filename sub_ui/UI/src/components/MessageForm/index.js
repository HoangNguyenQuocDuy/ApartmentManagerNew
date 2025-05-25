import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useState, useRef } from 'react';
import Tippy, { useSingleton } from '@tippyjs/react/headless';
import 'tippy.js/dist/tippy.css'; // optional
import { faImage, faThumbsUp, faFaceSmile, faPaperPlane, faX } from '@fortawesome/free-solid-svg-icons';
import classNames from 'classnames/bind';

import styles from './messageForm.module.scss';
import { StickersIcon, Gif } from '../../assets/icon';
import Icon from '../Icon';
import { useSelector } from 'react-redux';

const cx = classNames.bind(styles);

function MessageForm({ sendMessage }) {
    const [source, target] = useSingleton();
    const [inputValue, setInputValue] = useState('');
    const { theme, selectedRoom } = useSelector(state => state.app);
    const [selectedFile, setSelectedFile] = useState();
    const inputFileRef = useRef();
    const inputTextRef = useRef()
    const { firstname, lastname } = useSelector(state => state.user)

    // const handleSendMessage = (e) => {
    //     if (e.key === 'Enter' && inputValue.trim() !== '') {
    //         const messageData = {
    //             content: inputValue,
    //             userId: id,
    //             chatRoomId: selectedRoom.id,
    //         };
    //         console.log(messageData)
    //         sendMessage(messageData);
    //         setInputValue('');
    //     }
    // };

    const handleSendMessage = (e) => {
        if (e.keyCode === 13) {
            if (inputValue.trim() !== '' && selectedFile) {
                sendMessage({ type: '@text', content: inputValue, file: null, displayName: firstname + ' ' + lastname })
                sendMessage({ type: '@image', content: null, file: selectedFile.data, displayName: firstname + ' ' + lastname })
            } else if (inputValue.trim() !== '') {
                sendMessage({ type: '@text', content: inputValue, file: null, displayName: firstname + ' ' + lastname })
            } else if (selectedFile) {
                sendMessage({ type: '@image', content: null, file: selectedFile.data, displayName: firstname + ' ' + lastname })
            }
            setInputValue('');
            setSelectedFile(null)
        }
    };

    const handleSelectedFile = (e) => {
        if (e.target.files[0] && e.target.files[0].type.includes('image')) {
            setSelectedFile({
                type: '@image',
                data: URL.createObjectURL(e.target.files[0]),
            });
        }
        inputTextRef.current.focus()
        // if (e.target.files[0].type.includes('video')) {
        //     setSelectedFile({
        //         type: '@video',
        //         data: URL.createObjectURL(e.target.files[0]),
        //     });
        // }
    };

    const handleRemoveFile = () => {
        setSelectedFile(null)
    }

    return (
        <div className={cx('wrapper')}>
            <Tippy
                singleton={source}
                delay={[50, 0]}
                interactive
                render={(attrs, content) => (
                    <div className={cx('tippy-box', { light: theme === 'light' })} tabIndex="-1" {...attrs}>
                        {content}
                    </div>
                )}
            />
            <Tippy singleton={target} content="Đính kèm file">
                <div>
                    <label
                        className={cx('file-btn')}
                        for="upload-photo"
                        onClick={(e) => {
                            if (e.target !== e.currentTarget) {
                                e.currentTarget.click();
                            }
                        }}
                    >
                        <Icon
                            ref={inputFileRef}
                            onClick={(e) => {
                                e.preventDefault();
                            }}
                            width="20"
                            height="20"
                            noBackground
                            className={cx('imgIcon')}
                            icon={faImage}
                        />
                    </label>
                    <input
                        onChange={handleSelectedFile}
                        type="file"
                        name="file"
                        id="upload-photo"
                        className={cx('input-file')}
                    />
                </div>
            </Tippy>
            <Tippy
                singleton={target}
                content={() => {
                    return <span>Chọn nhãn dán</span>;
                }}
            >
                <Icon
                    noBackground
                    className={cx('stickerIcon')}
                    IconAccess={StickersIcon}
                    width="30"
                    // height="20"
                    color="#31a24c"
                />
            </Tippy>
            <Tippy
                content={() => {
                    return <span>Chọn file gif</span>;
                }}
                singleton={target}
            >
                <Icon noBackground className={cx('gifIcon')} IconAccess={Gif} height="30" width="30" color="#31a24c" />
            </Tippy>
            <div className={cx('inputMessage')}>
                {selectedFile && (
                    <div className={cx('file-wrapper', { light: theme === 'light' })}>
                        <div className={cx('file-show')}>
                            {selectedFile.type === '@image' && (
                                <img src={selectedFile.data} className={cx('file-image')} />
                            )}
                            {/* {selectedFile.type === '@video' && (
                                <video src={selectedFile.data} className={cx('file-video')}></video>
                            )} */}
                            <span onClick={handleRemoveFile} className={cx('file-close')}>
                                <FontAwesomeIcon className={cx('icon')} icon={faX} />
                            </span>
                        </div>
                    </div>
                )}
                <div className={cx('text-box')}>
                    <input
                        onKeyDown={(e) => {
                            handleSendMessage(e);
                        }}
                        value={inputValue}
                        onChange={(e) => {
                            setInputValue(e.target.value);
                        }}
                        className={cx('input', { light: theme === 'light', small: selectedFile })}
                        type="text"
                        placeholder="Aa"
                        ref={inputTextRef}
                    />
                    <Tippy singleton={target} noBackground placement="right-start" content="Chọn biểu tượng cảm xúc">
                        <Icon noBackground className={cx('smileIcon')} icon={faFaceSmile} />
                    </Tippy>
                </div>
            </div>
            <Tippy
                placement="right-start"
                singleton={target}
                content={inputValue !== '' ? 'Nhấn Enter để gửi' : 'Gửi lượt thích'}
            >
                <Icon noBackground className={cx('likeIcon')} icon={inputValue !== '' ? faPaperPlane : faThumbsUp} />
            </Tippy>
        </div>
    );
}

export default MessageForm;
