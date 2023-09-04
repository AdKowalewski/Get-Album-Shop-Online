var order_button = document.getElementById('order-button');
var cart_items = document.getElementById('cart-items');
var cart_total = document.getElementById('cart-total');

function set_total_price(cart){
    let total_price = 0;
    // let keys = Object.keys(cart);
    // keys.forEach((key) => {
    //     total_price += key.price * key.quantity;
    // });
    for(const key in cart){
        total_price += cart[key].price * cart[key].quantity;
    }
    console.log(total_price);
    sessionStorage.setItem("total-price", total_price);
    window.location.reload();
}

window.addEventListener("load", () => {
    if(sessionStorage.getItem('total-price') != null){
        document.getElementById('cart-total-price').innerText = `${Math.round(sessionStorage.getItem('total-price') * 100) / 100}.00 zł`;
        //cart_total.innerText = `Total: ${sessionStorage.getItem('total-price')}.00 zł`;
    } else {
        document.getElementById('cart-total-price').innerText = "0.00 zł";
    }
});

const fetch_product_by_id_cart = async (id) => {
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

var contains = (keys, item) => {
    for(var i = 0; i < keys.length; i++) {
        if (keys[i] == item) {
            return true;
        }
    }
    return false;
}
 
const new_cart_prod = async (prod_id) => {
    let cart = JSON.parse(sessionStorage.getItem('cart'));
    if(cart == null){
        cart = {}
    }
    //if(!contains(Object.keys(cart), prod_id)){
        //const prod = products.filter((p) => p.id == prod_id)[0]
        await fetch_product_by_id_cart(prod_id).then((data) => {
            cart[prod_id] = {'title': data.title, 'price': data.price, 'quantity': 1}
        });
    //} else {
    //    notify("error", "This album is already in cart!");
    //}
    sessionStorage.setItem('cart', JSON.stringify(cart));
    set_total_price(cart);
}

function add_cart(prod_id){
    console.log("add cart");
    prod_id = parseInt(prod_id);
    let cart = JSON.parse(sessionStorage.getItem('cart'));
    cart[prod_id].quantity += 1;
    if(cart[prod_id].quantity > 100) cart[prod_id].quantity = 100;
    sessionStorage.setItem('cart', JSON.stringify(cart));
    set_total_price(cart);
}

function sub_cart(prod_id){
    console.log("sub cart");
    prod_id = parseInt(prod_id);
    let cart = JSON.parse(sessionStorage.getItem('cart'));
    cart[prod_id].quantity -= 1
    if(cart[prod_id].quantity < 1) cart[prod_id].quantity = 1;
    sessionStorage.setItem('cart', JSON.stringify(cart));
    set_total_price(cart);
}

function delete_cart_prod(prod_id){
    let cart = JSON.parse(sessionStorage.getItem('cart'));
    delete cart[prod_id];
    //cart.remove(cart[prod_id]);
    //if(cart.length > 0){
        sessionStorage.setItem('cart', JSON.stringify(cart));
        set_total_price(cart);
    //} else {
    //    sessionStorage.setItem('cart', 'empty');
    //    window.location.reload();
    //}
}

function change_quantity(e, prod_id){
    e.preventDefault();
    const max = parseInt(e.target.max)
    const min = parseInt(e.target.min)
    if(e.target.value > max) {
        e.target.value = max;
    } else if(e.target.value < min) {
        e.target.value = min;
    }
    console.log("change cart");
    prod_id = parseInt(prod_id);
    let cart = JSON.parse(sessionStorage.getItem('cart'));
    cart[prod_id].quantity = Math.floor(e.target.value);
    sessionStorage.setItem('cart', JSON.stringify(cart));
    set_total_price(cart);
}

function goToProductPageCart(id){
    localStorage.setItem("product-id", id);
    window.location.href = "product-details.html";
}

var cart_item = (params) => {
    return `
    <tr>
        <td onclick="goToProductPageCart(${params.id})" style="cursor: pointer;">
            <figure class="image is-64x64">
                <img src="http://localhost:8080/images/item_${params.id}.jpg" alt="album cover">
            </figure>
            ${params.title}
        </td>
        <td></td>
        <td>
            <button class='button' onclick="sub_cart(${params.id})"><span class="icon">-</span></button>
            <input style="width:4em" size="4" class="input has-text-centered" type="number" min="1" max="100" step="1" value=${params.quantity} onchange="change_quantity(event, ${params.id})">
            <button class='button' onclick="add_cart(${params.id})"><span class="icon">+</span></button>
        </td>
        <td><button class="delete has-background-danger is-medium" onclick="delete_cart_prod(${params.id})"></button></td>
        <td>${params.price * params.quantity} zł</td>
    </tr>
    `
}

function draw_cart(cart){
    if(cart && Object.keys(cart).length > 0){
        cart_items.innerHTML = "";
        for (const key in cart) {
            cart_items.innerHTML += cart_item({'id':key, ...cart[key]});
        }
        cart_total.innerText = `Total: ${sessionStorage.getItem('total-price')}.00 zł`;
        //order_button.classList.remove("hidden");
        order_button.disabled = false;
    }else{
        cart_items.innerHTML = `<tr><td colspan="5"><b>Cart is empty</b></td></tr>`;
        //order_button.classList.add("hidden");
        order_button.disabled = true;
    }
}

draw_cart(JSON.parse(sessionStorage.getItem('cart')));

// if(sessionStorage.getItem('cart') != {}){
//     order_button.disabled = false;
// } else {
//     order_button.disabled = true;
// }

// else if(sessionStorage.getItem('user_id') != null && (sessionStorage.getItem('cart') == null || sessionStorage.getItem('cart') == {})) {
//     order_button.disabled = true;
// } else if(sessionStorage.getItem('user_id') == null && (sessionStorage.getItem('cart') == null || sessionStorage.getItem('cart') == {})){
//     order_button.disabled = true;
// }