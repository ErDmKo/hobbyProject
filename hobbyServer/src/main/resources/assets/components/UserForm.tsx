import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Link } from 'react-router-dom';
import { Card, CardText } from 'material-ui/Card';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';


export const UserForm = ({
      title,
      onSubmit,
      onChange,
      errors,
      user,
}) => (
      <Card className="container">
        <form action="/" onSubmit={onSubmit}>
          <h2 className="card-heading">{title}</h2>

          {errors.summary && <p className="error-message">{errors.summary}</p>}

          <div className="field-line">
            <TextField
              floatingLabelText="Name"
              name="name"
              errorText={errors.name}
              onChange={onChange}
              value={user.name}
            />
          </div>

          <div className="field-line">
            <TextField
              floatingLabelText="Password"
              type="password"
              name="password"
              onChange={onChange}
              errorText={errors.password}
              value={user.password}
            />
          </div>

          <div className="button-line">
            <RaisedButton type="submit" label="Create New Account" primary />
          </div>

          <CardText>Already have an account? <Link to={'/login'}>Log in</Link></CardText>
        </form>
      </Card>
);
