import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Link } from 'react-router-dom';

const Base = ({ children }) => (
    <div>
        <div className="top-bar">
            <div className="top-bar-left">
                <Link to="/">React App</Link>
            </div>

            <div className="top-bar-right">
                <Link to="/login">Log in</Link>
                <Link to="/signup">Sign up</Link>
            </div>

        </div>

        {children}

    </div>
);
export default Base;
