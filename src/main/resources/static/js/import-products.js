const addProductButton = document.getElementById("addProduct");
const productList = document.getElementById("product-list");
const productTemplate = document.getElementById("import-template");
const positionTemplate = document.getElementById("position-template").content;
const productForm = document.querySelector(".import-form");
let targetProduct = null;

const preventDuplicatePositionChange = (selectedElement) => {
  const positionList = selectedElement.closest(".position-list");
  const positionSelects = positionList.querySelectorAll("select.position-select");
  const oldIndex = selectedElement.oldIndex;
  positionSelects.forEach((select) => {
    select.options[selectedElement.selectedIndex].disabled = true;
    select.options[oldIndex].disabled = oldIndex === 0;
  });
  selectedElement.oldIndex = selectedElement.selectedIndex;
  selectedElement.options[selectedElement.selectedIndex].disabled = false;
  $(positionList.querySelectorAll(".position-select")).selectpicker("refresh");
};

const preventDuplicatePositionRemove = (selectedElement, positionList) => {
  const positionSelects = positionList.querySelectorAll("select.position-select");
  positionSelects.forEach((select) => {
    select.options[selectedElement.selectedIndex].disabled = false;
  });
  $(positionList.querySelectorAll(".position-select")).selectpicker("refresh");
};

const preventDuplicatePositionAdd = (selectedElement, positionList) => {
  const positionSelects = positionList.querySelectorAll("select.position-select");
  positionSelects.forEach((select) => {
    selectedElement.options[select.selectedIndex].disabled = true;
  });
  selectedElement.oldIndex = 0;
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

const checkQuantity = (quantityValue) => {
  if (quantityValue === 0) {
    return "A quantity is required";
  } else if (!Number.isInteger(quantityValue)) {
    return "A quantity must be interger";
  } else if (quantityValue < 1) {
    return "Quantity cannot be less than 1";
  }
  return null;
};

const checkQuantityInput = (quantityInputs, totalQuantity) => {
  let sum = 0;
  const totalQuantityValue = Number(totalQuantity.value);
  const productItem = totalQuantity.closest(".product-item");
  const errorMessageSum = productItem.querySelector(".error-message-sum-quantity");
  const addPositionButton = productItem.querySelector(".add-position");
  for (q of quantityInputs) {
    const errorMessage = q.parentElement.querySelector(".error-message-quantity-input");
    const currMax = totalQuantityValue - sum;
    const currVal = Number(q.value);
    const message = checkQuantity(currVal);
    if (message != null) {
      errorMessage.textContent = message;
      errorMessage.style.removeProperty("display");
      return -1;
    }
    if (currMax < 0) {
      q.max = 0;
      errorMessage.textContent = "total quantity has been exceeded total quantity";
      errorMessage.style.display = "block";
    } else if (currMax === 0) {
      q.max = 0;
      errorMessage.textContent = "Total quantity is enough";
      errorMessage.style.display = "block";
    } else {
      q.max = currMax;
      errorMessage.textContent = "Quantity cannot be more than " + currMax;
      errorMessage.style.removeProperty("display");
    }
    sum += currVal;
  }
  const quantityCheck = totalQuantityValue - sum;
  if (quantityCheck > 0) {
    errorMessageSum.textContent =
      "Need to set the position for " + quantityCheck + " quantities left";
    errorMessageSum.style.display = "block";
    addPositionButton.style.display = "block";
  } else if (quantityCheck < 0) {
    errorMessageSum.textContent = "Product quantities do not match!";
    errorMessageSum.style.display = "block";
    addPositionButton.style.display = "none";
  } else {
    errorMessageSum.style.display = "none";
    addPositionButton.style.display = "none";
  }
  return quantityCheck;
};

const checkTotalQuantityPostionItem = () => {
  const productItems = document.querySelectorAll(".product-item");
  let check = true;
  productItems.forEach((p) => {
    const totalQuantity = p.querySelector("input.total-quantity");
    const quantityInputs = p.querySelectorAll("input.quantity-input");
    const quantityCheck = checkQuantityInput(quantityInputs, totalQuantity);
    if (quantityCheck != 0) {
      check = false;
    }
  });
  return check;
};

const loadDatatoProductItem = (selectElement) => {
  const selectedOption = selectElement.options[selectElement.selectedIndex];
  const productItem = selectElement.closest(".product-item");

  const providerNameElement = productItem.querySelector("div[name=providerName]");
  providerNameElement.textContent = selectedOption.getAttribute("data-providerName");

  const categoryElement = productItem.querySelector("div[name=category]");
  categoryElement.textContent = selectedOption.getAttribute("data-category");

  const unitElement = productItem.querySelector("div[name=unit]");
  unitElement.textContent = selectedOption.getAttribute("data-unit");
};

addProductButton?.addEventListener("click", () => {
  const newProduct = document.importNode(productTemplate.content, true);
  $(newProduct.querySelector(".product-select")).selectpicker();
  productList.appendChild(newProduct);
  document.querySelector(".error-message-product-length").style.removeProperty("display");
});

productList.addEventListener("click", (event) => {
  if (event.target.matches(".remove-product")) {
    targetProduct = event.target.closest(".product-item");
    if (targetProduct) {
      targetProduct.remove();
      targetProduct = null;
    }
  }

  if (event.target.matches(".add-position-button")) {
    const positionList = event.target.closest(".position-list");
    const newPosition = document.importNode(positionTemplate, true);
    const addButton = event.target.closest(".add-position");
    const totalQuantity = positionList
      .closest(".product-item")
      .querySelector("input.total-quantity");
    const quantityInputs = positionList.querySelectorAll("input.quantity-input");
    const checkQuantity = checkQuantityInput(quantityInputs, totalQuantity);
    const quantityInput = newPosition.querySelector("input.quantity-input");
    if (checkQuantity > 0) {
      quantityInput.max = checkQuantity;
      quantityInput.value = checkQuantity;
    } else {
      quantityInput.max = 0;
    }

    const selectedElement = newPosition.querySelector("select.position-select");
    preventDuplicatePositionAdd(selectedElement, positionList);
    $(newPosition.querySelector(".position-select")).selectpicker();
    positionList.insertBefore(newPosition, addButton);
    addButton.style.display = "none";
    positionList.querySelector(".error-message-sum-quantity").style.removeProperty("display");
  }

  if (event.target.matches(".remove-position")) {
    const positionList = event.target.closest(".position-list");
    const position = event.target.closest(".position-item");
    const selectElement = position.querySelector("select.position-select");
    preventDuplicatePositionRemove(selectElement, positionList);
    position.remove();
    const totalQuantity = positionList
      .closest(".product-item")
      .querySelector("input.total-quantity");
    const quantityInputs = positionList.querySelectorAll("input.quantity-input");
    checkQuantityInput(quantityInputs, totalQuantity);
  }
});

productList.addEventListener("input", (event) => {
  if (event.target.classList.contains("total-quantity")) {
    const inputElement = event.target;
    const quantityValue = Number(inputElement.value);
    const productItem = inputElement.closest(".product-item");
    const positionList = productItem.querySelector(".position-list");
    const errorMessage = productItem.querySelector(".error-message-total-quantity");
    const message = checkQuantity(quantityValue);
    const quantityInputs = positionList.querySelectorAll("input.quantity-input");
    checkQuantityInput(quantityInputs, inputElement);
    errorMessage.textContent = message;
    if (message != null) {
      positionList.style.display = "none";
    } else {
      positionList.style.display = "block";
    }
  }
  if (event.target.classList.contains("quantity-input")) {
    const inputElement = event.target;
    const productItem = inputElement.closest(".product-item");
    const totalQuantity = productItem.querySelector("input.total-quantity");
    const positionList = inputElement.closest(".position-list");
    const quantityInputs = positionList.querySelectorAll("input.quantity-input");
    checkQuantityInput(quantityInputs, totalQuantity);
  }
});

productList.addEventListener("change", (event) => {
  if (event.target.classList.contains("product-select")) {
    const selectElement = event.target;
    loadDatatoProductItem(selectElement);
  }
  if (event.target.classList.contains("position-select")) {
    preventDuplicatePositionChange(event.target);
  }
});

productForm.addEventListener(
  "submit",
  (e) => {
    if (
      !productForm.checkValidity() ||
      !checkTotalQuantityPostionItem() ||
      !checkLengthProductItem()
    ) {
      e.preventDefault();
      e.stopPropagation();
      productForm.classList.add("was-validated");
    }
  },
  false,
);
