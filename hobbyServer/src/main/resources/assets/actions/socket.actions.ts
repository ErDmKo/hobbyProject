import { userConstants } from '../constants';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs/lib/stomp';
import { exists } from 'fs';

let service: {
    stompClient?: any
} = {

}

const send = (message) => {
    if (service.stompClient) {
        service.stompClient.send(
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
    let socket = new SockJS('/wsIn');
    let stompClient= Stomp.Stomp.over(socket);
    const headers = {};
    let spliter = document.cookie.split('XSRF-TOKEN=');
    if (spliter.length > 0) {
        headers['X-XSRF-TOKEN'] = spliter[1].split(';')[0]
    }
    return (dispach: Function) => {
        dispach({
            type: userConstants.SOCKET_CONNECT_START
        })
        service.stompClient = stompClient;
        stompClient.connect(headers, (frame) => {
            dispach({
                type: userConstants.SOCKET_CONNECT_SUCCESS
            })
            stompClient.subscribe('/wsOut', (serverMessage) => {
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