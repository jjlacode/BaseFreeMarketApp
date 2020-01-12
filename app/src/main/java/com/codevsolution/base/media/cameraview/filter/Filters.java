package com.codevsolution.base.media.cameraview.filter;

import androidx.annotation.NonNull;

import com.codevsolution.base.media.cameraview.filters.AutoFixFilter;
import com.codevsolution.base.media.cameraview.filters.BlackAndWhiteFilter;
import com.codevsolution.base.media.cameraview.filters.BrightnessFilter;
import com.codevsolution.base.media.cameraview.filters.ContrastFilter;
import com.codevsolution.base.media.cameraview.filters.CrossProcessFilter;
import com.codevsolution.base.media.cameraview.filters.DocumentaryFilter;
import com.codevsolution.base.media.cameraview.filters.DuotoneFilter;
import com.codevsolution.base.media.cameraview.filters.FillLightFilter;
import com.codevsolution.base.media.cameraview.filters.GammaFilter;
import com.codevsolution.base.media.cameraview.filters.GrainFilter;
import com.codevsolution.base.media.cameraview.filters.GrayscaleFilter;
import com.codevsolution.base.media.cameraview.filters.HueFilter;
import com.codevsolution.base.media.cameraview.filters.InvertColorsFilter;
import com.codevsolution.base.media.cameraview.filters.LomoishFilter;
import com.codevsolution.base.media.cameraview.filters.PosterizeFilter;
import com.codevsolution.base.media.cameraview.filters.SaturationFilter;
import com.codevsolution.base.media.cameraview.filters.SepiaFilter;
import com.codevsolution.base.media.cameraview.filters.SharpnessFilter;
import com.codevsolution.base.media.cameraview.filters.TemperatureFilter;
import com.codevsolution.base.media.cameraview.filters.TintFilter;
import com.codevsolution.base.media.cameraview.filters.VignetteFilter;


/**
 * Contains commonly used {@link Filter}s.
 * <p>
 * You can use {@link #newInstance()} to create a new instance and
 * pass it to {@link com.otaliastudios.cameraview.CameraView#setFilter(Filter)}.
 */
public enum Filters {

    /**
     * @see NoFilter
     */
    NONE(NoFilter.class),

    /**
     * @see AutoFixFilter
     */
    AUTO_FIX(AutoFixFilter.class),

    /**
     * @see BlackAndWhiteFilter
     */
    BLACK_AND_WHITE(BlackAndWhiteFilter.class),

    /**
     * @see BrightnessFilter
     */
    BRIGHTNESS(BrightnessFilter.class),

    /**
     * @see ContrastFilter
     */
    CONTRAST(ContrastFilter.class),

    /**
     * @see CrossProcessFilter
     */
    CROSS_PROCESS(CrossProcessFilter.class),

    /**
     * @see DocumentaryFilter
     */
    DOCUMENTARY(DocumentaryFilter.class),

    /**
     * @see DuotoneFilter
     */
    DUOTONE(DuotoneFilter.class),

    /**
     * @see FillLightFilter
     */
    FILL_LIGHT(FillLightFilter.class),

    /**
     * @see GammaFilter
     */
    GAMMA(GammaFilter.class),

    /**
     * @see GrainFilter
     */
    GRAIN(GrainFilter.class),

    /**
     * @see GrayscaleFilter
     */
    GRAYSCALE(GrayscaleFilter.class),

    /**
     * @see HueFilter
     */
    HUE(HueFilter.class),

    /**
     * @see InvertColorsFilter
     */
    INVERT_COLORS(InvertColorsFilter.class),

    /**
     * @see LomoishFilter
     */
    LOMOISH(LomoishFilter.class),

    /**
     * @see PosterizeFilter
     */
    POSTERIZE(PosterizeFilter.class),

    /**
     * @see SaturationFilter
     */
    SATURATION(SaturationFilter.class),

    /**
     * @see SepiaFilter
     */
    SEPIA(SepiaFilter.class),

    /**
     * @see SharpnessFilter
     */
    SHARPNESS(SharpnessFilter.class),

    /**
     * @see TemperatureFilter
     */
    TEMPERATURE(TemperatureFilter.class),

    /**
     * @see TintFilter
     */
    TINT(TintFilter.class),

    /**
     * @see VignetteFilter
     */
    VIGNETTE(VignetteFilter.class);

    private Class<? extends Filter> filterClass;

    Filters(@NonNull Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
    }

    /**
     * Returns a new instance of the given filter.
     *
     * @return a new instance
     */
    @NonNull
    public Filter newInstance() {
        try {
            return filterClass.newInstance();
        } catch (IllegalAccessException e) {
            return new NoFilter();
        } catch (InstantiationException e) {
            return new NoFilter();
        }
    }
}
