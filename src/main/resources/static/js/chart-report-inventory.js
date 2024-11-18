let chart;

function drawReportInventoryChart() {
  const table = document.getElementById("dataTable");
  const rows = table.querySelectorAll("tbody tr");

  const labels = [];
  const inventoryData = [];
  const importedData = [];
  const exportedData = [];
  const destroyedData = [];

  rows.forEach((row) => {
    const cells = row.querySelectorAll("td");
    labels.push(cells[0].innerText);
    inventoryData.push(parseInt(cells[1].innerText));
    importedData.push(parseInt(cells[2].innerText));
    exportedData.push(parseInt(cells[3].innerText));
    destroyedData.push(parseInt(cells[4].innerText));
  });

  if (!chart) {
    const canvas = document.querySelector("canvas#report-inventory-chart");
    chart = new Chart(canvas, {
      type: "bar",
      data: {
        labels: labels,
        datasets: [
          {
            label: "Inventory Quantity",
            data: inventoryData,
            type: "line",
            backgroundColor: "rgba(54, 162, 235, 0.2)",
            borderColor: "rgba(54, 162, 235, 1)",
            borderWidth: 2,
            fill: false,
          },
          {
            label: "Imported Quantity",
            data: importedData,
            backgroundColor: "rgba(75, 192, 192, 0.2)",
            borderColor: "rgba(75, 192, 192, 1)",
            borderWidth: 1,
          },
          {
            label: "Exported Quantity",
            data: exportedData,
            backgroundColor: "rgba(153, 102, 255, 0.2)",
            borderColor: "rgba(153, 102, 255, 1)",
            borderWidth: 1,
          },
          {
            label: "Destroyed Quantity",
            data: destroyedData,
            backgroundColor: "rgba(255, 99, 132, 0.2)",
            borderColor: "rgba(255, 99, 132, 1)",
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
      },
    });
  } else {
    chart.data.labels = labels;
    chart.data.datasets[0].data = inventoryData;
    chart.data.datasets[1].data = importedData;
    chart.data.datasets[2].data = exportedData;
    chart.data.datasets[3].data = destroyedData;
    chart.update();
  }
}

drawReportInventoryChart();
$("#dataTable").on("draw.dt", drawReportInventoryChart);
