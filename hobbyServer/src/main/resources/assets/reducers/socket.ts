import { userConstants } from '../constants';

const initialState = {
    error: false,
    clientMessage: '',
    serverMessage: {},
    connected: false
};

export const socket = (state = initialState, action) => {
    switch (action.type) {
        case userConstants.SOCKET_CONNECT_CLIENT_ERROR:
            return {
                ...state,
                error: action.text,
                connected: false
            }
        case userConstants.SOCKET_CONNECT_CLIENT_MESSAGE:
            return {
                ...state,
                clientMessage: action.body,
            }
        case userConstants.SOCKET_CONNECT_START:
            return {
                ...state
            }
        case userConstants.SOCKET_CONNECT_SUCCESS:
            return {
                ...state,
                connected: true
            }
        case userConstants.SOCKET_CONNECT_SERVER_MESSAGE:
            return {
                ...state,
                serverMessage: action.body,
            }
        default:
            return initialState
    }
};