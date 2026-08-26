(function () {
      const grid = document.getElementById('productGrid');
      const cards = grid ? Array.from(grid.querySelectorAll('.prod-card')) : [];
      const filterBar = document.getElementById('filterBar');
      const sortSelect = document.getElementById('sortSelect');
      const resultCount = document.getElementById('resultCount');
      const emptyState = document.getElementById('storeEmpty');

      let activeFilter = 'all';

      function applyFilter() {
      let visibleCount = 0;

      cards.forEach(card => {
        const match =
          activeFilter === 'all' ||
          card.dataset.category === activeFilter;

        card.style.display = match ? '' : 'none';

        if (match) {
          visibleCount++;
        }
  });

  resultCount.textContent = visibleCount;

  if (emptyState) {
    emptyState.classList.toggle('show', visibleCount === 0);
  }

  if (grid) {
    grid.style.display = visibleCount === 0 ? 'none' : 'grid';
  }
}

      function applySort() {
        const value = sortSelect.value;
        const sorted = cards.slice().sort((a, b) => {
          if (value === 'price-asc') return parseFloat(a.dataset.price) - parseFloat(b.dataset.price);
          if (value === 'price-desc') return parseFloat(b.dataset.price) - parseFloat(a.dataset.price);
          if (value === 'name-asc') return a.dataset.name.localeCompare(b.dataset.name);
          return 0;
        });
        sorted.forEach(card => grid.appendChild(card));
      }

      filterBar.addEventListener('click', (e) => {
        const btn = e.target.closest('.filter-pill');
        if (!btn) return;
        filterBar.querySelectorAll('.filter-pill').forEach(p => p.classList.remove('active'));
        btn.classList.add('active');
        activeFilter = btn.dataset.filter;
        applyFilter();
      });

      sortSelect.addEventListener('change', applySort);

      applyFilter();
    })();