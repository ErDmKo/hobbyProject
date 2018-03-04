import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Card, CardTitle, CardText } from 'material-ui/Card';
import { User } from '../components/User'
import { connect } from 'react-redux';
import { userActions } from '../actions/user.actions';

const HomePage = ({ userName }) => {
  return <Card className="container">
    <CardTitle title="React Application" subtitle="This is the home page." />
    <CardText>
      <User userName={userName}></User>
    </CardText>
  </Card>
}
const mapStateToProps = (state) => {
  return {
    userName: state.auth.userName
  };
}
export default connect(mapStateToProps)(HomePage);