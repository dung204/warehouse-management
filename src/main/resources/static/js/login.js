const loginForm = document.querySelector("form#login-form");

loginForm.addEventListener("submit", (e) => {
  if (!loginForm.checkValidity()) {
    e.preventDefault();
    e.stopPropagation();
    loginForm.classList.add("was-validated");
  }
});
