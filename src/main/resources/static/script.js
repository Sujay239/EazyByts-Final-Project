const API_BASE = 'http://localhost:8080/api/news';

// Fetch and display news articles

async function loadNews(category = 'general') {
    const categoryDiv = document.getElementById('current-category');
    categoryDiv.textContent = category.charAt(0).toUpperCase() + category.slice(1);
    try {
        const response = await fetch(`${API_BASE}/category/${category}`);
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        const data = await response.json();

        // Ensure data is an array
        const articles = Array.isArray(data) ? data : [];
        if (articles.length === 0) {
            try{
                const responseAll = await fetch(`${API_BASE}/fetch/${category}`, { method: 'POST' });
                if (responseAll.ok) {
                    const dataAll = await responseAll.json();
                    if (Array.isArray(dataAll) && dataAll.length > 0) {
                        articles.push(...dataAll); // add fetched articles
                    }
                }
            }catch (error) {
                console.warn(`No articles found for category: ${category}`);
                document.getElementById('news-grid').innerHTML = '<div class="col-12"><p class="alert alert-info">No news available. Try fetching data from backend.</p></div>';
                return;
            }
        }

        const grid = document.getElementById('news-grid');

        grid.innerHTML = '';
        articles.forEach(article => {
            const card = `
                <div class="col-md-6 col-lg-4 news-cards">
                    <div class="card article-card fade-in">
                        ${article.imageUrl ? `<img src="${article.imageUrl}" class="card-img-top" alt="${article.title}" onerror="this.style.display='none'">` : ''}
                        <div class="card-body">
                            <h5 class="card-title">${article.title}</h5>
                            <p class="card-text">${(article.content || '').substring(0, 150)}...</p>
                            <a href="${article.url || '#'}" target="_blank" class="btn btn-primary">Read More</a>
                            <small class="text-muted d-block">${article.source || 'Unknown'} - ${article.publishedAt ? new Date(article.publishedAt).toLocaleDateString() : 'N/A'}</small>
                        </div>
                    </div>
                </div>
            `;
            grid.innerHTML += card;
        });
        const newsCards = document.querySelectorAll(".news-cards"); // select all cards
        newsCards.forEach(card => {
            if(localStorage.getItem("theme") === "dark"){
                card.classList.add("dark-mode");
            } else {
                card.classList.remove("dark-mode");
            }
        });

    } catch (error) {
        console.error('Error fetching news:', error);
        const grid = document.getElementById('news-grid');
        grid.innerHTML = `<div class="col-12"><p class="alert alert-danger">Failed to load news: ${error.message}. Check backend console and ensure API key is set.</p></div>`;
    }

    //dark mode implementation

}

// Load initial news
window.onload = () => loadNews();
