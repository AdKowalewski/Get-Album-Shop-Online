const fetch_product_by_id = async (id) => {
    try{
        const response = await fetch(`http://127.0.0.1:8080/api/v1/product/${id}`, {
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

const album_name_det = document.getElementById('album-name-details');
const album_img_det = document.getElementById('album-img-details');
const album_artist_det = document.getElementById('album-artist-details');
const album_label_det = document.getElementById('album-label-details');
const album_year_det = document.getElementById('album-year-details');
const album_price_det = document.getElementById('album-price-details');
const add_to_cart = document.getElementById('add-to-cart');

function goToProductPage(id){
    localStorage.setItem("product-id", id);
    window.location.href = "product-details.html";
}

window.addEventListener("load",() => {
    fetch_product_by_id(localStorage.getItem("product-id")).then((data) => {
        album_name_det.innerText = data.title + " - details";
        album_img_det.innerHTML = `<img class="" src="http://localhost:8080/images/item_${localStorage.getItem("product-id")}.jpg" width="340" height="340" alt="album cover">`;
        album_artist_det.innerText = data.artist;
        album_label_det.innerText = "Label: " + data.label;
        album_year_det.innerText = "Released in: " + data.releaseYear;
        album_price_det.innerText = "Price: " + data.price + " zł";
    });
});

function cartContains(cart, prod_id){
    for(let key in cart){
        if(key == prod_id){
            return true;
        }
    }
    return false;
}

add_to_cart.onclick = () => {
    if(sessionStorage.getItem('user_id') != null){
        let prod_id = localStorage.getItem('product-id');
        let cart = JSON.parse(sessionStorage.getItem('cart'));
        if(cart){
            if(cartContains(cart, prod_id)){
                add_cart(localStorage.getItem('product-id'));
                notify("success","Quantity of this item has been increased");
            } else {
                new_cart_prod(localStorage.getItem("product-id"));
                notify("success","Product has been added to cart");
            }
        } else {
            new_cart_prod(localStorage.getItem("product-id"));
            notify("success","Product has been added to cart");
        }
    } else {
        notify("error", "You must log in!");
    }
}

function removeIdFromLocalStorage(){
    localStorage.removeItem("product-id");
    window.location.href = "index.html";
}

// function isInCart(prod_id, cart_keys){
//     return cart_keys.includes(prod_id);
// }

// if(Object.keys(sessionStorage.getItem('cart')).includes(localStorage.getItem('product-id'))){
//     add_to_cart.disabled = true;
// } else if (!Object.keys(sessionStorage.getItem('cart')).includes(localStorage.getItem('product-id'))) {
//     add_to_cart.disabled = false;
// }