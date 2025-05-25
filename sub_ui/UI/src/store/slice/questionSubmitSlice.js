import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    singleChoise: [],
    multipleChoise: []
}

export const questionSubmitSlice = createSlice({
    name: 'questionsSubmit',
    initialState,
    reducers: {
        setSingleChoise: (state, action) => {
            if (state.singleChoise.length > 0) {
                const existingQuestionIndex = state.singleChoise.findIndex(q => q.questionId === action.payload.questionId);
                if (existingQuestionIndex !== -1) {
                    state.singleChoise.splice(existingQuestionIndex, 1);
                }
            }
            state.singleChoise.push(action.payload);
        },
        setMultipelChoice: (state, action) => {
            let updatedMultipleChoice = [...state.multipleChoise];
            if (updatedMultipleChoice.length > 0) {
                action.payload.forEach(payloadItem => {
                    updatedMultipleChoice = updatedMultipleChoice.filter(q =>
                        q.optionId !== payloadItem.optionId
                    );
                });

                // Trả về một state mới, kết hợp với multipleChoise mới
                console.log('updatedMultipleChoice: ', updatedMultipleChoice)

                return {
                    ...state,
                    multipleChoise: [...updatedMultipleChoice, ...action.payload]
                };
            }
            console.log('updatedMultipleChoice: ', action.payload)

            return {
                ...state,
                multipleChoise: action.payload
            }
            // Lặp qua từng phần tử trong mảng payload
        },
        clearQuestions: (state, action) => {
            return { singleChoise: [], multipleChoise: [] }
        }
    },
    extraReducers: (builder) => { }
})

export const { addQuestion, clearQuestions, setMultipelChoice, setSingleChoise } = questionSubmitSlice.actions

export default questionSubmitSlice.reducer