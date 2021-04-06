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
        return cocktail.name.toLowerCase().includes(searchingCharacters);
        // || cocktail. .artist.toLowerCase().includes(searchingCharacters);
    });
    displayCocktails(filteredCocktails);
})


const displayCocktails = (cocktail) => {
    cocktailsList.innerHTML = cocktail
        .map((c) => {
            return `  <div sec:authorize="isAuthenticated()" class="mx-auto mt-5 " id="cocktail_of_the_day_container">
            <h2 class="text-center text-white my-3">Cocktail of the day</h2>
            <div th:object="${cocktailSearchViewModel}" class="d-flex" id="cocktail-box">

                <img class="h-25" style="" th:src="*{imgUrl}" id="cocktail_details_img" alt="cocktail image">

                <div class="flex-column p-2">
                    <div><h3 th:text="*{name}" class="my-4"></h3></div>
                    <div><p th:text="*{description}"></p></div>
                    <div>
                        <ul>
                            <li th:each="i: ${cocktailSearchViewModel.getIngredientsNames()}"  th:text="${i}"
                                style="list-style: circle inside;"></li>
                        </ul>
                    </div>
                    <div class="d-flex justify-content-around my-3">
                        <a th:href="@{/cocktails/details/{id}(id=*{id})}" class="btn btn-dark">Details</a>
                    </div>
                </div>
            </div>
        </div>
                `
        })
        .join('');

}
