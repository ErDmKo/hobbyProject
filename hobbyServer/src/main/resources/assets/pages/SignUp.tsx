import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { UserForm } from '../components/UserForm';
import { connect } from 'react-redux';
import { userActions } from '../actions/user.actions';

export interface Props {
    dispatch: Function
}
export interface State {
    errors: Object,
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
      errors: {},
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
        errors={this.state.errors}
        user={this.state.user}
      />
    );
  }

}

export default connect()(SignUpPage);
