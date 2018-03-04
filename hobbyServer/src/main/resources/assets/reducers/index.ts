import { combineReducers } from 'redux';
import { users } from './user';
import { auth } from './auth';

export const rootReducer = combineReducers({
    users,
    auth
})