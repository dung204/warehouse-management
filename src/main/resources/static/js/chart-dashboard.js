$("#changeStock").on("changed.bs.select", function (e, clickedIndex, isSelected, oldValue) {
  window.location.href = `/dashboard/${e.target.value}`;
});

const ctx = document.getElementById("nearly-expired-chart").getContext("2d");
const daysLeft = days.map((d) => d + " days left");
const chart = new Chart(ctx, {
  type: "bar",
  data: {
    labels: daysLeft,
    datasets: [
      {
        label: "Nearly expired Products",
        data: nearlyExpiredProducts,
        backgroundColor: [
          "rgba(255, 99, 132, 0.5)",
          "rgba(255, 159, 64, 0.5)",
          "rgba(255, 205, 86, 0.5)",
          "rgba(153, 102, 255, 0.5)",
          "rgba(75, 192, 192, 0.5)",
          "rgba(54, 162, 235, 0.5)",
        ],
        borderWidth: 2,
        fill: true,
      },
    ],
  },
  options: {
    responsive: true,
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        callbacks: {
          label: function (tooltipItem) {
            return `${tooltipItem.label}: ${tooltipItem.raw} products`;
          },
        },
      },
    },
  },
});

const ctx1 = document.getElementById("myPieChart").getContext("2d");

const chart1 = new Chart(ctx1, {
  type: "pie",
  data: {
    labels: ["Unexpired Products", "Expired Products"],
    datasets: [
      {
        data: [pStatusDTO.unexpiredProducts, pStatusDTO.expiredProducts],
        backgroundColor: ["rgba(54, 162, 235, 0.8)", "rgba(255, 99, 132, 0.8)"],
      },
    ],
  },
  options: {
    responsive: true,
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        callbacks: {
          label: function (tooltipItem) {
            return `${tooltipItem.label}: ${tooltipItem.raw} products`;
          },
        },
      },
    },
  },
});

const labels = categorySummaries.map((summary) => summary.category.name);
const data = categorySummaries.map((summary) => summary.quantityProducts);
const backgroundColors = [
  "#FF6384", // Red Pink
  "#36A2EB", // Blue
  "#FFCE56", // Yellow
  "#4BC0C0", // Turquoise
  "#9966FF", // Purple
  "#FF9F40", // Orange
  "#FF6347", // Tomato Red
];

const ctx2 = document.getElementById("categoriesChart").getContext("2d");
const chart2 = new Chart(ctx2, {
  type: "bar",
  data: {
    labels: labels,
    datasets: [
      {
        label: "Quantity Products",
        data: data,
        backgroundColor: backgroundColors,
        borderColor: backgroundColors,
        borderWidth: 1,
      },
    ],
  },
  options: {
    indexAxis: "y",
    plugins: {
      legend: {
        display: false,
      },
    },
    scales: {
      x: {
        beginAtZero: true,
      },
    },
  },
});

const labels1 = productSummaries.map((summary) => summary.productInfo.name);
const data1 = productSummaries.map((summary) => summary.totalQuantity);

const ctx3 = document.getElementById("productChart").getContext("2d");

const canvas = document.getElementById("productChart");
const heightPerItem = 50; // Chiều cao cho mỗi mục
canvas.height = heightPerItem * data1.length;

const chart3 = new Chart(ctx3, {
  type: "bar",
  data: {
    labels: labels1,
    datasets: [
      {
        label: "Quantity Products",
        data: data1,
        backgroundColor: backgroundColors,
        borderColor: backgroundColors,
        borderWidth: 1,
      },
    ],
  },
  options: {
    indexAxis: "y",
    plugins: {
      legend: {
        display: false,
      },
    },
    scales: {
      x: {
        beginAtZero: true,
      },
    },
  },
});
