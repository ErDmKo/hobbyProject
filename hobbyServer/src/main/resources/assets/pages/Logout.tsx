import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { userActions } from '../actions/user.actions';
import { connect } from 'react-redux';

class LogoutPage extends React.Component<{ dispatch: Function }, {}>  {
    constructor(props) {
        super(props);
    }
    componentDidMount() {
        const { dispatch } = this.props;
        dispatch(userActions.logout());
    }
    render() {
        return <div>Bye!</div>;
    }
}

export default connect()(LogoutPage);