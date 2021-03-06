package com.mealsmadeeasy.data

import com.mealsmadeeasy.data.service.MealsMadeEasyService
import com.mealsmadeeasy.data.service.getSearchResults
import com.mealsmadeeasy.data.service.model.SparseMealPlan
import com.mealsmadeeasy.model.*
import com.mealsmadeeasy.utils.forceUtc
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import retrofit2.Response
import java.io.IOException

class NetMealStore(
        private val userManager: UserManager,
        private val service: MealsMadeEasyService
) : MealStore {

    private val mealPlan = RxLoader {
        userManager.getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap { service.getMealPlan(it) }
                .map { it.unwrap() }
    }

    private val suggestedMeals = RxLoader {
        userManager.getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap { service.getSuggestedMeals(it) }
                .map { it.unwrap() }
    }

    private val recipes = mutableMapOf<MealId, RxLoader<Recipe>>()

    private val searchFilters = RxLoader {
        service.getAvailableFilters()
                .subscribeOn(Schedulers.io())
                .map { it.unwrap() }
    }

    private val groceryList = RxLoader {
        userManager.getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap { service.getGroceryList(it) }
                .map { it.unwrap() }
    }

    override fun getMealPlan(): Observable<MealPlan> {
        return mealPlan.getOrComputeValue()
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getIngredientsForMealPlan(): Observable<GroceryList> {
        return groceryList.getOrComputeValue()
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun markIngredientPurchased(ingredient: Ingredient, purchased: Boolean) {
        check(groceryList.getValue() != null) {
            "Grocery list hasn't loaded yet"
        }

        val currentList = groceryList.getValue()!!.items
        groceryList.setValue(GroceryList(currentList.map {
            if (it.ingredient == ingredient) it.copy(purchased = purchased) else it
        }))

        userManager.getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap {
                    if (purchased) {
                        service.markIngredientPurchased(it, ingredient)
                    } else {
                        service.markIngredientNotPurchased(it, ingredient)
                    }
                }
                .subscribe { result -> result.unwrap() }
    }

    override fun getRecipe(id: String): Single<Recipe> {
        val loader = recipes[id] ?: run {
            RxLoader {
                service.getRecipe(id)
                        .subscribeOn(Schedulers.io())
                        .map { it.unwrap() }
            }.also { recipes[id] = it }
        }

        return loader.getOrComputeValue()
                .firstOrError()
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun addMealToMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod, servings: Int) {
        mealPlan.getOrComputeValue().firstOrError()
                .subscribeOn(Schedulers.io())
                .map {
                    it + MealPlanEntry(
                            date = date.forceUtc().withTimeAtStartOfDay(),
                            mealPeriod = mealPeriod,
                            meals = listOf(MealPortion(meal, servings)))
                }
                .flatMap { updateMealPlan(it)}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun editMealPlanServings(meal: Meal, date: DateTime, mealPeriod: MealPeriod, servings: Int) {
        mealPlan.getOrComputeValue().firstOrError()
                .subscribeOn(Schedulers.io())
                .map {
                    it - MealPlanEntry(
                            date = date.forceUtc().withTimeAtStartOfDay(),
                            mealPeriod = mealPeriod,
                            meals = listOf(MealPortion(meal, Int.MAX_VALUE)))
                }
                .map {
                    it + MealPlanEntry(
                            date = date.forceUtc().withTimeAtStartOfDay(),
                            mealPeriod = mealPeriod,
                            meals = listOf(MealPortion(meal, servings)))
                }
                .flatMap { updateMealPlan(it)}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun removeMealFromMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod) {
        mealPlan.getOrComputeValue().firstOrError()
                .subscribeOn(Schedulers.io())
                .map {
                    it - MealPlanEntry(
                            date = date.forceUtc().withTimeAtStartOfDay(),
                            mealPeriod = mealPeriod,
                            meals = listOf(MealPortion(meal, Int.MAX_VALUE)))
                }
                .flatMap { updateMealPlan(it)}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun updateMealPlan(updatedPlan: MealPlan): Single<Unit> {
        mealPlan.setValue(updatedPlan)
        return userManager.getUserToken()
                .flatMap { token -> service.setMealPlan(token, SparseMealPlan(updatedPlan)) }
                .map { it.unwrap() }
                .doOnSuccess { groceryList.invalidate() }
    }

    override fun getSuggestedMeals(): Single<List<Meal>> {
        return suggestedMeals.getOrComputeValue()
                .first(emptyList())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun findMealById(id: String): Single<Meal> {
        return service.getMeal(id).subscribeOn(Schedulers.io())
                .map { it.unwrap() }
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun search(query: String, filters: List<Filter>): Single<List<Meal>> {
        return service.getSearchResults(query, filters)
                .subscribeOn(Schedulers.io())
                .map { it.unwrap() }
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAvailableFilters(): Single<List<FilterGroup>> {
        return searchFilters.getOrComputeValue().firstOrError()
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun <T> Response<T>.unwrap(): T {
        if (!isSuccessful) {
            throw IOException("${code()}: ${errorBody()}")
        } else {
            return body() ?: throw IOException("No data was returned")
        }
    }

}