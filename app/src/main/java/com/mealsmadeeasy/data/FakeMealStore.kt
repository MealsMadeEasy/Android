package com.mealsmadeeasy.data

import com.mealsmadeeasy.model.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.DateTime

class FakeMealStore : MealStore {

    private val mealPlan: BehaviorSubject<MealPlan>
    private val ingredients: Map<String, BehaviorSubject<List<Ingredient>>>

    init {
        val bagels = Meal(
                id = "bagel", name = "Bagel",
                description = """
                    Bagels with cream cheese.
                """.trimIndent(),
                thumbnailUrl = "https://s-i.huffpost.com/gen/1692024/images/o-CREAM-CHEESE-facebook.jpg"
        )

        val banana = Meal(
                id = "phallic-fruit", name = "Banana",
                description = """
                    It's a (particularly photogenic) banana.
                """.trimIndent(),
                thumbnailUrl = "https://cdn1.medicalnewstoday.com/content/images/articles/271157-bananas.jpg"
        )

        val sandwich = Meal(
                id = "sandwich", name = "Sandwich",
                description = """
                    Turkey sandwich.
                """.trimIndent(),
                thumbnailUrl = "https://www.joyofkosher.com/.image/t_share/MTMxNzYwODM4Mzk3NzA4Mjk4/turkey-sandwich.jpg"
        )

        val tacos = Meal(
                id = "tacos", name = "Tacos",
                description = """
                    It's the best food product.
                """.trimIndent(),
                thumbnailUrl = "https://www.forksoverknives.com/wp-content/uploads/lentiltacos-6553WP-edit.jpg")

        val iceCream = Meal(
                id = "ice_cream", name = "Chocolate ice cream",
                description = """
                    It's chocolate ice cream. Yup.
                """.trimIndent(),
                thumbnailUrl = "https://chocolatecoveredkatie.com/wp-content/uploads/2017/02/nice-cream.jpg"
        )

        mealPlan = BehaviorSubject.createDefault(MealPlan(listOf(
                MealPlanEntry(date = DateTime.now(), mealPeriod = MealPeriod.BREAKFAST, meals = listOf(MealPortion(bagels, 1), MealPortion(banana, 1))),
                MealPlanEntry(date = DateTime.now(), mealPeriod = MealPeriod.LUNCH, meals = listOf(MealPortion(sandwich, 1))),
                MealPlanEntry(date = DateTime.now(), mealPeriod = MealPeriod.DINNER, meals = listOf(MealPortion(tacos, 3), MealPortion(iceCream, 2)))
        )))

        ingredients = mapOf(
                bagels to listOf(
                        Ingredient(name = "Bagel", quantity = 1, unit = "Whole bagel"),
                        Ingredient(name = "Cream cheese", quantity = 2, unit = "Tbsp")
                ),
                banana to listOf(
                        Ingredient(name = "Banana", quantity = 1, unit = "Bananas")
                ),
                sandwich to listOf(
                        Ingredient(name = "Bread", quantity = 2, unit = "Slices"),
                        Ingredient(name = "Sliced turkey", quantity = 3, unit = "Oz."),
                        Ingredient(name = "Cheese", quantity = 1, unit = "Slices"),
                        Ingredient(name = "Lettuce", quantity = 4, unit = "Leaves")
                ),
                tacos to listOf(
                        Ingredient(name = "Flour tortilla shells", quantity = 3, unit = "Shells"),
                        Ingredient(name = "Chicken", quantity = 0.5, unit = "lbs"),
                        Ingredient(name = "Shredded cheese", quantity = 0.75, unit = "Cups"),
                        Ingredient(name = "Lettuce", quantity = 2, unit = "Leaves"),
                        Ingredient(name = "Tomato", quantity = 0.5, unit = "Tomatoes")
                ),
                iceCream to listOf(
                        Ingredient(name = "Chocolate ice cream", quantity = 1, unit = "Pint"),
                        Ingredient(name = "Chocolate syrup", quantity = 1, unit = "Fl.oz.")
                ))
                .mapValues { (_, ingredients) -> BehaviorSubject.createDefault(ingredients) }
                .mapKeys { (meal, _) -> meal.id }
    }

    override fun getMealPlan() = mealPlan

    private fun getIngredientsForRecipe(meal: Meal) = ingredients[meal.id]
            ?: Observable.just(emptyList<Ingredient>())!!

    override fun getIngredientsForMealPlan(): Observable<GroceryList> {
        return getMealPlan()
                .map { it.meals }
                .flatMap { meals ->
                    Observable.fromIterable(meals)
                            .flatMap { Observable.fromIterable(it.meals) }
                            .map { it.meal }
                            .flatMapSingle { getIngredientsForRecipe(it).first(emptyList()) }
                            .map { ingredients ->
                                ingredients.map { GroceryListEntry(ingredient = it, purchased = false, dependants = listOf("A food")) }
                            }
                            .collect<MutableList<GroceryListEntry>>(::mutableListOf, { all, meal ->
                                all.addAll(meal)
                            })
                            .toObservable()
                }
                .map { it.sortedBy { it.ingredient.name } }
                .map { GroceryList(it) }
    }

    override fun markIngredientPurchased(ingredient: Ingredient, purchased: Boolean) {
        TODO("not implemented")
    }

    override fun addMealToMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod, servings: Int) {
        mealPlan.onNext(mealPlan.value + MealPlanEntry(date, mealPeriod, listOf(MealPortion(meal, servings))))
    }

    override fun editMealPlanServings(meal: Meal, date: DateTime, mealPeriod: MealPeriod, servings: Int) {
        removeMealFromMealPlan(meal, date, mealPeriod)
        addMealToMealPlan(meal, date, mealPeriod, servings)
    }

    override fun removeMealFromMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod) {
        mealPlan.onNext(mealPlan.value - MealPlanEntry(date, mealPeriod, listOf(MealPortion(meal, Int.MAX_VALUE))))
    }

    override fun getSuggestedMeals(): Single<List<Meal>> {
        val tacos = Meal(
                id = "tacos", name = "Tacos",
                description = """
                    It's the best food product.
                """.trimIndent(),
                thumbnailUrl = "https://www.forksoverknives.com/wp-content/uploads/lentiltacos-6553WP-edit.jpg"
        )

        val iceCream = Meal(
                id = "ice_cream", name = "Chocolate ice cream",
                description = """
                    It's chocolate ice cream. Yup.
                """.trimIndent(),
                thumbnailUrl = "https://chocolatecoveredkatie.com/wp-content/uploads/2017/02/nice-cream.jpg"
        )

        return Single.just(listOf(tacos, iceCream))
    }

    override fun findMealById(id: String): Single<Meal> {
        val tacos = Meal(
                id = "tacos", name = "Tacos",
                description = """
                    It's the best food product.
                """.trimIndent(),
                thumbnailUrl = "https://www.forksoverknives.com/wp-content/uploads/lentiltacos-6553WP-edit.jpg"
        )

        val iceCream = Meal(
                id = "ice_cream", name = "Chocolate ice cream",
                description = """
                    It's chocolate ice cream. Yup.
                """.trimIndent(),
                thumbnailUrl = "https://chocolatecoveredkatie.com/wp-content/uploads/2017/02/nice-cream.jpg"
        )
        when (id) {
            "tacos" -> return Single.just(tacos)
            else -> return Single.just(iceCream)
        }
    }

    override fun getRecipe(id: String): Single<Recipe> {
        val tacosRecipe = Recipe(
                content = """
                    <p>These tacos are the best tacos ever.</p>
                    <p>1. Cut chicken into 1/2 inch strips.</p>
                    <p>2. Heat oil in large skillet over medium-high heat, adding chicken once hot.
                        Cook and stir for 10 minutes or until cooked through.</p>
                    <p>3. Spoon 1/3 of the chicken into each tortilla and sprinkle with cheese.
                            Top evenly with lettuce and tomato.</p>
                """
        )

        val iceCreamRecipe = Recipe(
                content = """
                    <p>🍦😍</p>
                    <p>1. Consume directly from container.</p>
                """
        )

        when (id) {
            "tacos" -> return Single.just(tacosRecipe)
            else -> return Single.just(iceCreamRecipe)
        }
    }

    override fun search(query: String, filters: List<Filter>): Single<List<Meal>> {
        throw NotImplementedError("FakeMealStore doesn't support searching.")
    }

    override fun getAvailableFilters(): Single<List<FilterGroup>> {
        return Single.just(emptyList())
    }
}