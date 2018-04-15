import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { UserForm } from '../components/UserForm';
import { connect } from 'react-redux';
import { userActions } from '../actions/user.actions';

export interface Props {
  dispatch: Function,
  errors: Array<FieldError>
  success: boolean
}
export interface FieldError {
  field: string,
  message: string
};

export interface State {
  auth: boolean,
  errors: Array<FieldError>,
  user: {
    name: string,
    password: string
  }
}
export class SignUpPage extends React.Component<Props, State> {

  /**
   * Class constructor.
   */
  constructor(props) {
    super(props);

    // set the initial component state
    this.state = {
      auth: false,
      errors: [],
      user: {
        name: '',
        password: ''
      }
    };

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
   * Process the form.
   *
   * @param {object} event - the JavaScript event object
   */
  processForm = (event) => {
    event.preventDefault();
    const { dispatch } = this.props;
    const { name, password } = this.state.user;
    if (name && password) {
      dispatch(userActions.register({
        username: name,
        password
      }));
    }
  }

  /**
  * Render the component.
   */
  render() {
    return (
      <UserForm
        title={'SignUp'}
        onSubmit={this.processForm}
        onChange={this.changeUser}
        errors={this.state.errors.concat(this.props.errors)}
        user={this.state.user}
        success={this.props.success}
      />
    );
  }

}
export const mapStateToProps = (state) => {
  return {
    errors: state.auth.errors && state.auth.errors.fieldErrors || [],
    success: state.auth.loggedIn
  };
}

export default connect(mapStateToProps)(SignUpPage);
