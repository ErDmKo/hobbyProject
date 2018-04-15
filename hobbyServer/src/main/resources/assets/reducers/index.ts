import { combineReducers } from 'redux';
import { users } from './user';
import { auth } from './auth';
import { socket } from './socket';

export const rootReducer = combineReducers({
    users,
    auth,
    socket
})