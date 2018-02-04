function handleResponse(response) {
    if (!response.ok) { 
        return Promise.reject(response.body);
    }

    return response.json();
}
export const userService = {
	login(username, password): Promise<Object> {
		const requestOptions = {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ username, password })
		};

		return fetch('/users/authenticate', requestOptions)
			.then(response => {
				if (!response.ok) { 
					return new Promise((resole, reject) => {
						response.json().then(json => {
							reject(json);
						});
					})
				}
				return response.json();
			})
			.then(user => {
				if (user && user.token) {
					localStorage.setItem('user', JSON.stringify(user));
				}
				return user;
			});
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

        return fetch('/users/register', requestOptions).then(handleResponse);
    }
}
