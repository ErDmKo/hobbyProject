import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Card, CardTitle } from 'material-ui/Card';

export const HomePage = () => (
  <Card className="container">
    <CardTitle title="React Application" subtitle="This is the home page." />
  </Card>
);
