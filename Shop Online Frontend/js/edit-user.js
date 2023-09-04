const edit_form = document.getElementById('user-edit-form');

window.addEventListener("load", async ()=>{
    await fetch_get_user().then((data)=>{
        edit_form.elements["name"].value = data.client.name;
        edit_form.elements["surname"].value = data.client.surname;
        edit_form.elements["email"].value = data.email;
    }).catch((e)=>{
        notify("error","Error: " + e.message);
    })
})

const fetch_user_edit = async (data) => {
    try{
        const response = await fetch(
            `http://localhost:8080/api/v1/user/${sessionStorage.getItem("user_id")}`,
            {
                'method':'PATCH',
                'body': JSON.stringify({
                    email: data.email,
                    password: data.password,
                    client:{
                        name: data.client.name,
                        surname: data.client.surname
                    }
                }),
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': sessionStorage.getItem("user_token")
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

const performEdit = (e) => {
    e.preventDefault();
    let oldName = "";
    let oldSurname = "";
    let oldEmail = "";
    let oldPassword = "";
    fetch_get_user().then((data) => {
        oldName = data.client.name;
        oldSurname = data.client.surname;
        oldEmail = data.email;
        oldPassword = data.password;
    });
    const user = {
        email: edit_form.elements['email'].value === oldEmail ? oldEmail : edit_form.elements['email'].value,
        password: edit_form.elements['password'].value === "" ? "" : edit_form.elements['password'].value,
        client:{
            name: edit_form.elements['name'].value === oldName ? oldName : edit_form.elements['name'].value,
            surname: edit_form.elements['surname'].value === oldSurname ? oldSurname : edit_form.elements['surname'].value
        } 
    }

    fetch_user_edit(user).then(()=>{
        edit_form.elements["name"].value = user.client.name;
        edit_form.elements["surname"].value = user.client.surname;
        edit_form.elements["email"].value = user.email;
        notify("success","Account edited successfully!");
    }).catch((e)=>{
        notify("error","Edit failed: " + e.message);
    })   

    return false;
}