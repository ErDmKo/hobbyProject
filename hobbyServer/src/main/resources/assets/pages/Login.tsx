import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { UserForm } from '../components/UserForm';
import { Props, State } from './SignUp'
import { connect } from 'react-redux';
import { userActions } from '../actions/user.actions';
export class LoginPage extends React.Component<Props, State> {

  /**
   * Class constructor.
   */
  constructor(props) {
    super(props);

    // set the initial component state
    this.state = {
      errors: {},
      user: {
        name: '',
        password: ''
      }
    };
  }

  /**
   * Process the form.
   *
   * @param {object} event - the JavaScript event object
   */
    processForm = (event) => {
        event.preventDefault();
        const { dispatch } = this.props;
        const { name, password } = this.state.user;
        if (name && password) {
            dispatch(userActions.login(name, password));
        }
        console.log('password:', this.state.user.password);
    }

  /**
   * Change the user object.
   *
   * @param {object} event - the JavaScript event object
   */
  changeUser = (event) => {
    const field = event.target.name;
    const user = this.state.user;
    user[field] = event.target.value;

    this.setState({
      user
    });
  }

  /**
   * Render the component.
   */
  render() {
    return (
      <UserForm
        title={"Login"}
        onSubmit={this.processForm}
        onChange={this.changeUser}
        errors={this.state.errors}
        user={this.state.user}
      />
    );
  }

}
const mapStateToProps = (state) => {
    const { loggingIn } = state.auth;
    return { loggingIn };
}
export default connect(mapStateToProps)(LoginPage);
