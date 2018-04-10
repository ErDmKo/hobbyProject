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
    const fields = [{
      label: 'Name',
      apiField: 'username',
      name: 'name',
      type: 'text'
    }, {
      label: 'Password',
      apiField: 'password',
      name: 'password',
      type: 'password'
    }]
    const getErrors = (field) => errors
      .filter(e => e.field == field)
      .map(e => e.message);
    const fieldErrors = fields.map(f => f.apiField);
    const authErrors = errors
      .filter(f => !fieldErrors.includes(f.field))
      .map(e => e.message)
    return success ? <Redirect push to="/"/> :
    <Card className="container">
        <form action="/" onSubmit={onSubmit}>
          <CardTitle title ={title}/>

          <CardText>
            {authErrors.length && <p className="error-message">{
                authErrors.map((message, i) => <span key={i}>{message}</span>)
            }</p> || ''}
            {fields.map((f, i) =>
            <div key={i}>
              <TextField
                floatingLabelText={f.name}
                errorText={getErrors(f.apiField).join(", ")}
                name={f.name}
                type={f.type}
                onChange={onChange}
                value={user[f.name]}
              />
            </div>
            )}
          </CardText>

          <CardActions>
            <RaisedButton type="submit" label={title} primary />
          </CardActions>

          {title.toLowerCase() != "login" ? <CardText>Already have an account? <Link to={'/login'}>Log in</Link></CardText> : '' }
        </form>
      </Card>
};
