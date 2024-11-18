document.addEventListener("DOMContentLoaded", () => {
  const addProductButton = document.getElementById("addProduct");
  const productList = document.getElementById("product-list");
  const productTemplate = document.getElementById("product-template").content;
  const productForm = document.querySelector(".product-form");
  let targetProduct = null;

  addProductButton.addEventListener("click", () => {
    const newProduct = document.importNode(productTemplate, true);
    $(newProduct.querySelector(".product-select")).selectpicker();
    productList.appendChild(newProduct);
    checkLengthProductItem();
    preventDuplicateProduct();
  });

  productList.addEventListener("click", (event) => {
    if (event.target.matches(".remove-product")) {
      targetProduct = event.target.closest(".product-item");
      $("#confirmModal").modal("show");
    }
  });

  document.getElementById("confirmDeleteButton").addEventListener("click", () => {
    if (targetProduct) {
      targetProduct.remove();
      targetProduct = null;
      preventDuplicateProduct();
    }
    $("#confirmModal").modal("hide");
  });

  $("#confirmModal").on("shown.bs.modal", () => {
    document.addEventListener("keydown", handleModalKeydown);
  });

  $("#confirmModal").on("hidden.bs.modal", () => {
    document.removeEventListener("keydown", handleModalKeydown);
    targetProduct = null;
  });

  function handleModalKeydown(event) {
    if (event.key === "Enter") {
      document.getElementById("confirmDeleteButton").click();
    }
  }

  const preventDuplicateProduct = () => {
    const productSelects = document.querySelectorAll("select.product-select");
    const selectedValueSet = new Set();
    productSelects.forEach((select) => selectedValueSet.add(select.value));
    productSelects.forEach((selectElement) => {
      for (const option of selectElement.options) {
        option.disabled = selectedValueSet.has(option.value);
      }
      selectElement.options[selectElement.selectedIndex].disabled = false;
      selectElement.options[0].disabled = true;
      $(selectElement.closest(".product-select")).selectpicker("refresh");
    });
  };

  const checkLengthProductItem = () => {
    const productItems = document.querySelectorAll(".product-item");
    const errorMessage = document.querySelector(".error-message-product-length");
    if (productItems.length === 0) {
      errorMessage.style.display = "block";
    } else {
      errorMessage.style.removeProperty("display");
    }
    return productItems.length > 0;
  };

  const checkQuantityProduct = (inputElement) => {
    const productItem = inputElement.closest(".product-item");
    const maxQuantity = Number(inputElement.max);
    const quantityValue = Number(inputElement.value);
    const errorMessage = productItem.querySelector(".error-message-input");
    if (quantityValue === 0) {
      errorMessage.textContent = "A quantity is required";
    } else if (!Number.isInteger(quantityValue)) {
      errorMessage.textContent = "A quantity must be interger";
    } else if (quantityValue > maxQuantity && maxQuantity > 1) {
      errorMessage.textContent = "Quantity cannot be more than " + maxQuantity;
    } else if (quantityValue < 1) {
      errorMessage.textContent = "Quantity cannot be less than 1";
    }
  };

  const loadDatatoProductItem = (selectElement) => {
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const productItem = selectElement.closest(".product-item");
    const maxQuantity = selectedOption.getAttribute("data-quantity");
    const inputElement = productItem.querySelector("input[name=quantity]");
    inputElement.max = maxQuantity;
    checkQuantityProduct(inputElement);
    const providerNameElement = productItem.querySelector("div[name=providerName]");
    providerNameElement.textContent =
      "Provider Name: " + selectedOption.getAttribute("data-providerName");
    const maxQuantityElement = productItem.querySelector("div[name=maxQuantity]");
    maxQuantityElement.textContent = "Available quantity: " + maxQuantity;
    const unitElement = productItem.querySelector("div[name=unit]");
    unitElement.textContent = "Unit: " + selectedOption.getAttribute("data-unit");
  };

  document.querySelectorAll(".product-select").forEach((selectElement) => {
    loadDatatoProductItem(selectElement);
  });

  productList.addEventListener("change", (event) => {
    if (event.target.classList.contains("product-select")) {
      const selectElement = event.target;
      loadDatatoProductItem(selectElement);
      preventDuplicateProduct();
    }
  });

  productList.addEventListener("input", (event) => {
    if (event.target.classList.contains("quantity-input")) {
      const inputElement = event.target;
      checkQuantityProduct(inputElement);
    }
  });

  productForm.addEventListener(
    "submit",
    (e) => {
      if (!productForm.checkValidity() || !checkLengthProductItem()) {
        e.preventDefault();
        e.stopPropagation();
        productForm.classList.add("was-validated");
      }
    },
    false,
  );
});

$(document).ready(function () {
  $("#invoiceModal").modal("show");
});
