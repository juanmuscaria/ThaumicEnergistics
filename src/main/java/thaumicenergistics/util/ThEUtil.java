package thaumicenergistics.util;

import java.util.EnumSet;

import net.minecraft.item.ItemStack;

import appeng.api.config.SortOrder;

import thaumcraft.api.aspects.Aspect;

import thaumicenergistics.item.ItemDummyAspect;

/**
 * @author BrockWS
 */
public class ThEUtil {

    public static ItemStack setAspect(ItemStack stack, Aspect aspect) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemDummyAspect))
            return stack;
        ItemDummyAspect item = (ItemDummyAspect) stack.getItem();
        item.setAspect(stack, aspect);
        return stack;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum> T rotateEnum(T e, EnumSet options, boolean backwards) {
        if (e == null || options == null)
            return e;
        int i = e.ordinal();
        if (i == 0)
            i = backwards ? options.size() - 1 : 1;
        else if (i >= options.size() - 1)
            i = backwards ? i - 1 : 0;
        else
            i = i + (backwards ? -1 : 1);
        T next = (T) options.toArray()[i];
        return ThEUtil.isInvalidSetting(next) ? ThEUtil.rotateEnum(next, options, backwards) : next;
    }

    public static boolean isInvalidSetting(Enum e) {
        // TODO: Add invtweaks integration
        return e == SortOrder.INVTWEAKS;
    }
}
