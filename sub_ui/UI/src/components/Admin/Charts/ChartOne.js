import React, { useEffect, useState } from 'react';
import ReactApexChart from 'react-apexcharts';
import newRequest from '../../../untils/request';
import { RootEnum } from '../../../assets/enum';
import { notify } from '../../../untils/notification';

const ChartOne = () => {
  const accesstoken = localStorage.getItem("accessToken")
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear())
  const [chartOptions, setChartOptions] = useState({
    legend: {
      show: false,
      position: 'top',
      horizontalAlign: 'left',
    },
    // colors: ['#3C50E0', '#80CAEE'],
    chart: {
      fontFamily: 'Satoshi, sans-serif',
      height: 335,
      type: 'area',
      dropShadow: {
        enabled: true,
        color: '#623CEA14',
        top: 10,
        blur: 4,
        left: 0,
        opacity: 0.1,
      },

      toolbar: {
        show: false,
      },
    },
    responsive: [
      {
        breakpoint: 1024,
        options: {
          chart: {
            height: 300,
          },
        },
      },
      {
        breakpoint: 1366,
        options: {
          chart: {
            height: 350,
          },
        },
      },
    ],
    stroke: {
      width: [2, 2],
      curve: 'straight',
    },
    // labels: {
    //   show: false,
    //   position: "top",
    // },
    grid: {
      xaxis: {
        lines: {
          show: true,
        },
      },
      yaxis: {
        lines: {
          show: true,
        },
      },
    },
    dataLabels: {
      enabled: false,
    },
    markers: {
      size: 4,
      colors: '#fff',
      strokeColors: ['#3056D3', '#80CAEE'],
      strokeWidth: 3,
      strokeOpacity: 0.9,
      strokeDashArray: 0,
      fillOpacity: 1,
      discrete: [],
      hover: {
        size: undefined,
        sizeOffset: 5,
      },
    },
    xaxis: {
      type: 'category',
      categories: [
        'Jan',
        'Feb',
        'Mar',
        'Apr',
        'May',
        'Jun',
        'Jul',
        'Aug',
        'Sep',
        'Oct',
        'Nov',
        'Dec',
      ],
      axisBorder: {
        show: false,
      },
      axisTicks: {
        show: false,
      },
    },
    yaxis: {
      title: {
        style: {
          fontSize: '0px',
        },
      },
      min: 0,
      // max: 100,
    },
  })
  const [state, setStateStat] = useState({
    series: [
      {
        name: 'Product One',
        data: [23, 11, 22, 27, 13, 22, 37, 21, 44, 22, 30, 45],
      },

      // {
      //   name: 'Product Two',
      //   data: [30, 25, 36, 30, 45, 35, 64, 52, 59, 36, 39, 51],
      // },
    ],
  });

  const getStatRevenueByYear = () => {
    console.log('selectedYear: ', selectedYear)
    newRequest.get(`${RootEnum.API_STAT}revenue?year=${selectedYear}`, {
      headers: {
        "Authorization": `Bearer ${accesstoken}`
      }
    })
      .then(data => {
        console.log('stat revenue by year: ', data.data.data)
        console.log('month: ', data.data.data.map(item => item[0]))
        console.log('data: ', data.data.data.map(item => item[1]))

        const month = data.data.data.map(item => item[0]);
        const values = data.data.data.map(item => item[1]);

        // setChartOptions(prevOptions => ({
        //   ...prevOptions,
        //   xaxis: {
        //     categories: categories,
        //   },
        // }));

        setStateStat({
          series: [
            {
              name: 'Sales',
              data: values,
            }
          ]
        })
      })
      .catch(err => {
        // alert('Error getting stat revenue by month: ' + err)
        notify('Error getting list stat revenue by month: ' + err, 'error')
      })
  }
  const handleReset = () => {
    setStateStat((prevState) => ({
      ...prevState,
    }));
  };

  useEffect(() => {
    handleReset()
    getStatRevenueByYear()
  }, [])
  return (
    <div className="transition col-span-12 rounded-sm border border-stroke bg-white px-5 pt-7.5 pb-5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:col-span-8">
      <div className="flex flex-wrap items-start justify-between gap-3 sm:flex-nowrap">
        <div className="flex w-full flex-wrap gap-3 sm:gap-5">
          <div className="flex min-w-47.5">
            <span className="mt-1 mr-2 flex h-4 w-full max-w-4 items-center justify-center rounded-full border border-primary">
              <span className="block h-2.5 w-full max-w-2.5 rounded-full bg-primary"></span>
            </span>
            <div className="w-full">
              <p className="font-semibold text-primary">Total Revenue</p>
              <div className='flex'>
                <input type='number' value={selectedYear} onChange={e => { setSelectedYear(e.target.value) }} className='shadow-md ps-4 py-2 me-4 rounded-md' placeholder='year...' />
                <button onClick={() => { getStatRevenueByYear() }} className='rounded-full bg-red-400 shadow-md px-6 transition text-white hover:bg-red-500'>Check</button>
              </div>
            </div>
          </div>
        </div>
        <div className="flex w-full max-w-45 justify-end">
          <div className="inline-flex items-center rounded-md bg-whiter p-1.5 dark:bg-meta-4">
            <button className="rounded py-1 px-3 text-xs font-medium text-black hover:bg-white hover:shadow-card dark:text-white dark:hover:bg-boxdark">
              Week
            </button>
            <button className="rounded py-1 px-3 text-xs font-medium text-black hover:bg-white hover:shadow-card dark:text-white dark:hover:bg-boxdark">
              Month
            </button>
          </div>
        </div>
      </div>

      <div>
        <div id="chartOne" className="-ml-5">
          <ReactApexChart
            options={chartOptions}
            series={state.series}
            type="area"
            height={350}
          />
        </div>
      </div>
    </div>
  );
};

export default ChartOne;
