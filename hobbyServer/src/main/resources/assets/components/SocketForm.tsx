import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs/lib/stomp';
import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { ServerTextMessage } from './ServerTextMessage';
import { ServerImageMessage } from './ServerImageMessage';

interface Props {
}
interface State {
    clientMessage: string,
    serverMessage: string,
    stompClient?: any
};

export class SocketForm extends React.Component<Props, State> {
  callBacks: Function[];
  constructor(props) {
      super(props);
      this.state = {
        clientMessage: '',
        serverMessage: ''
      };
      this.sendMessage = this.sendMessage.bind(this);
      this.setText = this.setText.bind(this);
      this.refresh = this.refresh.bind(this);
      this.callBacks = [];
  }

  subscribe = () => {
    let socket = new SockJS('/wsIn');
    let stompClient= Stomp.Stomp.over(socket);
    this.setState({ stompClient });
    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/wsOut', (serverMessage) => {
            this.callBacks.forEach((fn)=>{
                fn(serverMessage.body);
            });
        });
    });
  }

  sendMessage() {
    if (this.state.stompClient) {
        this.state.stompClient.send(
            "/app/wsIn",
            {},
            JSON.stringify({ 'text': this.state.clientMessage })
        );
    }
  }
  refresh(fn) {
    this.callBacks.push(fn);
  }
  setText(e){
    this.setState({clientMessage: e.target.value});
  }
  render() {
        return (
            <div>
                <div><ServerTextMessage message={this.state.serverMessage} onRefresh={this.refresh}/></div>
                <button onClick={this.subscribe}>subscribe</button>
                <input type="text" value={this.state.clientMessage} onChange={this.setText} />
                <button onClick={this.sendMessage}>Send message</button>
                <ServerImageMessage message={this.state.serverMessage} onRefresh={this.refresh}/>
            </div>
        )

  }
}