import { useEffect, useState } from 'react';
import ReactApexChart from 'react-apexcharts';
import newRequest from '../../../untils/request';
import { RootEnum } from '../../../assets/enum';
import { notify } from '../../../untils/notification';

const ChartTwo = () => {
  const accesstoken = localStorage.getItem("accessToken")
  const [statRevenueByMonth, setStatRevenueByMonth] = useState([])
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1)
  const [chartOptions, setChartOptions] = useState({
    colors: ['#3C50E0', '#80CAEE'],
    chart: {
      fontFamily: 'Satoshi, sans-serif',
      type: 'bar',
      height: 335,
      stacked: true,
      toolbar: {
        show: false,
      },
      zoom: {
        enabled: false,
      },
    },
    plotOptions: {
      bar: {
        horizontal: false,
        borderRadius: 0,
        columnWidth: '25%',
        borderRadiusApplication: 'end',
        borderRadiusWhenStacked: 'last',
      },
    },
    dataLabels: {
      enabled: false,
    },
    xaxis: {
      categories: [], // This will be updated dynamically
    },
    legend: {
      position: 'top',
      horizontalAlign: 'left',
      fontFamily: 'Satoshi',
      fontWeight: 500,
      fontSize: '14px',
      markers: {
        radius: 99,
      },
    },
    fill: {
      opacity: 1,
    },
  });
  const [state, setStateStat] = useState({
    series: [
      {
        name: 'Sales',
        data: [...statRevenueByMonth.map((i) => (i[1]))],
      }
    ],
  });

  const handleReset = () => {
    setStateStat((prevState) => ({
      ...prevState,
    }));
  };

  useEffect(() => {
    handleReset()
    getStatRevenueByMonth()
    getStatRevenueByMonth()
  }, [selectedMonth])

  const getStatRevenueByMonth = () => {
    console.log(selectedMonth)
    newRequest.get(`${RootEnum.API_STAT}revenue?month=${selectedMonth}&year=2025`, {
      headers: {
        "Authorization": `Bearer ${accesstoken}`
      }
    })
      .then(data => {
        console.log('stat revenue by month: ', data.data.data)
        setStatRevenueByMonth(data.data.data)
        console.log('type: ', data.data.data.map(item => item[0]))
        console.log('data: ', data.data.data.map(item => item[1]))

        const categories = data.data.data.map(item => item[0]);
        const values = data.data.data.map(item => item[1]);

        setChartOptions(prevOptions => ({
          ...prevOptions,
          xaxis: {
            categories: categories,
          },
        }));

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

  return (
    <div className="transition col-span-12 rounded-sm border border-stroke bg-white p-7.5 shadow-default dark:border-strokedark dark:bg-boxdark xl:col-span-4">
      <div className="mb-4 justify-between gap-4 sm:flex">
        <div>
          <h4 className="text-xl font-semibold text-black dark:text-white">
            Revenue Detail
          </h4>
        </div>
        <div>
          <div className="relative z-20 inline-block">
            <select
              onChange={(e) => { setSelectedMonth(Number(e.target.value)) }}
              name="#"
              id="#"
              value={selectedMonth}
              className="relative z-20 inline-flex appearance-none bg-transparent py-1 pl-3 pr-8 text-sm font-medium outline-none"
            >
              <option value="1" className='dark:bg-boxdark'>January</option>
              <option value="2" className='dark:bg-boxdark'>Febuarary</option>
              <option value="3" className='dark:bg-boxdark'>March</option>
              <option value="3" className='dark:bg-boxdark'>April</option>
              <option value="5" className='dark:bg-boxdark'>May</option>
              <option value="6" className='dark:bg-boxdark'>June</option>
              <option value="7" className='dark:bg-boxdark'>July</option>
              <option value="8" className='dark:bg-boxdark'>August</option>
              <option value="9" className='dark:bg-boxdark'>September</option>
              <option value="10" className='dark:bg-boxdark'>October</option>
              <option value="11" className='dark:bg-boxdark'>Novenber</option>
              <option value="12" className='dark:bg-boxdark'>December</option>
            </select>
            <span className="absolute top-1/2 right-3 z-10 -translate-y-1/2">
              <svg
                width="10"
                height="6"
                viewBox="0 0 10 6"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M0.47072 1.08816C0.47072 1.02932 0.500141 0.955772 0.54427 0.911642C0.647241 0.808672 0.809051 0.808672 0.912022 0.896932L4.85431 4.60386C4.92785 4.67741 5.06025 4.67741 5.14851 4.60386L9.09079 0.896932C9.19376 0.793962 9.35557 0.808672 9.45854 0.911642C9.56151 1.01461 9.5468 1.17642 9.44383 1.27939L5.50155 4.98632C5.22206 5.23639 4.78076 5.23639 4.51598 4.98632L0.558981 1.27939C0.50014 1.22055 0.47072 1.16171 0.47072 1.08816Z"
                  fill="#637381"
                />
                <path
                  fillRule="evenodd"
                  clipRule="evenodd"
                  d="M1.22659 0.546578L5.00141 4.09604L8.76422 0.557869C9.08459 0.244537 9.54201 0.329403 9.79139 0.578788C10.112 0.899434 10.0277 1.36122 9.77668 1.61224L9.76644 1.62248L5.81552 5.33722C5.36257 5.74249 4.6445 5.7544 4.19352 5.32924C4.19327 5.32901 4.19377 5.32948 4.19352 5.32924L0.225953 1.61241C0.102762 1.48922 -4.20186e-08 1.31674 -3.20269e-08 1.08816C-2.40601e-08 0.905899 0.0780105 0.712197 0.211421 0.578787C0.494701 0.295506 0.935574 0.297138 1.21836 0.539529L1.22659 0.546578ZM4.51598 4.98632C4.78076 5.23639 5.22206 5.23639 5.50155 4.98632L9.44383 1.27939C9.5468 1.17642 9.56151 1.01461 9.45854 0.911642C9.35557 0.808672 9.19376 0.793962 9.09079 0.896932L5.14851 4.60386C5.06025 4.67741 4.92785 4.67741 4.85431 4.60386L0.912022 0.896932C0.809051 0.808672 0.647241 0.808672 0.54427 0.911642C0.500141 0.955772 0.47072 1.02932 0.47072 1.08816C0.47072 1.16171 0.50014 1.22055 0.558981 1.27939L4.51598 4.98632Z"
                  fill="#637381"
                />
              </svg>
            </span>
          </div>
        </div>
      </div>

      <div>
        <div id="chartTwo" className="-ml-5 -mb-9">
          <ReactApexChart
            options={chartOptions}
            series={state.series}
            type="bar"
            height={350}
          />
        </div>
      </div>
    </div>
  );
};

export default ChartTwo;
