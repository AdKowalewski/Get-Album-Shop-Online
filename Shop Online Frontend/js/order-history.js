const fetch_order_history = async () => {
    const user_id = parseInt(sessionStorage.getItem("user_id"));
    try{
        const response = await fetch(
            `http://localhost:8080/api/v1/order/client/${user_id}`,
            {
                'method':'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': sessionStorage.getItem("user_token")
                },
            }
        );
        return Promise.resolve(await response.json());
    }catch(e){
        return Promise.reject({"message" : e});
    }
}

const fetch_get_order = async (id) => {
    try{
        const response = await fetch(
            `http://localhost:8080/api/v1/order/${id}`,
            {
                'method':'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': sessionStorage.getItem("user_token")
                },
            }
        );
        return Promise.resolve(await response.json());
    }catch(e){
        return Promise.reject({"message" : e});
    }

}

const fetch_get_order_products = async (id) => {
    try{
        const response = await fetch(
            `http://localhost:8080/api/v1/order/${id}/products`,
            {
                'method':'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': sessionStorage.getItem("user_token")
                },
            }
        );
        return Promise.resolve(await response.json());
    }catch(e){
        return Promise.reject({"message" : e});
    }
}

const user_history = document.getElementById("history-table-container");

window.addEventListener("load", async function(){
    fetch_order_history().then(async (order) => {
        user_history.querySelector("tbody").innerHTML = ``;
        if(order.length > 0){
            for(let i=0; i<order.length; i++){
                const prods = await fetch_get_order_products(order[i].id);
                let total = 0;
                prods.forEach((p) => {
                    total += p.price * p.quantity;
                });
                user_history.querySelector("tbody").innerHTML += user_hist_table(order[i], prods, total);
            }
        } else {
            user_history.querySelector("tbody").innerHTML = `<tr><td colspan="3">You have no orders yet</td></tr>`
        }
    }).catch((e) => {
        notify("error","Error: " + e.message)
    }) 
})

var user_hist_table = (order, products, total)=>{
    let cell = (prod) =>{
        return `<tr>
            <td onclick="goToProductPage(${prod.id})" style="cursor: pointer;">
            <figure class="image is-64x64">
                <img src="http://localhost:8080/images/item_${prod.id}.jpg" alt="album cover">
            </figure>
            <b>${prod.artist}</b>: ${prod.title}
            </td>
            <td>${prod.quantity}</td>
        </tr>`
    }
    let cells = ``;
    try{
        products.forEach(p => {
            cells += cell(p);
        });
    }catch(e){}
    return `<tr>
    <td>${order.id}</td>
    <td>${order.created.slice(0,10)}</td>
    <td>${total} zł</td>
    <td>
        <table class="table is-fullwidth">
        <thead>
            <tr>
                <th>Product</th>
                <th>Quantity</th>
            </tr>
        </thead>
        <tbody style="width: 100%;">
            ${cells}
        </tbody>
        </table>
    </td>
    </tr>`;
}