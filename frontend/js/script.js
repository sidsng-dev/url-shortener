const API_BASE_URL = "http://localhost:8080";

const urlForm = document.getElementById("urlForm");
const urlInput = document.getElementById("urlInput");
const aliasInput = document.getElementById("aliasInput");

const shortenButton = document.getElementById("shortenButton");

const loading = document.getElementById("loading");
const errorMessage = document.getElementById("errorMessage");
const result = document.getElementById("result");

const shortUrl = document.getElementById("shortUrl");
const originalUrl = document.getElementById("originalUrl");
const shortCode = document.getElementById("shortCode");
const clickCount = document.getElementById("clickCount");

const copyButton = document.getElementById("copyButton");
const statsButton = document.getElementById("statsButton");


let currentShortCode = null;


// Create shortened URL
urlForm.addEventListener("submit", async (event) => {

    event.preventDefault();

    hideError();
    result.classList.add("hidden");

    const url = urlInput.value.trim();
    const customAlias = aliasInput.value.trim();

    if (!url) {
        showError("Please enter a URL.");
        return;
    }

    const requestBody = {
        url: url
    };

    if (customAlias) {
        requestBody.customAlias = customAlias;
    }

    setLoading(true);

    try {

        const response = await fetch(
            `${API_BASE_URL}/api/urls`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestBody)
            }
        );

        const data = await response.json();

        if (!response.ok) {
            throw new Error(
                data.message || "Failed to shorten URL."
            );
        }

        currentShortCode = data.shortCode;

        displayResult(data);

    } catch (error) {

        showError(
            error.message ||
            "Something went wrong. Please try again."
        );

    } finally {

        setLoading(false);
    }
});


// Display shortened URL
function displayResult(data) {

    const generatedShortUrl =
        data.shortUrl ||
        `${API_BASE_URL}/${data.shortCode}`;

    shortUrl.textContent = generatedShortUrl;
    shortUrl.href = generatedShortUrl;

    originalUrl.textContent = data.originalUrl;

    shortCode.textContent = data.shortCode;

    clickCount.textContent =
        data.clickCount ?? 0;

    result.classList.remove("hidden");
}


// Copy shortened URL
copyButton.addEventListener("click", async () => {

    if (!shortUrl.textContent) {
        return;
    }

    try {

        await navigator.clipboard.writeText(
            shortUrl.textContent
        );

        const originalText = copyButton.textContent;

        copyButton.textContent = "Copied!";

        setTimeout(() => {
            copyButton.textContent = originalText;
        }, 1500);

    } catch (error) {

        showError(
            "Unable to copy the URL."
        );
    }
});


// Refresh statistics
statsButton.addEventListener("click", async () => {

    if (!currentShortCode) {
        return;
    }

    try {

        statsButton.disabled = true;
        statsButton.textContent = "Loading...";

        const response = await fetch(
            `${API_BASE_URL}/api/urls/${encodeURIComponent(currentShortCode)}/stats`
        );

        const data = await response.json();

        if (!response.ok) {
            throw new Error(
                data.message || "Unable to fetch statistics."
            );
        }

        clickCount.textContent =
            data.clickCount ?? 0;

    } catch (error) {

        showError(
            error.message ||
            "Unable to fetch statistics."
        );

    } finally {

        statsButton.disabled = false;
        statsButton.textContent = "Refresh Statistics";
    }
});


// Loading state
function setLoading(isLoading) {

    if (isLoading) {

        loading.classList.remove("hidden");

        shortenButton.disabled = true;
        shortenButton.textContent = "Creating...";

    } else {

        loading.classList.add("hidden");

        shortenButton.disabled = false;
        shortenButton.textContent = "Shorten URL";
    }
}


// Show error
function showError(message) {

    errorMessage.textContent = message;
    errorMessage.classList.remove("hidden");
}


// Hide error
function hideError() {

    errorMessage.textContent = "";
    errorMessage.classList.add("hidden");
}