const cocktailsList = document.getElementById('cocktailsList')
const searchBar = document.getElementById('searchInput')

const allCocktails = [];

fetch("http://localhost:8080/cocktails/search").then(response => response.json()).then(data => {
    for (let cocktail of data) {
        allCocktails.push(cocktail);
    }
})

console.log(allCocktails);

searchBar.addEventListener('keyup', (e) => {
    const searchingCharacters = searchBar.value.toLowerCase();
    let filteredCocktails = allCocktails.filter(cocktail => {
        console.log(cocktail);
        return cocktail.name.toLowerCase().includes(searchingCharacters)
            || cocktail.description.toLowerCase().includes(searchingCharacters);
    });
    displayCocktails(filteredCocktails);
})


const displayCocktails = (cocktail) => {
    cocktailsList.innerHTML = cocktail
        .map((c) => {
            return `  <div class="mx-auto mt-5 " id="cocktail_of_the_day_container">
            <h2 class="text-center text-white my-3" hidden>Cocktail of the day</h2>
            <div  class="d-flex" id="cocktail-box">

                <img class="h-25" style="" src="${c.imgUrl}" id="cocktail_details_img" alt="cocktail image"
                data-holder-rendered="true">

                <div class="flex-column p-2">
                    <div><h3  class="my-4">${c.name}</h3></div>
                    <div><p >${c.description}</p></div>
                    <div class="d-flex justify-content-around my-3">
                        <a href="/cocktails/details/${c.id}" class="btn btn-dark">Details</a>
                    </div>
                </div>
            </div>
        </div>
                `
        })
        .join('');

}
