const login_form = document.getElementById('login-form');

const fetch_login = async (data) => {
    try{
        const response = await fetch(
            "http://localhost:8080/api/v1/auth/login",
            {
                method:'POST',
                body: JSON.stringify({
                    email: data.email,
                    password: data.password
                }),
                headers: {
                    'Content-Type': 'application/json'
                },
            }
        );
        if(Math.floor(response.status / 100) == 2)          
            return Promise.resolve(await response.json());
        return Promise.reject(await response.json());
    }catch(e){
        return Promise.reject({"message" : e});
    }
}

const performLogin = (e) => {
    e.preventDefault();
    const user = {
        email:login_form.elements['email'].value,
        password:login_form.elements['password'].value
    }

    console.log(user.email);

    fetch_login(user).then((data) => {
        sessionStorage.setItem("user_id", data.id);
        sessionStorage.setItem("user_token", data.token);
        console.log(sessionStorage.getItem("user_token"));
        window.location.href = "index.html";
        //window.history.pushState('index', 'index', '/index.hmtl');
        //window.history.replaceState(null, null, 'index.html');
        notify("success","Logged in successfully!");
        //window.location.href = "index.html";
    }).catch((e)=>{
        notify("error","Login failed: " + e.message);
    })

    return false;
}

const isLoggedIn = () => {
    return sessionStorage.getItem('user_id') != null;
}
 
const register_form = document.getElementById('register-form');

const fetch_register = async (data) => {
    try{
        const response = await fetch(
            "http://localhost:8080/api/v1/auth/register",
            {
                'method':'POST',
                'body': JSON.stringify({
                    email: data.email,
                    password: data.password,
                    client:{
                        name: data.client.name,
                        surname: data.client.surname
                    }
                }),
                headers: {
                    'Content-Type': 'application/json'
                },
            }
        );
        if(response.status / 100 == 2)
            return Promise.resolve(await response.json());
        return Promise.reject(await response.json())
    }catch(e){
        return Promise.reject({"message" : e});
    }
}

const performRegister = (e) => {
    e.preventDefault();
    const user = {
        email: register_form.elements['email'].value,
        password: register_form.elements['password'].value,
        client:{
            name: register_form.elements['name'].value,
            surname: register_form.elements['surname'].value
        } 
    }

    fetch_register(user).then((data)=>{
        sessionStorage.setItem("user_id", data.id);
        sessionStorage.setItem("user_token", data.token);
        console.log(sessionStorage.getItem("user_token"));
        window.location.href = "index.html";
        notify("success","Registered successfully!");
    }).catch((e)=>{
        notify("error","Register failed: " + e.message);
    })   

    return false;
}

const logout_tab = document.getElementById("logout-button");

const performLogout = (e) => {
    e.preventDefault();
    sessionStorage.removeItem("user_id");
    sessionStorage.removeItem("user_token");
    sessionStorage.removeItem("cart");
    sessionStorage.removeItem("total-price");
    sessionStorage.removeItem("order");
    localStorage.removeItem('product-id');
    window.location.href = "index.html";

    return false;
}

//--------------------------------------------------
const fetch_get_user = async () => {
    try{
        const response = await fetch(
            `http://localhost:8080/api/v1/user/${sessionStorage.getItem("user_id")}`,
            {
                'method':'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
            }
        );
        if(Math.floor(response.status / 100) == 2)
            return Promise.resolve(await response.json());
        return Promise.reject(await response.json())
    }catch(e){
        return Promise.reject({"message" : e});
    }
}