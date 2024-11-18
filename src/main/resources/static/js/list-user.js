const dateRangeFilterFormDob = document.querySelector("#filter-date-range-form-dob");
const dateRangeInvalidAlertDob = document.querySelector("#date-range-invalid-alert-dob");
const fromDateInputDob = document.querySelector("input#from-date-dob");
const toDateInputDob = document.querySelector("input#to-date-dob");

const dateRangeFilterFormCreatedTimestamp = document.querySelector(
  "#filter-date-range-form-created-timestamp",
);
const dateRangeInvalidAlertCreatedTimestamp = document.querySelector(
  "#date-range-invalid-alert-created-timestamp",
);
const fromDateInputCreatedTimestamp = document.querySelector("input#from-date-created-timestamp");
const toDateInputCreatedTimestamp = document.querySelector("input#to-date-created-timestamp");

fromDateInputDob.addEventListener("change", () => {
  if (
    toDateInputDob.value != null &&
    new Date(toDateInputDob.value) < new Date(fromDateInputDob.value)
  ) {
    toDateInputDob.value = "";
  }

  toDateInputDob.min = fromDateInputDob.value;
});

DataTable.ext.search.push(function (settings, data, dataIndex) {
  const min = new Date(fromDateInputDob.value);
  const max = new Date(toDateInputDob.value);
  const date = new Date(data[4]) || -1;

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

dateRangeFilterFormDob.addEventListener("submit", (e) => {
  e.preventDefault();
  e.stopPropagation();

  $("#dataTable").DataTable().draw();
  $("#filter-date-range-modal-dob").modal("hide");
});

fromDateInputCreatedTimestamp.addEventListener("change", () => {
  if (
    toDateInputCreatedTimestamp.value != null &&
    new Date(toDateInputCreatedTimestamp.value) < new Date(fromDateInputCreatedTimestamp.value)
  ) {
    toDateInputCreatedTimestamp.value = "";
  }

  toDateInputCreatedTimestamp.min = fromDateInputCreatedTimestamp.value;
});

DataTable.ext.search.push(function (settings, data, dataIndex) {
  const min = new Date(fromDateInputCreatedTimestamp.value);
  const max = new Date(toDateInputCreatedTimestamp.value);
  const date = new Date(data[8]) || -1;

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

dateRangeFilterFormCreatedTimestamp.addEventListener("submit", (e) => {
  e.preventDefault();
  e.stopPropagation();

  $("#dataTable").DataTable().draw();
  $("#filter-date-range-modal-created-timestamp").modal("hide");
});
