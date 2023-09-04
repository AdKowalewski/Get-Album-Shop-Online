const address_form = document.getElementById('address-form');
const user_addresses = document.getElementById("user-addresses");
const order_items = document.getElementById('order-items');
const make_order_button = document.getElementById('make-order-button');

const fetch_create_address = async () => {
    const data = {
        "country" : address_form.elements['country'].value,
        "town" : address_form.elements['town'].value,
        "street" : address_form.elements['street'].value,
        "streetNumber" : address_form.elements['street-number'].value,
        "localNumber" : address_form.elements['local-number'].value,
        "postalCode" : address_form.elements['postal-code'].value,
        "clientId" : parseInt(sessionStorage.getItem('user_id'))
    }
    try{
        const response = await fetch(
            `http://localhost:8080/api/v1/address`,
            {
                'method':'POST',
                'body': JSON.stringify(data),
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': sessionStorage.getItem("user_token")
                },
            }
        );
        if(Math.floor(response.status / 100) == 2)
            return Promise.resolve(await response.json());
        return Promise.reject(await response.json());
    }catch(e){
        return Promise.reject({"message":e});
    }
}

const fetch_get_client_addresses = async () =>{
    const user_id = parseInt(sessionStorage.getItem("user_id"));
    try{
        const response = await fetch(
        `http://127.0.0.1:8080/api/v1/address/client/${user_id}`, 
        {
            'method':'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem("user_token")
            }
        });
        if(Math.floor(response.status / 100) == 2)
            return Promise.resolve(await response.json());
        return Promise.reject(await response.json());
    }catch(e){
        return Promise.reject({"message":e});
    }
}

const fetch_delete_address = async (address_id) => {
    try{
        const response = await fetch(
            `http://localhost:8080/api/v1/address/${address_id}`,
            {
                'method':'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': sessionStorage.getItem("user_token")
                }
            }
        );
        if(Math.floor(response.status/100) == 2)
            return Promise.resolve();
        return Promise.reject(await response.json());
    }catch(e){
        return Promise.reject({"message":e});
    }
}

const fetch_create_order = async () => {
    const client_id = parseInt(sessionStorage.getItem("user_id"));
    const cart = JSON.parse(sessionStorage.getItem("cart"));
    const products = {};
    for(let key in cart){
        products[key] = cart[key].quantity;
    }   
    let data = {
        "client" : {
            "id": client_id
        },
        "address" : {
            "id": user_addresses.elements['address'].value
        },
        "products" : products
    }
    
    try{
        const response = await fetch(
            `http://127.0.0.1:8080/api/v1/order`, 
            {
                'method':'POST',
                'body': JSON.stringify(data),
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': sessionStorage.getItem("user_token")
            }
        });
        if(Math.floor(response.status/100) == 2)
            return Promise.resolve(await response.json());
        return Promise.reject(await response.json());
    }catch(e){
        return Promise.reject({"message":e});
    }
}

address_form.addEventListener("submit", async function(e){
    e.preventDefault();
    fetch_create_address().then((data) => {
        address_form.reset();
        //user_addresses.querySelector("fieldset").insertAdjacentHTML('beforeend', address_card(data));
        user_addresses.querySelector('fieldset').innerHTML += address_card(data);
        user_addresses.querySelector("button").disabled = false;
        notify("success","New address added successfully");
    }).catch((e)=>{
        console.log(e);
        notify("error","Error: " + e.message);
    });
})

async function delete_address(e, address_id){
    await fetch_delete_address(address_id).then(()=>{
        e.preventDefault();
        e.stopImmediatePropagation();
        e.target.closest("[data-address]").remove();
        notify("success","Address was deleted");
        if(user_addresses.querySelector("fieldset").children.length == 0){
            user_addresses.querySelector("button").disabled = true;
            document.getElementById('client-addresses-title').innerText = "No addresses - You must add new address";
        }
    }).catch((e)=>{
        notify("error","Error: " + e.message)
    })
}

const address_card = (adr) => {
    if(adr.localNumber != 0){
        return `
        <div class="block" data-address>
            <input type="radio" name="address" value="${adr.id}" id="address-${adr.id}" required="required">
            <div style="position: relative;">
                <a class="delete" style="border-radius: 0px; position: absolute; top: 0px; right: 0px; z-index: 1;" onclick="delete_address(event, ${adr.id})"></a>
                <label class="box my-5" style="border-radius: 0px; position: relative;" for="address-${adr.id}">
                    <ul>
                        <li><b>${adr.country}</b></li>
                        <li>${adr.town}</li>
                        <li>${adr.street} ${adr.streetNumber}/${adr.localNumber}</li>
                        <li>${adr.postalCode}</li>
                    </ul>
                </label>
            </div>
        </div>
        `
    } else {
        return `
        <div class="block" data-address>
            <input type="radio" name="address" value="${adr.id}" id="address-${adr.id}" required="required">
            <div style="position: relative;">
                <a class="delete" style="border-radius: 0px; position: absolute; top: 0px; right: 0px; z-index: 1;" onclick="delete_address(event, ${adr.id})"></a>
                <label class="box my-5" style="border-radius: 0px; position: relative;" for="address-${adr.id}">
                    <ul>
                        <li><b>${adr.country}</b></li>
                        <li>${adr.town}</li>
                        <li>${adr.street} ${adr.streetNumber}</li>
                        <li>${adr.postalCode}</li>
                    </ul>
                </label>
            </div>
        </div>
        `
    }
}

// document.getElementById("order-button").addEventListener("click", async function(){  
//     const addresses = await fetch_get_client_addresses();
//     const address_list = user_addresses.querySelector("fieldset")
//     address_list.innerHTML = ``;
//     if(addresses.length > 0){
//         user_addresses.querySelector("button").disabled = false;
//         addresses.forEach((adr, i) => {
//             user_addresses.firstElementChild.innerHTML += address_card(adr);
//         });
//         address_list.querySelector("input").checked = true;
//     }else{
//         user_addresses.querySelector("button").disabled = true;
//         //user_addresses.querySelector("fieldset").innerHTML = `<div class="block m-5"><p class="title is-3 has-text-centered">No addresses</p></div>`
//     }
// })

async function draw_addresses(){
    const addresses = await fetch_get_client_addresses();
    const address_list = user_addresses.querySelector("fieldset")
    address_list.innerHTML = ``;
    if(addresses.length > 0){
        document.getElementById('client-addresses-title').innerText = "Your addresses:";
        user_addresses.querySelector("button").disabled = false;
        addresses.forEach((adr, i) => {
            user_addresses.querySelector('fieldset').innerHTML += address_card(adr);
        });
        address_list.querySelector("input").checked = true;
    }else{
        user_addresses.querySelector("button").disabled = true;
        //user_addresses.querySelector("fieldset").innerHTML = `<p class="subtitle is-5">No addresses - You must add new address</p>`
        document.getElementById('client-addresses-title').innerText = "No addresses - You must add new address";
    }
}

function draw_order(cart){
    if(cart){
        let total_price = sessionStorage.getItem('total-price');
        order_items.innerHTML = "";
        for (const key in cart) {
            order_items.innerHTML += `
            <tr>
                <td>
                    <figure class="image is-64x64">
                        <img src="http://localhost:8080/images/item_${key}.jpg" alt="album cover">
                    </figure>
                    ${cart[key].title}
                </td>
                <td>
                    <span>${cart[key].quantity}</span>
                </td>
                <td>${cart[key].price * cart[key].quantity} zł</td>
            </tr>
            `
        }
        order_items.innerHTML += `
        <tr>
            <td></td>
            <td><b class="title is-4">Total:</b></td>
            <td><b class="title is-4">${Math.round(total_price * 100) / 100}.00 zł</b></td>
        </tr>
        `
    }else{
        order_items.innerHTML = "";
    }
}

user_addresses.addEventListener("submit", async function(e){
    //e.target.classList.add("is-loading");
    e.preventDefault();
    console.log("Making an order")
    
    if(user_addresses.querySelector("fieldset").children.length != 0){
        fetch_create_order().then((order) => {
            // order_bar.classList.add("shrinkx");
            // const modal = document.getElementById("modal-container");
            // modal.innerHTML = order_modal(order);
            // modal.classList.add("fade-in");
            //console.log('order: ' + order);
            sessionStorage.setItem('order', JSON.stringify(order));
            sessionStorage.removeItem("cart");
            window.location.href = "summary.html";
        }).catch((e)=>{
            notify("error","Error: " + e.message);
        })
        //e.target.classList.remove("is-loading");
    } else {
        notify("error","You must add address")
    }
})

window.addEventListener("load", () => {
    draw_order(JSON.parse(sessionStorage.getItem('cart')));
    draw_addresses();
});

if(user_addresses.querySelector("fieldset").children.length == 0){
    make_order_button.disabled = true;
} else {
    make_order_button.disabled = false;
}