import * as React from 'react';
import * as ReactDOM from 'react-dom';
import {
      BrowserRouter as Router,
      Route, Switch,
      Link
} from 'react-router-dom'

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import { routes } from './routes';
import Base from './pages/Base';
import { store } from './utils/store';
import { Provider } from 'react-redux';

ReactDOM.render(
  <Provider store={store}>
    <MuiThemeProvider>
      <Router>
          <Switch>
          {routes.map((route, i) => 
              <Route
                  key = {i}
                  exact
                  path = {route.path}
                  render = {props => (
                      <Base>
                          <route.component />
                      </Base>
                  )}
              />
          )}
          </Switch>
      </Router>
    </MuiThemeProvider>
  </Provider>,
  document.getElementById('bodyContanier')
);
