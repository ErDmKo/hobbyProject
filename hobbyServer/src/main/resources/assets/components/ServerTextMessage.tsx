import * as React from 'react';
import * as ReactDOM from 'react-dom';

export interface Props {
    url?: string,
    text?: string
}

export const ServerTextMessage = ({
    url,
    text
}: Props) => {
    let out: JSX.Element = <span>No server messages</span>
    if (text || url) {
        out = (
            <div>
                {text ? <div>Text "{text}"</div> : ''}
                {url ? <div>Url "{url}"</div> : ''}
            </div>
        )
    }
    return out;
};