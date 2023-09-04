const fetch_all_products = async () => {
    try{
        const response = await fetch(`http://127.0.0.1:8080/api/v1/product`, {
            'method':'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return Promise.resolve(await response.json());
    }catch(e){
        return Promise.reject(e);
    }
}

const fetch_product_by_title = async (title) => {
    try{
        const response = await fetch(`http://127.0.0.1:8080/api/v1/product/title/${title}`, {
            'method':'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return Promise.resolve(await response.json());
    }catch(e){
        return Promise.reject(e);
    }
}

const fetch_products_by_artist = async (artist) => {
    try{
        console.log(artist)
        const response = await fetch(`http://127.0.0.1:8080/api/v1/product/artist/${artist}`, {
            'method':'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return Promise.resolve(await response.json());
    }catch(e){
        return Promise.reject(e);
    }
}

const fetch_products_by_title_or_artist = async (titleOrArtist) => {
    try{
        console.log(titleOrArtist)
        const response = await fetch(`http://127.0.0.1:8080/api/v1/product/titleOrArtist/${titleOrArtist}`, {
            'method':'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return Promise.resolve(await response.json());
    }catch(e){
        return Promise.reject(e);
    }
}

const fetch_products_by_genre = async (genre) => {
    try{
        const response = await fetch(`http://127.0.0.1:8080/api/v1/product/genre/${genre}`, {
            'method':'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return Promise.resolve(await response.json());
    }catch(e){
        return Promise.reject(e);
    }
}

const update_product_list = () => {
    const product_list = document.getElementById("product-list");
    product_list.innerHTML = "";
    console.log(products);
    if(products.length != 0){
        products.forEach((p)=>{
            product_list.innerHTML += card(p);
        })

        function place_products(){
            let i = 0;
            const anim = setInterval(()=>{
                if(i < product_list.children.length){
                    product_list.children[i].classList.toggle('shrink');
                }
                else{
                    clearInterval(anim);
                }
                i++;
            }, 50)
        }
        
        place_products();
    } else {
        product_list.innerHTML += no_such_products_info();
    }
}

//const search_title = document.getElementById("search-title");
//const search_artist = document.getElementById("search-artist");
const search_button = document.getElementById("search-button");

search_button.onclick = async () => {
    let input = document.getElementById("search-value").value;
    if(input == "" || input == null){
        products = await fetch_all_products();
    } else {
        products = await fetch_products_by_title_or_artist(input);
    }
    update_product_list();
}

async function searchOnEnter(e, element){
    if(e.key === 'Enter'){
        e.preventDefault();
        let input = element.value;
        if(input == "" || input == null){
            products = await fetch_all_products();
        } else {
            products = await fetch_products_by_title_or_artist(input);
        }
        update_product_list();
    }
}

search_button.addEventListener("keypress", async (e) => {
    if(e.key === "Enter"){
        e.preventDefault();
        let input = document.getElementById("search-value").value;
        if(input == "" || input == null){
            products = await fetch_all_products();
        } else {
            products = await fetch_products_by_title_or_artist(input);
        }
        update_product_list();
    }
})

const genre_bar = document.getElementById("dropdown-menu");
const genres = genre_bar.querySelectorAll("a");

genres.forEach((g) => 
    g.onclick = async () => {
        products = await fetch_products_by_genre(g.getAttribute("id"));
        update_product_list();
    }
)

window.onload = async () => {
    products = await fetch_all_products();
    update_product_list();
}

var no_such_products_info = () => {
    return `
        <p class="title is-5">There are no such products</p>
    `;
}

var card = (params) => {
    return `
    <div class="card scale shrink" dataset-prod-id="${params.id}" onclick="goToProductPage(${params.id})">
        <div class="card-image" style="margin:1em;">
            <figure class="image">
                <img src="http://localhost:8080/images/item_${params.id}.jpg" alt="album cover">
            </figure>
        </div>
        <hr style="margin: 0px;">
        <div class="card-content">
            <p class="title is-5">${params.title}</p>
            <p class="title is-6">${params.artist}</p>
            <p class="subtitle is-5">${(params.price == null ? 37.21 : params.price)} zł</p>
        </div>
    </div>
    `;
}