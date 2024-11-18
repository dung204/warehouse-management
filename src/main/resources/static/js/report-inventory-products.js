const inventoryReportForm = document.querySelector("form#inventory-report-form");
const fromDateInputExpiryDate = document.querySelector("input#from-date");
const toDateInputExpiryDate = document.querySelector("input#to-date");

fromDateInputExpiryDate.max = toDateInputExpiryDate.max = toIsoString(new Date()).split("T")[0];

fromDateInputExpiryDate.addEventListener("change", () => {
  if (
    toDateInputExpiryDate.value != null &&
    new Date(toDateInputExpiryDate.value) < new Date(fromDateInputExpiryDate.value)
  ) {
    toDateInputExpiryDate.value = "";
  }

  toDateInputExpiryDate.min = fromDateInputExpiryDate.value;
});

inventoryReportForm.addEventListener("submit", (e) => {
  if (!inventoryReportForm.checkValidity()) {
    e.preventDefault();
    e.stopPropagation();
    inventoryReportForm.classList.add("was-validated");
  }
});

function toIsoString(date) {
  const tzo = -date.getTimezoneOffset(),
    dif = tzo >= 0 ? "+" : "-",
    pad = function (num) {
      return (num < 10 ? "0" : "") + num;
    };

  return (
    date.getFullYear() +
    "-" +
    pad(date.getMonth() + 1) +
    "-" +
    pad(date.getDate()) +
    "T" +
    pad(date.getHours()) +
    ":" +
    pad(date.getMinutes()) +
    ":" +
    pad(date.getSeconds()) +
    dif +
    pad(Math.floor(Math.abs(tzo) / 60)) +
    ":" +
    pad(Math.abs(tzo) % 60)
  );
}
