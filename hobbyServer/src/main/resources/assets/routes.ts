import HomePage from './pages/Home';
import SignUpPage from './pages/SignUp';
import LoginPage from './pages/Login';

export const routes = [{
        path: '/',
        component: HomePage,
        isPrivate: true
    }, {
        path: '/signup',
        component: SignUpPage,
        isPrivate: false
    }, {
        path: '/login',
        component: LoginPage,
        isPrivate: false
    }
] 
