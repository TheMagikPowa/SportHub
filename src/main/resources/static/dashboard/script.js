document.addEventListener('DOMContentLoaded', () => {
    // ----------------------------------------------------
    // INIZIALIZZAZIONE DATI DASHBOARD
    // ----------------------------------------------------
    initDashboard();

    // Event listener per il pulsante di logout
    const logoutBtn = document.getElementById('btn-logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', handleLogout);
    }
});

function initDashboard() {
    // Recupero dati utente da LocalStorage (o fallback predefiniti)
    const username = localStorage.getItem('sh_username') || 'Utente';
    const activeOrdersCount = localStorage.getItem('sh_active_orders') || '2';
    const totalPostsCount = localStorage.getItem('sh_user_posts') || '5';
    const totalQnaCount = localStorage.getItem('sh_user_qna') || '3';

    // Popolamento Header Benvenuto
    const userGreeting = document.getElementById('user-greeting');
    if (userGreeting) {
        userGreeting.textContent = `Bentornato, ${username}!`;
    }

    // Popolamento Contatori KPI
    const ordersEl = document.getElementById('kpi-orders');
    const postsEl = document.getElementById('kpi-posts');
    const qnaEl = document.getElementById('kpi-qna');

    if (ordersEl) ordersEl.textContent = activeOrdersCount;
    if (postsEl) postsEl.textContent = totalPostsCount;
    if (qnaEl) qnaEl.textContent = totalQnaCount;

    // Caricamento Attività Recenti
    loadRecentActivities();
}

// ----------------------------------------------------
// POPOLAMENTO ATTIVITÀ RECENTI
// ----------------------------------------------------
function loadRecentActivities() {
    const activityList = document.getElementById('recent-activity-list');
    if (!activityList) return;

    // Esempio dati attività recenti
    const activities = [
        { type: 'order', title: 'Ordine #1048 confermato', date: 'Oggi, 14:30' },
        { type: 'post', title: 'Hai pubblicato un nuovo post nell\'Hub', date: 'Ieri, 18:15' },
        { type: 'qna', title: 'Risposta ricevuta nella sezione Q&A', date: '20 Ago 2026' }
    ];

    activityList.innerHTML = activities.map(act => `
        <li class="activity-item">
            <span class="activity-badge badge-${act.type}">${act.type.toUpperCase()}</span>
            <div class="activity-info">
                <p class="activity-title">${act.title}</p>
                <small class="activity-date">${act.date}</small>
            </div>
        </li>
    `).join('');
}

// ----------------------------------------------------
// LOGOUT
// ----------------------------------------------------
function handleLogout(e) {
    e.preventDefault();
    if (confirm('Sei sicuro di voler effettuare il logout?')) {
        // Rimuove la sessione utente corrente
        localStorage.removeItem('sh_logged_in');
        window.location.href = '../login/login.html';
    }
}