const fetch_product_by_id_summary = async (id) => {
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

const order_number = document.getElementById('order-number');
const summary_items = document.getElementById('summary-items');
const summary_total = document.getElementById('summary-total');
var summary_total_price = sessionStorage.getItem('total-price');
const continue_button = document.getElementById('continue');

var order = JSON.parse(sessionStorage.getItem('order'));
order_number.innerText = `Your order number is: ${order.id}`;

// async function count_summary_total_cost(order){
//     let total_cost = 0;
//     for(const key in order.products){
//         await fetch_product_by_id_summary(key).then((p) => {
//             total_cost += p.price * order.products[key];
//         })
//     }
//     return total_cost;
// }

summary_total.innerText = `Total: ${summary_total_price}.00 zł`;

async function draw_summary(order){
    if(order){
        summary_items.innerHTML = "";
        for (const key in order.products) {
            await fetch_product_by_id_summary(key).then((p) => {
                summary_items.innerHTML += `
                <tr>
                    <td>
                        <figure class="image is-64x64">
                            <img src="http://localhost:8080/images/item_${p.id}.jpg" alt="album cover">
                        </figure>
                        ${p.title}
                    </td>
                    <td>
                        <span>${order.products[key]}</span>
                    </td>
                    <td>${p.price * order.products[key]} zł</td>
                </tr>
                `
            })
        }
    }else{
        order_items.innerHTML = "";
    }
}

continue_button.addEventListener("click", () => {
    sessionStorage.removeItem('total-price');
    sessionStorage.removeItem('order');
});

draw_summary(order);