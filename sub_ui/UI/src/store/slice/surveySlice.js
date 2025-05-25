import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import newRequest from "../../untils/request";

const initialState = []

export const fetchSurvey = createAsyncThunk(
    'payments/fetchSurvey',
    async ({ page, userId, accessToken }, { rejectWithValue }) => {
        console.log(page, userId, accessToken)
        try {
            const response = await newRequest.get(`/surveyoptions/?page=${page}&userId=${userId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });
            return response.data;
        } catch (error) {
            return rejectWithValue(new Error(error.response.data));
        }
    }
)

export const surveySlice = createSlice({
    name: 'surveys',
    initialState,
    reducers: {
    },
    extraReducers: (builder) => {
        builder.addCase(fetchSurvey.fulfilled, (state, action) => {
            return action.payload.reduce((acc, current) => {
                const surveyId = current.questionId.surveyId.id;
                const questionId = current.questionId.id;

                // Find or create the survey
                let survey = acc.find(s => s.survey.id === surveyId);
                if (!survey) {
                    survey = {
                        survey: current.questionId.surveyId,
                        questions: []
                    };
                    acc.push(survey);
                }

                // Find or create the question within the survey
                let question = survey.questions.find(q => q.id === questionId);
                if (!question) {
                    question = {
                        ...current.questionId,
                        options: []
                    };
                    survey.questions.push(question);
                }

                // Add the option to the question
                question.options.push({
                    id: current.id,
                    optionText: current.optionText
                });

                return acc;
            }, [])
        })
    }
})

// export const {  } = paymentSlice.actions

export default surveySlice.reducer