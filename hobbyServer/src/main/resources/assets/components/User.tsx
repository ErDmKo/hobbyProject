import * as React from 'react';
import * as ReactDOM from 'react-dom';

export const User = ({userName}) => {
    return <div>User name is {userName ? userName : 'Anonimus'}</div>
}