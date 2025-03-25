document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector(".search-form");

    form.addEventListener("submit", function (event) {
        let input = form.querySelector(".search-input");
        let nameValue = input.value.trim();

        if (nameValue === "") {
            event.preventDefault();

            const url = new URL(form.action, window.location.origin);
            window.location.href = url.toString();
        }
    });
});
