(function () {
    const url = new URL(window.location.href);

    const actualPage = window.actualPageFromServer;

    const currentPageInUrl = url.searchParams.get("page");
    if (actualPage && currentPageInUrl !== String(actualPage)) {
        url.searchParams.set("page", actualPage);
        history.replaceState(null, "", url);
    }
})();
