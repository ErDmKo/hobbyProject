import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Card, CardText, CardTitle, CardActions } from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import { connect } from 'react-redux';
import { socketActions } from '../actions/socket.actions';
import { ServerTextMessage } from './ServerTextMessage';
import { ServerImageMessage } from './ServerImageMessage';
import { RaisedButton } from 'material-ui';

interface Props {
    clientMessage: string,
    connected: boolean,
    serverMessage: {
        text?: string,
        url?: string
    },
    dispatch: Function
}
interface State {
    clientMessage: string,
};

class SocketForm extends React.Component<Props, State> {
    constructor(props) {
        super(props);
        this.state = {
            clientMessage: props.clientMessage,
        };
    }
    subscribe = () => {
        this.props.dispatch(socketActions.subscribe());
    }
    sendMessage = () => {
        this.props.dispatch(socketActions.send(this.state.clientMessage));
    }
    setText = (e) => {
        this.setState({ clientMessage: e.target.value });
    }
    render() {
        return (
            <Card>
                <CardTitle title ={'Listen server socket'}/>
                <CardText>
                    <ServerTextMessage
                        text={this.props.serverMessage.text}
                    />
                    <ServerImageMessage
                        url={this.props.serverMessage.url}
                    />
                </CardText>
                <CardActions>
                    <RaisedButton
                        onClick={this.subscribe}>
                        Subscribe
                    </RaisedButton>
                    <TextField
                        floatingLabelText={'Message to send'}
                        disabled={!this.props.connected}
                        type={'text'}
                        value={this.state.clientMessage}
                        onChange={this.setText}
                    />
                    <RaisedButton
                        onClick={this.sendMessage}>
                        Send
                    </RaisedButton>
                </CardActions>
            </Card>
        )

    }
}
export const mapStateToProps = (state) => {
    return {
        errors: state.socket.errors,
        connected: state.socket.connected,
        serverMessage: state.socket.serverMessage,
        clientMessage: state.socket.clientMessage || ''
    };
}

export default connect(mapStateToProps)(SocketForm);