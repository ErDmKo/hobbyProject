import { userConstants } from '../constants';

let user = JSON.parse(localStorage.getItem('user'));
const initialState = user ? {
    loggedIn: true,
    user,
    userName: ''
} : {};

export function auth(state = initialState, action) {
    switch (action.type) {
        case userConstants.INFO_REQUEST:
            return {
                ...state,
                callInfo: true,
            }
        case userConstants.INFO_FAILURE:
            return {
                ...state,
                callInfo: false,
                errors: action.error
            }
        case userConstants.INFO_SUCCESS:
            return {
                ...state,
                callInfo: false,
                userName: action.userName
            }
        case userConstants.LOGIN_REQUEST:
            return {
                loggingIn: true,
                user: action.user
            };
        case userConstants.LOGIN_SUCCESS:
            return {
                loggedIn: true,
                user: action.user
            };
        case userConstants.LOGIN_FAILURE:
            return {
                ...state,
                errors: action.error
            };
        case userConstants.LOGOUT:
            return {};
        default:
            return state
    }
}
