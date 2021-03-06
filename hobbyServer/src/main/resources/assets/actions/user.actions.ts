import { userConstants } from '../constants';
import { userService } from '../services';
import { fullBlack } from 'material-ui/styles/colors';

const info = () => dispach => {
    const request = () => dispach({
        type: userConstants.INFO_REQUEST
    })
    const success = (userName) => dispach({
        type: userConstants.INFO_SUCCESS,
        userName
    })
    const fail = (error) => dispach({
        type: userConstants.INFO_FAILURE,
        error
    })
    dispach(request);
    userService
        .info()
        .then(success, fail)
}

const login = (username: string, password: string) => {
    const request = (user) => ({ 
        type: userConstants.LOGIN_REQUEST, 
        user 
    })
    const success = (user) => ({
        type: userConstants.LOGIN_SUCCESS,
        user
    })
    const failure = (error) => ({
        type: userConstants.LOGIN_FAILURE,
        error
    })

    return dispatch => {
        dispatch(request({ username }));

        userService.login(username, password)
            .then(
                user => { 
                    dispatch(success(user));
                },
                error => {
                    dispatch(failure(error));
                }
            );
    };

}
const logout = () => {
    userService.logout();
    return { type: userConstants.LOGOUT };
}
function register(user) {
    const request = (user) => ({
         type: userConstants.REGISTER_REQUEST, 
         user 
    })
    const success = (user) => ({
        type: userConstants.REGISTER_SUCCESS,
        user 
    })
    const failure = (error) => ({ 
        type: userConstants.REGISTER_FAILURE, 
        error 
    })

    return dispatch => {
        dispatch(request(user));

        userService.register(user)
            .then(
                user => { 
                    dispatch(success(user));
                },
                error => {
                    dispatch(failure(error));
                }
            );
    };
}

export const userActions = {
    login,
    logout,
    register,
    info
}