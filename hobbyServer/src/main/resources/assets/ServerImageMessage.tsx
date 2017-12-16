import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { ServerTextMessage, Props, State } from './ServerTextMessage'

export class ServerImageMessage extends ServerTextMessage<Props, State> {
    render() {
        if (this.state.url) {
            return (
                <div><img src={this.state.url}/></div>
            )
        } else {
            return (
                <div></div>
            )
        }
    }
}