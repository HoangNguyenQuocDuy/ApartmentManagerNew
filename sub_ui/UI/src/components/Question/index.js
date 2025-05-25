import classnames from "classnames/bind";

import styles from './question.module.scss'
import { FaRegCircle } from "react-icons/fa";
import { PiRectangleBold } from "react-icons/pi";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setMultipelChoice, setSingleChoise } from "../../store/slice/questionSubmitSlice";
import { useParams } from "react-router-dom";

const cx = classnames.bind(styles)

function Question({ question }) {
    const dispatch = useDispatch()
    const [selectedRadioOption, setSelectedRadioOption] = useState()
    const [selectedMultipleOptions, setSelectedMultipleOptions] = useState([])
    const user = useSelector(state => state.user)
    const { isSubmitServey } = useSelector(state => state.app)
    const { surveyId } = useParams()
    const type = question.questionType

    const handleClickRadioOption = (option) => {
        setSelectedRadioOption({ questionId: question.id, optionId: option.id, userId: user.id, surveyId });
    };

    const handleClickMultipleOption = (option) => {
        setSelectedMultipleOptions(prevSelectedOptions => {
            if (prevSelectedOptions.find(p => p.optionId === option.id)) {
                return prevSelectedOptions.filter(optionIndex => optionIndex.optionId !== option.id);
            } else {
                return [...prevSelectedOptions, { questionId: question.id, optionId: option.id, userId: user.id, surveyId, type }];
            }
        });
    };

    useEffect(() => {
        if (selectedMultipleOptions.length > 0) {
            console.log('selectedMultipleOptions: ', selectedMultipleOptions)
            dispatch(setMultipelChoice(selectedMultipleOptions))
        }
    }, [selectedMultipleOptions])

    useEffect(() => {
        if (selectedRadioOption) {
            console.log('selectedRadioOption: ', selectedRadioOption)
            dispatch(setSingleChoise(selectedRadioOption))
        }
    }, [selectedRadioOption])

    return (
        <div className={cx('container')}>
            {
                question &&
                <div className={cx('wrapper')}>
                    <div className={cx('title')}>{question.questionText}</div>
                    <div className={cx('options')}>
                        {
                            question.options.map(o => (
                                <div key={o.id} className={cx('option')}>
                                    <div onClick={() => {
                                        if (type === 'Single choice' || type === "Rating") {
                                            handleClickRadioOption(o)
                                        } else {
                                            handleClickMultipleOption(o)
                                        }
                                    }} className={cx('icon', {
                                        'active': selectedRadioOption && selectedRadioOption.optionId === o.id
                                            || selectedMultipleOptions.find(s => s.optionId === o.id)
                                    })}>
                                        {type === 'Single choice' || type === 'Rating' ?
                                            <FaRegCircle size={24} /> :
                                            <PiRectangleBold size={30}
                                            />}
                                    </div>
                                    <div className={cx('optionText')}>{o.optionText}</div>
                                </div>
                            ))
                        }
                    </div>
                </div>
            }
        </div>
    );
}

export default Question;