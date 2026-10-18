const registerForm       = document.getElementById('registerForm');
const registerFormResult = document.getElementById('registerFormResult');

if (registerForm) registerForm.addEventListener( 'submit', async(event) =>
{
    event.preventDefault();

    const submitButton = registerForm.querySelector('button[type="submit"]');
    const originalContent = submitButton.innerHTML;
    submitButton.disabled = true;
    submitButton.classList.add('is-loading');
    submitButton.textContent = 'Création du compte…';
    registerForm.setAttribute('aria-busy', 'true');
    registerFormResult.textContent = '';

    const formData  = new FormData(event.target);
    const objetData = Object.fromEntries(formData.entries());
    const jsonForm  = JSON.stringify(objetData);

    try 
    {
        const dataFetch = await fetch('/register', 
        {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: jsonForm
        });

        const response = await dataFetch.text();
        registerFormResult.textContent = response;

        /*if (dataFetch.ok && response) 
        {
            window.location.assign("login.html");
        }*/
    } catch (error) {
        registerFormResult.textContent = "Une erreur est survenue. Réessayez.";
        console.error("Registration failed:", error);
    } finally {
        submitButton.disabled = false;
        submitButton.classList.remove('is-loading');
        submitButton.innerHTML = originalContent;
        registerForm.removeAttribute('aria-busy');
    }
});
