import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import React from 'react';
import ReactDOM from 'react-dom';
import createFragment from 'react-addons-create-fragment';

class ServerTextMessage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            text: '',
            url: ''
        }
    }
    componentDidMount(){
        this.props.onRefresh((message)=>{
            this.setState(JSON.parse(message || {}));
        });
    }
    render() {
        return (
            <div>{this.state.text}, {this.state.url}</div>
        )
    }
};

class ServerImageMessage extends ServerTextMessage {
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

class Base extends React.Component {
  constructor(props) {
      super(props);
      this.state = {
        clientMessage: '',
        serverMessage: ''
      };
      this.subscribe = this.subscribe.bind(this);
      this.sendMessage = this.sendMessage.bind(this);
      this.setText = this.setText.bind(this);
      this.refresh = this.refresh.bind(this);
      this.callBacks = [];
  }

  subscribe(){
    let socket = new SockJS('/wsIn');
    let stompClient = Stomp.over(socket);
    this.setState({stompClient: stompClient});
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

ReactDOM.render(
  <Base />,
  document.getElementById('bodyContanier')
);