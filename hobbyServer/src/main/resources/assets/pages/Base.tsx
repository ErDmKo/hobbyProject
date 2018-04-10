import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Link } from 'react-router-dom';
import { FlatButton } from 'material-ui';
import { connect } from 'react-redux';
import { userActions } from '../actions/user.actions';
import { routes } from '../routes';

class Base extends React.Component<{dispatch: Function, user: {loggedIn: boolean}}, {}>  {
    constructor(props) {
        super(props);
    }
    componentDidMount() {
        const { dispatch } = this.props;
        dispatch(userActions.info());
    }
    render() {
        const isAnon = this.props.user.loggedIn;
        return <div>
                {routes
                    .filter(r => r.forAll || (isAnon ? !r.showAnon : r.showAnon))
                    .map(r => {
                    return <FlatButton
                        key={r.path}
                        containerElement={<Link to={r.path} />}
                        label={r.menuLabel} />
                })}
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