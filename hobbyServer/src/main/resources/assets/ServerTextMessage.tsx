import * as React from 'react';
import * as ReactDOM from 'react-dom';

export interface State {
    url: string,
    text: string
    [key:string]: string
}
export interface Props {
    onRefresh: Function,
    message: string
}

export class ServerTextMessage<T, E> extends React.Component<Props, State> {
    constructor(props) {
        super(props);
        this.state = {
            text: 'Init state',
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