package net.crescenthikari.bakingapp.features.list.presenter;

import net.crescenthikari.bakingapp.data.model.Recipe;
import net.crescenthikari.bakingapp.data.repository.RecipeRepository;
import net.crescenthikari.bakingapp.features.list.contract.RecipeListContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Muhammad Fiqri Muthohar on 9/18/17.
 */

public class RecipeListPresenter implements RecipeListContract.Presenter {

    private RecipeListContract.View view;
    private RecipeRepository recipeRepository;
    private CompositeDisposable disposables;

    public RecipeListPresenter(RecipeListContract.View view,
                               RecipeRepository recipeRepository) {
        this.view = view;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void bindRecipeList() {
        disposables = new CompositeDisposable();
        getRecipeList();
    }

    @Override
    public void getRecipeList() {
        view.hideRecipeList();
        view.hideRecipeListMessage();
        view.showProgressBar();
        recipeRepository
                .getRecipes()
                .subscribe(new Observer<List<Recipe>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Recipe> recipes) {
                        if (recipes.isEmpty()) {
                            view.showEmptyRecipeListMessage();
                        } else {
                            view.showRecipeList(recipes);
                        }
                        view.hideRecipeListMessage();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.showEmptyRecipeListMessage();
                        view.hideRecipeList();
                        view.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgressBar();
                    }
                });
    }

    @Override
    public void unbind() {
        view = null;
        recipeRepository = null;
        disposables.dispose();
    }
}
