import HomePage from './pages/Home';
import SignUpPage from './pages/SignUp';
import LoginPage from './pages/Login';
import LogoutPage from './pages/Logout';

export const routes = [{
        path: '/',
        menuLabel: 'Home',
        component: HomePage,
        showAnon: true,
        forAll: true
    }, {
        path: '/signup',
        menuLabel: 'Register',
        component: SignUpPage,
        showAnon: true,
        forAll: false
    }, {
        path: '/login',
        menuLabel: 'Login',
        component: LoginPage,
        showAnon: true,
        forAll: false
    }, {
        path: '/logout',
        menuLabel: 'Logout',
        component: LogoutPage,
        showAnon: false,
        forAll: false
    }
] 
