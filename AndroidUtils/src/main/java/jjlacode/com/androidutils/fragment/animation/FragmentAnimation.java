package jjlacode.com.androidutils.fragment.animation;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;

public class FragmentAnimation {

    private final @AnimatorRes
    @AnimRes
    int enter;

    @AnimatorRes
    private final @AnimRes
    int exit;

    @AnimatorRes
    private final @AnimRes
    int popEnter;

    @AnimatorRes
    private final @AnimRes
    int popExit;

    public FragmentAnimation(int enterAnimation, int exitAnimation) {
        this.enter = enterAnimation;
        this.exit = exitAnimation;
        popEnter = 0;
        popExit = 0;
    }

    public FragmentAnimation(int enterAnimation,
                             int exitAnimation,
                             int exitCurrentAnimation,
                             int enterCurrentAnimation) {
        this.enter = enterAnimation;
        this.exit = exitAnimation;
        this.popEnter = exitCurrentAnimation;
        this.popExit = enterCurrentAnimation;
    }

    public int getEnter() {
        return enter;
    }

    public int getExit() {
        return exit;
    }

    public int getPopEnter() {
        return popEnter;
    }

    public int getPopExit() {
        return popExit;
    }
}
