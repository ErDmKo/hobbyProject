import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Link } from 'react-router-dom';
import { FlatButton } from 'material-ui';
import { connect } from 'react-redux';
import { userActions } from '../actions/user.actions';

class Base extends React.Component<{dispatch: Function, user: {loggedIn: boolean}}, {}>  {
    constructor(props) {
        super(props);
    }
    componentDidMount() {
        const { dispatch } = this.props;
        dispatch(userActions.info());
    }
    render() {
        return <div>
            <div className="top-bar">
                <div className="top-bar-right">
                    <FlatButton
                        containerElement={<Link to="/" />}
                        label="Home" />
                    {!this.props.user.loggedIn &&
                        <span><FlatButton
                            containerElement={<Link to="/login" />}
                            label="Log in" />
                            <FlatButton
                                containerElement={<Link to="/signup" />}
                                label="Sign up" />
                        </span> || <FlatButton
                            containerElement={<Link to="/logout" />}
                            label="logout" />}
                </div>
            </div>
            {this.props.children}
        </div>
    }
}
const mapStateToProps = (state) => {
    return {
        user: state.auth
    }
}
export default connect(mapStateToProps)(Base);