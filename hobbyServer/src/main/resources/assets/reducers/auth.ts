import { userConstants } from '../constants';

let token = JSON.parse(localStorage.getItem('user'));
const initialState = token ? {
    token,
} : {};

export function auth(state = initialState, action) {
    switch (action.type) {
        case userConstants.REGISTER_REQUEST:
            return {
                ...state
            }
        case userConstants.REGISTER_SUCCESS:
            return {
                loggedIn: true,
                token: action.token
            }
        case userConstants.REGISTER_FAILURE:
            return {
                loggedIn: false,
                errors: action.error
            }
        case userConstants.INFO_REQUEST:
            return {
                ...state
            }
        case userConstants.INFO_FAILURE:
            return {
                ...state,
                loggedIn: false ,
                errors: action.error
            }
        case userConstants.INFO_SUCCESS:
            return {
                ...state,
                loggedIn: true,
                userName: action.userName
            }
        case userConstants.LOGIN_REQUEST:
            return {
                ...state
            };
        case userConstants.LOGIN_SUCCESS:
            return {
                loggedIn: true,
                token: action.token
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
