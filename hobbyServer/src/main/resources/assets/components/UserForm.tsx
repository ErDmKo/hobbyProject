import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Link } from 'react-router-dom';
import { Card, CardText } from 'material-ui/Card';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import { FieldError } from '../pages/SignUp';


export const UserForm = ({
      title,
      onSubmit,
      onChange,
      errors,
      user,
}: {
  title: string
  onSubmit: any,
  onChange: any,
  errors: FieldError[],
  user: any
}) => {
    const getErrors = (field) => errors
      .filter(e => e.field == field)
      .map(e => e.message);
    return <Card className="container">
        <form action="/" onSubmit={onSubmit}>
          <h2 className="card-heading">{title}</h2>

          {errors.length ? <p className="error-message">{
              getErrors('Auth')
              .map((message, i) => <span key={i}>{message}</span>)
          }</p> : ""}

          <div className="field-line">
            <TextField
              floatingLabelText="Name"
              errorText={getErrors('username').join(", ")}
              name="name"
              onChange={onChange}
              value={user.name}
            />
          </div>

          <div className="field-line">
            <TextField
              floatingLabelText="Password"
              errorText={getErrors('password').join(", ")}
              onChange={onChange}
              type="password"
              name="password"
              value={user.password}
            />
          </div>

          <div className="button-line">
            <RaisedButton type="submit" label={title} primary />
          </div>

          {title.toLowerCase() != "login" ? <CardText>Already have an account? <Link to={'/login'}>Log in</Link></CardText> : '' }
        </form>
      </Card>
};
