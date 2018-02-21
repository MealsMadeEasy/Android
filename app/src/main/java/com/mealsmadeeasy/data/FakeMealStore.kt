package com.mealsmadeeasy.data

import com.mealsmadeeasy.model.*
import com.mealsmadeeasy.utils.replace
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
        val entry = mealPlan.value.meals
                .filter { it.date.dayOfMonth() == date.dayOfMonth() }
                .filter { it.mealPeriod == mealPeriod }
                .firstOrNull()

        if (entry != null) {
            val updatedEntry = entry.copy(meals = entry.meals + MealPortion(meal, servings))
            mealPlan.onNext(mealPlan.value.copy(
                    meals = mealPlan.value.meals.replace(old = entry, new = updatedEntry)
            ))
        } else {
            mealPlan.onNext(mealPlan.value.copy(
                    meals = mealPlan.value.meals + MealPlanEntry(date, mealPeriod, listOf(MealPortion(meal, servings)))
            ))
        }
    }

    override fun removeMealFromMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod) {
        val entry = mealPlan.value.meals
                .filter { it.date.dayOfMonth() == date.dayOfMonth() }
                .filter { it.mealPeriod == mealPeriod }
                .firstOrNull()

        if (entry != null) {
            val updatedEntry = entry.copy(meals = entry.meals.filter { it.meal == meal })
            mealPlan.onNext(mealPlan.value.copy(
                    meals = mealPlan.value.meals.replace(old = entry, new = updatedEntry)
            ))
        }
    }

    override fun getSuggestedMeals(): Single<List<Meal>> {
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

        return Single.just(listOf(tacos, iceCream))
    }

    override fun findMealById(id: String): Single<Meal> {
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
        when (id) {
            "tacos" -> return Single.just(tacos)
            else -> return Single.just(iceCream)
        }
    }
}