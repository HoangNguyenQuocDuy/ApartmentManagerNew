import classnames from 'classnames/bind'

import styles from './feedback.module.scss'
import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useRef, useState } from 'react';
import { AiOutlinePlusCircle } from 'react-icons/ai';
import newRequest from '../../untils/request'
import moment from 'moment';
import { MdOutlineDeleteOutline } from "react-icons/md";
import { notify } from '../../untils/notification';
import { Pagination } from 'flowbite-react';
import { setIsActiveNavbar } from '../../store/slice/applicationSlice';
const cx = classnames.bind(styles)

function Feedbacks() {
    const dispatch = useDispatch()
    const titleRef = useRef()
    const [isShowAddBox, setIsShowAddBox] = useState(false)
    const [title, setTitle] = useState('')
    const [content, setContent] = useState('')
    const accessToken = localStorage.getItem("accessToken")
    const [feedbacks, setFeedbacks] = useState()
    const user = useSelector(state => state.user)
    const [currentPage, setCurrentPage] = useState(1);
    const onPageChange = page => {
        console.log(page)
        setCurrentPage(page)
    }
    const { pageSize } = useSelector(state => state.app)

    useEffect(() => {
        handleGetFeedbacks()
        dispatch(setIsActiveNavbar(true))
    }, [])

    useEffect(() => {
        if (!isShowAddBox) {
            setTitle('')
            setContent('')
        } else {
            const timeout = () => {
                setTimeout(() => {
                    titleRef.current.focus()
                }, 210)
            }
            timeout()

            return () => {
                clearTimeout(timeout)
            }
        }
    }, [isShowAddBox])

    useEffect(() => {
        handleGetFeedbacks()
    }, [currentPage])


    const handleAddFB = async (e) => {
        e.preventDefault()
        if (title.trim() === '') {
            alert('Error when create feedback: Title is required!')
        } else if (content.trim() === '') {
            alert('Error when create feedback: Content is required!')
        } else {
            await newRequest.post(`/feedbacks/`, {
                userId: user.id,
                title, content
            }, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
                .then(data => {
                    setIsShowAddBox(false)
                    handleGetFeedbacks()
                    notify('Feedback has been created!', 'success')
                })
                .catch(err => {
                    notify('Error when create feedback: ' + err, 'error')
                })
        }
    }

    const handleGetFeedbacks = () => {
        newRequest.get(`/feedbacks/?userId=${user.id}&page=${currentPage - 1}&size=${pageSize}`, {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        })
            .then((data) => {
                console.log('data.data: ', data.data.data)
                setFeedbacks(data.data.data);
            })
            .catch(err => {
                console.log('error get feedbacks: ', err)
                notify('Error when get feedbacks: ' + err, 'success')
            })
    }

    const handleDeleteFeedback = async (id) => {
        if (window.confirm('Do you want to delete this feedback?')) {
            await newRequest.delete(`/feedbacks/${id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
                .then((data) => {
                    notify('Feedback has been deleted!', 'success')
                    handleGetFeedbacks()
                    if (isShowAddBox) {
                        setIsShowAddBox(false)
                    }
                })
                .catch(err => {
                    notify(`Error when deleting feedback with id=${id}: ` + err, 'error')
                })
                .finally(() => {
                })
        }
    }

    return (
        <div className={cx('container')}>
            <div className={cx('btnBox', 'addNewBox')}>
                <button onClick={() => { setIsShowAddBox(!isShowAddBox) }}>Add new
                    <AiOutlinePlusCircle size={20} />
                </button>
            </div>

            <form onSubmit={(e) => { handleAddFB(e) }} className={cx('addNewFeedbackBox', { active: isShowAddBox })}>
                <div className={cx('wrapper')}>
                    <label htmlFor='title'>Title</label>
                    <div className={cx('title')}>
                        <input ref={titleRef} onChange={(e) => { setTitle(e.target.value) }} value={title} id='title' placeholder='Title' />
                    </div>
                    <label htmlFor='content'>Content</label>
                    <div className={cx('content')}>
                        <textarea onChange={(e) => { setContent(e.target.value) }} value={content} id='content' placeholder='Content' />
                    </div>
                    <div className={cx('btnBox', 'addBtn')}>
                        <button type='submit' >Add feedback</button>
                    </div>
                </div>
            </form>

            <div className={cx('wrapper')}>
                <table className={cx('table')} cellSpacing="0" cellPadding="0">
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Content</th>
                            <th>Created At</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            feedbacks && feedbacks.content.map(f => (
                                <tr key={f.id}>
                                    <td>{f.title}</td>
                                    <td>{f.content}</td>
                                    <td>{moment(f.createdAt).format("HH:mm:ss-DD/MM/YYYY")}</td>
                                    <td>
                                        <div className={cx('actionIconBox')}>
                                            <div onClick={() => { handleDeleteFeedback(f.id) }} className={cx('trashIcon')}><MdOutlineDeleteOutline size={24} /></div>
                                        </div>
                                    </td>
                                </tr>
                            ))
                            // : <div className={cx('loadingBox')}>
                            //     <ReactLoading type='spin' color={'#999'} height={'20%'} width={'20%'} />
                            // </div>
                        }
                    </tbody>
                </table>

            </div>
            <div className="flex sm:justify-center mb-20 absolute w-full">
                <Pagination currentPage={currentPage} totalPages={feedbacks ? feedbacks.totalPages : 0} onPageChange={onPageChange} showIcons />
            </div>
        </div>
    );
}

export default Feedbacks;