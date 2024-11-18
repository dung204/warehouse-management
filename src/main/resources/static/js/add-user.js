const addUserForm = document.querySelector("#add-user-form");

addUserForm.addEventListener("submit", (e) => {
  if (!addUserForm.checkValidity()) {
    e.preventDefault();
    e.stopPropagation();
    addUserForm.classList.add("was-validated");
  }
});
