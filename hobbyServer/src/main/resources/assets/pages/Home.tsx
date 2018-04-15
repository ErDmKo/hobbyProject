import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Card, CardTitle, CardText } from 'material-ui/Card';
import { User } from '../components/User'
import SocketForm from '../components/SocketForm'
import { connect } from 'react-redux';
import { userActions } from '../actions/user.actions';

const HomePage = ({ userName }) => {
  return <Card className="container">
    <CardTitle 
      title="Android bot App"
      subtitle="Get images from connected Android device" />
    <CardText>
      <User userName={userName}></User>
    </CardText>
    {userName && <CardText>
       <SocketForm/>
    </CardText>}

  </Card>
}
const mapStateToProps = (state) => {
  return {
    userName: state.auth.userName
  };
}
export default connect(mapStateToProps)(HomePage);