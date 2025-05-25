import { jwtDecode } from 'jwt-decode';
import routes from "../../config/routes";
import { Navigate } from "react-router-dom";

function PrivateRoute({ children, role }) {
    const isAuthenticated = localStorage.getItem('accessToken')
    let isTokenExpired = false;

    if (isAuthenticated) {
        try {
            const decodedToken = jwtDecode(isAuthenticated);
            const currentTime = Date.now() / 1000; 
            if (decodedToken.exp < currentTime) {
                isTokenExpired = true;
            }
        } catch (error) {
            console.error('Invalid token:', error);
            isTokenExpired = true;
        }
    }

    if (!isAuthenticated || isTokenExpired) {
        return <Navigate to={routes.login} />;
    }

    return children;
}

export default PrivateRoute;