const dateRangeFilterForm = document.querySelector("#filter-date-range-form");
const dateRangeInvalidAlert = document.querySelector("#date-range-invalid-alert");
const fromDateInput = document.querySelector("input#from-date");
const toDateInput = document.querySelector("input#to-date");

fromDateInput.addEventListener("change", () => {
  if (toDateInput.value != null && new Date(toDateInput.value) < new Date(fromDateInput.value)) {
    toDateInput.value = "";
  }

  toDateInput.min = fromDateInput.value;
});

DataTable.ext.search.push(function (settings, data, dataIndex) {
  const min = new Date(fromDateInput.value);
  const max = new Date(toDateInput.value);
  const date = new Date(data[1]) || -1;

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

dateRangeFilterForm.addEventListener("submit", (e) => {
  e.preventDefault();
  e.stopPropagation();
  debugger;

  $("#dataTable").DataTable().draw();
  $("#filter-date-range-modal").modal("hide");
});
