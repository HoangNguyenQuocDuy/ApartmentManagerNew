import './App.css';
import DefaultLayout from './Layout/DefaultLayout';
import Login from './pages/Login'
import routes from './config/routes';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Invoice from './pages/Invoice';
import Payment from './pages/Payment';
import Feedbacks from './pages/Feedbacks';
import Personal from './pages/Personal';
import Service from './pages/Service';
import 'react-toastify/dist/ReactToastify.css';
import Admin from './pages/Admin';
import './css/style.css';
import Dashboard from './pages/Admin/Dashboard';
import Rooms from './pages/Admin/Rooms';
import InvoiceAdmin from './pages/Admin/Invoices/invoices';
import Residents from './pages/Admin/Users';
import Visitors from './pages/Admin/Visitors';
import PaymentAdmin from './pages/Admin/payment';
import ParkingRights from './pages/Admin/Parking';
import EntryRights from './pages/Admin/EntryRight';
import FeedbacksAdmin from './pages/Admin/Feedback';
import OrderAdmin from './pages/Admin/OrderAdmin';
import PrivateRoute from './components/PrivateRoute';
import Contract from './pages/Admin/Contract';
import ChatRoom from './components/ChatRoom'
import VisitRequest from './components/VisitRequest/VisitRequest';
import NewUserLayout from './Layout/NewUserLayout/NewUserLayout';
import Contracts from './components/Contracts/Contracts';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />

        {/* Private routes */}
        <Route
          path={routes.chat}
          element={<PrivateRoute role=""><ChatRoom /></PrivateRoute>}
        />
        <Route
          path={routes.home}
          element={<PrivateRoute role="ROLE_RESIDENT"><NewUserLayout children={''} /></PrivateRoute>}
        />
        <Route
          path={routes.contracts}
          element={<PrivateRoute role="ROLE_RESIDENT"><NewUserLayout children={<Contracts />} /></PrivateRoute>}
        />
        {/* <Route
          path={routes.locker}
          element={<PrivateRoute role="ROLE_RESIDENT"><DefaultLayout children={<Locker />} /></PrivateRoute>}
        /> */}
        <Route
          path={routes.invoice}
          element={<PrivateRoute role="ROLE_RESIDENT"><NewUserLayout children={<Invoice />} /></PrivateRoute>}
        />
        <Route
          path={routes.visitOperator}
          element={<PrivateRoute role="ROLE_RESIDENT"><NewUserLayout children={<VisitRequest />} /></PrivateRoute>}
        />
        <Route
          path={routes.payment}
          element={<PrivateRoute role="ROLE_RESIDENT"><NewUserLayout children={<Payment />} /></PrivateRoute>}
        />
        <Route
          path={routes.feedback}
          element={<PrivateRoute role="ROLE_RESIDENT"><NewUserLayout children={<Feedbacks />} /></PrivateRoute>}
        />
        <Route
          path={routes.personal}
          element={<PrivateRoute role="ROLE_RESIDENT"><NewUserLayout children={<Personal />} /></PrivateRoute>}
        />
        <Route
          path={routes.services}
          element={<PrivateRoute role="ROLE_RESIDENT"><NewUserLayout children={<Service />} /></PrivateRoute>}
        />

        {/* ADMIN */}
        <Route
          path={routes.adminDashboard}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<Dashboard />} /></PrivateRoute>}
        />
        <Route
          path={routes.adminRooms}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<Rooms />} /></PrivateRoute>}
        />
        {/* <Route
          path={routes.adminLocker}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<LockerAdmin />} /></PrivateRoute>}
        /> */}
        <Route
          path={routes.adminContract}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<Contract />} /></PrivateRoute>}
        />
        <Route
          path={routes.adminUsers}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<Residents />} /></PrivateRoute>}
        />
        <Route
          path={routes.adminParkingRights}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<ParkingRights />} /></PrivateRoute>}
        />
        <Route
          path={routes.adminVisitor}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<Visitors />} /></PrivateRoute>}
        />
        <Route
          path={routes.adminPayments}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<PaymentAdmin />} /></PrivateRoute>}
        />
        <Route
          path={routes.adminEntryRights}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<EntryRights />} /></PrivateRoute>}
        />
        <Route
          path={routes.adminFeedbacks}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<FeedbacksAdmin />} /></PrivateRoute>}
        />
        <Route
          path={routes.adminOrder}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<OrderAdmin />} /></PrivateRoute>}
        />
        <Route
          path={routes.adminInvoices}
          element={<PrivateRoute role="ROLE_ADMIN"><Admin children={<InvoiceAdmin />} /></PrivateRoute>}
        />
        {/* <Route path={routes.home} element={<Home />} />
        <Route path={routes.locker} element={<DefaultLayout children={<Locker />} />} />
        <Route path={routes.invoice} element={<DefaultLayout children={<Invoice />} />} />
        <Route path={routes.payment} element={<DefaultLayout children={<Payment />} />} />
        <Route path={routes.surveys} element={<DefaultLayout children={<Surveys />} />} />
        <Route path={routes.survey} element={<DefaultLayout children={<Survey />} />} />
        <Route path={routes.feedback} element={<DefaultLayout children={<Feedbacks />} />} />
        <Route path={routes.personal} element={<Personal />} />
        <Route path={routes.admin} element={<Admin />} />
        <Route path={routes.services} element={<DefaultLayout children={<Service />} />} />
        <Route path={routes.login} element={<Login />} /> */}

        {/* <Route path={routes.adminDashboard} element={<Admin children={<Dashboard />} />} />
        <Route path={routes.adminRooms} element={<Admin children={<Rooms />} />} />
        <Route path={routes.adminLocker} element={<Admin children={<LockerAdmin />} />} />
        <Route path={routes.adminInvoices} element={<Admin children={<InvoiceAdmin />} />} />
        <Route path={routes.adminUsers} element={<Admin children={<Residents />} />} />
        <Route path={routes.adminParkingRights} element={<Admin children={<ParkingRights />} />} />
        <Route path={routes.adminVisitor} element={<Admin children={<Visitors />} />} />
        <Route path={routes.adminPayments} element={<Admin children={<PaymentAdmin />} />} />
        <Route path={routes.adminEntryRights} element={<Admin children={<EntryRights />} />} />
        <Route path={routes.adminFeedbacks} element={<Admin children={<FeedbacksAdmin />} />} />
        <Route path={routes.adminOrder} element={<Admin children={<OrderAdmin />} />} /> */}
        {/* <Route path={routes.chat} element={<Admin children={<ChatRoom />} />} /> */}

        {/* <Route
          path="/calendar"
          element={
            <>
              <PageTitle title="Calendar | TailAdmin - Tailwind CSS Admin Dashboard Template" />
            </>
          }
        /> */}
        {/* <Route
          path="/profile"
          element={
            <>
              <PageTitle title="Profile | TailAdmin - Tailwind CSS Admin Dashboard Template" />
              <Profile />
            </>
          }
        />
        <Route
          path="/forms/form-elements"
          element={
            <>
              <PageTitle title="Form Elements | TailAdmin - Tailwind CSS Admin Dashboard Template" />
              <FormElements />
            </>
          }
        />
        <Route
          path="/forms/form-layout"
          element={
            <>
              <PageTitle title="Form Layout | TailAdmin - Tailwind CSS Admin Dashboard Template" />
              <FormLayout />
            </>
          }
        />
        <Route
          path="/tables"
          element={
            <>
              <PageTitle title="Tables | TailAdmin - Tailwind CSS Admin Dashboard Template" />
              <Tables />
            </>
          }
        />
        <Route
          path="/settings"
          element={
            <>
              <PageTitle title="Settings | TailAdmin - Tailwind CSS Admin Dashboard Template" />
              <Settings />
            </>
          }
        />
        <Route
          path="/chart"
          element={
            <>
              <PageTitle title="Basic Chart | TailAdmin - Tailwind CSS Admin Dashboard Template" />
              <Chart />
            </>
          }
        />
        <Route
          path="/ui/alerts"
          element={
            <>
              <PageTitle title="Alerts | TailAdmin - Tailwind CSS Admin Dashboard Template" />
              <Alerts />
            </>
          }
        />
        <Route
          path="/ui/buttons"
          element={
            <>
              <PageTitle title="Buttons | TailAdmin - Tailwind CSS Admin Dashboard Template" />
              <Buttons />
            </>
          }
        /> */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
