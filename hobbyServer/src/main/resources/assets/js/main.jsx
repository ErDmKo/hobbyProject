import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import React from 'react';
import ReactDOM from 'react-dom';

class Hello extends React.Component {
  constructor(props) {
      super(props);
      this.state = {name: 'Inital Name'};
      this.subscribe = this.subscribe.bind(this);
      this.sendMessage = this.sendMessage.bind(this);
      this.setText = this.setText.bind(this);
  }
  subscribe(){
    let socket = new SockJS('/wsIn');
    let stompClient = Stomp.over(socket);
    this.setState({stompClient: stompClient});
    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/wsOut', (greeting) => {
            this.setState({
              name: JSON.parse(greeting.body).text
            })
        });
    });
  }

  sendMessage() {
    if (this.state.stompClient) {
        this.state.stompClient.send(
            "/app/wsIn",
            {},
            JSON.stringify({ 'text': this.state.message })
        );
    }
  }
  setText(e){
    this.setState({message: e.target.value});
  }
  render() {
        return (
            <div>
                <div> Hello !, {this.state.name}!</div>
                <button onClick={this.subscribe}>Connect to socket</button>
                <input type="text" value={this.state.message} onChange={this.setText} />
                <button onClick={this.sendMessage}>Send message</button>
            </div>
        )

  }
}

ReactDOM.render(
  <Hello />,
  document.getElementById('bodyContanier')
);