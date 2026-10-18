const internshipStorageKey = 'stageflow.internships.v1';
const internshipForm = document.getElementById('internshipForm');
const formPanel = document.getElementById('internshipFormPanel');
const internshipList = document.getElementById('internshipList');
const emptyState = document.getElementById('emptyState');
const statusFilter = document.getElementById('statusFilter');

// Load the saved internships when the dashboard opens.
let internships = [];

try {
    internships = JSON.parse(localStorage.getItem(internshipStorageKey) || '[]');
    
    if (!Array.isArray(internships)) 
        internships = [];
    } catch (error) {
    console.error('Could not read saved internships:', error);
    }

// Keep the list in this browser so it is still here after a refresh
function saveInternships()
{
    try {
        localStorage.setItem(internshipStorageKey, JSON.stringify(internships));
        return true;
    } catch (error) {
        console.error('Could not save internships:', error);
        return false;
    }
}

// Create text elements safely before adding them to the page
function addText(parent, tagName, className, text)
{
    const element = document.createElement(tagName);
    element.className = className;
    element.textContent = text;
    // Add it to the parent element passed into this function.
    parent.appendChild(element);
    return element;
}

// Draw the list again when an internship is added, removed, or filtered
function renderInternships()
{
    // Clear the previous rows so the page can show the latest list
    internshipList.replaceChildren();

    // Only show internships that match the selected status
    const selectedStatus = statusFilter.value;
    let visibleCount = 0;

    internships.forEach(function (internship)
    {
        // Skip this internship if it does not match the selected status.
        if (selectedStatus !== 'all' && internship.status !== selectedStatus) 
            return;

        visibleCount++;

        // Build one row with the company, role, location, date, and status
        const row = document.createElement('article');
        row.className = 'internship-row';

        const company = document.createElement('div');
        company.className = 'company-heading';
        addText(company, 'span', 'company-avatar', internship.company.charAt(0).toUpperCase());

        const details = document.createElement('div');
        // Add the company and role inside the details container.
        addText(details, 'h3', '', internship.company);
        addText(details, 'p', '', internship.role);
        company.appendChild(details);
        row.appendChild(company);

        const place = [internship.location, internship.workMode].filter(Boolean).join(' · ');
        addText(row, 'span', 'row-location', place || 'Lieu non précisé');
        addText(row, 'span', 'row-date', internship.applicationDate || 'Date non précisée');

        const badge = addText(row, 'span', 'status-badge', internship.status);
        // The CSS uses this value to give some statuses a different color.
        badge.dataset.status = internship.status;

        const deleteButton = document.createElement('button');
        deleteButton.type = 'button';
        deleteButton.className = 'delete-internship';
        deleteButton.textContent = 'x';
        deleteButton.setAttribute('aria-label', 'Supprimer la candidature chez ' + internship.company);
        // Attach a click action to this row's delete button.
        deleteButton.addEventListener('click', function ()
        {
            if(confirm("Êtes-vous sûr de vouloir supprimer ce champ"))
            {
                // Remove this internship, save the change, and redraw the list
                internships = internships.filter(function (item)
                {
                    return item.id !== internship.id;
                });
                saveInternships();
                renderInternships();
            }
            else
            {
                return;
            }
        });

        row.appendChild(deleteButton);
        internshipList.appendChild(row);
    });

    // Show the empty message only when there are no saved internships
    emptyState.hidden = internships.length > 0;
    if (internships.length > 0 && visibleCount === 0)
    {
        addText(internshipList, 'p', 'no-filter-results', 'Aucune candidature avec ce statut.');
    }

    // Update the summary numbers shown above the list
    document.getElementById('totalCount').textContent = internships.length;
    document.getElementById('activeCount').textContent = internships.filter(function (item)
    {
        return item.status !== 'Refusée' && item.status !== 'Acceptée';
    }).length;
    document.getElementById('interviewCount').textContent = internships.filter(function (item)
    {
        return item.status === 'Entretien';
    }).length;
}

// Show the form and put the cursor in its first field
function openInternshipForm()
{
    formPanel.hidden = false;
    document.getElementById('company').focus();
}

// Hide the form and return focus to the add button
function closeInternshipForm()
{
    formPanel.hidden = true;
    document.getElementById('openInternshipForm').focus();
}

// Connect the dashboard buttons and status filter to their actions.
document.getElementById('openInternshipForm').addEventListener('click', openInternshipForm);
document.getElementById('emptyAddButton').addEventListener('click', openInternshipForm);
document.getElementById('closeInternshipForm').addEventListener('click', closeInternshipForm);
document.getElementById('cancelInternshipForm').addEventListener('click', closeInternshipForm);
statusFilter.addEventListener('change', renderInternships);

// Read the form, save the new internship, and refresh the dashboard
internshipForm.addEventListener('submit', function (event)
{
    event.preventDefault();

    // Collect the form fields and add a simple ID for later removal
    const formData   = new FormData(event.target);
    const internship = Object.fromEntries(formData.entries());

    internship.id = Date.now().toString();
    internships.unshift(internship);

    // If browser storage is unavailable, undo the addition and keep the form open
    if (!saveInternships())
    {
        internships.shift();
        window.alert('Impossible d’enregistrer dans le stockage du navigateur.');
        return;
    }

    // Clear and close the form, then show the updated dashboard
    internshipForm.reset();
    statusFilter.value = 'all';
    closeInternshipForm();
    renderInternships();
});

renderInternships();
