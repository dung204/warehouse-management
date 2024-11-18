const modalProductId = document.querySelector("#changePositionModal_productId");
const modalProductName = document.querySelector("#changePositionModal_productName");
const modalCurrentPosition = document.querySelector("#changePositionModal_currentPosition");
const modalQuantity = document.querySelector("#changePositionModal_quantity");
const newPositions = document.querySelector(".new-positions");
const changePositionForm = document.querySelector("form#change-position-form");
const addPositionBtn = document.querySelector(".add-position-btn");
const quantityInvalidAlert = document.querySelector("#quantity-invalid-alert");
const dateRangeFilterForm = document.querySelector("#filter-date-range-form");
const dateRangeInvalidAlert = document.querySelector("#date-range-invalid-alert");
const fromDateInput = document.querySelector("input#from-date");
const toDateInput = document.querySelector("input#to-date");
const positionSelectOptions = [...document.querySelectorAll("select.position-select > option")];
const moveToAnotherStockButton = document.querySelector("button#move-to-another-stock-btn");
const moveToAnotherStockTableBody = document.querySelector("#move-to-another-stock-table tbody");
const moveToAnotherStockForm = document.querySelector("#move-to-another-stock-form");

let selectedProducts = [];

let countPosition = 0;

$("#dataTable").on("init.dt", setTableEvents).on("draw.dt", setTableEvents);

[...document.querySelectorAll(".searchable")].forEach((elem) => {
  elem.onclick = (e) => {
    console.log(e.target.tagName.toLowerCase());
    if (e.target.tagName.toLowerCase() === "input") {
      e.stopPropagation();
    }
  };
});

addPositionBtn.addEventListener("click", () => {
  clearChangePositionFormValidation();
  addNewPositionInstance();
  preventDuplicatePosition();
});

$("#changePositionModal").on("hidden.bs.modal", resetChangePositionModal);

changePositionForm.addEventListener("submit", validateChangePositionForm, false);

fromDateInput.addEventListener("change", () => {
  if (toDateInput.value != null && new Date(toDateInput.value) < new Date(fromDateInput.value)) {
    toDateInput.value = "";
  }

  toDateInput.min = fromDateInput.value;
});

DataTable.ext.search.push(function (settings, data, dataIndex) {
  const min = new Date(fromDateInput.value);
  const max = new Date(toDateInput.value);
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

dateRangeFilterForm.addEventListener("submit", (e) => {
  e.preventDefault();
  e.stopPropagation();
  debugger;

  $("#dataTable").DataTable().draw();
  $("#filter-date-range-modal").modal("hide");
});

$("#move-to-another-stock-modal").on("show.bs.modal", setMoveToAnotherStockModalContent);
$("#move-to-another-stock-modal").on("hidden.bs.modal", resetMoveToAnotherStockForm);

moveToAnotherStockForm.addEventListener("submit", validateMoveToAnotherStockForm, false);

$("select.position-select").on("changed.bs.select", preventDuplicatePosition);

function setTableEvents() {
  [...document.querySelectorAll(".toggle-change-position-modal-btn")].forEach((btn) => {
    btn.onclick = (e) => {
      const productId = e.target.getAttribute("data-product-id");
      const productName = document.querySelector(`#productName_${productId}`).textContent;
      const currentPositionElem = document.querySelector(`#productPosition_${productId}`);
      const quantity = document.querySelector(`#productQuantity_${productId}`).textContent;

      changePositionForm.action = `/products/change-position/${productId}`;
      modalProductId.textContent = productId;
      modalProductName.textContent = productName;
      modalCurrentPosition.textContent = currentPositionElem.textContent;
      modalCurrentPosition.dataset.positionId = currentPositionElem.dataset.positionId;
      modalQuantity.textContent = quantity;

      document.querySelector("select.position-select").innerHTML = positionSelectOptions
        .filter((el) => el.value !== currentPositionElem.dataset.positionId)
        .map((el) => el.outerHTML)
        .join("");

      $(".position-select").selectpicker("refresh");
    };
  });

  [...document.querySelectorAll(".select-product-checkbox")].forEach((checkbox) => {
    if (!selectedProducts.find((product) => product.id === checkbox.dataset.productId)) {
      checkbox.checked = false;
    }

    checkbox.onchange = (e) => {
      if (e.target.checked) {
        selectedProducts.push({
          id: e.target.dataset.productId,
          name: document.querySelector(`#productName_${e.target.dataset.productId}`).textContent,
          currentPosition: document.querySelector(`#productPosition_${e.target.dataset.productId}`)
            .textContent,
          expiredDate: document.querySelector(`#productExpiredDate_${e.target.dataset.productId}`)
            .textContent,
          quantity: document.querySelector(`#productQuantity_${e.target.dataset.productId}`)
            .textContent,
        });
      } else {
        selectedProducts = selectedProducts.filter(
          (product) => product.id !== e.target.dataset.productId,
        );
      }

      if (selectedProducts.length > 0) {
        moveToAnotherStockButton.style.display = "initial";
      } else {
        moveToAnotherStockButton.style.display = "none";
      }
    };
  });
}

function resetChangePositionModal() {
  clearChangePositionFormValidation();

  changePositionForm.action = "";
  modalProductId.textContent = "";
  modalProductName.textContent = "";
  modalCurrentPosition.textContent = "";
  modalCurrentPosition.dataset.positionId = "";
  modalQuantity.textContent = "";

  [...newPositions.children].forEach((el, index) => {
    if (index > 2) {
      el.remove();
    }
  });

  countPosition = 0;

  $(".position-select").selectpicker("val", "");
  newPositions.children[1].querySelector("input").value = "";

  document.querySelector("select.position-select").innerHTML = positionSelectOptions
    .map((el) => el.outerHTML)
    .join("");
  $(".position-select").selectpicker("refresh");
}

function preventDuplicatePosition() {
  debugger;
  const positionSelects = document.querySelectorAll("select.position-select");
  const selectedValueSet = new Set();
  positionSelects.forEach((select) => selectedValueSet.add(select.value));
  positionSelects.forEach((selectElement) => {
    for (const option of selectElement.options) {
      option.disabled = selectedValueSet.has(option.value);
    }
    selectElement.options[selectElement.selectedIndex].disabled = false;
    selectElement.options[0].disabled = true;
    $(selectElement.closest(".position-select")).selectpicker("refresh");
  });
}

function validateChangePositionForm(e) {
  quantityInvalidAlert.style.display = "none";

  const positionSelects = [...document.querySelectorAll("select.position-select")];
  const positionValues = positionSelects.map((el) => el.value).filter((v) => v !== "");
  const uniquePositionValues = [...new Set(positionValues)];

  uniquePositionValues.forEach((value) => {
    if (positionValues.filter((v) => v === value).length > 1) {
      const duplicatedPositionSelects = positionSelects.filter((el) => el.value === value);
      duplicatedPositionSelects.forEach((el) => {
        el.parentElement.nextElementSibling.nextElementSibling.style.display = "block";
        e.preventDefault();
        e.stopPropagation();
      });
    }
  });

  if (changePositionForm.checkValidity()) {
    const quantities = [...document.querySelectorAll("input.position-quantity")].map(
      (el) => +el.value,
    );
    const totalQuantity = quantities.reduce((acc, curr) => acc + curr, 0);
    const currentQuantity = +modalQuantity.textContent;

    if (totalQuantity > currentQuantity) {
      e.preventDefault();
      e.stopPropagation();
      quantityInvalidAlert.style.display = "block";
      return;
    }

    return;
  }

  e.preventDefault();
  e.stopPropagation();
  changePositionForm.classList.add("was-validated");
}

function clearChangePositionFormValidation() {
  quantityInvalidAlert.style.display = "none";
  changePositionForm.classList.remove("was-validated");
  [...document.querySelectorAll(".selected-position-feedback")].forEach((el) => {
    el.style.display = "none";
  });
}

function addNewPositionInstance() {
  const newPositionSelectContainer = newPositions.children[0].cloneNode();
  const newPositionSelect = document.querySelector("select.position-select").cloneNode(true);
  const newPositionSelectInvalidFeedback = document
    .querySelector(".invalid-feedback")
    .cloneNode(true);
  const selectedPositionFeedback = document
    .querySelector(".selected-position-feedback")
    .cloneNode(true);
  const newPositionQuantity = newPositions.children[1].cloneNode(true);

  const deleteBtnContainer = document.createElement("div");
  deleteBtnContainer.classList.add("col-1", "py-2");

  const deleteBtn = document.createElement("button");
  deleteBtn.type = "button";
  deleteBtn.classList.add("btn", "btn-danger");

  const deleteIcon = document.createElement("i");
  deleteIcon.classList.add("fa-solid", "fa-trash");

  deleteBtn.appendChild(deleteIcon);
  deleteBtnContainer.appendChild(deleteBtn);

  countPosition++;
  newPositionSelectContainer.dataset.positionIndex = countPosition;
  newPositionQuantity.dataset.positionIndex = countPosition;
  deleteBtnContainer.dataset.positionIndex = countPosition;
  deleteBtn.dataset.deleteIndex = countPosition;
  deleteIcon.dataset.deleteIndex = countPosition;

  newPositionSelectContainer.append(
    newPositionSelect,
    newPositionSelectInvalidFeedback,
    selectedPositionFeedback,
  );
  newPositions.append(newPositionSelectContainer, newPositionQuantity, deleteBtnContainer);

  $(".position-select").selectpicker("render");

  deleteBtn.onclick = deleteIcon.onclick = (e) => {
    console.log(e.target);
    console.log(e.target.dataset.deleteIndex);

    const elems = document.querySelectorAll(
      `[data-position-index="${e.target.dataset.deleteIndex}"]`,
    );

    [...elems].forEach((el) => el.remove());
    $("select.position-select").on("changed.bs.select", preventDuplicatePosition);
    preventDuplicatePosition();
  };
  $("select.position-select").on("changed.bs.select", preventDuplicatePosition);
}

function setMoveToAnotherStockModalContent() {
  renderSelectedProducts();

  [...document.querySelectorAll(".move-to-another-stock-delete-btn")].forEach((btn) => {
    btn.onclick = () => {
      const productId = btn.dataset.productId;
      selectedProducts = selectedProducts.filter((product) => product.id !== productId);
      const checkbox = document.querySelector(
        `.select-product-checkbox[data-product-id="${productId}"]`,
      );

      if (checkbox) {
        checkbox.checked = false;
      }

      moveToAnotherStockTableBody.querySelector(`tr[data-product-id="${productId}"]`).remove();

      if (selectedProducts.length <= 0) {
        $("#move-to-another-stock-modal").modal("hide");
        moveToAnotherStockButton.style.display = "none";
        return;
      }
    };
  });
}

function renderSelectedProducts() {
  moveToAnotherStockTableBody.innerHTML = selectedProducts
    .map((product) => {
      return `
      <tr data-product-id="${product.id}">
        <input type="hidden" name="product-id" value="${product.id}" />
        <td>${product.name}</td>
        <td>${product.expiredDate}</td>
        <td>${product.currentPosition}</td>
        <td>${product.quantity}</td>
        <td>
          <input type="number" class="form-control move-to-another-stock-quantity" name="quantity" min="1" max="${product.quantity}" required />
          <div class="invalid-feedback">A quantity must be entered and has a minimum value of 1, maximum value of ${product.quantity}.</div>
        </td>
        <td class="text-center">
          <button type="button" class="btn btn-danger move-to-another-stock-delete-btn" data-product-id="${product.id}">
            <i class="bi bi-trash-fill"></i>
          </button>
        </td>
      </tr>
    `;
    })
    .join("");
}

function validateMoveToAnotherStockForm(e) {
  if (!moveToAnotherStockForm.checkValidity()) {
    e.preventDefault();
    e.stopPropagation();
    moveToAnotherStockForm.classList.add("was-validated");
    return;
  }
}

function resetMoveToAnotherStockForm() {
  moveToAnotherStockForm.classList.remove("was-validated");
  [...document.querySelectorAll(".move-to-another-stock-quantity")].forEach(
    (el) => (el.value = ""),
  );
  $(".stock-select").selectpicker("val", "");
}

function setMoveToAnotherStockModalContent() {
  renderSelectedProducts();

  [...document.querySelectorAll(".move-to-another-stock-delete-btn")].forEach((btn) => {
    btn.onclick = () => {
      const productId = btn.dataset.productId;
      selectedProducts = selectedProducts.filter((product) => product.id !== productId);
      const checkbox = document.querySelector(
        `.select-product-checkbox[data-product-id="${productId}"]`,
      );

      if (checkbox) {
        checkbox.checked = false;
      }

      moveToAnotherStockTableBody.querySelector(`tr[data-product-id="${productId}"]`).remove();

      if (selectedProducts.length <= 0) {
        $("#move-to-another-stock-modal").modal("hide");
        moveToAnotherStockButton.style.display = "none";
        return;
      }
    };
  });
}

function renderSelectedProducts() {
  moveToAnotherStockTableBody.innerHTML = selectedProducts
    .map((product) => {
      return `
      <tr data-product-id="${product.id}">
        <input type="hidden" name="product-id" value="${product.id}" />
        <td>${product.name}</td>
        <td>${product.expiredDate}</td>
        <td>${product.currentPosition}</td>
        <td>${product.quantity}</td>
        <td>
          <input type="number" class="form-control move-to-another-stock-quantity" name="quantity" min="1" max="${product.quantity}" required />
          <div class="invalid-feedback">A quantity must be entered and has a minimum value of 1, maximum value of ${product.quantity}.</div>
        </td>
        <td class="text-center">
          <button type="button" class="btn btn-danger move-to-another-stock-delete-btn" data-product-id="${product.id}">
            <i class="bi bi-trash-fill"></i>
          </button>
        </td>
      </tr>
    `;
    })
    .join("");
}

function validateMoveToAnotherStockForm(e) {
  if (!moveToAnotherStockForm.checkValidity()) {
    e.preventDefault();
    e.stopPropagation();
    moveToAnotherStockForm.classList.add("was-validated");
    return;
  }
}

function resetMoveToAnotherStockForm() {
  moveToAnotherStockForm.classList.remove("was-validated");
  [...document.querySelectorAll(".move-to-another-stock-quantity")].forEach(
    (el) => (el.value = ""),
  );
  $(".stock-select").selectpicker("val", "");
}
