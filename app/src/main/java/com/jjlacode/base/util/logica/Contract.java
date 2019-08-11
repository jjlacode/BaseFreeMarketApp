package com.jjlacode.base.util.logica;

import com.jjlacode.base.util.BasePresenter;
import com.jjlacode.base.util.BaseView;

public interface Contract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
