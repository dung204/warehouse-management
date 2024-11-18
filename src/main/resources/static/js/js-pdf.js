function generatePDF(callback) {
  const { jsPDF } = window.jspdf;
  var doc = new jsPDF("p", "pt", "a4");

  doc.setFontSize(14);
  doc.text(
    document
      .getElementById("invoice-info")
      .textContent.replace(/ {2,}/g, "")
      .replace(/\n{2,}/g, "#")
      .replace(/\n/g, " ")
      .replace(/#/g, "\n")
      .trim(),
    25,
    25,
  );

  doc.autoTable({
    html: "#invoiceTable",
    startY: 150,
    theme: "grid",
    headStyles: { fillColor: [255, 255, 255], textColor: [0, 0, 0] },
    styles: { overflow: "linebreak" },
    margin: { top: 40, bottom: 40, left: 40, right: 40 },
    didDrawPage: function (data) {
      if (data.pageNumber === 1) {
        doc.setFontSize(16);
      }
    },
  });

  if (callback) {
    callback(doc);
  }
}

document.getElementById("previewPDF")?.addEventListener("click", function () {
  generatePDF(function (doc) {
    var pdfBlob = doc.output("blob");
    var url = URL.createObjectURL(pdfBlob);
    window.open(url, "_blank");
  });
});

document.getElementById("downloadPDF")?.addEventListener("click", function () {
  generatePDF(function (doc) {
    doc.save("invoice.pdf");
  });
});
