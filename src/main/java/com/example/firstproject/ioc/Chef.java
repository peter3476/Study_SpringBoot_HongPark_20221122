package com.example.firstproject.ioc;

import org.springframework.stereotype.Component;

@Component
public class Chef {
    // 셰프가 식재료 공장과 협업하기 위한 DI
    private IngredientFactory ingredientFactory;

    public Chef(IngredientFactory ingredientFactory) {
        this.ingredientFactory = ingredientFactory;
    }

    public String cook(String menu){
        // 재료 준비
        Ingredient ingredient = ingredientFactory.get(menu);

        return ingredient.getName() + "으로 만든 " + menu;
    }
}
