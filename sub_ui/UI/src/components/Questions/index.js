import classNames from 'classnames/bind'

import styles from './questions.module.scss'
import Question from '../Question';

const cx = classNames.bind(styles)

function Questions({ questions }) {
    return (
        <div className={cx('questions')}>
            {questions.map((q) => (
                <Question key={q.id} question={q} />
            ))}
        </div>
    );
}

export default Questions;