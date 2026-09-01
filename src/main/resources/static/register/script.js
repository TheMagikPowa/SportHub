function setupPasswordPreview(inputId, buttonId) {
    const passwordInput = document.getElementById(inputId);
    const toggleButton = document.getElementById(buttonId);

    if (!passwordInput || !toggleButton) {
        return;
    }

    toggleButton.addEventListener("mouseenter", () => {
        passwordInput.type = "text";
        toggleButton.setAttribute("aria-label", "Hide password");
    });

    toggleButton.addEventListener("mouseleave", () => {
        passwordInput.type = "password";
        toggleButton.setAttribute("aria-label", "Show password");
    });
}

setupPasswordPreview("signupPassword", "togglePwSignup");
setupPasswordPreview("repeatPassword", "togglePwRepeat");