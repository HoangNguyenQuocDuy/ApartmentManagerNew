import Sidebar from "./Sidebar";
import Header from "../../components/Admin/Header";

function Admin({ children }) {

    return (
        <div className='flex w-full h-full'>
            <Sidebar />
            <div className="relative flex flex-1 flex-col overflow-y-auto overflow-x-hidden dark:bg-boxdark-2 dark:text-bodydark h-screen transition">
                <Header />
                {children}
            </div>
        </div>
    );
}

export default Admin;