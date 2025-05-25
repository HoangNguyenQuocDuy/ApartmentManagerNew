import classnames from 'classnames/bind'

import styles from './survey.module.scss'
import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
import { fetchSurvey } from '../../store/slice/surveySlice';
import Status from '../../components/Status';
import moment from 'moment';
import { useNavigate } from 'react-router-dom';
import routes from '../../config/routes';
import { setIsActiveNavbar } from '../../store/slice/applicationSlice';

const cx = classnames.bind(styles)

function Surveys() {

    const dispatch = useDispatch()
    const accessToken = localStorage.getItem("accessToken")
    const user = useSelector(state => state.user)
    const surveys = useSelector(state => state.surveys)
    const [page, setPage] = useState(1)
    const navigate = useNavigate()


    useEffect(() => {
        dispatch(setIsActiveNavbar(true))
        dispatch(fetchSurvey({ page, userId: user.id, accessToken }))
    }, [])

    console.log('surveys: ', surveys && surveys)

    const handleSearchSurveys = () => {
    }

    const handleOpenSurvey = (id) => {
        const surveyPath = routes.survey.replace(':surveyId', id);
        navigate(surveyPath);
    }

    return (
        <div className={cx('container')}>

            <div className={cx('wrapper')}>
                <table className={cx('table')} cellSpacing="0" cellPadding="0">
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Description</th>
                            <th>Status</th>
                            <th>Created At</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {surveys && surveys.map(s => (
                            <tr>
                                <td>{s.survey.title}</td>
                                <td>
                                    {s.survey.description && s.survey.description !== '' ?
                                        s.survey.description : '-----'}
                                </td>
                                <td className={cx('ct')}><Status status={s.survey.status} /></td>
                                <td>{moment(s.survey.createdAt).format("DD/MM/YYYY")}</td>
                                <td>
                                    <button onClick={() => { handleOpenSurvey(s.survey.id) }} className={cx('btn', { 'close': s.survey.status === 'Close' })}>
                                        Dispose
                                    </button>
                                </td>
                            </tr>
                        ))
                        }
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default Surveys;