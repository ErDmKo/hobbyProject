import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Card, CardTitle } from 'material-ui/Card';
import { User } from '../components/User'
import { connect } from 'react-redux';
import { userActions } from '../actions/user.actions';

interface Props {
  dispatch: Function,
  userName: string
}

interface State {
  userName: string
}

class HomePage extends React.Component<Props, State>  {
  constructor(props) {
    super(props);

    // set the initial component state
    this.state = {
      userName: props.userName 
    };
  }
  componentDidMount() {
    const { dispatch } = this.props;
    dispatch(userActions.info());
  }
  render() {
    return <Card className="container">
      <CardTitle title="React Application" subtitle="This is the home page." />
      <User userName={this.state.userName}></User>
    </Card>
  }
};
const mapStateToProps = (state) => {
  return {
    errors: state.auth.errors && state.auth.errors.fieldErrors || [],
    userName: state.auth.userName
  };
}
export default connect(mapStateToProps)(HomePage);