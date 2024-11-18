$(document).ready(function () {
  $("#dataTable").DataTable({
    initComplete: function () {
      this.api()
        .columns(".searchable")
        .every(function () {
          let column = this;
          let title = column.footer().dataset.colName;

          $(
            `<input type="text" class="form-control form-control-sm" placeholder="Search ${title}..." />`,
          )
            .appendTo($(column.footer()).empty())
            .on("keyup change clear", function () {
              if (column.search() !== this.value) {
                column.search(this.value).draw();
              }
            });
        });
    },
    columnDefs: [
      {
        targets: "no-sort",
        orderable: false,
      },
    ],
  });
});
