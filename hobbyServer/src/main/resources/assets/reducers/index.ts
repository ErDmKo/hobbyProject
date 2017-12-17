import { combineReducers } from 'redux';
import { users } from './user';
import { auth } from './auth';
import { reg } from './reg';

export const rootReducer = combineReducers({
    users,
    auth
})
