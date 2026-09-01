const passwordInput = document.getElementById("password");
const togglePassword = document.getElementById("togglePw");

if (passwordInput && togglePassword) {
    togglePassword.addEventListener("mouseenter", () => {
        passwordInput.type = "text";
        togglePassword.setAttribute("aria-label", "Hide password");
    });

    togglePassword.addEventListener("mouseleave", () => {
        passwordInput.type = "password";
        togglePassword.setAttribute("aria-label", "Show password");
    });
}