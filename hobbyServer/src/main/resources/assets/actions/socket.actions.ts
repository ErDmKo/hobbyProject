import { userConstants } from '../constants';
import { socketService } from '../services';

const send = (message) => {
    if (socketService.getClient()) {
        socketService.send(
            "/app/wsIn",
            {},
            JSON.stringify({ 'text': message })
        );
        return {
            type: userConstants.SOCKET_CONNECT_CLIENT_MESSAGE,
            body: message
        }
    } else {
        return {
            type: userConstants.SOCKET_CONNECT_CLIENT_ERROR,
            text: 'Client doesn\'t exists'
        }
    }
}
const subscribe = () => {
    return (dispach: Function) => {
        dispach({
            type: userConstants.SOCKET_CONNECT_START
        })
        socketService.setClient()
        socketService.connect((frame) => {
            dispach({
                type: userConstants.SOCKET_CONNECT_SUCCESS
            })
            socketService.subscribe('/wsOut', (serverMessage) => {
                dispach({
                    type: userConstants.SOCKET_CONNECT_SERVER_MESSAGE,
                    body: JSON.parse(serverMessage.body)
                })
            });
        });
    }
}
export const socketActions = {
    subscribe,
    send
}