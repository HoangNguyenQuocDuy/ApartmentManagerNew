import { useState, useEffect, createContext } from "react";
import { useNavigate } from "react-router-dom";

import { auth } from "~/firebase/config";

const AuthContext = createContext();

function AuthProvider({ children }) {
  const [user, setUser] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    const unsubscribed = auth.onAuthStateChanged((user) => {
      // console.log(user);

      if (user) {
        const { displayName, email, uid, photoURL } = user;
        setUser({ displayName, email, uid, photoURL });
        navigate("/chat-app");
      } else {
        navigate("/login");
      }
    });

    return () => {
      unsubscribed();
    };
  }, []);

  return <AuthContext.Provider value={user}>{children}</AuthContext.Provider>;
}

export { AuthProvider, AuthContext };
