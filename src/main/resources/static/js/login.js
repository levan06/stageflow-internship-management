const loginForm = document.getElementById('loginForm');
const loginFormResult = document.getElementById('loginFormResult');

if (loginForm) {
loginForm.addEventListener( 'submit', async(event) =>
{
    event.preventDefault();

    const formData  = new FormData(event.target);
    const objetData = Object.fromEntries(formData.entries());
    const jsonForm  = JSON.stringify(objetData);

    try 
    {
        const dataFetch = await fetch('/login',
        {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: jsonForm
        });

        const response = await dataFetch.text();
        loginFormResult.textContent = response;

        if( dataFetch.ok && response.trim() === "Valid Account" )
        {
            window.location.replace("/dashboard");
        }
    } catch (error) {
        loginFormResult.textContent = "Une erreur est survenue. Réessayez.";
        console.error("Registration failed:", error);
    }
});


}

/* logout Part */
const logoutBtn = document.getElementById('logoutBtn');

if (logoutBtn) {
    logoutBtn.addEventListener("click", async () =>
    {
        logoutBtn.disabled = true;

        try {
            const csrfToken = document.querySelector(
                'meta[name="csrf-token"]'
            )?.content;
            
            if (!csrfToken) 
            {
                throw new Error("CSRF token is missing");
            }

            const response = await fetch("/logout",
            {
                method: "POST",

                // (JSESSIONID) accompanies the fetch request to the backend.
                credentials: "same-origin",

                headers: { "X-CSRF-TOKEN": csrfToken }
            });

            if (!response.ok) {
                throw new Error("Logout request failed");
            }

            window.location.replace("/login.html");
        } catch (error) {
            logoutBtn.disabled = false;
            console.error("Logout failed : ", error);
        }
    });
}
