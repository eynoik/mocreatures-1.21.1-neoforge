package drzhark.mocreatures.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MoCItemFood extends MoCItem {
    public int itemUseDuration;

    protected MoCItemFood(MoCItemFood.Builder builder) {
        super(builder.properties.food(builder.foodBuilder.build()));
        this.itemUseDuration = builder.itemUseDuration;
    }
    public int getUseDuration(ItemStack stack) {
        return itemUseDuration == 0 ? 32 : itemUseDuration;
    }

    public static class Builder {
        private final FoodProperties.Builder foodBuilder;
        private int itemUseDuration;
        private final Item.Properties properties;

        public Builder(Item.Properties properties, int amount) {
            this(properties, amount, 0.6F, false);
        }

        public Builder(Item.Properties properties, int amount, float saturation, boolean isWolfFood) {
            this(properties, amount, saturation, isWolfFood, 32);
        }

        public Builder(Item.Properties properties, int amount, float saturation, boolean isWolfFood, int eatingSpeed) {
            this.properties = properties;
            this.foodBuilder = new FoodProperties.Builder().nutrition(amount).saturationModifier(saturation);
            if (isWolfFood) {
            }
            this.itemUseDuration = eatingSpeed;
        }

        public Builder setAlwaysEdible() {
            this.foodBuilder.alwaysEdible();
            return this;
        }

        public Builder setPotionEffect(MobEffectInstance effectIn, float probability) {
            this.foodBuilder.effect(() -> effectIn, probability);
            return this;
        }

        public MoCItemFood build() {
            return new MoCItemFood(this);
        }
    }
}
