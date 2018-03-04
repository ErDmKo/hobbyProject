import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Link } from 'react-router-dom';
import { Card, CardText, CardTitle, CardActions } from 'material-ui/Card';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import { FieldError } from '../pages/SignUp';
import { Redirect } from 'react-router';

export const UserForm = ({
      title,
      onSubmit,
      onChange,
      errors,
      user,
      success
}: {
  title: string
  onSubmit: any,
  onChange: any,
  errors: FieldError[],
  user: any,
  success: boolean
}) => {
    const getErrors = (field) => errors
      .filter(e => e.field == field)
      .map(e => e.message);
    const authErrors = getErrors('Auth');
    return success ? <Redirect push to="/"/> :
    <Card className="container">
        <form action="/" onSubmit={onSubmit}>
          <CardTitle title ={title}/>

          <CardText>
            {authErrors.length && <p className="error-message">{
                authErrors.map((message, i) => <span key={i}>{message}</span>)
            }</p> || ''}
            <div>
              <TextField
                floatingLabelText="Name"
                errorText={getErrors('username').join(", ")}
                name="name"
                onChange={onChange}
                value={user.name}
              />
            </div>
            <div>
              <TextField
                floatingLabelText="Password"
                errorText={getErrors('password').join(", ")}
                onChange={onChange}
                type="password"
                name="password"
                value={user.password}
              />
            </div>
          </CardText>

          <CardActions>
            <RaisedButton type="submit" label={title} primary />
          </CardActions>

          {title.toLowerCase() != "login" ? <CardText>Already have an account? <Link to={'/login'}>Log in</Link></CardText> : '' }
        </form>
      </Card>
};
