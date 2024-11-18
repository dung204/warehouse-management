const dateRangeFilterFormExpiryDate = document.querySelector("#filter-date-range-form-expiry-date");
const dateRangeInvalidAlertExpiryDate = document.querySelector(
  "#date-range-invalid-alert-expiry-date",
);
const fromDateInputExpiryDate = document.querySelector("input#from-date-expiry-date");
const toDateInputExpiryDate = document.querySelector("input#to-date-expiry-date");

const dateRangeFilterFormDestroyedTimestamp = document.querySelector(
  "#filter-date-range-form-destroyed-timestamp",
);
const dateRangeInvalidAlertDestroyedTimestamp = document.querySelector(
  "#date-range-invalid-alert-destroyed-timestamp",
);
const fromDateInputDestroyedTimestamp = document.querySelector(
  "input#from-date-destroyed-timestamp",
);
const toDateInputDestroyedTimestamp = document.querySelector("input#to-date-destroyed-timestamp");

fromDateInputExpiryDate.addEventListener("change", () => {
  if (
    toDateInputExpiryDate.value != null &&
    new Date(toDateInputExpiryDate.value) < new Date(fromDateInputExpiryDate.value)
  ) {
    toDateInputExpiryDate.value = "";
  }

  toDateInputExpiryDate.min = fromDateInputExpiryDate.value;
});

DataTable.ext.search.push(function (settings, data, dataIndex) {
  const min = new Date(fromDateInputExpiryDate.value);
  const max = new Date(toDateInputExpiryDate.value);
  const date = new Date(data[2]) || -1;

  min.setHours(0, 0, 0, 0);
  max.setHours(0, 0, 0, 0);
  date.setHours(0, 0, 0, 0);

  if (
    (Number.isNaN(min.getTime()) && Number.isNaN(max.getTime())) ||
    (Number.isNaN(min.getTime()) && date.getTime() <= max.getTime()) ||
    (min.getTime() <= date.getTime() && Number.isNaN(max.getTime())) ||
    (min.getTime() <= date.getTime() && date.getTime() <= max.getTime())
  ) {
    return true;
  }
  return false;
});

dateRangeFilterFormDestroyedTimestamp.addEventListener("submit", (e) => {
  e.preventDefault();
  e.stopPropagation();
  debugger;

  $("#dataTable").DataTable().draw();
  $("#filter-date-range-modal-expiry-date").modal("hide");
});

fromDateInputDestroyedTimestamp.addEventListener("change", () => {
  if (
    toDateInputDestroyedTimestamp.value != null &&
    new Date(toDateInputDestroyedTimestamp.value) < new Date(fromDateInputDestroyedTimestamp.value)
  ) {
    toDateInputDestroyedTimestamp.value = "";
  }

  toDateInputDestroyedTimestamp.min = fromDateInputDestroyedTimestamp.value;
});

DataTable.ext.search.push(function (settings, data, dataIndex) {
  const min = new Date(fromDateInputDestroyedTimestamp.value);
  const max = new Date(toDateInputDestroyedTimestamp.value);
  const date = new Date(data[6]) || -1;

  min.setHours(0, 0, 0, 0);
  max.setHours(0, 0, 0, 0);
  date.setHours(0, 0, 0, 0);

  if (
    (Number.isNaN(min.getTime()) && Number.isNaN(max.getTime())) ||
    (Number.isNaN(min.getTime()) && date.getTime() <= max.getTime()) ||
    (min.getTime() <= date.getTime() && Number.isNaN(max.getTime())) ||
    (min.getTime() <= date.getTime() && date.getTime() <= max.getTime())
  ) {
    return true;
  }
  return false;
});

dateRangeFilterFormDestroyedTimestamp.addEventListener("submit", (e) => {
  e.preventDefault();
  e.stopPropagation();
  debugger;

  $("#dataTable").DataTable().draw();
  $("#filter-date-range-modal-destroyed-timestamp").modal("hide");
});
