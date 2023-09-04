const hamburger = document.getElementById("hamburger");

hamburger.addEventListener('click', () => {

    const target = hamburger.dataset.target;
    const $target = document.getElementById(target);

    hamburger.classList.toggle('is-active');
    $target.classList.toggle('is-active');

});