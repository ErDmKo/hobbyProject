interface TokenInfo {
	access_token: string
	expires_in: number
	refresh_token: string
	token_type: string
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
		return fetch('/users/info', {
			headers: {
				'Content-Type': 'application/json',
				'Authorization': `Bearer ${token.access_token}`
			},
		})
			.then(response => {
				return response.json();
			});
	},
	login(username, password): Promise<Object> {
		const requestOptions = {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ username, password })
		};

		return fetch('/users/authenticate', requestOptions)
			.then(handleToken)
			.then();
	},
    logout() {
        localStorage.removeItem('user');
    },
    register(user) {
        const requestOptions = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(user)
            };

		return fetch('/users/register', requestOptions)
			.then(handleToken);
    }
}
