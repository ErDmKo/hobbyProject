import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs/lib/stomp';

interface TokenInfo {
	access_token: string
	expires_in: number
	refresh_token: string
	token_type: string
}
function apiFetch(...args) {
	let [url, options] = args;
	options.headers = {
		...options.headers,
		...getXSRF()
	}
	return fetch.apply(null, args);
}
function getXSRF() {
    let spliter = document.cookie.split('XSRF-TOKEN=');
    if (spliter.length > 0) {
        return {
			['X-XSRF-TOKEN']: spliter[1].split(';')[0]
		}
	}
	return {}
}
function handleToken(response) {
	let out: {[key: string]: string} = {};
	if (!response.ok) {
		return new Promise((resole, reject) => {
			response.json().then(json => {
				reject(json);
			});
		})
	} else {
		out = response
			.json()
			.then((tokenInfo: TokenInfo) => {
				if (tokenInfo && tokenInfo.access_token) {
					localStorage.setItem('user', JSON.stringify(tokenInfo));
				}
			});
	}
	return out;
}
export const socketService = {
	setClient() {
		let socket = new SockJS('/wsIn');
		this.stompClient = Stomp.Stomp.over(socket);
	},
	send(...args) {
		this.stompClient.send.apply(this.stompClient, args);
	},
	getClient() {
		return this.stompClient;
	},
	connect(cb: Function) {
		this.stompClient.connect(getXSRF(), cb);
	},
	subscribe(...args) {
		this.stompClient.subscribe.apply(this.stompClient, args);
	}
}
export const userService = {
	info(): Promise<string> {
		const rawToken = localStorage.getItem('user')
		let token: TokenInfo;
		if  (rawToken) {
			try {
				token = JSON.parse(rawToken);
			} catch (e) {
			}
		}
		if (!token) {
			return Promise.reject('Empty or wrong token');
		}
		return apiFetch('/users/info', {
			credentials: 'same-origin',
			headers: {
				'Content-Type': 'application/json',
				'Authorization': `Bearer ${token.access_token}`
			},
		})
		.then(response => {
			if (response.ok) {
				return response.text();
			} else {
				return response.json()
					.then(json => Promise.reject(json));
			}
		});
	},
	login(username, password): Promise<Object> {
		const requestOptions = {
			credentials: 'same-origin' as RequestCredentials,
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ username, password })
		};

		return apiFetch('/users/authenticate', requestOptions)
			.then(handleToken)
			.then();
	},
    logout() {
        localStorage.removeItem('user');
    },
    register(user) {
        const requestOptions = {
				credentials: 'same-origin' as RequestCredentials,
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(user)
            };

		return apiFetch('/users/register', requestOptions)
			.then(handleToken);
    }
}
