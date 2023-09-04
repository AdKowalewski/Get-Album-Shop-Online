const history_cont = document.getElementById('history-container');
const settings_cont = document.getElementById('settings-container');
const login_cont = document.getElementById('login-container');
const logout_cont = document.getElementById('logout-container');

const isLoggedInNavbar = () => {
    return sessionStorage.getItem('user_id') != null;
}

const changeNavbarWhenLoggedIn = () => {
    if(isLoggedInNavbar()){
        history_cont.style.display = "block";
        settings_cont.style.display = "block";
        login_cont.style.display = "none";
        logout_cont.style.display = "block";
    } else {
        history_cont.style.display = "none";
        settings_cont.style.display = "none";
        login_cont.style.display = "block";
        logout_cont.style.display = "none";
    }
}

changeNavbarWhenLoggedIn();

const hamburger = document.getElementById("hamburger");

hamburger.addEventListener('click', () => {

    const target = hamburger.dataset.target;
    const $target = document.getElementById(target);

    hamburger.classList.toggle('is-active');
    $target.classList.toggle('is-active');

});

const gContainer = document.getElementById("genres-container");

gContainer.addEventListener("click",() => {
    gContainer.classList.toggle('is-active');
});