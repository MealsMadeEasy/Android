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
                        Ingredient(id = "single-bagel", name = "Bagel", quantity = 1, unitName = "Whole bagel"),
                        Ingredient(id = "cream-cheese", name = "Cream cheese", quantity = 2, unitName = "Tbsp")
                ),
                banana to listOf(
                        Ingredient(id = "single-banana", name = "Banana", quantity = 1, unitName = "Bananas")
                ),
                sandwich to listOf(
                        Ingredient(id = "bread", name = "Bread", quantity = 2, unitName = "Slices"),
                        Ingredient(id = "lunch-meat_turkey", name = "Sliced turkey", quantity = 3, unitName = "Oz."),
                        Ingredient(id = "sliced_cheese", name = "Cheese", quantity = 1, unitName = "Slices"),
                        Ingredient(id = "lettuce", name = "Lettuce", quantity = 4, unitName = "Leaves")
                ),
                tacos to listOf(
                        Ingredient(id = "taco-shell", name = "Flour tortilla shells", quantity = 3, unitName = "Shells"),
                        Ingredient(id = "taco-meat_chicken", name = "Chicken", quantity = 0.5, unitName = "lbs"),
                        Ingredient(id = "shredded_cheese", name = "Shredded cheese", quantity = 0.75, unitName = "Cups"),
                        Ingredient(id = "lettuce", name = "Lettuce", quantity = 2, unitName = "Leaves"),
                        Ingredient(id = "tomato", name = "Tomato", quantity = 0.5, unitName = "Tomatoes")
                ),
                iceCream to listOf(
                        Ingredient(id = "ice-cream_chocolate", name = "Chocolate ice cream", quantity = 1, unitName = "Pint"),
                        Ingredient(id = "chocolate-syrup", name = "Chocolate syrup", quantity = 1, unitName = "Fl.oz.")
                ))
                .mapValues { (_, ingredients) -> BehaviorSubject.createDefault(ingredients) }
                .mapKeys { (meal, _) -> meal.id }
    }

    override fun getMealPlan() = mealPlan

    override fun getIngredientsForRecipe(meal: Meal) = ingredients[meal.id]
            ?: Observable.just(emptyList<Ingredient>())!!


    override fun addMealToMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod, servings: Int) {
        mealPlan.onNext(mealPlan.value + MealPlanEntry(date, mealPeriod, listOf(MealPortion(meal, Int.MAX_VALUE))))
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
                prepTime = 20,
                steps = listOf(
                        RecipeStep("Cut chicken into 1/2 inch strips."),
                        RecipeStep("Heat oil in large skillet over medium-high heat, adding chicken once hot. " +
                                "Cook and stir for 10 minutes or until cooked through."),
                        RecipeStep("Spoon 1/3 of the chicken into each tortilla and sprinkle with cheese. " +
                                "Top evenly with lettuce and tomato.")
                ),
                ingredients = listOf(
                        Ingredient(id = "taco-shell", name = "Flour tortilla shells", quantity = 3, unitName = "Shells", isMeasurable = false),
                        Ingredient(id = "taco-meat_chicken", name = "Chicken", quantity = 0.5, unitName = "lbs"),
                        Ingredient(id = "shredded_cheese", name = "Shredded cheese", quantity = 0.75, unitName = "Cups"),
                        Ingredient(id = "lettuce", name = "Lettuce leaves", quantity = 2, unitName = "Leaves", isMeasurable = false),
                        Ingredient(id = "tomato", name = "Tomato", quantity = 0.5, unitName = "Tomatoes", isMeasurable = false)
                )
        )

        val iceCreamRecipe = Recipe(
                prepTime = 5,
                steps = listOf(
                        RecipeStep("Take the ice cream out of the freezer, letting it thaw for 5 minutes."),
                        RecipeStep("Scoop desired amount into bowl and top evenly with chocolate syrup.")
                ),
                ingredients = listOf(
                        Ingredient(id = "ice-cream_chocolate", name = "Chocolate ice cream", quantity = 1, unitName = "Pint"),
                        Ingredient(id = "chocolate-syrup", name = "Chocolate syrup", quantity = 1, unitName = "Fl.oz.")
                )
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