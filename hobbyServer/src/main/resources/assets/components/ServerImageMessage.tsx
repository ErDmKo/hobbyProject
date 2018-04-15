import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { Props } from './ServerTextMessage'

export const ServerImageMessage = ({ url }: Props) => {
    return url ? <div><img src={ url } /></div> : <div></div>
}