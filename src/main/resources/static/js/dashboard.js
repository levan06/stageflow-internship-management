const internshipStorageKey = 'stageflow.internships.v1';
const internshipForm = document.getElementById('internshipForm');
const formPanel = document.getElementById('internshipFormPanel');
const internshipList = document.getElementById('internshipList');
const emptyState = document.getElementById('emptyState');
const statusFilter = document.getElementById('statusFilter');

let internships = [];

try {
    internships = JSON.parse(localStorage.getItem(internshipStorageKey) || '[]');
    if (!Array.isArray(internships)) internships = [];
} catch (error) {
    console.error('Could not read saved internships:', error);
}

function saveInternships() {
    try {
        localStorage.setItem(internshipStorageKey, JSON.stringify(internships));
        return true;
    } catch (error) {
        console.error('Could not save internships:', error);
        return false;
    }
}

function addText(parent, tagName, className, text) {
    const element = document.createElement(tagName);
    element.className = className;
    element.textContent = text;
    parent.appendChild(element);
    return element;
}

function renderInternships() {
    internshipList.replaceChildren();

    const selectedStatus = statusFilter.value;
    let visibleCount = 0;

    internships.forEach(function (internship) {
        if (selectedStatus !== 'all' && internship.status !== selectedStatus) return;
        visibleCount++;

        const row = document.createElement('article');
        row.className = 'internship-row';

        const company = document.createElement('div');
        company.className = 'company-heading';
        addText(company, 'span', 'company-avatar', internship.company.charAt(0).toUpperCase());

        const details = document.createElement('div');
        addText(details, 'h3', '', internship.company);
        addText(details, 'p', '', internship.role);
        company.appendChild(details);
        row.appendChild(company);

        const place = [internship.location, internship.workMode].filter(Boolean).join(' · ');
        addText(row, 'span', 'row-location', place || 'Lieu non précisé');
        addText(row, 'span', 'row-date', internship.applicationDate || 'Date non précisée');

        const badge = addText(row, 'span', 'status-badge', internship.status);
        badge.dataset.status = internship.status;

        const deleteButton = document.createElement('button');
        deleteButton.type = 'button';
        deleteButton.className = 'delete-internship';
        deleteButton.textContent = '×';
        deleteButton.setAttribute('aria-label', 'Supprimer la candidature chez ' + internship.company);
        deleteButton.addEventListener('click', function () {
            internships = internships.filter(function (item) {
                return item.id !== internship.id;
            });
            saveInternships();
            renderInternships();
        });

        row.appendChild(deleteButton);
        internshipList.appendChild(row);
    });

    emptyState.hidden = internships.length > 0;
    if (internships.length > 0 && visibleCount === 0) {
        addText(internshipList, 'p', 'no-filter-results', 'Aucune candidature avec ce statut.');
    }

    document.getElementById('totalCount').textContent = internships.length;
    document.getElementById('activeCount').textContent = internships.filter(function (item) {
        return item.status !== 'Refusée' && item.status !== 'Acceptée';
    }).length;
    document.getElementById('interviewCount').textContent = internships.filter(function (item) {
        return item.status === 'Entretien';
    }).length;
}

function openInternshipForm() {
    formPanel.hidden = false;
    document.getElementById('company').focus();
}

function closeInternshipForm() {
    formPanel.hidden = true;
    document.getElementById('openInternshipForm').focus();
}

document.getElementById('openInternshipForm').addEventListener('click', openInternshipForm);
document.getElementById('emptyAddButton').addEventListener('click', openInternshipForm);
document.getElementById('closeInternshipForm').addEventListener('click', closeInternshipForm);
document.getElementById('cancelInternshipForm').addEventListener('click', closeInternshipForm);
statusFilter.addEventListener('change', renderInternships);

internshipForm.addEventListener('submit', function (event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const internship = Object.fromEntries(formData.entries());
    internship.id = Date.now().toString();
    internships.unshift(internship);

    if (!saveInternships()) {
        internships.shift();
        window.alert('Impossible d’enregistrer dans le stockage du navigateur.');
        return;
    }

    internshipForm.reset();
    statusFilter.value = 'all';
    closeInternshipForm();
    renderInternships();
});

renderInternships();
