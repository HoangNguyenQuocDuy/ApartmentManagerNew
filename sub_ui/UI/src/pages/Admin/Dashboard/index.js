import React, { useEffect, useState } from 'react';
import CardDataStats from '../../../components/Admin/CardDataStats';
import ChartOne from '../../../components/Admin/Charts/ChartOne';
import ChartTwo from '../../../components/Admin/Charts/ChartTwo';
import newRequest from '../../../untils/request';
import { RootEnum } from '../../../assets/enum';
import { useDispatch } from 'react-redux';
import { loadListContract } from '../../../store/slice/contractSlice';
import { notify } from '../../../untils/notification';
import { ToastContainer } from 'react-toastify';
import ChatCard from '../../../components/Admin/Chat/ChatCard';

const Dashboard = () => {

  const [revanueData, setRevanueData] = useState()
  const dispatch = useDispatch()

  const accessToken = localStorage.getItem('accessToken')

  useEffect(() => {
    handleGetListContract(accessToken)
  }, [])

  const handleGetListContract = async (token) => {
    await newRequest.get(`${RootEnum.API_CONTRACT}?all=true`, {
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then(data => {
        console.log("list contract: ", data.data.data)
        dispatch(loadListContract(data.data.data.data.items))
      })
      .catch(err => {
        notify('Error getting contracts: ' + err, 'error')
      })
  }

  return (
    <>
      <ToastContainer />
      <div className="p-4 flex-1 grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-6 xl:grid-cols-4 2xl:gap-7.5">
        <CardDataStats title="Total rooms" current="12 Using Rooms" total="32 rooms" levelUp>
          <svg className="fill-primary dark:fill-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 24 24">
            <path fillRule="nonzero" d="M11.293 3.293a1 1 0 0 1 1.414 0l6 6 2 2a1 1 0 0 1-1.414 1.414L19 12.414V19a2 2 0 0 1-2 2h-3a1 1 0 0 1-1-1v-3h-2v3a1 1 0 0 1-1 1H7a2 2 0 0 1-2-2v-6.586l-.293.293a1 1 0 0 1-1.414-1.414l2-2 6-6Z" clipRule="evenodd" />
          </svg>
        </CardDataStats>
        <CardDataStats title="Total Revenue" total="$45,2K" current="3000$" levelUp>
          <svg className="fill-primary dark:fill-white" width='20' height='20' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M64 32C28.7 32 0 60.7 0 96L0 416c0 35.3 28.7 64 64 64l384 0c35.3 0 64-28.7 64-64l0-224c0-35.3-28.7-64-64-64L80 128c-8.8 0-16-7.2-16-16s7.2-16 16-16l368 0c17.7 0 32-14.3 32-32s-14.3-32-32-32L64 32zM416 272a32 32 0 1 1 0 64 32 32 0 1 1 0-64z" /></svg>
        </CardDataStats>
        <CardDataStats title="Total invoices for a month" total="30 invoices" current="28 Payments" levelUp>
          <svg className='fill-primary dark:fill-white' stroke="currentColor" fill="none" strokeWidth="0" viewBox="0 0 32 32" height="24" width="24" xmlns="http://www.w3.org/2000/svg"><path d="M 6 3 L 6 29 L 22 29 L 22 27 L 8 27 L 8 5 L 18 5 L 18 11 L 24 11 L 24 13 L 26 13 L 26 9.5996094 L 25.699219 9.3007812 L 19.699219 3.3007812 L 19.400391 3 L 6 3 z M 20 6.4003906 L 22.599609 9 L 20 9 L 20 6.4003906 z M 10 13 L 10 15 L 22 15 L 22 13 L 10 13 z M 27 15 L 27 17 C 25.3 17.3 24 18.7 24 20.5 C 24 22.5 25.5 24 27.5 24 L 28.5 24 C 29.3 24 30 24.7 30 25.5 C 30 26.3 29.3 27 28.5 27 L 25 27 L 25 29 L 27 29 L 27 31 L 29 31 L 29 29 C 30.7 28.7 32 27.3 32 25.5 C 32 23.5 30.5 22 28.5 22 L 27.5 22 C 26.7 22 26 21.3 26 20.5 C 26 19.7 26.7 19 27.5 19 L 31 19 L 31 17 L 29 17 L 29 15 L 27 15 z M 10 18 L 10 20 L 17 20 L 17 18 L 10 18 z M 19 18 L 19 20 L 22 20 L 22 18 L 19 18 z M 10 22 L 10 24 L 17 24 L 17 22 L 10 22 z M 19 22 L 19 24 L 22 24 L 22 22 L 19 22 z"></path></svg>
        </CardDataStats>
        <CardDataStats title="Total Users" current="10 Active Users" total="12" levelUp>
          <svg
            className="fill-primary dark:fill-white"
            width="22"
            height="18"
            viewBox="0 0 22 18"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M7.18418 8.03751C9.31543 8.03751 11.0686 6.35313 11.0686 4.25626C11.0686 2.15938 9.31543 0.475006 7.18418 0.475006C5.05293 0.475006 3.2998 2.15938 3.2998 4.25626C3.2998 6.35313 5.05293 8.03751 7.18418 8.03751ZM7.18418 2.05626C8.45605 2.05626 9.52168 3.05313 9.52168 4.29063C9.52168 5.52813 8.49043 6.52501 7.18418 6.52501C5.87793 6.52501 4.84668 5.52813 4.84668 4.29063C4.84668 3.05313 5.9123 2.05626 7.18418 2.05626Z"
              fill=""
            />
            <path
              d="M15.8124 9.6875C17.6687 9.6875 19.1468 8.24375 19.1468 6.42188C19.1468 4.6 17.6343 3.15625 15.8124 3.15625C13.9905 3.15625 12.478 4.6 12.478 6.42188C12.478 8.24375 13.9905 9.6875 15.8124 9.6875ZM15.8124 4.7375C16.8093 4.7375 17.5999 5.49375 17.5999 6.45625C17.5999 7.41875 16.8093 8.175 15.8124 8.175C14.8155 8.175 14.0249 7.41875 14.0249 6.45625C14.0249 5.49375 14.8155 4.7375 15.8124 4.7375Z"
              fill=""
            />
            <path
              d="M15.9843 10.0313H15.6749C14.6437 10.0313 13.6468 10.3406 12.7874 10.8563C11.8593 9.61876 10.3812 8.79376 8.73115 8.79376H5.67178C2.85303 8.82814 0.618652 11.0625 0.618652 13.8469V16.3219C0.618652 16.975 1.13428 17.4906 1.7874 17.4906H20.2468C20.8999 17.4906 21.4499 16.9406 21.4499 16.2875V15.4625C21.4155 12.4719 18.9749 10.0313 15.9843 10.0313ZM2.16553 15.9438V13.8469C2.16553 11.9219 3.74678 10.3406 5.67178 10.3406H8.73115C10.6562 10.3406 12.2374 11.9219 12.2374 13.8469V15.9438H2.16553V15.9438ZM19.8687 15.9438H13.7499V13.8469C13.7499 13.2969 13.6468 12.7469 13.4749 12.2313C14.0937 11.7844 14.8499 11.5781 15.6405 11.5781H15.9499C18.0812 11.5781 19.8343 13.3313 19.8343 15.4625V15.9438H19.8687Z"
              fill=""
            />
          </svg>
        </CardDataStats>
      </div>
      <div className="p-4 mt-4 grid grid-cols-12 gap-4 md:mt-6 md:gap-6 2xl:mt-7.5 2xl:gap-7.5">
        <ChartOne />
        <ChartTwo />
        {/* <ChatCard /> */}

        {/* <ChartTwo />
        <ChartThree />
        <MapOne />
        <div className="col-span-12 xl:col-span-8">
          <TableOne />
        </div>
        <ChatCard /> */}
      </div>
      {/* <div className="mt-4 grid grid-cols-12 gap-4 md:mt-6 md:gap-6 2xl:mt-7.5 2xl:gap-7.5">
        <ChartOne />
        <ChartTwo />
        <ChartThree />
        <MapOne />
        <div className="col-span-12 xl:col-span-8">
          <TableOne />
        </div>
        <ChatCard />
      </div> */}
    </>
  );
};

export default Dashboard;
